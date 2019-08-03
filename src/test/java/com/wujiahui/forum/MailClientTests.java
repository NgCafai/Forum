package com.wujiahui.forum;

import com.wujiahui.forum.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * Test whether email can be sent using MailClient
 *
 * Created by NgCafai on 2019/8/1 17:59.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MailClientTests {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testHtmlMail() throws Exception{
        Context context = new Context();
        context.setVariable("name", "man");
        String content = templateEngine.process("/demo/email-demo", context);

        mailClient.sendMail("779717276@qq.com", "test mail client", content);

    }

}
