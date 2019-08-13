package com.wujiahui.forum.util;

/**
 * Created by NgCafai on 2019/8/12 12:54.
 */
public class RedisKeyUtil {
    private static final String SPLIT = ":";
    private static final String PREFIX_KAPTCHA = "kaptcha";
    private static final String PREFIX_TICKET = "ticket";
    private static final String PREFIX_USER = "user";

    public static String getKaptchaKey(String owner) {
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    public static String getTicketKey(String ticket) {
        return PREFIX_TICKET + SPLIT + ticket;
    }

    public static String getUserKey(int id) {
        return PREFIX_USER + SPLIT + id;
    }
}
