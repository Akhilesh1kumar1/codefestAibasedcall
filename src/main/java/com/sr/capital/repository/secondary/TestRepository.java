package com.sr.capital.repository.secondary;

import com.sr.capital.entity.secondary.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestRepository extends JpaRepository<TestEntity,Long> {

    List<TestEntity> findAll();
}
