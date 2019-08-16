package com.wujiahui.forum;

import com.wujiahui.forum.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ForumApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(ForumApplicationTests.class);

	@Test
	public void testLogger() {
		System.out.println(logger.getName());

		logger.debug("debug 日志");
		logger.info("info 日志");
		logger.warn("warn log");
		logger.error("error log");
	}

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
