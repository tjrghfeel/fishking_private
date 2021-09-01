package com.tobe.fishking.v2.entity.fishing;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import lombok.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@AllArgsConstructor
@Table(name = "fishing_tube")
public class FishingTube {

    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', realtime_video, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    @Column(columnDefinition = "varchar(50)   comment 'youtube video id'  ")
    private String videoId;

    // EXEC sp_addextendedproperty 'MS_Description', N'선상', 'USER', DBO, 'TABLE', realtime_video, 'COLUMN',  ship_id
//    @ManyToOne
//    @JoinColumn(name="rtvideos_ship_id" , columnDefinition = "bigint  NOT NULL   comment '선상'")
//    @JsonManagedReference
//    private Ship ships;

    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', realtime_video, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = " bigint not null comment '생성자'")
    private Member createdBy;

    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', realtime_video, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" ,  columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;

    @Builder
    public FishingTube(Member member, String videoId) {
        this.createdBy = member;
        this.modifiedBy = member;
        this.videoId = videoId;
    }

}
