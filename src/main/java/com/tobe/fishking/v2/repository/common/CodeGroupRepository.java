package com.tobe.fishking.v2.repository.common;

import com.tobe.fishking.v2.entity.common.CodeGroup;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeGroupRepository extends JpaRepository<CodeGroup, Long> {
    CodeGroup findByName(String name);

}
