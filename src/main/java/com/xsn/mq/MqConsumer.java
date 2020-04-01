package com.xsn.mq;

import com.alibaba.fastjson.JSON;
import com.xsn.dao.GoodsStockDOMapper;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Component
public class MqConsumer {
//    绑定consumer
    private DefaultMQPushConsumer consumer;
    @Value("${mq.nameserver.addr}")
    private String nameAddr;
    @Value("${mq.topicname}")
    private String topicName;
    @Autowired
    private GoodsStockDOMapper goodsStockDOMapper;
    @PostConstruct
    public void init() throws MQClientException {
        consumer = new DefaultMQPushConsumer("stock_consumer_group");
        consumer.setNamesrvAddr(nameAddr);
        consumer.subscribe(topicName,"*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
//                实现扣减逻辑
                Message msg = list.get(0);
                String jsonStr = new String(msg.getBody());
                Map<String,Object> map = JSON.parseObject(jsonStr, Map.class);
                Integer goodsId = (Integer) map.get("goodsId");
                Integer amount = (Integer) map.get("amount");
                goodsStockDOMapper.desStorck(goodsId,amount);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
    }
    
}
