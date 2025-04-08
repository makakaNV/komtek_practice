package com.lab.repository;

import com.lab.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TestRepository extends JpaRepository<Test, Long> {
    List<Test> findByOrderId(Long orderId);
    List<Test> findAllByTestTypeId(Long testTypeId);
}
