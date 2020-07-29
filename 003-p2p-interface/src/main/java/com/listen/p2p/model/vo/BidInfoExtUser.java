package com.listen.p2p.model.vo;

import com.listen.p2p.model.loan.BidInfo;
import com.listen.p2p.model.user.User;

/**
 * @Author: Listen
 * @Date: 2020/7/27
 */
public class BidInfoExtUser extends BidInfo {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "BidInfoExtUser{" +
                "user=" + user +
                '}';
    }
}
