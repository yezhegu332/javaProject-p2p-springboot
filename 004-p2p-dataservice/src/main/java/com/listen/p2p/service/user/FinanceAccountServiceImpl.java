package com.listen.p2p.service.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.listen.p2p.mapper.user.FinanceAccountMapper;
import com.listen.p2p.model.user.FinanceAccount;
import com.listen.p2p.service.FinanceAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Listen
 * @Date: 2020/7/28
 */
@Component
@Service(interfaceClass = FinanceAccountService.class,version = "1.0.0",timeout = 15000)
public class FinanceAccountServiceImpl implements FinanceAccountService {
    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public FinanceAccount queryFinanceAccountByUid(Integer uid) {
        return financeAccountMapper.selectFinanceAccountByUid(uid);
    }
}
