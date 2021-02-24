package com.tobe.fishking.v2.repository.common;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Take;
import com.tobe.fishking.v2.enums.common.TakeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TakeRepository extends JpaRepository<Take, Long> {

    List<Take> findByCreatedBy(Member member);

    int countByCreatedBy(Member member);

    /*업체 프로필보기 > 업체가 받은 총 찜 수 카운트*/
    @Query(
            value = "select count(t.id) from take t " +
                    "where " +
                    "   t.take_type=0 " +
                    "   and t.link_id in " +
                    "       (select g.id from goods g where g.goods_ship_id in " +
                    "           ( select s.id from ship s where s.company_id = :companyId )" +
                    "       )",
            nativeQuery = true
    )
    int countForCompanyProfile(@Param("companyId") Long companyId);

    @Query("select count(l) from Take l where l.linkId = :linkId and l.createdBy = :createBy and l.takeType = :type")
    Integer findByLinkIdAndMemberAndType(Long linkId, Member createBy, TakeType type);
}
