package com.listen.p2p.service.loan;

import com.alibaba.dubbo.config.annotation.Service;
import com.listen.constants.Constants;
import com.listen.p2p.mapper.loan.BidInfoMapper;
import com.listen.p2p.mapper.loan.LoanInfoMapper;
import com.listen.p2p.model.vo.BidInfoExtUser;
import com.listen.p2p.model.vo.PaginationVo;
import com.listen.p2p.model.loan.LoanInfo;
import com.listen.p2p.service.LoanInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Listen
 * @Date: 2020/7/25
 */
@Component
@Service(interfaceClass = LoanInfoService.class, version = "1.0.0", timeout = 15000)
public class LoanInfoInfoServiceImpl implements LoanInfoService {
    @Autowired
    private LoanInfoMapper loanInfoMapper;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public Double queryHistoryAverageRate() {
        //设置RedisTemplate对象的RedisSerializer属性的方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        Double historyAverageRate = (Double) redisTemplate.opsForValue().get(Constants.HISTORY_AVERAGE_RATE);
        if (!ObjectUtils.allNotNull(historyAverageRate)) {
            synchronized (this) {
                historyAverageRate = (Double) redisTemplate.opsForValue().get(Constants.HISTORY_AVERAGE_RATE);
                if (!ObjectUtils.allNotNull(historyAverageRate)) {
                    historyAverageRate = loanInfoMapper.selectHistoryAverageRate();
                    redisTemplate.opsForValue().set(Constants.HISTORY_AVERAGE_RATE, historyAverageRate, 25, TimeUnit.SECONDS);
                } else {
                    historyAverageRate = (Double) redisTemplate.opsForValue().get(Constants.HISTORY_AVERAGE_RATE);
                }
            }
        }
        return historyAverageRate;
    }

    @Override
    public List<LoanInfo> queryLoanInfoListByProductType(Map<String, Object> paramMap) {
        return loanInfoMapper.selectLoanInfoListByProductType(paramMap);
    }

    @Override
    public PaginationVo<LoanInfo> queryLoanInfoListByPage(Map<String, Object> paramMap) {
        PaginationVo<LoanInfo> paginationVo = new PaginationVo<>();
        //查询总记录数
        Long total = loanInfoMapper.selectTotal(paramMap);
        paginationVo.setTotal(total);

        //每页展示的数据集合
        List<LoanInfo> loanInfoList = loanInfoMapper.selectLoanInfoListByProductType(paramMap);
        paginationVo.setDataList(loanInfoList);
        return paginationVo;
    }

    @Override
    public LoanInfo queryLoanInfoById(Integer id) {
        return loanInfoMapper.selectByPrimaryKey(id);
    }


}
