package com.wujiahui.forum.util;

/**
 * Created by NgCafai on 2019/8/2 12:06.
 */
public interface ForumConstant {

    /**
     * successfully activate the new account
     */
    int ACTIVATION_SUCCESS = 0;

    /**
     * already activated
     */
    int ACTIVATION_REPEAT= 1;


    /**
     * fail to activate
     */
    int ACTIVATION_FAILURE = 2;

    /**
     * 默认状态的登录凭证的超时时间
     */
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    /**
     * 记住状态的登录凭证超时时间
     */
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;
}
