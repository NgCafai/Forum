package com.wujiahui.forum.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 在特定事件发生后，使用 Event 对象封装事件的信息，然后放进消息队列中，
 * 再由对应的消费者处理事件
 *
 * Created by NgCafai on 2019/8/15 17:04.
 */
public class Event {

    private String topic;
    // 触发当前事件的用户的 id
    private int userId;
    private int entityType;
    private int entityId;
    // 目标用户的 id。 例如当前用户对另一用户的帖子进行评论时，用于给目标用户发通知
    private int entityUserId;
    // 存储额外信息
    private Map<String, Object> data = new HashMap<>();

    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

}
