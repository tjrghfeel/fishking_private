package com.tobe.fishking.v2.repository.common;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.LoveTo;
import com.tobe.fishking.v2.enums.common.TakeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoveToRepository extends JpaRepository<LoveTo, Long> {

    int countByTakeTypeAndLinkId(TakeType takeType, Long linkId);

    List<LoveTo> findByCreatedBy(Member member);

    int countByCreatedBy(Member member);

    /*업체 프로필보기 > 게시글 등에서 받은 좋아요 수 카운트 메소드. */
    @Query(
            value = "select count(l.id) " +
                    "from loveto l " +
                    "where (l.take_type=3 and " +
                    "       l.link_id in (select d.id from fishing_diary d where d.fishing_diary_member_id=:memberId)) " +
                    "    or (l.take_type=2 and " +
                    "       l.link_id in (select d.id from fishing_diary d where d.fishing_diary_member_id=:memberId)) " +
                    "   or (l.take_type=4 and " +
                    "       l.link_id in (select c.id from fishing_diary_comment c where c.created_by=:memberId)) " +
                    "   ",
            nativeQuery = true
    )
    int countLikeCountForCompanyProfile(@Param("memberId") Long memberId);
}
