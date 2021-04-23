package com.tobe.fishking.v2.model.admin.company;

import com.tobe.fishking.v2.enums.Constants;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    @Size(min=2,max = 20)
    @Pattern(regexp = Constants.STRING)
    private String companyName;//
    @Size(min=2,max = 20)
    @Pattern(regexp = Constants.STRING)
    private String shipowner;
    @Size(min=2,max = 20)
    @Pattern(regexp = Constants.STRING)
    private String sido;
    @Size(min=2,max = 20)
    @Pattern(regexp = Constants.STRING)
    private String gungu;

    @Size(min=2,max = 20)
    @Pattern(regexp = Constants.NUMBER)
    private String tel;//
    @Size(min=2,max = 20)
    @Pattern(regexp = Constants.NUMBER)
    private String phoneNumber;
    @Pattern(regexp = Constants.BIZ_NO)
    private String bizNo;
    @Size(min=2,max = 20)
    @Pattern(regexp = Constants.STRING)
    private String harbor;//
    @Size(min=2,max = 20)
    @Pattern(regexp = Constants.STRING)
    private String bank ;

    @Size(max=100)
    @Pattern(regexp = Constants.STRING)
    private String accountNo ;
    @Size(min=2,max = 100)
    @Pattern(regexp = Constants.STRING)
    private String ownerWording;//not null
    private Boolean isOpen;//
    @Size(max = 100)
    @Pattern(regexp = Constants.STRING)
    private String  skbAccount;
    private String skbPassword;
    @Size(max = 100)
    @Pattern(regexp = Constants.STRING)
    private String adtId;
    private String adtPw;
    @Size(max = 100)
    @Pattern(regexp = Constants.STRING)
    private String nhnId;
    private String nhnPw;
    //    @Size(min=10,max=50)
    @Size(min=2,max = 20)
    @Pattern(regexp = Constants.STRING)
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
