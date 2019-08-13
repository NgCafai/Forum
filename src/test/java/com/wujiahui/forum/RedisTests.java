package com.wujiahui.forum;

import com.wujiahui.forum.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * Created by NgCafai on 2019/8/12 14:53.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTests {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testSerializer() {
        User user = new User();
        user.setId(5);
        user.setCreateTime(new Date());
        user.setUsername("name");
        redisTemplate.opsForValue().set("user", user);

        User user1 = (User) redisTemplate.opsForValue().get("user123");
        System.out.println(user1 == null);
//
//        redisTemplate.opsForValue().set("s", "string123");
//        System.out.println(redisTemplate.opsForValue().get("s"));

    }
}
