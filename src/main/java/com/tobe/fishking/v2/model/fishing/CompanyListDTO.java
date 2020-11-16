package com.tobe.fishking.v2.model.fishing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/*Company를 리스트로 볼때 Paging형식으로 보여주며, 몇가지 정보만을 보여주기위해 사용되는 DTO. */
public interface CompanyListDTO {
    Long getId();//not null, pk
     Long getMember();  //name이 없을 경우 member_id, fk
     String getCompanyName();//
     String getShipOwner();//
     String getSido();
     String getGungu();
     String getTel();//
    //private String bizNo;
    //private String harbor;//
    //private String bank ;
    //private String accountNo ;
    //private String ownerWording;//not null
     boolean getIsOpen();
    //private String  skbAccount;
    //private String skbPassword;
    //private String companyAddress;//
     boolean getIsRegistered();
    //private Long createdBy;//not null, fk
    //private Long modifiedBy;//not null, fk
    //private String bizNoFilesUrl ;
    //private String representFilesUrl ;
    //private String accountFileUrl;
    //private Long bizNoFile;//not null
    //private Long representFile;//not null
    //private Long accountFile;//not null


}
