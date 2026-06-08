package com.bage.study.ai.best.practice.exception.analysis.context;

import java.time.LocalDateTime;

public class MqMetrics {

    private String mqName;
    private String topic;
    private String consumerGroup;
    private long messageCount;
    private long consumerCount;
    private double produceRate;
    private double consumeRate;
    private long lag;
    private double lagIncreaseRate;
    private String status;
    private LocalDateTime timestamp;

    public MqMetrics() {
    }

    public MqMetrics(String mqName, String topic, String consumerGroup,
                   long messageCount, long consumerCount,
                   double produceRate, double consumeRate,
                   long lag, double lagIncreaseRate, String status) {
        this.mqName = mqName;
        this.topic = topic;
        this.consumerGroup = consumerGroup;
        this.messageCount = messageCount;
        this.consumerCount = consumerCount;
        this.produceRate = produceRate;
        this.consumeRate = consumeRate;
        this.lag = lag;
        this.lagIncreaseRate = lagIncreaseRate;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public String getMqName() {
        return mqName;
    }

    public void setMqName(String mqName) {
        this.mqName = mqName;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public long getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(long messageCount) {
        this.messageCount = messageCount;
    }

    public long getConsumerCount() {
        return consumerCount;
    }

    public void setConsumerCount(long consumerCount) {
        this.consumerCount = consumerCount;
    }

    public double getProduceRate() {
        return produceRate;
    }

    public void setProduceRate(double produceRate) {
        this.produceRate = produceRate;
    }

    public double getConsumeRate() {
        return consumeRate;
    }

    public void setConsumeRate(double consumeRate) {
        this.consumeRate = consumeRate;
    }

    public long getLag() {
        return lag;
    }

    public void setLag(long lag) {
        this.lag = lag;
    }

    public double getLagIncreaseRate() {
        return lagIncreaseRate;
    }

    public void setLagIncreaseRate(double lagIncreaseRate) {
        this.lagIncreaseRate = lagIncreaseRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isBacklog() {
        return lag > 10000 || lagIncreaseRate > 0;
    }
}