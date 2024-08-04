package com.chenzhen.blog.util;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author ChenZhen
 * @Description 一个简单的内存数据库类，类似Redis,用于存储键值对，支持设置过期时间，并提供简单的CRUD操作
 * @create 2024/8/4 18:22
 * @QQ 1583296383
 * @WeXin(WeChat) ShockChen7
 */
@Component
public  class  MemoryDBUtil <T> {

    private final Map<String, Object> store = new ConcurrentHashMap<>();
    private final Map<String, Long> expiryMap = new ConcurrentHashMap<>();

    public MemoryDBUtil() {
        // 启动定时任务，每隔1秒检查是否有过期的键值对，并删除
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::removeExpiredKeys, 1, 1, TimeUnit.SECONDS);
    }

    public void set(String key, Object value) {
        store.put(key, value);
        //默认过期时间为0，即不过期
        expiryMap.remove(key);
    }

    /**
     * 获取键值对，如果过期则为空
     * @param key
     * @return
     */
    public <T> T get(String key) {
        if (isExpired(key)) {
            store.remove(key);
            expiryMap.remove(key);
            return null;
        }
        if (!store.containsKey(key)){
            return null;
        }
        return (T) store.get(key);
    }

    /**
     * 设置键值对，并设置过期时间
     * @param key
     * @param value
     * @param ttl
     * @param timeUnit
     */
    public void setWithExpire(String key, Object value, long ttl, TimeUnit timeUnit) {
        set(key, value);
        long expiryTime = System.currentTimeMillis() + timeUnit.toMillis(ttl);
        expiryMap.put(key, expiryTime);
    }

    public boolean containsKey(String key) {
        return store.containsKey(key) && !isExpired(key);
    }

    public void delete(String key) {
        store.remove(key);
        expiryMap.remove(key);
    }

    private boolean isExpired(String key) {
        Long expiryTime = expiryMap.get(key);
        return expiryTime != null && System.currentTimeMillis() > expiryTime;
    }

    private void removeExpiredKeys() {
        for (String key : expiryMap.keySet()) {
            if (isExpired(key)) {
                store.remove(key);
                expiryMap.remove(key);
            }
        }
    }

    public Long increment(String key, long delta,long timeout) {
        return modifyValue(key, delta, timeout);
    }

    public Long decrement(String key, long delta,long timeout) {
        return modifyValue(key, -delta,timeout);
    }

    private Long modifyValue(String key, long delta,long timeout) {
        synchronized (this) {
            Long currentValue = (Long) store.getOrDefault(key, 0L);
            currentValue += delta;
            setWithExpire(key, currentValue,timeout, TimeUnit.SECONDS);
            return currentValue;
        }
    }

}
