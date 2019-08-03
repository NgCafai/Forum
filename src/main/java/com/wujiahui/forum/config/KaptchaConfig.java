package com.wujiahui.forum.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;

import java.util.Properties;

/**
 * 将 Producer 实现类的对象装配进 spring 容器中
 *
 * Created by NgCafai on 2019/8/2 15:22.
 */
@Configuration
public class KaptchaConfig {

    @Bean
    public Producer kaptchaProducer() {
        DefaultKaptcha kaptcha = new DefaultKaptcha();

        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width", "100");
        properties.setProperty("kaptcha.image.height", "40");
        properties.setProperty("kaptcha.textproducer.font.size", "32");
        properties.setProperty("kaptcha.textproducer.font.color", "0,0,0");
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYAZ");
        properties.setProperty("kaptcha.textproducer.char.length", "5");

        // 决定采用哪个干扰类，这里是配置不使用干扰
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");

        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }
}
