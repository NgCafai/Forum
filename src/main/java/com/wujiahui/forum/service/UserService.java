package com.wujiahui.forum.service;

import com.wujiahui.forum.dao.UserMapper;
import com.wujiahui.forum.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by NgCafai on 2019/7/28 17:24.
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }
}
