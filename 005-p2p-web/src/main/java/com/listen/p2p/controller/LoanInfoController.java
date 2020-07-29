package com.listen.p2p.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.listen.p2p.model.vo.BidInfoExtUser;
import com.listen.p2p.model.vo.BidInfoLoan;
import com.listen.p2p.model.vo.PaginationVo;
import com.listen.p2p.model.loan.LoanInfo;
import com.listen.p2p.service.BidInfoService;
import com.listen.p2p.service.LoanInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Listen
 * @Date: 2020/7/25
 */
@Controller
public class LoanInfoController {
    @Reference(interfaceClass = LoanInfoService.class, version = "1.0.0", check = false)
    private LoanInfoService loanInfoService;
    @Reference(interfaceClass = BidInfoService.class, version = "1.0.0", check = false)
    private BidInfoService bidInfoService;


    @RequestMapping("/loan/loan")
    public String loan(HttpServletRequest request, Model model,
                       @RequestParam(value = "productType", required = false) Integer productType,
                       @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                       @RequestParam(value = "pageSize", defaultValue = "9") Integer pageSize) {
        //准备分页查询的参数
        Map<String, Object> paramMap = new HashMap<>();
        //判断产品类型是否有值
        if (ObjectUtils.allNotNull(productType)) {
            paramMap.put("productType", productType);
        }
        paramMap.put("currentPage", (currentPage - 1) * pageSize);
        paramMap.put("pageSize", pageSize);

        //根据产品类型分页查询产品信息列表（产品类型，页码，每页显示条数）-> 每页展示的数据、总记录数
        //返回的数据封装到一个分页模型对象：PaginationVo，该对象有两个属性：List、Long
        PaginationVo<LoanInfo> paginationVo = loanInfoService.queryLoanInfoListByPage(paramMap);

        //计算总页数
        model.addAttribute("productType", productType);
        model.addAttribute("loanInfoList", paginationVo.getDataList());
        model.addAttribute("totalNum", paginationVo.getTotal());
        model.addAttribute("totalPages", Double.valueOf(Math.ceil(Double.valueOf(paginationVo.getTotal()) / pageSize)).intValue());
        model.addAttribute("currentPage", currentPage);

        //TODO
        //用户投资排行榜

        return "loan";
    }

    @RequestMapping("/loan/loanInfo/{id}")
    public String loanInfo(@PathVariable(required = true) Integer id,Model model) {
        //根据产品标识获取产品详情
        LoanInfo loanInfo = loanInfoService.queryLoanInfoById(id);
        model.addAttribute("loanInfo", loanInfo);
        //根据产品标识获取最近前10笔投资记录（产品标识，起始下标，截取长度 -> 返回List<BidInfoExtUser>）
        //如果查询显示的字符来自于多张表，处理方式如下：
        //1.创建一个VO对象来接收显示的属性【推荐】
        //2.创建一个扩展对象【推荐】
        //3.返回的是Map<String,Object>，那其中Map集合的key是查询SQL语句显示的字段名称
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("loanId", id);
        paramMap.put("currentPage", 0);
        paramMap.put("pageSize", 10);
        List<BidInfoExtUser> bidInfoExtUserList = bidInfoService.queryRecentlyBidInfoByLoanId(paramMap);
        model.addAttribute("bidInfoExtUserList", bidInfoExtUserList);

        //TODO
        //根据用户标识获取账户余额

        return "loanInfo";
    }
}
