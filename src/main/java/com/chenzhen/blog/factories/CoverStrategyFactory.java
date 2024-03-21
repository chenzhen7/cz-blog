package com.chenzhen.blog.factories;

import com.chenzhen.blog.strategies.CoverStrategy;
import com.chenzhen.blog.strategies.Impl.BaseCoverStrategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CoverStrategyFactory {

    private static Map<String, CoverStrategy> coverStrategyMap = new ConcurrentHashMap<>();

    public static CoverStrategy getCoverStrategy(String fileExtension) {
        if (!coverStrategyMap.containsKey(fileExtension)) {
            return new BaseCoverStrategy();
        }
        return coverStrategyMap.get(fileExtension);
    }

    public static void register(String fileExtension,CoverStrategy coverStrategy) {
        coverStrategyMap.put(fileExtension, coverStrategy);
    }
}
