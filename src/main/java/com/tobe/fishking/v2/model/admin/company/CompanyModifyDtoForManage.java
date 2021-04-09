package com.tobe.fishking.v2.model.admin.company;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyModifyDtoForManage {
    @NotNull
    private Long id;//not null, pk
    @Size(min=4,max = 10)
    private String companyName;//
    private String shipowner;
    private String sido;//
    private String gungu;//

    private String tel;//
    private String phoneNumber;

    private String bizNo;//
    private String harbor;//
    private String bank ;//

    private String accountNo ;//
    private String ownerWording;//not null//
    private Boolean isOpen;//
    private String  skbAccount;//
    private String skbPassword;//
    private String adtId;
    private String adtPw;
    private String nhnId;
    private String nhnPw;
    @Size(min=10,max=50)
    private String companyAddress;//
    private Boolean isRegistered;//
    /*@NotNull
    private Long createdBy;//not null, fk
    @NotNull
    private Long modifiedBy;//not null, fk*/
    //    private String bizNoFilesUrl ;
//    private String representFilesUrl ;
//    private String accountFileUrl;
    @NotNull
    private Long bizNoFile;
    @NotNull
    private Long representFile;
    @NotNull
    private Long accountFile;
}
