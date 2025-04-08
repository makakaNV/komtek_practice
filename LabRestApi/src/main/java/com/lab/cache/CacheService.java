package com.lab.cache;

import com.lab.entity.Order;
import com.lab.entity.Patient;
import com.lab.entity.Test;
import com.lab.entity.TestType;

public interface CacheService {
    void evictPatientCaches(Patient patient);

    void evictOrderCaches(Order order);
    void evictTestCaches(Test test);
    void evictTestTypeCaches(TestType testType);
}
