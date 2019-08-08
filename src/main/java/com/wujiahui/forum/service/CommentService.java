package com.wujiahui.forum.service;

import com.wujiahui.forum.dao.CommentMapper;
import com.wujiahui.forum.dao.DiscussPostMapper;
import com.wujiahui.forum.entity.Comment;
import com.wujiahui.forum.entity.DiscussPost;
import com.wujiahui.forum.util.ForumConstant;
import com.wujiahui.forum.util.ForumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by NgCafai on 2019/8/8 15:05.
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    public List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentsByEntity(entityType, entityId, offset, limit);
    }

    public int findCommentCount(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

    /**
     * 作为事务，来向 comment 表中插入一条评论，
     * 并且更新 discuss_post 表中对应的 comment 字段（如果当前评论是针对帖子的话）
     * @param comment
     * @return
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        // 对 html 标签进行转义
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        int rows = commentMapper.insertComment(comment);

        if (comment.getEntityType() == ForumConstant.ENTITY_TYPE_POST) {
            int count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            discussPostMapper.updateCommentCount(comment.getEntityId(), count);
        }

        return rows;
    }

    public Comment findCommentById(int id) {
        return commentMapper.selectCommentById(id);
    }

}
