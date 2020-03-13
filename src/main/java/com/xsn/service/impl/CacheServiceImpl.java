package com.xsn.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.xsn.service.CacheService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Service
public class CacheServiceImpl implements CacheService {
    private Cache<String,Object> commonCache;
    
    @PostConstruct
    public void init(){
        commonCache = CacheBuilder.newBuilder()
                //设置缓存初始容量
                .initialCapacity(10)
                //设置缓存中最大可以存储100个Key，超过100个之后会按照LRU策略移除缓存项
                .maximumSize(100)
                //设置写缓存后多少秒国企
                .expireAfterWrite(60,TimeUnit.SECONDS).build();
    }
    @Override
    public void setCommonCache(String key, Object value) {
        commonCache.put(key,value);
    }

    @Override
    public Object getFromCommonCache(String key) {
        return commonCache.getIfPresent(key);
    }
}
