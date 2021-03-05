package com.tobe.fishking.v2.repository.common;


import com.querydsl.core.annotations.QueryEmbeddable;
import com.tobe.fishking.v2.entity.common.CodeGroup;
import com.tobe.fishking.v2.entity.common.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface CommonCodeRepository extends JpaRepository<CommonCode, Long>, CommonCodeRepositoryCustom {

    @Query("select a from CommonCode a where a.codeName = :codeName")
    public Optional<CommonCode> findByCodeName(@Param("codeName") String codeName);

    @Query("select a from CommonCode a where a.codeName = :codeName and a.codeGroup = :codeGroup")
    public Optional<CommonCode> findByCodeNameAndDiv(@Param("codeName") String codeName, @Param("codeGroup") CodeGroup codeGroup);

    @Query("select a from CommonCode a where a.codeGroup = :codeGroup and a.isActive = true")
    public List<CommonCode> findAllByCodeGroup(@Param("codeGroup") CodeGroup codeGroup);

    //public CommonCode findByCode(String code);

    /*code 코드리스트 조회*/
    @Query("select a from CommonCode a where a.codeGroup = :codeGroup and a.code in (:codeList)")
    public List<CommonCode> findCommonCodesByCodeGroupAndCodes(@Param("codeGroup") CodeGroup  codeGroup,@Param("codeList") List<String> codeList);

    public CommonCode findByCodeGroupAndCode(CodeGroup codeGroup, String code);

    @Query("select c.codeName from CommonCode c where c.codeGroup = :codeGroup and c.code in :codeList")
    public String[] findCodeNameByCodeList(@Param("codeList") String[] codeList, @Param("codeGroup") CodeGroup codeGroup);

    @Query("select c from CommonCode c where c.code = :code")
    CommonCode getByCode(String code);

    @Query("select c from CommonCode c where c.codeGroup.id = :groupId")
    List<CommonCode> getByGroupId(Long groupId);

    @Query("select c.code from CommonCode c where c.codeGroup = :codeGroup and c.codeName in :codeNameList")
    ArrayList<String> findCodeByCodeNameAndCodeGroup(@Param("codeNameList") String[] codeNameList, @Param("codeGroup") CodeGroup codeGroup);
}
