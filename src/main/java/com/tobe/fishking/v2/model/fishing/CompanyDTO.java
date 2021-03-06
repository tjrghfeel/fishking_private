package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CompanyDTO {
    private Long id;//not null, pk
    private Long member;  //name이 없을 경우 member_id, fk
    private String memberUid;
    private String companyName;//
    private String memberName;//
    private String shipowner;
    private String sido;
    private String gungu;
    private String tel;//
    private String phoneNumber;
    private String bizNo;
    private String harbor;//
    private String bank ;
    private String accountNo ;
    private String ownerWording;//not null
    private Boolean isOpen;
    private String  skbAccount;
//    private String skbPassword;
    private String companyAddress;//
    private Boolean isRegistered;
    private String nhnId;
    private Long createdBy;//not null, fk
    private Long modifiedBy;//not null, fk
    private String bizNoFilesUrl ;
    private String representFilesUrl ;
    private String accountFileUrl;
    private Long bizNoFileId;
    private Long representFileId;
    private Long accountFileId;
    private String createdDate;
    private String registeredDate;
    //private Long bizNoFile;//not null
    //private Long representFile;//not null
    //private Long accountFile;//not null

    public CompanyDTO(Company company, Member member, String url){
        id = company.getId();
        this.member = company.getMember().getId();
        memberUid = member.getUid();
        companyName = company.getCompanyName();
        memberName = member.getMemberName();
        shipowner = company.getShipOwner();
        sido = company.getSido();
        gungu = company.getGungu();
        tel = company.getTel();
        phoneNumber = company.getPhoneNumber();
        bizNo = company.getBizNo();
        harbor = company.getHarbor();
        bank = company.getBank();
        accountNo = company.getAccountNo();
        ownerWording = company.getOwnerWording();
        isOpen = company.getIsOpen();
        skbAccount = company.getSkbAccount();
//        skbPassword = company.getSkbPassword();
        companyAddress = company.getCompanyAddress();
        isRegistered = company.getIsRegistered();
        nhnId = company.getNhnId();
        createdBy = company.getCreatedBy().getId();
        modifiedBy = company.getModifiedBy().getId();
        bizNoFilesUrl = url + company.getBizNoFileDownloadUrl();
        representFilesUrl = url + company.getRepresentFileDownloadUrl();
        accountFileUrl = url + company.getAccountFileDownloadUrl();
        bizNoFileId = company.getBizNoFileId().getId();
        representFileId = company.getRepresentFileId().getId();
        accountFileId = company.getAccountFileId().getId();
        createdDate = company.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if(company.getRegisteredDate()!=null){  registeredDate = company.getRegisteredDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));}

    }

}
