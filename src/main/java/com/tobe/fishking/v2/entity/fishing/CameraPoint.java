package com.tobe.fishking.v2.entity.fishing;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.model.common.Location;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "camera_point")
public class CameraPoint extends BaseTime{
    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE'

    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', company, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    // EXEC sp_addextendedproperty 'MS_Description', N'계정', 'USER', DBO, 'TABLE', company, 'COLUMN',  member_id
//    @ManyToOne
//    @JoinColumn(columnDefinition = "int NOT NULL   comment '계정'  ")
//    private Member member;  //name이 없을 경우 member_id

    // EXEC sp_addextendedproperty 'MS_Description', N'업체명', 'USER', DBO, 'TABLE', company, 'COLUMN',  commany_name
    @Column(columnDefinition = "varchar(50) not null comment '위치명'  ")
    private String name;

    //공통코드
    // EXEC sp_addextendedproperty 'MS_Description', N'시도', 'USER', DBO, 'TABLE', company, 'COLUMN',  sido
    @Column(columnDefinition = "varchar(50) comment '시도'  ")
    private String sido;

    // EXEC sp_addextendedproperty 'MS_Description', N'군구', 'USER', DBO, 'TABLE', company, 'COLUMN',  gungu
    @Column(columnDefinition = "varchar(50) comment '군구'  ")
    private String gungu;

    // EXEC sp_addextendedproperty 'MS_Description', N'주소', 'USER', DBO, 'TABLE', company, 'COLUMN',  address
    @Column(columnDefinition = "varchar(100)   comment '주소'  ")
    private String address;

    private Location location;

//    @Column(columnDefinition = "varchar(50) comment 'Skb계정' ")
//    private String  skbAccount;

//    @Convert(converter = StringConverter.class)
//    @Column(columnDefinition = "varchar(150) comment 'Skb패스워드' ")
//    private String skbPassword;

    @Column(columnDefinition = "varchar(20) comment 'ADT캡스 아이디'")
    private String adtId;

    @Column(columnDefinition = "varchar(200) comment 'ADT캡스 비번'")
    private String adtPw;

    @OneToMany
    @JoinColumn(name = "rtvideos_ship_id" , columnDefinition = "bigint  comment '실시간영상'  ")
    //  @Builder.Default
    @JsonBackReference
    private final List<RealTimeVideo> shiipRealTimeVideos = new ArrayList<>();

//    @Column(columnDefinition = "varchar(20) comment 'NHN토스트캠 아이디'")
//    private String nhnId;

//    @Column(columnDefinition = "varchar(20) comment 'NHN토스트캠 비번'")
//    private String nhnPw;

    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', company, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by", updatable= false , columnDefinition  = " bigint not null  comment '생성자'")
    private Member createdBy;

    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', company, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by", columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;

    public CameraPoint(String name, Member member) {
        this.name = name;
        this.createdBy = member;
        this.modifiedBy =  member ;
    }

    public void modify(
            String name, String sido, String gungu, String address, Double lat, Double lon,
            String adtId, String adtPw, Member modifiedBy

    ){
        this.name = name;
        this.sido = sido;
        this.gungu = gungu;
        this.address = address;

        Location location = Location.builder().latitude(lat).longitude(lon).build();
        this.location = location;

        this.adtId =adtId ;
        this.adtPw = adtPw;
        this.modifiedBy = modifiedBy;

        return;
    }

}
