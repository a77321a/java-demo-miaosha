package com.xsn.mq;


import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Component
public class MqProducer {
    
    private DefaultMQProducer producer;
    
    @Value("${mq.nameserver.addr}")
    private String nameAddr;
    @Value("${mq.topicname}")
    private String topicName;
    
    
    @PostConstruct
    public void init() throws MQClientException {
        producer = new DefaultMQProducer("producer_group");
        producer.setNamesrvAddr(nameAddr);
        producer.start();
    }
    
    //同步库存扣减消息
    public boolean asyncReduceStock(Integer goodsId, Integer amount) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        Map<String,Object> bodyMap = new HashMap<>();
        bodyMap.put("goodsId",goodsId);
        bodyMap.put("amount",amount);
        Message message = new Message(topicName,"increase", JSON.toJSON(bodyMap).toString().getBytes(Charset.forName("UTF-8")));
        try{
            producer.send(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (RemotingException e) {
            e.printStackTrace();
            return false;
        } catch (MQClientException e) {
            e.printStackTrace();
            return false;
        } catch (MQBrokerException e) {
            e.printStackTrace();
            return false;
        }
//        return producer.send(message);
        return true;
    }
    
}
