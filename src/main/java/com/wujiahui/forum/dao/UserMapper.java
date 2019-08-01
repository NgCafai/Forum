package com.wujiahui.forum.dao;

import com.wujiahui.forum.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by NgCafai on 2019/7/28 14:58.
 */
@Mapper
public interface UserMapper {
    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    // return the number of user having been inserted by this function
    int insertUser(User user);

    int updateStatus(int id, int status);

    int updateHeader(int id, String headerUrl);

    int updatePassword(int id, String password);
}
