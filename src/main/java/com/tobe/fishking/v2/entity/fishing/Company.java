package com.tobe.fishking.v2.entity.fishing;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.model.fishing.CompanyUpdateDTO;
import com.tobe.fishking.v2.model.fishing.CompanyWriteDTO;
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
    @JoinColumn(columnDefinition = "bigint  commont '정산 통장 사본'  ")
    private FileEntity accountFileId;

    // EXEC sp_addextendedproperty 'MS_Description', N'정산계좌은행 ', 'USER', DBO, 'TABLE', company, 'COLUMN',  bank
    @Column(columnDefinition = "varchar(10)   comment '정산계좌은행'  ")
    private String bank ;

    // EXEC sp_addextendedproperty 'MS_Description', N'정산계좌번호', 'USER', DBO, 'TABLE', company, 'COLUMN',  account_no
    @Column(columnDefinition = "varchar(20)   comment '정산계좌번호'  ")
    private String accountNo ;

    // EXEC sp_addextendedproperty 'MS_Description', N'사장님한마디', 'USER', DBO, 'TABLE', company, 'COLUMN',  owner_wording
    @Column(columnDefinition = "varchar(500)  comment '사장님한마디'  ")
    private String ownerWording;


    // EXEC sp_addextendedproperty 'MS_Description', N'영업상태', 'USER', DBO, 'TABLE', company, 'COLUMN',  is_open
    @Column(columnDefinition = "bit default 1 comment '영업상태- default :영업중' ")
    private boolean isOpen;


    // EXEC sp_addextendedproperty 'MS_Description', N'Skb계정', 'USER', DBO, 'TABLE', company, 'COLUMN',  skb_account
    @Column(columnDefinition = "varchar(50) comment 'Skb계정' ")
    private String  skbAccount;

    // EXEC sp_addextendedproperty 'MS_Description', N'Skb패스워드', 'USER', DBO, 'TABLE', company, 'COLUMN',  skb_password
    @Column(columnDefinition = "varchar(50) comment 'Skb패스워드' ")
    private String skbPassword;

    // EXEC sp_addextendedproperty 'MS_Description', N'주소', 'USER', DBO, 'TABLE', company, 'COLUMN',  address
    @Column(columnDefinition = "varchar(100)   comment '주소'  ")
    private String companyAddress;


    // EXEC sp_addextendedproperty 'MS_Description', N'등록여부', 'USER', DBO, 'TABLE', company, 'COLUMN',  is_regisitered
    @Column(columnDefinition = "bit default 0   comment '등록여부'  ")
    private boolean isRegisitered;


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

    public void updateCompanyRegisterRequest(CompanyWriteDTO dto, Member modifiedBy, FileEntity[] files){
        //private Long id;//not null, pk
        //private Long member;  //name이 없을 경우 member_id, fk
        companyName = dto.getCompanyName();//
        shipOwner = dto.getShipOwner();//
        sido = dto.getSido();
        gungu = dto.getGungu();
        tel = dto.getTel();//
        bizNo = dto.getBizNo();
        harbor = dto.getHarbor();//
        //private String bizNoFilesUrl ;
        //private String representFilesUrl ;
        bank = dto.getBank();
        accountNo = dto.getAccountNo();
        ownerWording = dto.getOwnerWording();//not null
        //isOpen = dto.isOpen();
        //private String  skbAccount;
        //private String skbPassword;
        companyAddress = dto.getCompanyAddress();//
        //isRegisitered = dto.isRegisitered();
        //private Member createdBy;//not null, fk
        this.modifiedBy = modifiedBy;//not null, fk
        bizNoFileId = files[0];//not null
        representFileId = files[1];//not null
        accountFileId = files[2];//not null
    }
}
