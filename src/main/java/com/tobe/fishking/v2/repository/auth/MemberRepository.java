package com.tobe.fishking.v2.repository.auth;

import com.tobe.fishking.v2.entity.auth.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

}
