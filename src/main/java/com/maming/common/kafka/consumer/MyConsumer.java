package com.maming.common.kafka.consumer;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import com.maming.common.kafka.conf.MyConsumerConfig;
import com.maming.common.kafka.conf.StoppableProcessor;

public class MyConsumer implements Iterator<String>,StoppableProcessor{

    private ConsumerConnector consumer;
    private ConsumerIterator<byte[], byte[]> consumerIterator;
    
    public MyConsumer(){
    	MyConsumerConfig consumerConfig = new MyConsumerConfig();
    	consumer = Consumer.createJavaConsumerConnector(createConsumerConfig(consumerConfig));
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(consumerConfig.getConsumerTopic(), 1); //设置一个线程处理

        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(consumerConfig.getConsumerTopic());
        consumerIterator = streams.get(0).iterator();//只有一个线程处理，取第一个即可
        
    }
    
    @Override
    public boolean hasNext() {
        return consumerIterator.hasNext();
    }

    @Override
    public String next() {
        MessageAndMetadata<byte[], byte[]> msgMeta = consumerIterator.next();
        return new String(msgMeta.message());
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void start() throws Exception {
        System.out.println("read register info service start");
    }

    @Override
    public void shutdown() throws Exception {
        if (null != consumer) {
            try {
                consumer.commitOffsets();
                consumer.shutdown();
            } catch (Exception e) {
                System.out.println("shutdown register info consumer error");
                e.printStackTrace();
            }
        }

        System.out.println("read register info service stop");
    }
    
    public void commitOffsets() throws Exception {
        if (null != consumer) {
            try {
                consumer.commitOffsets();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("read register info service commitOffsets");
    }
    

    private static ConsumerConfig createConsumerConfig(MyConsumerConfig consumerConfig) {
        Properties props = new Properties();
        props.put("zookeeper.connect", consumerConfig.getConsumerZookeeperConnect());
        props.put("group.id", consumerConfig.getConsumerGroupId());
        props.put("auto.offset.reset", "smallest");
        props.put("zookeeper.session.timeout.ms", consumerConfig.getConsumerZookeeperSessionTimeoutMs());
        props.put("zookeeper.sync.time.ms", consumerConfig.getConsumerZookeeperSyncTimeMs());
        props.put("auto.commit.interval.ms", consumerConfig.getConsumerAutoCommitIntervalMs());
        return new ConsumerConfig(props);
    }
    
    public static void main(String[] args) {
    	MyConsumer myConsumer = new MyConsumer();
    	while(true){
        	if(myConsumer.hasNext()){
        		System.out.println(myConsumer.next());
        	/*	try {
					myConsumer.commitOffsets();
				} catch (Exception e) {
					e.printStackTrace();
				}*/
        	}else{
        		System.out.println("false");
        	}
        	/*try {
    			Thread.sleep(2000l);
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}*/
    	}
    	
	}
}