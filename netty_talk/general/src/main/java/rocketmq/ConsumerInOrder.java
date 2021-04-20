package rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Program: netty_talk
 * @Description
 * @Author XieFeng
 * @Create 2020-12-29-16-17
 **/


public class ConsumerInOrder {
    public static void main(String[] args) throws Exception {
        var consumer=new DefaultMQPushConsumer("please_rename_unique_group_name_3");
        consumer.setNamesrvAddr("localhost:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("TopicTest","Tag||TagC||TagD");
        consumer.registerMessageListener((MessageListenerOrderly)(msgs,context)->{
            var random=new Random();
            context.setAutoCommit(true);
            for (var msg:msgs)
            {
                System.out.println("consumeThread="+Thread.currentThread().getName()+"queueId="+msg.getQueueId()
                +",content:"+new String(msg.getBody()));
            }

            try {
                TimeUnit.SECONDS.sleep(random.nextInt(10));
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return ConsumeOrderlyStatus.SUCCESS;
        });

        consumer.start();
        System.out.println("Consumer Started");
    }

}
