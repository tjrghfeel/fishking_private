package com.tobe.fishking.v2.model;

import com.tobe.fishking.v2.entity.auth.Member;

import java.util.List;

public interface NoNameDTOInterface {
    Long getId();
    List<Member> getScrabMembers();
}
