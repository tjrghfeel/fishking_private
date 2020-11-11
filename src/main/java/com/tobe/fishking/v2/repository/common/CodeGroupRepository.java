package com.tobe.fishking.v2.repository.common;


import com.tobe.fishking.v2.entity.common.CodeGroup;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

public interface CodeGroupRepository extends BaseRepository<CodeGroup, Long> {

    @Query("select a from CodeGroup a where a.code = :code")
    public CodeGroup findByCode(String code);


}
