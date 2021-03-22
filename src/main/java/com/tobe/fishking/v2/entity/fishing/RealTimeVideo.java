package com.tobe.fishking.v2.entity.fishing;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@AllArgsConstructor
@Table(name = "realtime_video")
public class RealTimeVideo extends BaseTime {

    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', realtime_video, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    // EXEC sp_addextendedproperty 'MS_Description', N'선상', 'USER', DBO, 'TABLE', realtime_video, 'COLUMN',  ship_id
    @ManyToOne
    @JoinColumn(name="rtvideos_ship_id" , columnDefinition = "bigint  NOT NULL   comment '선상'")
    @JsonManagedReference
    private Ship ships;

    // EXEC sp_addextendedproperty 'MS_Description', N'번호', 'USER', DBO, 'TABLE', realtime_video, 'COLUMN',  r_no
    @Column(columnDefinition = "int comment '번호'  ")
    private int rNo;

    // EXEC sp_addextendedproperty 'MS_Description', N'위치', 'USER', DBO, 'TABLE', realtime_video, 'COLUMN',  r_no
    @Column(columnDefinition = "Varchar(10) comment '위치'  ")
    private String position;

    // EXEC sp_addextendedproperty 'MS_Description', N'썸네일URL', 'USER', DBO, 'TABLE', realtime_video, 'COLUMN',  thumbnail_url
    @Column(columnDefinition = "varchar(200)   comment '썸네일URL'  ")
    private String thumbnailUrl;


    // EXEC sp_addextendedproperty 'MS_Description', N'URL', 'USER', DBO, 'TABLE', realtime_video, 'COLUMN',  url
    @Column(columnDefinition = "varchar(200)   comment 'URL'  ")
    private String url;


    // EXEC sp_addextendedproperty 'MS_Description', N'카메리위치', 'USER', DBO, 'TABLE', realtime_video, 'COLUMN',  camera_loc
    @Column(columnDefinition = "varchar(5)   comment '카메리위치'  ")
    private String cameraLoc;

    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', realtime_video, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = " bigint not null comment '생성자'")
    private Member createdBy;


    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', realtime_video, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" ,  columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;

    @Column(columnDefinition = "varchar(10) comment '타입' ")
    private String type;

    @Column(columnDefinition = "varchar(400) comment '토큰' ")
    private String token;

    @Column(columnDefinition = "varchar(400) comment '타임스탬프' ")
    private String expireTime;

    @Column(columnDefinition = "varchar(100) comment '이름' ")
    private String name;

    @Column(columnDefinition = "varchar(100) comment '시리얼넘버' ")
    private String serial;

    @Column(columnDefinition = "bit comment '사용여부' ")
    private Boolean isUse;

    @Builder
    public RealTimeVideo(Integer rNo, Member member, Ship ship, String name, String serial, String token, String expireTime, String type) {
        this.rNo = rNo;
        this.createdBy = member;
        this.modifiedBy = member;
        this.ships = ship;
        this.name = name;
        this.serial = serial;
        this.token = token;
        this.expireTime = expireTime;
        this.isUse = true;
        this.type = type;
    }

    public void updateToken(String token) {
        this.token = token;
    }

    public void setUse() {
        this.isUse = true;
    }

    public void setNotUse() {
        this.isUse = false;
    }
}
