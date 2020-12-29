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
    @NotNull
    private Long memberId;  //name이 없을 경우 member_id, fk
    @Size(min=4,max = 10)
    private String companyName;//
    @Size(min=2,max=5)
    private String memberName;//
    @Size(min=2,max=15)
    private String sido;//
    @Size(min=2,max=15)
    private String gungu;//

    private String tel;//

    private String bizNo;//
    @Size(min=2,max=15)
    private String harbor;//
    @Size(min=2,max=15)
    private String bank ;//

    private String accountNo ;//
    @NotNull
    private String ownerWording;//not null//
    private Boolean isOpen;//
    private String  skbAccount;//
    private String skbPassword;//
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
    private MultipartFile bizNoFile;
    @NotNull
    private MultipartFile representFile;
    @NotNull
    private MultipartFile accountFile;
}
