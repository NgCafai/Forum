package com.wujiahui.forum;

import com.wujiahui.forum.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ForumApplicationTests {

	@Test
	public void testGrammer() {
		User user = new User();
		System.out.println(user.getId());
		set(user);
		System.out.println(user.getId());
	}

	public void set(User user) {
		user.setId(100);
	}

}
