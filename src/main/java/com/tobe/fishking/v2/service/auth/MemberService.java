package com.tobe.fishking.v2.service.auth;


import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.exception.CMemberExistException;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;


import java.util.Optional;

@AllArgsConstructor
@Service
public class MemberService {

    private MemberRepository memberRepository;

    public Member getMemberBySessionToken(final String sessionToken) {
        final Optional<Member> optionalMember =  memberRepository.findBySessionToken(sessionToken);
        optionalMember.orElseThrow(() -> new CMemberExistException(sessionToken));
        return optionalMember.get();
//        return optionalMember.isEmpty() ? null : optionalMember.get();
    }

    public long getMemberSeqBySessionToken(final String sessionToken) {
        final Optional<Member> optionalMember =  memberRepository.findBySessionToken(sessionToken);
        optionalMember.orElseThrow(() -> new CMemberExistException(sessionToken));
        return optionalMember.get().getId();
    }



}
