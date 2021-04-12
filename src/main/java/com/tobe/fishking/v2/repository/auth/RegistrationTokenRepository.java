package com.tobe.fishking.v2.repository.auth;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.auth.RegistrationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationTokenRepository extends JpaRepository<RegistrationToken, Long> {

    List<RegistrationToken> findAllByMemberAndToken(Member member, String token);
    List<RegistrationToken> findAllByMember(Member member);
}
