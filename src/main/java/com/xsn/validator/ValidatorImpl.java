package com.xsn.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class ValidatorImpl implements InitializingBean {
     private Validator validator;

     //实现校验方法并返回校验结果
    public  ValidResult validate(Object bean){
        ValidResult result = new ValidResult();
        Set<ConstraintViolation<Object>> constraintViolationSet =  validator.validate(bean);
        if(constraintViolationSet.size()>0){
            result.setHsErrs(true);
            constraintViolationSet.forEach(objectConstraintViolation -> {
                String errMsg = objectConstraintViolation.getMessage();
                String propertyName = objectConstraintViolation.getPropertyPath().toString();
                result.getErrMsgMap().put(propertyName,errMsg);
            });
        }
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        将hinernate validator通过工厂的初始化方式使其实例化
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
