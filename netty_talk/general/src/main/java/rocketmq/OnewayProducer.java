package rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @Program: netty_talk
 * @Description
 * @Author XieFeng
 * @Create 2020-12-28-13-49
 **/


public class OnewayProducer {
    public static void main(String[] args) throws Exception {
        var producer=new DefaultMQProducer();
        producer.setNamesrvAddr("localhost:9876");
        producer.start();

        for (int i=0;i<100;i++)
        {
            var msg=new Message("TopicTest"
                    ,"TagA"
                    ,("Hello MQ"+i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.sendOneway(msg);
        }

        producer.shutdown();

    }
}
