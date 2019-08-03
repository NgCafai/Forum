package com.wujiahui.forum.util;

import com.wujiahui.forum.entity.User;
import org.springframework.stereotype.Component;

/**
 * Contain a user object for each thread
 * Created by NgCafai on 2019/8/2 20:39.
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> user = new ThreadLocal<>();

    public User getUser() {
        return user.get();
    }

    public void setUser(User user) {
        HostHolder.user.set(user);
    }

    public void remove() {
        user.remove();
    }
}
