package algorithm;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @Program: netty_talk
 * @Description
 * @Author miyabi
 * @Create 2021-01-06-13-22
 **/


public class Snowflake {
    /**
     * 起始参照时间
     */
    private final long startTimeStamp;
    /**
     * 数据中心ID位数
     */
    private final int dataCenterBits = 5;
    /**
     * 机器ID位数
     */
    private final int machineBits = 5;
    /**
     * 序列号位数
     */
    private final int sequenceBits = 12;
    /**
     * 数据中心ID最大值
     */
    private final long maxDataCenterId = ~(-1L << dataCenterBits);
    /**
     * 机器ID最大值
     */
    private final long maxMachineId = ~(-1L << machineBits);
    /**
     * 序列号最大值
     */
    private final long maxSequence = ~(-1L << sequenceBits);
    /**
     * 时间戳位移量
     */
    private final int timeStampLeftShift = sequenceBits + machineBits + dataCenterBits;
    /**
     * 数据中心ID位移量
     */
    private final int dataCenterLeftShift = machineBits + sequenceBits;
    /**
     * 机器ID位移量
     */
    private final int machineLeftShift = sequenceBits;
    /**
     * 数据中心ID
     */
    private long dataCenterId;
    /**
     * 机器ID
     */
    private long machineId;
    /**
     * 序列号
     */
    private long sequence;
    /**
     * 最后一次获取的时间戳
     */
    private long lastTimeStamp;

    {
        startTimeStamp = new Calendar.Builder().setDate(2021, 0, 1).build().getTimeInMillis();
    }

    public Snowflake(long machineId, long dataCenterId) {
        if (machineId > maxMachineId || machineId < 0) {
            throw new IllegalArgumentException("error machineId value");
        }
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException("error dataCenter value");
        }
        this.machineId = machineId;
        this.dataCenterId = dataCenterId;
    }

    /**
     * @Description: 同步方法，获取ID
     * @Param:
     * @return: ID
     * @Author:
     * @date: 2021-01-06
     */
    public synchronized long nextId() {
        long timeStamp;
        timeStamp = getTimeInMills();
        //        系统时间回退抛出异常
        if (timeStamp < lastTimeStamp) {
            throw new RuntimeException(String.format("Clock moved backwards. " +
                    "Refusing to generate id for %d milliseconds", lastTimeStamp - timeStamp));
        }
        //        同一时间，改变序列号
        if (timeStamp == lastTimeStamp) {
            sequence = (sequence + 1) & maxSequence;
            if (sequence == 0) {
                timeStamp = getNextTimeInMills(timeStamp);
            }
        } else {
            //            非同一时间，从0开始
            sequence = 0;
        }
        lastTimeStamp = timeStamp;

        //        拼接生成ID
        return timeStamp - startTimeStamp << timeStampLeftShift
                | dataCenterId << dataCenterLeftShift
                | machineId << machineLeftShift
                | sequence;
    }

    public long getTimeInMills() {
        return System.currentTimeMillis();
    }

    public long getNextTimeInMills(long timeStamp) {
        var nowTime = getTimeInMills();
        //        循环阻塞到获取到新的时间戳
        while (nowTime == timeStamp) {
            nowTime = getTimeInMills();
        }
        return nowTime;
    }


    public static void main(String[] args) throws InterruptedException {
        var sf1 = new Snowflake(10, 20);
        IntStream.range(0, 1000000).parallel().forEach(i -> System.out.println(sf1.nextId()));
        TimeUnit.MINUTES.sleep(1);
    }
}
