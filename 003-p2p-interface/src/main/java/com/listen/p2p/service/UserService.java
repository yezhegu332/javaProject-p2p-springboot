package com.listen.p2p.service;

import com.listen.p2p.model.user.User;

/**
 * @Author: Listen
 * @Date: 2020/7/25
 */
public interface UserService {
    //获取平台注册总人数
    Integer queryAllUserCount();

    //根据手机号查询用户
    User queryUserByPhone(String phone);

    User register(String phone, String passWord) throws Exception;

    //根据用户标识，更新用户信息
    int modifyUserById(User userUpdate);

}
