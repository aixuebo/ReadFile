package com.maming.common.kafka.product;


import java.util.List;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import com.maming.common.kafka.conf.MyConsumerConfig;

public class MyProduct {

	  private Producer<String, String> producer;
	  private String topic;
    
    public MyProduct(){
    	MyConsumerConfig consumerConfig = new MyConsumerConfig();
    	this.topic = consumerConfig.getConsumerTopic();
    	producer = new Producer<String, String>(MyProduct.createProducerConfig(consumerConfig));
    }
    
    public void produceMsg(long key, String msg) {
        producer.send(new KeyedMessage<String, String>(topic,Long.toString(key), msg));
      }

      //todo 消息类型 1：String， 2：byte[]
      public void produceMsgList(List<KeyedMessage<String, String>> msgList) {
        producer.send(msgList);
      }

      public void close() {
        producer.close();
      }

      public String getTopic() {
        return topic;
      }

    private static ProducerConfig createProducerConfig(MyConsumerConfig consumerConfig) {
        Properties props = new Properties();
        props.put("metadata.broker.list", consumerConfig.getProductBrokerConnect());
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        //props.put("partitioner.class", MsgPartitioner.class.getCanonicalName());
        //props.put("request.required.acks", "1");
        return new ProducerConfig(props);
    }
    
    public static void main(String[] args) {
    	MyProduct myProduct = new MyProduct();
    	
    	int i=10;
    	
    	while(i<20){
    		myProduct.produceMsg(i, "xxx"+i);
    		i++;
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
	}
}
