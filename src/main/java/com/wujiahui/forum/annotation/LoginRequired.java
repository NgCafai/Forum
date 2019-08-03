package com.wujiahui.forum.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于对 METHOD 类型的 handler 进行标注，表明该方法需要登录以后才能访问
 *
 * Created by NgCafai on 2019/8/3 11:10.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {

}
