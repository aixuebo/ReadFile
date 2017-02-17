package com.maming.common.kafka.conf;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class MsgPartitioner implements Partitioner {

	public MsgPartitioner(VerifiableProperties v){
		
	}
	
    @Override
    public int partition(Object key, int partitionsNum) {
    	return Math.abs(key.hashCode() % partitionsNum);
    }

}
