package com.tobe.fishking.v2.repository.common;

import com.tobe.fishking.v2.entity.common.Take;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TakeRepository extends JpaRepository<Take, Long> {

}
