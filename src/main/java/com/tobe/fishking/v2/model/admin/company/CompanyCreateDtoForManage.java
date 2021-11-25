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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class    CompanyCreateDtoForManage {

    @NotNull(message = "회원 아이디를 입력해주세요")
    @Size(max=50, message = "회원 아이디는 50자 이하이어야합니다")
    @Pattern(regexp = Constants.EMAIL, message = "회원 아이디의 이메일 형식이 맞지 않습니다")
    private String memberUid;  //name이 없을 경우 member_id, fk
    
    @NotNull(message = "업체명을 입력하세요")
    @Size(min=1, message = "업체명을 입력하세요")
    @Size(max=50, message = "업체명은 50자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "업체명은 한글, 숫자 또는 영문자로 구성되어야합니다")
    private String companyName;//
    
    @Size(max=50, message = "업주명은 50자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "업주명은 한글, 숫자 또는 영문자로 구성되어야합니다")
    private String shipowner;

    @Size(max=50, message = "주소(시도)는 50자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "주소(시,도)는 한글, 숫자 또는 영문자로 구성되어야합니다")
    private String sido;
    
    @Size(max=50, message = "주소(군,구)는 50자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "주소(군,구)는 한글, 숫자 또는 영문자로 구성되어야합니다")
    private String gungu;

    @Size(max=30, message = "업체전화번호는 30자 이하이어야합니다")
    @Pattern(regexp = Constants.NUMBER, message = "업체전화번호는 숫자로 구성되어야합니다")
    private String tel;//
    
    @NotNull(message = "휴대전화번호를 입력하세요")
    @Size(min=1, message = "휴대전화번호를 입력하세요")
    @Size(max=30, message = "휴대전화번호는 30자 이하이어야합니다")
    @Pattern(regexp = Constants.NUMBER, message = "휴대전화번호는 숫자로 구성되어야합니다")
    private String phoneNumber;
    
    @Size(max=30, message = "사업자등록번호는 30자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "사업자등록번호는 한글, 숫자 또는 영문자로 구성되어야합니다")
    private String bizNo;
    
    @Size(max=30, message = "항구명은 30자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "항구명은 한글, 숫자 또는 영문자로 구성되어야합니다")
    private String harbor;//
    
    @Size(max=10, message = "은행명은 10자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "은행명은 한글, 숫자 또는 영문자로 구성되어야합니다")
    private String bank ;

    @Size(max=20, message = "계좌번호는 20자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "계좌번호는 한글, 숫자 또는 영문자로 구성되어야합니다")
    private String accountNo ;
    
    @Size(max=500, message = "사장님한마디는 500자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "사장님한마디는 한글, 숫자 또는 영문자로 구성되어야합니다")
    private String ownerWording;//not null
    
//    @Size(max=50, message = "SKB아이디는 50자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "SKB아이디는 한글, 숫자 또는 영문자로 구성되어야합니다")
//    private String  skbAccount;
    
//    @Size(max=150, message = "SKB비밀번호는 150자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "SKB비밀번호는 한글, 숫자 또는 영문자로 구성되어야합니다")
//    private String skbPassword;
    
//    @Size(max=20, message = "NHN아이디는 20자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "업체명는 한글, 숫자 또는 영문자로 구성되어야합니다")
//    private String nhnId;
    
//    @Size(max=20, message = "NHN비밀번호는 20자 이하이어야합니다")
//    private String nhnPw;
    
//    @Size(min=10,max=50)
    @NotNull(message = "지역명을 입력하세요")
    @Size(min=1, message = "지역명을 입력하세요")
    @Size(max=100, message = "지역명은 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "지역명은 한글, 숫자 또는 영문자로 구성되어야합니다")
    private String companyAddress;//
    
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
    @NotNull(message = "정산 통장사본을 입력하세요")
    private Long accountFile;
    private Long fishingBoatBizReportFile;
    private Long marineLicenseFile;

}
