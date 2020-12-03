package com.tobe.fishking.v2.service.auth;

import com.tobe.fishking.v2.model.auth.SignUpDto;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import javax.xml.validation.Validator;

/*public class SignUpValidator extends Validator {

    @Autowired
    MemberRepository memberRepository;

    public boolean support(Class clazz){
        return SignUpDto.class.equals(clazz);
    }

    public void validate(Object o, Errors e){
        SignUpDto dto = (SignUpDto)o;

        if(memberRepository.existsMemberByEmail(dto.getEmail())==true){
            e.rejectValue();
        }

    }
}*/
