package com.listen.p2p.service.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.listen.constants.Constants;
import com.listen.p2p.mapper.user.FinanceAccountMapper;
import com.listen.p2p.mapper.user.UserMapper;
import com.listen.p2p.model.user.FinanceAccount;
import com.listen.p2p.model.user.User;
import com.listen.p2p.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Listen
 * @Date: 2020/7/25
 */
@Component
@Service(interfaceClass = UserService.class, version = "1.0.0", timeout = 15000)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private FinanceAccountMapper financeAccountMapper;


    @Override
    public Integer queryAllUserCount() {
        //获取到操作制定key的操作对象
        BoundValueOperations<Object, Object> boundValueOps = redisTemplate.boundValueOps(Constants.ALL_USER_COUNT);
        //从该对象中获取注册总人数
        Integer allUserCount = (Integer) boundValueOps.get();
        //判断
        if (!ObjectUtils.allNotNull(allUserCount)) {
            synchronized (this) {
                allUserCount = (Integer) boundValueOps.get();
                if (!ObjectUtils.allNotNull(allUserCount)) {
                    allUserCount = userMapper.selectAllUserCount();
                    boundValueOps.set(allUserCount, 25, TimeUnit.SECONDS);
                } else {
                    allUserCount = (Integer) boundValueOps.get();
                }
            }
        }
        return allUserCount;
    }

    @Override
    public User queryUserByPhone(String phone) {
        return userMapper.selectUserByPhone(phone);
    }

    @Override
    @Transactional
    public User register(String phone, String loginPassWord) throws Exception {
        //1.新增用户
        User userRecord = new User();
        userRecord.setPhone(phone);
        userRecord.setLoginPassword(loginPassWord);
        userRecord.setAddTime(new Date());
        userRecord.setLastLoginTime(new Date());

        int userInsertCount = userMapper.insertSelective(userRecord);
        if (userInsertCount < 0) {
            throw new Exception();
        }

        //2.新增账户
        FinanceAccount financeAccount = new FinanceAccount();
        financeAccount.setUid(userRecord.getId());
        financeAccount.setAvailableMoney(888.0);
        int insertFinanceAccountCount = financeAccountMapper.insertSelective(financeAccount);
        if (insertFinanceAccountCount < 0) {
            throw new Exception();
        }
        return userRecord;
    }

    @Override
    public int modifyUserById(User userUpdate) {
        return userMapper.updateByPrimaryKeySelective(userUpdate);
    }
}
