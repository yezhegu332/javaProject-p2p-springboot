package com.listen.p2p.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.listen.constants.Constants;
import com.listen.p2p.model.loan.LoanInfo;
import com.listen.p2p.service.BidInfoService;
import com.listen.p2p.service.LoanInfoService;
import com.listen.p2p.service.UserService;
import com.listen.utils.MoneyUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Listen
 * @Date: 2020/7/24
 */
@Controller
public class IndexController {
    @Reference(interfaceClass = LoanInfoService.class, version = "1.0.0", check = false)
    private LoanInfoService loanInfoService;

    @Reference(interfaceClass = BidInfoService.class, version = "1.0.0", check = false)
    private BidInfoService bidInfoService;

    @Reference(interfaceClass = UserService.class, version = "1.0.0", check = false)
    private UserService userService;


    @RequestMapping(value = {"/", "/index"})
    public String index(Model model) {
        //获取平台历史平均年化收益率
        Double historyAverageRate = loanInfoService.queryHistoryAverageRate();

        model.addAttribute(Constants.HISTORY_AVERAGE_RATE, historyAverageRate);
        //平台注册总人数
        Integer allUserCount = userService.queryAllUserCount();
        model.addAttribute(Constants.ALL_USER_COUNT, allUserCount);
        //平台累计投资金额
        Integer allBidMoney = bidInfoService.queryAllBidMoney();
        model.addAttribute(Constants.ALL_BID_MONEY, allBidMoney);

        //将以下查询看做是一个分页，实质上他们并不是一个分页功能
        //使用MYSQL中的limit关键字，limit 起始下标,截取长度
        //创建一个方法，根据产品类型获取产品列表（产品类型，(显示页码-1)*显示条数，显示条数） -> List<产品>
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("currentPage", 0);
        //新手宝，产品类型0，显示第1页，显示1个
        paramMap.put("productType", Constants.PRODUCT_TYPE_X);
        paramMap.put("pageSize", 1);
        List<LoanInfo> xLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);
        model.addAttribute("xLoanInfoList", xLoanInfoList);

        //优选产品，产品类型1，显示第1页，显示4个
        paramMap.put("productType", Constants.PRODUCT_TYPE_U);
        paramMap.put("pageSize", 4);
        List<LoanInfo> uLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);
        model.addAttribute("uLoanInfoList", uLoanInfoList);

        //散标产品，产品类型2，显示第1页，显示8个
        paramMap.put("productType", Constants.PRODUCT_TYPE_S);
        paramMap.put("pageSize", 8);
        List<LoanInfo> sLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);
        model.addAttribute("sLoanInfoList", sLoanInfoList);
        return "index";
    }
}
