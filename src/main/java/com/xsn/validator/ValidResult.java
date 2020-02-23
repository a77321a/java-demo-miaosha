package com.xsn.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ValidResult {
    private boolean hsErrs;
    private Map<String,String> errMsgMap = new HashMap<>();

    public boolean isHsErrs() {
        return hsErrs;
    }

    public void setHsErrs(boolean hsErrs) {
        this.hsErrs = hsErrs;
    }

    public Map<String, String> getErrMsgMap() {
        return errMsgMap;
    }

    public void setErrMsgMap(Map<String, String> errMsgMap) {
        this.errMsgMap = errMsgMap;
    }
    public String getErrMsg(){
      return StringUtils.join(errMsgMap.values().toArray(),",");
    }
}
