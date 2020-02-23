package com.xsn.controller;


import com.xsn.controller.viewobject.UserVO;
import com.xsn.error.BusinessException;
import com.xsn.error.EmBusinessError;
import com.xsn.response.CommonReturnType;
import com.xsn.service.UserService;
import com.xsn.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;
@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
//    httpServletRequest是一个单例模式
//    Bean
//    本质是一个proxy 内部threadlocal方式 处理线程对应的request
    private HttpServletRequest httpServletRequest;
    //用户注册
    @RequestMapping(value = "/login",method = {RequestMethod.POST},consumes = {"application/x-www-form-urlencoded"})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name="mobile")String mobile,
                                  @RequestParam(name="password")String password
                                  ) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        UserModel userModel =  userService.login(mobile,password);
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);
        UserVO userVO = convertFromModel(userModel);
        return CommonReturnType.create(userVO);
    }



    //用户注册
    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes = {"application/x-www-form-urlencoded"})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name="mobile")String mobile,
                                     @RequestParam(name="otpCode")String otpCode,
                                     @RequestParam(name="name")String name,
                                     @RequestParam(name="password")String password,
                                     @RequestParam(name="gender")Integer gender,
                                     @RequestParam(name="age") Integer age
                                     ) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号与对应otpcode
        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(mobile);
        if(!com.alibaba.druid.util.StringUtils.equals(otpCode,inSessionOtpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
        }
        UserModel userModel = new UserModel();
        userModel.setAge(age);
        userModel.setGender(gender.byteValue());
        userModel.setMobile(mobile);
        userModel.setName(name);
        userModel.setRegisterMode("byMobile");
        userModel.setEncrptPassword(this.EncodeBySecurity(password));
        userService.register(userModel);
        return  CommonReturnType.create(null);
    }
    public String EncodeBySecurity(String str){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(str);
    }
    // 用户获取otp短信接口
    @RequestMapping(value = "/getotp",method = {RequestMethod.POST},consumes = {"application/x-www-form-urlencoded"})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name="mobile")String mobile){
        //按照一定规则生成otp验证码
        Random random = new Random();
        int randomInt = random.nextInt(899999);
        randomInt+=100000;
        String optCode = String.valueOf(randomInt);
        // 将opt验证码同对应用户手机号关联 使用httpsession的方式绑定otpcode与mobile
        httpServletRequest.getSession().setAttribute(mobile,optCode);
        //将OTP验证码通过短信通道发送给用户 省略
        System.out.println("mobile："+mobile+"optCode："+optCode);
        return CommonReturnType.create(null);
    }
    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id")Integer id) throws BusinessException {
//        调用service
       UserModel userModel =  userService.getUserById(id);
       if(userModel ==null){
           throw new BusinessException(EmBusinessError.USER_NOT_EXIT);
       }
//       将核心领域模型 转化为给前端使用的viewobject
        UserVO userVO = convertFromModel(userModel);
//        将核心领域模型 转化为给ui使用的viewObject
       return CommonReturnType.create(userVO);
    }
    private UserVO convertFromModel(UserModel userModel){
        if(userModel ==null) return null;
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }

}
