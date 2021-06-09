package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.auth.Member;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyWriteDTO {
    //    private Long id;//not null, pk
    @Size(max = 50, message = "업체명은 50자 이하이어야합니다")
    private String companyName;//업체명
    @Size(max=50, message = "대표자명은 50자 이하이어야합니다")
    private String memberName;//대표자명?
//    @Size(min=2,max=15)
//    private String sido;
//    @Size(min=2,max=15)
//    private String gungu;
    @Size(max=30, message = "전화번호는 30자 이하이어야합니다")
    private String tel;//전화번호
    @Size(max=30, message = "휴대폰번호는 30자 이하이어야합니다")
    private String phoneNumber;//휴대폰번호

    @Size(max=30, message = "사업자등록번호는 30자 이하이어야합니다")
    private String bizNo;
    @Size(max=30, message = "항구명은 30자 이하이어야합니다")
    private String harbor;//
    @Size(max=10, message = "은행명은 10자 이하이어야합니다")
    private String bank ;

    @Size(max=30, message = "정산계좌번호는 20자 이하이어야합니다")
    private String accountNo ;
//    @NotNull
//    private String ownerWording;//not null
//    private Boolean isOpen;
//    private String  skbAccount;
//    private String skbPassword;
    @Size(max=100, message = "주소는 100자 이하이어야합니다")
    private String companyAddress;//지역명?
//    private Boolean isRegistered;
    /* @NotNull
     private Long createdBy;//not null, fk
     @NotNull
     private Long modifiedBy;//not null, fk*/
//    private String bizNoFilesUrl ;
//    private String representFilesUrl ;
//    private String accountFileUrl;
    @NotNull(message = "사업자등록증을 입력하세요")
    private Long bizNoFile;
    @NotNull(message = "대표자신분증을 입력하세요")
    private Long representFile;
    @NotNull(message = "정산통장사본을 입력하세요")
    private Long accountFile;

//    @Size(max=30, message = "ADT 아이디는 20자 이하이어야합니다")
//    private String adtId;
//    @Size(max=30, message = "ADT 비밀번호는 200자 이하이어야합니다")
//    private String adtPw;
//    @Size(max=30, message = "NHN 아이디는 20자 이하이어야합니다")
//    private String nhnId;
//    @Size(max=30, message = "NHN 비밀번호는 200자 이하이어야합니다")
//    private String nhnPw;
}
