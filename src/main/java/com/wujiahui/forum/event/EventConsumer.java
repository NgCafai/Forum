package com.wujiahui.forum.event;

import com.alibaba.fastjson.JSONObject;
import com.wujiahui.forum.entity.DiscussPost;
import com.wujiahui.forum.entity.Event;
import com.wujiahui.forum.entity.Message;
import com.wujiahui.forum.service.DiscussPostService;
import com.wujiahui.forum.service.ElasticsearchService;
import com.wujiahui.forum.service.MessageService;
import com.wujiahui.forum.util.ForumConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by NgCafai on 2019/8/16 16:13.
 */
@Component
public class EventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private ElasticsearchService elasticsearchService;

    @KafkaListener(topics = {ForumConstant.TOPIC_COMMENT})
    public void handleCommentMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("消息内容为空！");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("消息格式错误！");
            return;
        }

        // 准备系统通知，并插入 message 表
        Message message = new Message();
        message.setFromId(ForumConstant.SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());

        // 设置消息的内容
        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());

        Map<String, Object> data = event.getData();
        if (!data.isEmpty()) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                content.put(entry.getKey(), entry.getValue());
            }
        }

        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);
    }

    /**
     * 消费发帖事件：将新发的帖子或者回帖加入到 Elasticsearch 中
     */
    @KafkaListener(topics = {ForumConstant.TOPIC_PUBLISH})
    public void handlePublishPost(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("消息内容为空");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("消息格式错误");
            return;
        }

        DiscussPost discussPost = discussPostService.findDiscussPostById(event.getEntityId());
        elasticsearchService.saveDiscussPost(discussPost);
    }

}
