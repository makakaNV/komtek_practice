package com.lab.cache.impl;

import com.lab.cache.CacheService;
import com.lab.entity.Order;
import com.lab.entity.Patient;
import com.lab.entity.Test;
import com.lab.entity.TestType;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;



@Service
public class CacheServiceImpl implements CacheService {


    @Override
    @Caching(evict = {
            @CacheEvict(value = "patients", key = "#patient.id"),
            @CacheEvict(value = "patientsByBirthDate", key = "#patient.birthDate"),
            @CacheEvict(value = "patientsByFio", key = "T(java.util.Objects).hash(#patient.lastName, #patient.firstName, #patient.middleName)")
    })
    public void evictPatientCaches(Patient patient) {

    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "orders", key = "#order.id")
    })
    public void evictOrderCaches(Order order) {
    }


    @Override
    @CacheEvict(value = "tests", key = "#test.id")
    public void evictTestCaches(Test test) {
    }

    @Override
    @CacheEvict(value = "testTypes", key = "#testType.id")
    public void evictTestTypeCaches(TestType testType) {
    }
}


