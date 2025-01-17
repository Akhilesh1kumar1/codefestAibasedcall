package com.sr.capital.util;

import com.sr.capital.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static com.sr.capital.helpers.constants.Constants.Separators.UNDERSCORE_SEPARATOR;
import static com.sr.capital.helpers.constants.Constants.ServiceConstants.FILE_UPLOAD_RECORD;
import static com.sr.capital.helpers.constants.Constants.ServiceConstants.PROGRESS_PERCENTAGE;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final LoggerUtil loggerUtil = LoggerUtil.getLogger(RedisUtil.class);
    final RedissonClient redissonClient;
    final AppProperties appConfig;
    public String getValue(String key) {
        RBucket<String> cache = redissonClient.getBucket(key);
        return cache.get();
    }

    public boolean deleteKey(String key) {
        return redissonClient.getBucket(key).delete();
    }

    public long getTtlForKey(String key) {
        RBucket<String> fileCache = redissonClient.getBucket(key);
        return fileCache.remainTimeToLive();
    }

    public void updateFileInCache(String tenantId, String fileName) {
        RBucket<String> fileCache = redissonClient.getBucket(String.join(UNDERSCORE_SEPARATOR, FILE_UPLOAD_RECORD, tenantId));
        fileCache.set(fileName, Duration.of(appConfig.getFileUploadCoolOffWindow(), TimeUnit.SECONDS.toChronoUnit()));
    }

    public boolean checkIfFileExists(String tenantId) {
        RBucket<String> fileCache = redissonClient.getBucket(String.join(UNDERSCORE_SEPARATOR, FILE_UPLOAD_RECORD, tenantId));
        String fileRecord = fileCache.get();
        return StringUtils.isEmpty(fileRecord);
    }

    public Double getFileProgressPercentage(String tenantId, String fileId) {
        RBucket<Double> fileCache = redissonClient.getBucket(String.join(UNDERSCORE_SEPARATOR, PROGRESS_PERCENTAGE, tenantId, fileId));
        return fileCache.get();
    }

    public void updateFileProgressPercentage(String tenantId, String fileId, double updatedProgressPercentage) {
        RBucket<Double> fileCache = redissonClient.getBucket(String.join(UNDERSCORE_SEPARATOR, PROGRESS_PERCENTAGE, tenantId, fileId));
        fileCache.set(updatedProgressPercentage);
    }

    public void removeFilePercentage(String tenantId, String fileId) {
        RBucket<Double> fileCache = redissonClient.getBucket(String.join(UNDERSCORE_SEPARATOR, PROGRESS_PERCENTAGE, tenantId, fileId));
        fileCache.delete();
    }
}
