package com.xsn.service;

import com.xsn.error.BusinessException;
import com.xsn.service.model.UserModel;

public interface UserService {
    UserModel getUserById(Integer id);
    //通过缓存获取用户对象
    UserModel getUserByIdInCache(Integer id);
    
    void register (UserModel userModel) throws BusinessException;
    UserModel login(String mobile,String password) throws BusinessException;
}
