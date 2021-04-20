package rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @Program: netty_talk
 * @Description
 * @Author XieFeng
 * @Create 2020-12-29-13-46
 **/


public class Producer {
    public static void main(String[] args) throws Exception {
        var producer = new DefaultMQProducer("please_rename_unique_group_name");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();

        var tags = new String[]{"TagA", "TagB", "TagC", "TagD"};
        var orderStepList = new Producer().builderOrders();
        var date = LocalDateTime.now();
        var dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        for (int i = 0; i < 10; i++) {
            assert orderStepList != null;
            var body = dateStr + " Hello RocketMQ " + orderStepList.get(i);
            var msg = new Message("TopicTest", tags[i % tags.length], "KEY" + i, body.getBytes());
            var sendResult = producer.send(msg, (list, message, o) -> {
                var id = (Long) o;
                var index = id % list.size();
                return list.get((int) index);
            }, orderStepList.get(i).getOrderId());

            System.out.printf("SendResult status:%s,queueId:%d,body:%s%n"
                    , sendResult.getSendStatus()
                    , sendResult.getMessageQueue().getQueueId()
                    , body);
        }

        producer.shutdown();
    }

    private List<OrderStep> builderOrders() {
        var orderList = new ArrayList<OrderStep>(16);
        OrderStep orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111039L);
        orderDemo.setDesc("创建");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111065L);
        orderDemo.setDesc("创建");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111039L);
        orderDemo.setDesc("付款");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103117235L);
        orderDemo.setDesc("创建");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111065L);
        orderDemo.setDesc("付款");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103117235L);
        orderDemo.setDesc("付款");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111065L);
        orderDemo.setDesc("完成");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111039L);
        orderDemo.setDesc("推送");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103117235L);
        orderDemo.setDesc("完成");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111039L);
        orderDemo.setDesc("完成");
        orderList.add(orderDemo);

        return orderList;
    }

    private static class OrderStep {
        private long orderId;
        private String desc;

        public void setOrderId(long orderId) {
            this.orderId = orderId;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public long getOrderId() {
            return orderId;
        }

        @Override
        public String toString() {
            return "OrderStep{" +
                    "orderId=" + orderId +
                    ", desc='" + desc + '\'' +
                    '}';
        }
    }
}
