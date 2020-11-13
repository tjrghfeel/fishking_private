package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.auth.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CompanyUpdateDTO {
    private Long id;//not null, pk
    private Long member;  //name이 없을 경우 member_id, fk
    private String companyName;//
    private String shipOwner;//
    private String sido;
    private String gungu;
    private String tel;//
    private String bizNo;
    private String harbor;//
    //private String bizNoFilesUrl ;
    //private String representFilesUrl ;
    private String bank ;
    private String accountNo ;
    private String ownerWording;//not null
    private boolean isOpen;
    //private String  skbAccount;
    //private String skbPassword;
    private String companyAddress;//
    private boolean isRegisitered;
    //private Member createdBy;//not null, fk
    private Long modifiedBy;//not null, fk
    private Long bizNoFile;//not null
    private Long representFile;//not null
    private Long accountFile;//not null
}
