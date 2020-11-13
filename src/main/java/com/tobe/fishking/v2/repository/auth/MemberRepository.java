package com.tobe.fishking.v2.repository.auth;

import com.tobe.fishking.v2.entity.auth.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select a from Member a where a.sessionToken = :sessionToken and a.isActive = true")
    Optional<Member> findBySessionToken(String sessionToken);

    Optional<Member> findByUid(String uId);

    // Optional<Member> findByEmail(String email);

  //  Optional<Member> findByUidAndProvider(String uid, String provider);

}
