package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CompanyDTO {
    private Long id;//not null, pk
    private Long member;  //name이 없을 경우 member_id, fk
    private String companyName;//
    private String shipOwner;//
    private String sido;
    private String gungu;
    private String tel;//
    private String bizNo;
    private String harbor;//
    private String bank ;
    private String accountNo ;
    private String ownerWording;//not null
    private Boolean isOpen;
    private String  skbAccount;
    private String skbPassword;
    private String companyAddress;//
    private Boolean isRegistered;
    private Long createdBy;//not null, fk
    private Long modifiedBy;//not null, fk
    private String bizNoFilesUrl ;
    private String representFilesUrl ;
    private String accountFileUrl;
    //private Long bizNoFile;//not null
    //private Long representFile;//not null
    //private Long accountFile;//not null

    public CompanyDTO(Company company){
        id = company.getId();
        member = company.getMember().getId();
        companyName = company.getCompanyName();
        shipOwner = company.getShipOwner();
        sido = company.getSido();
        gungu = company.getGungu();
        tel = company.getTel();
        bizNo = company.getBizNo();
        harbor = company.getHarbor();
        bank = company.getBank();
        accountNo = company.getAccountNo();
        ownerWording = company.getOwnerWording();
        isOpen = company.getIsOpen();
        skbAccount = company.getSkbAccount();
        skbPassword = company.getSkbPassword();
        companyAddress = company.getCompanyAddress();
        isRegistered = company.getIsRegistered();
        createdBy = company.getCreatedBy().getId();
        modifiedBy = company.getModifiedBy().getId();
        bizNoFilesUrl = company.getBizNoFileDownloadUrl();
        representFilesUrl =company.getRepresentFileDownloadUrl();
        accountFileUrl = company.getAccountFileDownloadUrl();
    }

}
