/*
package com.tobe.fishking.v2.service.auth;

import com.tobe.fishking.v2.model.auth.SignUpDto;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class SignUpValidator implements Validator {

    @Autowired
    MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz){
        return SignUpDto.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors e){
        SignUpDto dto = (SignUpDto)o;

//        if(memberRepository.existsMemberByEmail(dto.getEmail())==true){
//            e.rejectValue();
//        }

    }
}
*/
