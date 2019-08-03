package com.wujiahui.forum.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Created by NgCafai on 2019/8/1 18:33.
 */
public class ForumUtil {
    // generate random String
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // use MD5 to encrypt a String
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    // get the cookie value corresponding to the specified cookie name
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
