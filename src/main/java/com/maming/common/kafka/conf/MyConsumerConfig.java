package com.maming.common.kafka.conf;

public class MyConsumerConfig {

	private String consumerTopic = "kafkaTestFor2";

    private String consumerZookeeperConnect = "10.1.5.91:2181";
    private String productBrokerConnect = "10.1.5.91:9092";

    private String consumerGroupId = "dau_statistic_3";

    private String consumerZookeeperSessionTimeoutMs = "5000";

    private String consumerZookeeperSyncTimeMs = "5000";

    private String consumerAutoCommitIntervalMs = "5000";

	public String getConsumerTopic() {
		return consumerTopic;
	}

	public void setConsumerTopic(String consumerTopic) {
		this.consumerTopic = consumerTopic;
	}

	public String getConsumerZookeeperConnect() {
		return consumerZookeeperConnect;
	}

	public void setConsumerZookeeperConnect(String consumerZookeeperConnect) {
		this.consumerZookeeperConnect = consumerZookeeperConnect;
	}

	public String getConsumerGroupId() {
		return consumerGroupId;
	}

	public void setConsumerGroupId(String consumerGroupId) {
		this.consumerGroupId = consumerGroupId;
	}

	public String getConsumerZookeeperSessionTimeoutMs() {
		return consumerZookeeperSessionTimeoutMs;
	}

	public void setConsumerZookeeperSessionTimeoutMs(
			String consumerZookeeperSessionTimeoutMs) {
		this.consumerZookeeperSessionTimeoutMs = consumerZookeeperSessionTimeoutMs;
	}

	public String getConsumerZookeeperSyncTimeMs() {
		return consumerZookeeperSyncTimeMs;
	}

	public void setConsumerZookeeperSyncTimeMs(String consumerZookeeperSyncTimeMs) {
		this.consumerZookeeperSyncTimeMs = consumerZookeeperSyncTimeMs;
	}

	public String getConsumerAutoCommitIntervalMs() {
		return consumerAutoCommitIntervalMs;
	}

	public void setConsumerAutoCommitIntervalMs(String consumerAutoCommitIntervalMs) {
		this.consumerAutoCommitIntervalMs = consumerAutoCommitIntervalMs;
	}

	public String getProductBrokerConnect() {
		return productBrokerConnect;
	}

	public void setProductBrokerConnect(String productBrokerConnect) {
		this.productBrokerConnect = productBrokerConnect;
	}
    
}
