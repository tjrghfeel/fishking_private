package com.tobe.fishking.v2.repository.common;


import com.tobe.fishking.v2.entity.common.CodeGroup;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CodeGroupRepository extends BaseRepository<CodeGroup, Long> {

    CodeGroup findByName(String name);

    @Query("select a from CodeGroup a where a.code = :code")
    public CodeGroup findByCode(@Param("code") String code);

}
