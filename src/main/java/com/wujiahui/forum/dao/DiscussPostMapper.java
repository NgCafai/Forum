package com.wujiahui.forum.dao;

import com.wujiahui.forum.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by NgCafai on 2019/7/28 16:48.
 */
@Mapper
public interface DiscussPostMapper {

    // return posts of the specified user if userId != 0;
    // otherwise, return posts no matter who posted them.
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int countWanted);

    int selectDiscussPostRows(@Param("userId") int userId);
}
