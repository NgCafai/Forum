package com.wujiahui.forum;

import com.wujiahui.forum.dao.DiscussPostMapper;
import com.wujiahui.forum.dao.LoginTicketMapper;
import com.wujiahui.forum.dao.MessageMapper;
import com.wujiahui.forum.dao.UserMapper;
import com.wujiahui.forum.entity.DiscussPost;
import com.wujiahui.forum.entity.LoginTicket;
import com.wujiahui.forum.entity.Message;
import com.wujiahui.forum.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Random;

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

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Value("${forum.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;



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


    @Test
    public void testInsertLoginTicket() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));

        loginTicketMapper.insertLoginTicket(loginTicket);
    }

    @Test
    public void testSelectLoginTicket() {
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);

        loginTicketMapper.updateStatus("abc", 1);
        loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);
    }

    @Test
    public void setHeaders() {
        List<User> users = userMapper.selectUsers();
        for (User user : users) {
            if (user.getId() <= 150) {
                userMapper.updateHeader(user.getId(), String.format(domain + contextPath + "/img/%d.jpg", new Random().nextInt(30)));
                userMapper.updateEmail(user.getId(), "null@sina.com");
            }
        }
    }





    @Test
    public void testSelectLetters() {
        List<Message> list = messageMapper.selectConversations(111, 0, 20);
        for (Message message : list) {
            System.out.println(message);
        }

        int count = messageMapper.selectConversationCount(111);
        System.out.println(count);

        list = messageMapper.selectMessages("111_112", 0, 10);
        for (Message message : list) {
            System.out.println(message);
        }

        count = messageMapper.selectMessageCount("111_112");
        System.out.println(count);

        count = messageMapper.selectUnreadMessageCount(131, "111_131");
        System.out.println(count);

    }
}
