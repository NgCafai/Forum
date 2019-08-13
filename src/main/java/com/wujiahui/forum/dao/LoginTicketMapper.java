package com.wujiahui.forum.dao;

import com.wujiahui.forum.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;

/**
 * At the beginning of this project, we use MySQL to store LoginTicket, which
 * contains the information of a specific login action of one user.
 *
 * Later, we use Redis instead so as to improve the performance of this project
 *
 * Created by NgCafai on 2019/8/2 17:06.
 */
@Deprecated
@Mapper
public interface LoginTicketMapper {
    LoginTicket selectByTicket(String ticket);

    int insertLoginTicket(LoginTicket loginTicket);

    int updateStatus(String ticket, int status);
}
