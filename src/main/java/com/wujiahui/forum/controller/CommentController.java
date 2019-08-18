package com.wujiahui.forum.controller;

import com.wujiahui.forum.annotation.LoginRequired;
import com.wujiahui.forum.entity.Comment;
import com.wujiahui.forum.entity.DiscussPost;
import com.wujiahui.forum.entity.Event;
import com.wujiahui.forum.entity.User;
import com.wujiahui.forum.event.EventProducer;
import com.wujiahui.forum.service.CommentService;
import com.wujiahui.forum.service.DiscussPostService;
import com.wujiahui.forum.util.ForumConstant;
import com.wujiahui.forum.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * Created by NgCafai on 2019/8/8 22:59.
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @LoginRequired
    @RequestMapping(path = "/comment/add/{discussPostId}")
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {
        User currentUser = hostHolder.getUser();
        comment.setUserId(currentUser.getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        // 触发评论事件：通过系统通知来告诉目标用户，他的帖子或评论收到了回复
        Event event = new Event()
                .setTopic(ForumConstant.TOPIC_COMMENT)
                .setUserId(currentUser.getId())
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("postId", discussPostId);

        // 根据当前 comment 的类型来判断应该来判断应该向谁发出通知，即设置 event 的 entityUserId
        if (comment.getEntityType() == ForumConstant.ENTITY_TYPE_POST) {
            // 这是对帖子直接进行回复的情况
            DiscussPost post = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(post.getUserId());

            // 发送时间到 Kafka
            eventProducer.fireEvent(event);
        } else if (comment.getEntityType() == ForumConstant.ENTITY_TYPE_COMMENT) {
            // 这是对回帖 / 评论进行回复的情况，此时可能需要通知帖子的作者、回帖作者、回复的作者
            // targetComment 属于回帖，即直接对帖子的评论
            Comment targetComment = commentService.findCommentById(comment.getEntityId());
            if (currentUser.getId() != targetComment.getUserId()) {
                event.setEntityUserId(targetComment.getUserId());
            }
            eventProducer.fireEvent(event);

            if (comment.getTargetId() != 0) {
                // 这是对回帖下的评论进行回复的情况
                event.setEntityUserId(comment.getTargetId());
                eventProducer.fireEvent(event);
            }

            // 通知帖子的作者
            DiscussPost discussPost = discussPostService.findDiscussPostById(discussPostId);
            if (currentUser.getId() != discussPost.getUserId()) {
                Event event2 = new Event()
                        .setTopic(ForumConstant.TOPIC_COMMENT)
                        .setUserId(currentUser.getId())
                        .setEntityType(ForumConstant.ENTITY_TYPE_POST)
                        .setEntityId(discussPostId)
                        .setData("postId", discussPostId);
                event2.setEntityUserId(discussPost.getUserId());
                eventProducer.fireEvent(event2);
            }
        }
        return "redirect:/post/detail/" + discussPostId;
    }
}
