package com.tobe.fishking.v2.entity.fishing;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.model.admin.company.CompanyModifyDtoForManage;
import com.tobe.fishking.v2.model.fishing.CompanyUpdateDTO;
import com.tobe.fishking.v2.model.fishing.CompanyWriteDTO;
import com.tobe.fishking.v2.service.StringConverter;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "company")
public class Company extends BaseTime {  //선상
    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE'

    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', company, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    // EXEC sp_addextendedproperty 'MS_Description', N'계정', 'USER', DBO, 'TABLE', company, 'COLUMN',  member_id
    @ManyToOne
    @JoinColumn(columnDefinition = "int NOT NULL   comment '계정'  ")
    private Member member;  //name이 없을 경우 member_id
    
    // EXEC sp_addextendedproperty 'MS_Description', N'업체명', 'USER', DBO, 'TABLE', company, 'COLUMN',  commany_name
    @Column(columnDefinition = "varchar(50) comment '업체명'  ")
    private String companyName;

    // EXEC sp_addextendedproperty 'MS_Description', N'선주', 'USER', DBO, 'TABLE', company, 'COLUMN',  ship_owner
    @Column(columnDefinition = "varchar(50) comment '선주'  ")
    private String shipOwner;

    //공통코드
    // EXEC sp_addextendedproperty 'MS_Description', N'시도', 'USER', DBO, 'TABLE', company, 'COLUMN',  sido
    @Column(columnDefinition = "varchar(50) comment '시도'  ")
    private String sido;

    // EXEC sp_addextendedproperty 'MS_Description', N'군구', 'USER', DBO, 'TABLE', company, 'COLUMN',  gungu
    @Column(columnDefinition = "varchar(50) comment '군구'  ")
    private String gungu;



    // EXEC sp_addextendedproperty 'MS_Description', N'연락처', 'USER', DBO, 'TABLE', company, 'COLUMN',  tel
    @Column(columnDefinition = "varchar(30)   comment '연락처'  ")
    private String tel;
    @Column(columnDefinition = "varchar(30) comment '업체전화번호'")
    private String phoneNumber;


    // EXEC sp_addextendedproperty 'MS_Description', N'사업자등록번호', 'USER', DBO, 'TABLE', company, 'COLUMN',  biz_no
    @Column(columnDefinition = "varchar(30)   comment '사업자등록번호'  ")
    private String bizNo;

    // EXEC sp_addextendedproperty 'MS_Description', N'항구명', 'USER', DBO, 'TABLE', company, 'COLUMN',  biz_no
    @Column(columnDefinition = "varchar(30)   comment '항구명'  ")
    private String harbor;
    
    // EXEC sp_addextendedproperty 'MS_Description', N'사업자등록증파일', 'USER', DBO, 'TABLE', company, 'COLUMN',  biz_no_file_url
    @OneToOne
    @JoinColumn(columnDefinition = "bigint   comment '사업자등록파일'  ")
    private FileEntity bizNoFileId;

    // EXEC sp_addextendedproperty 'MS_Description', N'대표자신분증파일', 'USER', DBO, 'TABLE', company, 'COLUMN',  represent_file_url
    @OneToOne
    @JoinColumn(columnDefinition = "bigint   comment '대표자신분증파일'  ")
    private FileEntity representFileId ;

    @OneToOne
    @JoinColumn(columnDefinition = "bigint  comment '정산 통장 사본'  ")
    private FileEntity accountFileId;

    @Column(columnDefinition = "varchar(150)  comment '사업자등록파일 downloadUrl")
    private String bizNoFileDownloadUrl;

    @Column(columnDefinition = "varchar(150) comment '대표자신분증파일 downloadUrl")
    private String representFileDownloadUrl;

    @Column(columnDefinition = "varchar(150) comment '정산통장사본파일 downloadUrl")
    private String accountFileDownloadUrl;

    // EXEC sp_addextendedproperty 'MS_Description', N'정산계좌은행 ', 'USER', DBO, 'TABLE', company, 'COLUMN',  bank
    @Column(columnDefinition = "varchar(10)   comment '정산계좌은행'  ")
    private String bank ;

    // EXEC sp_addextendedproperty 'MS_Description', N'정산계좌번호', 'USER', DBO, 'TABLE', company, 'COLUMN',  account_no
    @Column(columnDefinition = "varchar(20)   comment '정산계좌번호'  ")
    private String accountNo ;

    @Column(columnDefinition = "varchar(20)   comment '정산계좌 예금주명'  ")
    private String accountName ;

    // EXEC sp_addextendedproperty 'MS_Description', N'사장님한마디', 'USER', DBO, 'TABLE', company, 'COLUMN',  owner_wording
    @Column(columnDefinition = "varchar(500)  comment '사장님한마디'  ")
    private String ownerWording;


    // EXEC sp_addextendedproperty 'MS_Description', N'영업상태', 'USER', DBO, 'TABLE', company, 'COLUMN',  is_open
    @Column(columnDefinition = "bit default 1 comment '영업상태- default :영업중' ")
    private Boolean isOpen;


    // EXEC sp_addextendedproperty 'MS_Description', N'Skb계정', 'USER', DBO, 'TABLE', company, 'COLUMN',  skb_account

    @Column(columnDefinition = "varchar(50) comment 'Skb계정' ")
    private String  skbAccount;



    // EXEC sp_addextendedproperty 'MS_Description', N'Skb패스워드', 'USER', DBO, 'TABLE', company, 'COLUMN',  skb_password
    @Convert(converter = StringConverter.class)
    @Column(columnDefinition = "varchar(150) comment 'Skb패스워드' ")
    private String skbPassword;

    // EXEC sp_addextendedproperty 'MS_Description', N'주소', 'USER', DBO, 'TABLE', company, 'COLUMN',  address
    @Column(columnDefinition = "varchar(100)   comment '주소'  ")
    private String companyAddress;


    // EXEC sp_addextendedproperty 'MS_Description', N'등록여부', 'USER', DBO, 'TABLE', company, 'COLUMN',  is_regisitered
    @Column(columnDefinition = "bit default 0   comment '등록여부'  ")
    private Boolean isRegistered;

    @Column(columnDefinition = "varchar(20) comment 'ADT캡스 아이디'")
    private String adtId;

    @Column(columnDefinition = "varchar(200) comment 'ADT캡스 비번")
    private String adtPw;

    @Column(columnDefinition = "varchar(20) comment 'NHN토스트캠 아이디'")
    private String nhnId;

    @Column(columnDefinition = "varchar(20) comment 'NHN토스트캠 비번'")
    private String nhnPw;

    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', company, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by", updatable= false , columnDefinition  = " bigint not null  comment '생성자'")
    private Member createdBy;


    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', company, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by", columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;

    public Company(String companyName, Member member) {
        this.companyName = companyName;
        this.createdBy = member;
        this.modifiedBy =  member ;
    }

    public void updateCompanyRegisterRequest(CompanyUpdateDTO dto, Member modifiedBy, FileEntity[] files){
        companyName = (dto.getCompanyName());
        shipOwner = (member.getMemberName());
//        sido = (dto.getSido());
//        gungu = (dto.getGungu());
        tel = (dto.getTel());
        bizNo = (dto.getBizNo());
        harbor = (dto.getHarbor());
        bank = (dto.getBank());
        accountNo = (dto.getAccountNo());
//        ownerWording = (dto.getOwnerWording());
//        isOpen=(dto.getIsOpen());
//        skbAccount=(dto.getSkbAccount());
//        if(dto.getSkbPassword()!=null){skbPassword=((dto.getSkbPassword()));}//비번같은경우, 변경안할경우 dto안의 값이 비어있기때문에, 확인후 업데이트해줌.
        companyAddress=(dto.getCompanyAddress());
//        isRegistered=(dto.getIsRegistered());
        bizNoFileId = (files[0]);
        representFileId = (files[1]);
        accountFileId = (files[2]);
        bizNoFileDownloadUrl = "/"+files[0].getFileUrl()+"/"+files[0].getStoredFile();
        representFileDownloadUrl = "/"+files[1].getFileUrl()+"/"+files[1].getStoredFile();
        accountFileDownloadUrl = "/"+files[2].getFileUrl()+"/"+files[2].getStoredFile();
        this.modifiedBy = (modifiedBy);
    }

    public void updateCompanyForManage(CompanyModifyDtoForManage dto, Member manager, Member member, FileEntity[] files){
        companyName = (dto.getCompanyName());
        shipOwner = (member.getMemberName());
        sido = (dto.getSido());
        gungu = (dto.getGungu());
        tel = (dto.getTel());
        bizNo = (dto.getBizNo());
        harbor = (dto.getHarbor());
        bank = (dto.getBank());
        accountNo = (dto.getAccountNo());
        ownerWording = (dto.getOwnerWording());
        isOpen=(dto.getIsOpen());
        skbAccount=(dto.getSkbAccount());
        if(dto.getSkbPassword()!=null){skbPassword=((dto.getSkbPassword()));}//비번같은경우, 변경안할경우 dto안의 값이 비어있기때문에, 확인후 업데이트해줌.
        companyAddress=(dto.getCompanyAddress());
        isRegistered=(dto.getIsRegistered());
        bizNoFileId = (files[0]);
        representFileId = (files[1]);
        accountFileId = (files[2]);
        bizNoFileDownloadUrl = "/"+files[0].getFileUrl()+"/"+files[0].getStoredFile();
        representFileDownloadUrl = "/"+files[1].getFileUrl()+"/"+files[1].getStoredFile();
        accountFileDownloadUrl = "/"+files[2].getFileUrl()+"/"+files[2].getStoredFile();
        modifiedBy = (manager);
        this.member = (member);
    }

    public void changeAccount(String bank, String accountNo, String accountName, Member member) {
        this.bank = bank;
        this.accountNo = accountNo;
        this.accountName = accountName;
        this.modifiedBy = member;
    }

    public void acceptRequest(){this.isRegistered = true;}
    public void setIsOpen(Boolean isOpen){this.isOpen = isOpen;}
}
