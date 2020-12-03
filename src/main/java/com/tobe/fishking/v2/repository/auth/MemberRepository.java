package com.tobe.fishking.v2.repository.auth;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select a from Member a where a.sessionToken = :sessionToken and a.isActive = true")
    Optional<Member> findBySessionToken(String sessionToken);

    Optional<Member> findByUid(String uId);

    // Optional<Member> findByEmail(String email);

  //  Optional<Member> findByUidAndProvider(String uid, String provider);

    /*이메일 중복체크*/
    /*@Query("select exists (select m from Member m where m.email = :email)")
    boolean existsMemberByEmail(@Param("email") String email);*/

    /*이메일 중복체크*/
    boolean existsByEmail(String email);
    /*휴대폰 번호 중복체크*/
    boolean existsByPhoneNumber(PhoneNumber phoneNumber);
    /*휴대폰 번호로 검색*/
    Member findByPhoneNumber(PhoneNumber phoneNumber);
    /*아이디로 검색*/
    Member findByEmail(String email);

}
