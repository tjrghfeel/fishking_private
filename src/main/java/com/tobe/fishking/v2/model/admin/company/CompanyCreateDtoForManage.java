package com.tobe.fishking.v2.model.admin.company;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class    CompanyCreateDtoForManage {

    @NotNull
    private String memberUid;  //name이 없을 경우 member_id, fk
//    @Size(min=4,max = 10)
    private String companyName;//
    private String shipowner;
//    @Size(min=2,max=15)
    private String sido;
//    @Size(min=2,max=15)
    private String gungu;

    private String tel;//
    private String phoneNumber;

    private String bizNo;
//    @Size(min=2,max=15)
    private String harbor;//
//    @Size(min=2,max=15)
    private String bank ;

    private String accountNo ;
    private String ownerWording;//not null
    private String  skbAccount;
    private String skbPassword;
    private String adtId;
    private String adtPw;
    private String nhnId;
    private String nhnPw;
//    @Size(min=10,max=50)
    private String companyAddress;//
   /* @NotNull
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
