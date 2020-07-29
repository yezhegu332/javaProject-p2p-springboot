package com.listen.p2p.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.listen.constants.Constants;
import com.listen.p2p.model.loan.RechargeRecord;
import com.listen.p2p.model.user.FinanceAccount;
import com.listen.p2p.model.user.User;
import com.listen.p2p.model.vo.BidInfoLoan;
import com.listen.p2p.model.vo.IncomeRecordExtLoanInfoVo;
import com.listen.p2p.service.*;
import com.listen.utils.HttpClientUtils;
import com.listen.utils.Result;
import org.apache.commons.lang3.ObjectUtils;


import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @Author: Listen
 * @Date: 2020/7/27
 */
@Controller
public class UserController {
    @Reference(interfaceClass = UserService.class, version = "1.0.0", check = false)
    private UserService userService;
    @Reference(interfaceClass = RedisService.class, version = "1.0.0", check = false)
    private RedisService redisService;
    @Reference(interfaceClass = FinanceAccountService.class, version = "1.0.0", check = false)
    private FinanceAccountService financeAccountService;
    @Reference(interfaceClass = BidInfoService.class, version = "1.0.0", check = false)
    private BidInfoService bidInfoService;
    @Reference(interfaceClass = RechargeRecordService.class, version = "1.0.0", check = false)
    private RechargeRecordService rechargeRecordService;
    @Reference(interfaceClass = IncomeRecordService.class, version = "1.0.0", check = false)
    private IncomeRecordService incomeRecordService;


    /*@RequestMapping("/loan/page/register")
    public String pageRegister(){
        return "register";
    }*/

    //验证注册的手机号是否重复
    /*
     * 接口名称：验证手机号码是否重复
     * 接口地址：http://localhost:8080/p2p/loan/checkPhone
     * 请求方式：http、get、post
     * 请求示例：http://localhost:8080/p2p/loan/checkPhone?phone=13000000000
     * 请求参数：手机号码phone String必填
     * 响应参数：格式为JSON，{"code":1,success:true}
     */
    @ResponseBody
    @RequestMapping("/loan/checkPhone")
    public Object checkPhone(@RequestParam(value = "phone", required = true) String phone) {
        //验证手机号码是否重复（手机号码）-> 返回int|User|boolean|String
        User user = userService.queryUserByPhone(phone);

        //根据手机号码查询用户信息（手机号码）-> 返回User
        if (ObjectUtils.allNotNull(user)) {
            //该手机号已被注册，请更换手机号码
            return Result.error("该手机号已被注册，请更换手机号");
        }
        return Result.success();
    }

    //获取短信验证码
    @ResponseBody
    @RequestMapping("/loan/messageCode")
    public Result messageCode(HttpServletRequest request, @RequestParam(value = "phone", required = true) String phone) {
        String randomCode = "";
        try {
            Map<String, Object> paramMap = new HashMap<>();
            //调用京东万象平台的106短信接口，发送短信验证码的内容
            paramMap.put("appkey", "56061cde51a56e0a56a30390a92cef8a");
            paramMap.put("mobile", phone);
            randomCode = this.getRandomCode(6);
            String content = "【凯信通】您的验证码是：" + randomCode;
            paramMap.put("content", content);

            /*String jsonString = HttpClientUtils.doPost("https://way.jd.com/kaixintong/kaixintong", paramMap);*/
            //虚拟报文，用于测试
            String jsonString = "{\n" +
                    "                \"code\": \"10000\",\n" +
                    "                    \"charge\": false,\n" +
                    "                    \"remain\": 0,\n" +
                    "                    \"msg\": \"查询成功\",\n" +
                    "                    \"result\": \"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\" ?><returnsms>\\n <returnstatus>Success</returnstatus>\\n <message>ok</message>\\n <remainpoint>-1111611</remainpoint>\\n <taskID>101609164</taskID>\\n <successCounts>1</successCounts></returnsms>\"\n" +
                    "            }";
            //使用fastjson来解析json格式的字符串
            //将json格式的字符串转为json对象
            JSONObject jsonObject = JSONObject.parseObject(jsonString);

            //从json对象中获取json的值
            String code = jsonObject.getString("code");

            //判断通信是否成功
            if (!StringUtils.equals(code, "10000")) {
                return Result.error("短信平台通信异常");
            }

            //获取result所对应的xml格式的字符串
            String resultXmlString = jsonObject.getString("result");

            //使用dom4j+xpath来解析xml格式的字符串
            //添加dom4j的依赖
            //将xml格式的字符串转换为Document对象
            Document document = DocumentHelper.parseText(resultXmlString);
            //获取returnstatus节点的xpath路径表达式：
            // /returnsms/returnstatus 或 returnsms/returnstatus 或 //returnstatus
            //获取returnstatus节点的对象
            Node returnstatusNode = document.selectSingleNode("/returnsms/returnstatus");
            //获取returnstatus节点的文本内容
            String returnstatusNodeText = returnstatusNode.getText();
            //判断业务处理结果
            if (!StringUtils.equals(returnstatusNodeText, "Success")) {
                return Result.error("短信平台发送失败");
            }
            //将生成的随机数字处存放在redis缓存中
            redisService.set(phone, randomCode);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("短信平台发送失败");
        }
        return Result.success(randomCode);
    }

    //注册手机号
    @ResponseBody
    @RequestMapping("/loan/register")
    public Result register(HttpSession session,
                           @RequestParam(value = "phone", required = true) String phone,
                           @RequestParam(value = "loginPassword", required = true) String loginPassWord,
                           @RequestParam(value = "messageCode", required = true) String messageCode) {
        try {
            String messageCodeCheck = redisService.get(phone);
            if (StringUtils.equals(messageCode, messageCodeCheck)) {
                //用户注册【1.新增用户  2.新增账户】（手机号码、登录密码）-> 返回值User
                User user = userService.register(phone, loginPassWord);
                //将用户的信息存放到session中
                session.setAttribute(Constants.SESSION_USER, user);
            } else {
                return Result.error("请输入正确的验证码");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("注册失败，请重试");
        }
        return Result.success();
    }

    @ResponseBody
    @RequestMapping("/loan/myFinanceAccount")
    public FinanceAccount myFinanceAccount(HttpSession session) {
        //从session中获取用户信息
        User sessionUser = (User) session.getAttribute(Constants.SESSION_USER);
        //根据用户表示获取账户信息
        FinanceAccount financeAccount = financeAccountService.queryFinanceAccountByUid(sessionUser.getId());
        return financeAccount;
    }

    @ResponseBody
    @RequestMapping("/loan/realName")
    public Result realName(HttpSession session,
                           @RequestParam(value = "realName", required = true) String realName,
                           @RequestParam(value = "idCard", required = true) String idCard,
                           @RequestParam(value = "messageCode", required = true) String messageCode) {

        try {
            User sessionUser = (User) session.getAttribute(Constants.SESSION_USER);
            //从redis中获取短信验证码
            String redisMessageCode = redisService.get(sessionUser.getPhone());

            //判断用户输入的与redis中的短信验证码是否一致
            if (!StringUtils.equals(redisMessageCode, messageCode)) {
                return Result.error("请输入正确的短信验证码");
            }

            //调用京东万象平台的"实名认证二要素接口"来验证是否匹配
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("cardNo", idCard);
            paramMap.put("realName", realName);
            paramMap.put("appkey", "56061cde51a56e0a56a30390a92cef8a");
            /*String jsonString = HttpClientUtils.doPost("https://way.jd.com/youhuoBeijing/test", paramMap);*/
            //虚拟报文，用于测试
            String jsonString = "{\n" +
                    "    \"code\": \"10000\",\n" +
                    "    \"charge\": false,\n" +
                    "    \"remain\": 1305,\n" +
                    "    \"msg\": \"查询成功\",\n" +
                    "    \"result\": {\n" +
                    "        \"error_code\": 0,\n" +
                    "        \"reason\": \"成功\",\n" +
                    "        \"result\": {\n" +
                    "            \"realname\": \"孙悟空\",\n" +
                    "            \"idcard\": \"371423111111110031\",\n" +
                    "            \"isok\": true\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";
            //使用fastjson解析json
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            String codeStr = jsonObject.getString("code");

            //判断通信是否成功
            if(!StringUtils.equals(codeStr, "10000")){
                return Result.error("实名认证平台通信发生异常，请稍后再试");
            }

            //判断是否匹配
            boolean isok = jsonObject.getJSONObject("result").getJSONObject("result").getBoolean("isok");
            if(!isok){
                return Result.error("真实姓名与身份证号码不匹配,请重新输入");
            }

            //更新用户信息
            User updateUser = new User();
            updateUser.setId(sessionUser.getId());
            updateUser.setName(realName);
            updateUser.setIdCard(idCard);
            int modifyUserCount = userService.modifyUserById(updateUser);
            if(modifyUserCount < 1){
                return Result.error("实名认证失败");
            }

            //更新session中的信息
            sessionUser.setIdCard(idCard);
            sessionUser.setName(realName);
            session.setAttribute(Constants.SESSION_USER, sessionUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success();
    }

    //随机生成验证码
    private String getRandomCode(int count) {
        StringBuilder sb = new StringBuilder();
        /*for (int i = 0; i < count; i++) {
            int index = (int) Math.round(Math.random() * 9);
            sb.append(index);
        }*/
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int index = random.nextInt(10);
            sb.append(index);
        }
        return sb.toString();
    }

    /*@RequestMapping("/loan/page/realName")
    public String pageRealName() {
        return "realName";
    }*/

    @RequestMapping(value = "/loan/myCenter")
    public String myCenter(HttpSession session, Model model){
        User sessionUser = (User) session.getAttribute(Constants.SESSION_USER);
        FinanceAccount financeAccount = financeAccountService.queryFinanceAccountByUid(sessionUser.getId());
//        FinanceAccount financeAccount = financeAccountService.queryFinanceAccountByUid(1);
        model.addAttribute("financeAccount",financeAccount);
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("uid", sessionUser.getId());
//        paramMap.put("uid", 1);
        paramMap.put("currentPage", 0);
        paramMap.put("pageSize", 5);

        //根据用户id查询最近投资记录
        List<BidInfoLoan> bidInfoLoanList =  bidInfoService.queryRecentlyBidInfoListByUid(paramMap);
        model.addAttribute("bidInfoLoanList",bidInfoLoanList);
        //根据用户id查询最近充值记录
        List<RechargeRecord> rechargeRecordList =  rechargeRecordService.queryRecentlyRechargeRecordListByUid(paramMap);
        model.addAttribute("rechargeRecordList",rechargeRecordList);
        //根据用户id查询最近收益记录
        List<IncomeRecordExtLoanInfoVo> incomeRecordExtLoanInfoVoList = incomeRecordService.queryIncomeRecordExtLoanInfoVoListByUid(paramMap);
        model.addAttribute("incomeRecordExtLoanInfoVoList",incomeRecordExtLoanInfoVoList);
        return "myCenter";
    }

    @RequestMapping("/loan/logout")
    public String logout(HttpServletRequest request){
        request.getSession().removeAttribute(Constants.SESSION_USER);
        return "redirect:/index";
    }

}
