package com.xsn.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.xsn.dao.UserDOMapper;
import com.xsn.dao.UserPasswordDOMapper;
import com.xsn.dataobject.UserDO;
import com.xsn.dataobject.UserPasswordDO;
import com.xsn.error.BusinessException;
import com.xsn.error.EmBusinessError;
import com.xsn.service.UserService;
import com.xsn.service.model.UserModel;
import com.xsn.validator.ValidResult;
import com.xsn.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;
    @Autowired
    private ValidatorImpl validatorImpl;
    @Override
    public UserModel getUserById(Integer id){
//        使用userMapper 获取用户dataobject
       UserDO userDO =  userDOMapper.selectByPrimaryKey(id);
       if(userDO == null){
           return null;
       }
       UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
       return convertFromDataObject(userDO,userPasswordDO);
    }
    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
        if(userDO==null){
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO,userModel);
        if(userPasswordDO!=null){
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }

        return userModel;
    }
    public UserModel login(String mobile,String password) throws BusinessException {
        UserDO userDO = userDOMapper.selectByMobile(mobile);
        if(userDO == null){
            throw new BusinessException(EmBusinessError.USER_LOGIN_ERROR);
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDataObject(userDO,userPasswordDO);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(!passwordEncoder.matches(password,userModel.getEncrptPassword())){
            throw new BusinessException(EmBusinessError.USER_LOGIN_ERROR);
        }
        return userModel;
    }
    @Override
//    让两个数据库操作在一个事务里
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if(userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
//        if(StringUtils.isEmpty(userModel.getName())||
//                userModel.getGender()==null||
//                userModel.getAge()==null||
//                StringUtils.isEmpty(userModel.getMobile())
//        ){
//            throw  new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//        }
         ValidResult result =  validatorImpl.validate(userModel);
        if(result.isHsErrs()){
            throw  new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());

        }
        UserDO userDO = convertFromModel(userModel);
        // insertSelective 判空 插入字段
        try {
            userDOMapper.insertSelective(userDO);
        }catch (DuplicateKeyException exp){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号已经被注册");
        }
        userModel.setId(userDO.getId());
        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);
    }
    private UserPasswordDO convertPasswordFromModel(UserModel userModel){
        if(userModel==null){
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }
    private UserDO convertFromModel(UserModel userModel){
        if(userModel==null){
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;
    }
}
