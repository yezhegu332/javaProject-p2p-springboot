package com.listen.p2p.service;

import com.listen.p2p.model.user.FinanceAccount;

/**
 * @Author: Listen
 * @Date: 2020/7/28
 */
public interface FinanceAccountService {
    FinanceAccount queryFinanceAccountByUid(Integer uid);
}
