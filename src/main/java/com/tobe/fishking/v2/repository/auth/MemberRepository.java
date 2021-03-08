package com.tobe.fishking.v2.repository.auth;

import com.querydsl.core.annotations.QueryEmbeddable;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.PhoneNumber;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.fishing.SNSType;
import com.tobe.fishking.v2.model.NoNameDTO;
import com.tobe.fishking.v2.model.admin.member.MemberManageDtoForPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select a from Member a where a.sessionToken = :sessionToken and a.isActive = true")
    Optional<Member> findBySessionToken(@Param("sessionToken") String sessionToken);

    Optional<Member> findByUid(String uId);

    // Optional<Member> findByEmail(String email);

    //  Optional<Member> findByUidAndProvider(String uid, String provider);

    /*이메일 중복체크*/
    /*@Query("select exists (select m from Member m where m.email = :email)")
    boolean existsMemberByEmail(@Param("email") String email);*/

    /*이메일 중복체크*/
    boolean existsByEmail(String email);
    boolean existsByUid(String uid);
    /*닉네임 중복체크*/
    boolean existsByNickName(String nickName);
    /*휴대폰 번호 중복체크*/
    boolean existsByPhoneNumber(PhoneNumber phoneNumber);
    /*pass인증번호 중복체크*/
    boolean existsByCertifiedNo(String certifiedNo);

    void deleteById(Long id);

    /*@Query(value = "select exists (select * from member m where m.areacode = :areaCode and m.localnumber = :localNumber)",
        nativeQuery = true
    )
    boolean existsByAreaCodeAndLocalNumber(@Param("areaCode") String areaCode, @Param("localNumber") String localNumber);*/
    @Query("select count(m) from Member m " +
            "where exists (select m2 from Member m2 " +
            "where m2.phoneNumber.areaCode = :areaCode and m2.phoneNumber.localNumber = :localNumber)")
    int existsByAreaCodeAndLocalNumber(@Param("areaCode") String areaCode, @Param("localNumber") String localNumber);
    /*휴대폰 번호로 검색*/
    Member findByPhoneNumber(PhoneNumber phoneNumber);


    @Query("select m from Member m where m.phoneNumber.areaCode = :areaCode and m.phoneNumber.localNumber = :localNumber")
    Member findByAreaCodeAndLocalNumber(@Param("areaCode") String areaCode, @Param("localNumber") String localNumber);

    /*전화번호, 실명으로 회원 검색 메소드. */
    @Query("select m from Member m " +
            "where m.phoneNumber.areaCode = :areaCode and m.phoneNumber.localNumber = :localNumber and m.memberName = :memberName")
    Member findByAreaCodeAndLocalNumberAndMemberName(
            @Param("areaCode") String areaCode, @Param("localNumber") String localNumber, @Param("memberName") String memberName);

    /*아이디로 검색*/
    Member findByEmail(String email);
    /*sns_type, sns_id로 검색*/
    Member findBySnsIdAndSnsType(String snsId, SNSType snsType);

    /*관리자용 검색 메소드*/
    @Query(value = "" +
            "select " +
            "   m.id id, " +
            "   m.uid uid, " +
            "   m.member_name memberName, " +
            "   m.nick_name nickName, " +
            "   m.password password, " +
            "   m.email email, " +
            "   m.gender gender, " +
            "   m.roles roles, " +
            "   m.profile_image profileImage, " +
            "   m.is_active isActive, " +
            "   m.certified_no certifiedNo, " +
            "   m.is_certified isCertified, " +
            "   m.join_dt joinDt, " +
            "   m.sns_type snsType, " +
            "   m.sns_id snsId, " +
            "   m.status_message statusMessage, " +
            "   m.city city, " +
            "   m.gu gu, " +
            "   m.dong dong, " +
            "   m.areacode areaCode, " +
            "   m.localnumber localNumber, " +
            "   m.created_date createdDate " +
            "from member m " +
            "where " +
            "   if(:id is null,true,(m.id = :id)) " +
            "   and if(:roles is null,true,(m.roles = :roles)) " +
            "   and if(:memberName is null,true,(m.member_name = :memberName)) " +
            "   and if(:uid is null,true,(m.uid like %:uid%)) " +
            "   and if(:nickName is null,true,(m.nick_name like %:nickName%)) " +
            "   and if(:email is null, true, (m.email like %:email%)) " +
            "   and if(:gender is null,true,(m.gender = :gender)) " +
            "   and if(:isActive is null,true,(m.is_active = :isActive)) " +
            "   and if(:certifiedNo is null, true, (m.certified_no like %:certifiedNo%)) " +
            "   and if(:isCertified is null, true, (m.is_certified = :isCertified)) " +
            "   and if(:snsType is null,true,(m.sns_type = :snsType)) " +
            "   and if(:snsId is null,true,(m.sns_id like %:snsId%)) " +
            "   and if(:city is null,true,(m.city = :city)) " +
            "   and if(:gu is null,true,(m.gu = :gu)) " +
            "   and if(:dong is null,true,(m.dong = :dong)) " +
            "   and if(:areaCode is null,true,(m.areacode = :areaCode)) " +
            "   and if(:localNumber is null,true,(m.localnumber = :localNumber)) " +
            "   and if(:joinDtStart  is null, true, (m.created_date > :joinDtStart)) " +
            "   and if(:joinDtEnd is null, true, (m.created_date < :joinDtEnd)) ",
            countQuery = "select m.id from member m " +
                    "where " +
                    "   if(:id is null,true,(m.id = :id)) " +
                    "   and if(:roles is null,true,(m.roles = :roles)) " +
                    "   and if(:memberName is null,true,(m.member_name = :memberName)) " +
                    "   and if(:uid is null,true,(m.uid like %:uid%)) " +
                    "   and if(:nickName is null,true,(m.nick_name like %:nickName%)) " +
                    "   and if(:email is null, true, (m.email like %:email%)) " +
                    "   and if(:gender is null,true,(m.gender = :gender)) " +
                    "   and if(:isActive is null,true,(m.is_active = :isActive)) " +
                    "   and if(:certifiedNo is null, true, (m.certified_no like %:certifiedNo%)) " +
                    "   and if(:isCertified is null, true, (m.is_certified = :isCertified)) " +
                    "   and if(:snsType is null,true,(m.sns_type = :snsType)) " +
                    "   and if(:snsId is null,true,(m.sns_id like %:snsId%)) " +
                    "   and if(:city is null,true,(m.city = :city)) " +
                    "   and if(:gu is null,true,(m.gu = :gu)) " +
                    "   and if(:dong is null,true,(m.dong = :dong)) " +
                    "   and if(:areaCode is null,true,(m.areacode = :areaCode)) " +
                    "   and if(:localNumber is null,true,(m.localnumber = :localNumber)) " +
                    "   and if(:joinDtStart  is null, true, (m.created_date > :joinDtStart)) " +
                    "   and if(:joinDtEnd is null, true, (m.created_date < :joinDtEnd)) ",
            nativeQuery = true
    )
    Page<MemberManageDtoForPage> findMemberListByConditions(
            @Param("id") Long id,
            @Param("roles") Integer roles,
            @Param("memberName") String memberName,
            @Param("uid") String uid,
            @Param("nickName") String nickName,
            @Param("email") String email,
            @Param("gender") Integer gender,
            @Param("isActive") Boolean isActive,
            @Param("certifiedNo") String certifiedNo,
            @Param("isCertified") Boolean isCertified,
            @Param("snsType") Integer snsType,
            @Param("snsId") String snsId,
            @Param("city") String city,
            @Param("gu") String gu,
            @Param("dong") String dong,
            @Param("areaCode") String areaCode,
            @Param("localNumber") String localNumber,
            @Param("joinDtStart") LocalDate joinDtStart,
            @Param("joinDtEnd") LocalDate joinDtEnd,
            Pageable pageable
    );

    @Query("select m from Member m where m.id in :list")
    ArrayList<Member> findList(@Param("list") ArrayList<Long> list);

}
