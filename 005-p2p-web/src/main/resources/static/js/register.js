//错误提示
function showError(id, msg) {
    $("#" + id + "Ok").hide();
    $("#" + id + "Err").html("<i></i><p>" + msg + "</p>");
    $("#" + id + "Err").show();
    $("#" + id).addClass("input-red");
}

//错误隐藏
function hideError(id) {
    $("#" + id + "Ok").hide();
    $("#" + id + "Err").hide();
    $("#" + id + "Err").html("");
    $("#" + id).removeClass("input-red");
}

//显示成功
function showSuccess(id) {
    $("#" + id + "Err").hide();
    $("#" + id + "Err").html("");
    $("#" + id + "Ok").show();
    $("#" + id).removeClass("input-red");
}


//打开注册协议弹层
function alertBox(maskid, bosid) {
    $("#" + maskid).show();
    $("#" + bosid).show();
}

//关闭注册协议弹层
function closeBox(maskid, bosid) {
    $("#" + maskid).hide();
    $("#" + bosid).hide();
}

//注册协议确认
$(function () {
    $("#agree").click(function () {
        var ischeck = document.getElementById("agree").checked;
        if (ischeck) {
            $("#btnRegist").attr("disabled", false);
            $("#btnRegist").removeClass("fail");
        } else {
            $("#btnRegist").attr("disabled", "disabled");
            $("#btnRegist").addClass("fail");
        }
    });

    //获取光标，隐藏提示显示
    $("#phone").focus(function () {
        hideError("phone");
    })
    $("#loginPassword").focus(function () {
        hideError("loginPassword");
    })
    $("#messageCode").focus(function () {
        hideError("messageCode");
    })

    //验证手机号
    $("#phone").on("blur", function () {
        var phone = $.trim($("#phone").val());
        if ("" == phone) {
            showError("phone", "请输入手机号");
        } else if (!/^1[1-9]\d{9}$/.test(phone)) {
            showError("phone", "请输入正确格式的手机号")
        } else {
            $.ajax({
                url: contextPath + "/loan/checkPhone",
                data: "phone=" + phone,
                type: "get",
                success: function (data) {
                    if (data.code == 1) {
                        showSuccess("phone");
                    } else {
                        showError("phone", data.message);
                    }
                },
                error: function () {
                    showError("phone", "系统繁忙，请稍后重试");
                }
            })
            showSuccess("phone");
        }
    })

    //验证登录密码
    $("#loginPassword").on("blur", function () {
        var loginPassword = $.trim($("#loginPassword").val());
        if ("" == loginPassword) {
            showError("loginPassword", "请输入登录密码");
        } else if (!/^[0-9a-zA-Z]+$/.test(loginPassword)) {
            showError("loginPassword", "密码字符只可使用数字和大小写英文字母");
        } else if (!/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(loginPassword)) {
            showError("loginPassword", "密码应同时包含英文和数字");
        } else {
            showSuccess("loginPassword");
        }
    })

    //判断验证码有无数据
    $("#messageCode").on("blur",function () {
        var messageCode = $.trim($("#messageCode").val());
        if ("" == messageCode) {
            showError("messageCode", "请输入短信验证码");
        } else {
            showSuccess("messageCode");
        }
    })

    //点击"注册"按钮
    $("#btnRegist").click(function () {
        //触发失去焦点事件
        $("#phone").blur();
        $("#loginPassword").blur();
        $("#messageCode").blur();

        //获取div标签中id属性值以Err结尾的div
        //方式一：
        /*var flag = true;
        $("div[id$='Err']").each(function () {
            var errorHtml = $(this).text();
            if (errorHtml != "") {
                flag = false;
                return;
            }
        })
        if(flag){
            alert("发起注册请求");
        }*/

        //方式二：
        var errorText = $("div[id$='Err']").text();
        if (errorText == "") {
            var phone = $.trim($("#phone").val());
            $("#loginPassword").val($.md5($("#loginPassword").val()));
            var loginPassword = $("#loginPassword").val();
            var messageCode = $("#messageCode").val();
            $.ajax({
                url: contextPath + "/loan/register",
                data: {
                    phone: phone,
                    loginPassword: loginPassword,
                    messageCode: messageCode
                },
                type: "post",
                success: function (data) {
                    if (data.code == 1) {
                        window.location.href = contextPath + "/loan/page/realName";
                    } else {
                        showError("messageCode", data.message);
                        $("#loginPassword").val("");
                        $("#message").val("");
                        hideError("loginPassword");
                        hideError("message");
                    }
                },
                error: function () {
                    showError("messageCode", "系统繁忙，请稍后重试");
                }
            })
        }
    })

    //点击"获取验证码"按钮
    $("#messageCodeBtn").on("click", function () {
        if (!$("#messageCodeBtn").hasClass("on")) {
            //判断其他元素是否通过，只有验证通过才可以进行倒计时
            //触发失去焦点的事件
            $("#phone").blur();
            $("#loginPassword").blur();
            hideError("messageCode");
            //获取错误消息
            var errorTexts = $("div[id$='Err']").text();
            if (errorTexts == "") {
                var phone = $.trim($("#phone").val());
                $.ajax({
                    url: contextPath + "/loan/messageCode",
                    data: "phone=" + phone,
                    type: "get",
                    success: function (data) {
                        alert("您的短信验证码是：" + data.data);
                        if (data.code == 1) {
                            $.leftTime(60, function (d) {
                                //d.status true说明正在倒计时
                                //d.status false说明倒计时结束
                                //d.s，倒计时秒
                                //判断是否正在倒计时
                                if (d.status) {
                                    $("#messageCodeBtn").addClass("on");
                                    $("#messageCodeBtn").html((d.s == "00" ? "60" : d.s) + "s后获取");
                                } else {
                                    $("#messageCodeBtn").removeClass("on");
                                    $("#messageCodeBtn").html("获取验证码");
                                }
                            })
                        } else {
                            showError("messageCode", data.message);
                        }
                    },
                    error: function () {
                        showError("messageCode", "短信平台异常，请稍后重试");
                    }
                })
            }
        }
    })
});

