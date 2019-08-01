package com.wujiahui.forum;

import com.wujiahui.forum.dao.DiscussPostMapper;
import com.wujiahui.forum.dao.UserMapper;
import com.wujiahui.forum.entity.DiscussPost;
import com.wujiahui.forum.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by NgCafai on 2019/7/28 15:30.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void testSelectUser() {
        User user = userMapper.selectById(101);
        System.out.println(user);

        user = userMapper.selectByName("liubei");
        System.out.println(user);

        user = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }

    @Test
    public void testInsertUser() {
        User user = new User();
        user.setUsername("小明");

        userMapper.insertUser(user);

        user = userMapper.selectByName("小明");
        System.out.println(user);
    }

    @Test
    public void updateUser() {
        int rows = userMapper.updateStatus(150, 1);
        System.out.println(rows);

        rows = userMapper.updateHeader(150, "http://www.nowcoder.com/102.png");
        System.out.println(rows);

        rows = userMapper.updatePassword(150, "hello");
        System.out.println(rows);
    }

    @Test
    public void testDiscussPostMapper() {
        List<DiscussPost> posts = discussPostMapper.selectDiscussPosts(0, 0, 5);
        for (DiscussPost post : posts) {
            System.out.println(post);
        }

        System.out.println(discussPostMapper.selectDiscussPostRows(0));
    }

}
