package com.wujiahui.forum.dao;

import com.wujiahui.forum.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by NgCafai on 2019/8/8 14:45.
 */
@Mapper
public interface CommentMapper {

    Comment selectCommentById(int id);

    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    int selectCountByEntity(int entityType, int entityId);

    int insertComment(Comment comment);

}
