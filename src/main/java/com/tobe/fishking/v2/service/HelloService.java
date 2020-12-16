package com.tobe.fishking.v2.service;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Address;
import com.tobe.fishking.v2.entity.common.PhoneNumber;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class HelloService {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    Environment env;

    public void noName() throws ResourceNotFoundException {
        Member member = memberRepository.findById(20L)
                .orElseThrow(()->new ResourceNotFoundException("asdf"));

        member.setMemberName("전세호");
        member.setNickName("쎄호");
        member.setEmail("asd123@naver.com");
        String encodedPw = encoder.encode("1234");
        member.setPassword(encodedPw);
        member.setProfileImage(env.getProperty("file.downloadUrl")+ "/profile/noProfileImage.jpg");
        PhoneNumber phoneNumber = new PhoneNumber("111","2312");
        member.setPhoneNumber(phoneNumber);
        Address address = new Address("성남시","수지구","수지동");
        member.setAddress(address);

        memberRepository.save(member);

        return;
    }
}
