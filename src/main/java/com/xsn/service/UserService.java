package com.xsn.service;

import com.xsn.error.BusinessException;
import com.xsn.service.model.UserModel;

public interface UserService {
    UserModel getUserById(Integer id);

    void register (UserModel userModel) throws BusinessException;
}
