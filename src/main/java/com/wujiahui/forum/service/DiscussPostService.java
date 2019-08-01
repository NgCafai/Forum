package com.wujiahui.forum.service;

import com.wujiahui.forum.dao.DiscussPostMapper;
import com.wujiahui.forum.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by NgCafai on 2019/7/28 17:16.
 */
@Service
public class DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> findDiscussPosts(int userId, int offset, int countWanted) {
        return discussPostMapper.selectDiscussPosts(userId, offset, countWanted);
    }

    public int findDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }
}
