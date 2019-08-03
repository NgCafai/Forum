package com.wujiahui.forum.dao;

import com.wujiahui.forum.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by NgCafai on 2019/8/2 17:06.
 */
@Mapper
public interface LoginTicketMapper {
    LoginTicket selectByTicket(String ticket);

    int insertLoginTicket(LoginTicket loginTicket);

    int updateStatus(String ticket, int status);
}
