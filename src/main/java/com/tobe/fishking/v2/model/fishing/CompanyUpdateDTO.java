package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.auth.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CompanyUpdateDTO {
    private Long companyId;//not null, pk
    @Size(min=4,max = 10)
    private String companyName;//
    @Size(min=2,max=5)
    private String memberName;//
    @Size(min=2,max=15)
    private String sido;
    @Size(min=2,max=15)
    private String gungu;

    private String tel;//

    private String bizNo;
    @Size(min=2,max=15)
    private String harbor;//
    @Size(min=2,max=15)
    private String bank ;

    private String accountNo ;
    @NotNull
    private String ownerWording;//not null
    private Boolean isOpen;
    private String  skbAccount;
    private String skbPassword;
    @Size(min=10,max=50)
    private String companyAddress;//
//    private Boolean isRegistered;
    /* @NotNull
     private Long createdBy;//not null, fk
     @NotNull
     private Long modifiedBy;//not null, fk*/
//    private String bizNoFilesUrl ;
//    private String representFilesUrl ;
//    private String accountFileUrl;
    @NotNull
    private MultipartFile bizNoFile;
    @NotNull
    private MultipartFile representFile;
    @NotNull
    private MultipartFile accountFile;
}
