package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.auth.Member;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyWriteDTO {
    private Long id;//not null, pk
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
    //private boolean isOpen;
    //private String  skbAccount;
    //private String skbPassword;
    private String companyAddress;//
    //private boolean isRegistered;
    //private Member createdBy;//not null, fk
    //private Long modifiedBy;//not null, fk
    //private Long bizNoFile;//not null
    //private Long representFile;//not null
    //private Long accountFile;//not null

    private MultipartFile[] files;

}
