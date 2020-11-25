package com.tobe.fishking.v2.entity.fishing;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.enums.fishing.FishingTechnic;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity(name = "FishingDiaryContents")
@Table(name = "fishing_diary_contents")
public class FishingDiaryContents extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // IDENTITY)
    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  id
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    // EXEC sp_addextendedproperty 'MS_Description', N'게시판그룹', 'USER', DBO, 'TABLE', orders, 'COLUMN',  fishing_ships
    @ManyToOne
    @JoinColumn(name = "board_id", columnDefinition = " bigint not null   comment '게시판그룹'  ")
    private Board board;

    //null이면 조행기, null이 아니면 조행일지
    // EXEC sp_addextendedproperty 'MS_Description', N'선상', 'USER', DBO, 'TABLE', ship, 'COLUMN',  ship
    @ManyToOne
    @JoinColumn(name="fishing_diary_ship_id",  columnDefinition = "bigint null   comment '선상'  ")
    private Ship ship;

    //null이면 조행일지, null이 아니면 조행기
    // EXEC sp_addextendedproperty 'MS_Description', N'글쓴이', 'USER', DBO, 'TABLE', ship, 'COLUMN',  ship
    @ManyToOne
    @JoinColumn(name="fishing_diary_member_id", columnDefinition = "bigint not null   comment '글쓴이'  ")
    private Member member ;


    // EXEC sp_addextendedproperty 'MS_Description', N'상품', 'USER', DBO, 'TABLE', ship, 'COLUMN',  goods
    @ManyToOne
    @JoinColumn(columnDefinition = "not null   comment '상품'  ")
    private Goods goods;


    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'제목', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  title
    @Column(columnDefinition = " varchar(200) not null comment '제목'  ")
    private String title;


    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'내용', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  contents
    @Column(columnDefinition = " varchar(1000) not null comment '내용'  ")
    private String contents;

    // EXEC sp_addextendedproperty 'MS_Description', N'사진 찍은 위치', 'USER', DBO, 'TABLE', blog, 'COLUMN',  location
    @Column(columnDefinition = "varchar(200)  comment '사진 찍은 위치'  ")
    private String location; //사진 찍은 위치 (로마)


    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'어종명', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  fishing_species_name
    @Column(columnDefinition = " varchar(200)  comment '어종명'  ")
    private String fishingSpeciesName;

    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'날짜', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  fishing_date
    @Column(columnDefinition = " varchar(10) comment '날짜' ")
    private String fishingDate;

    // @Column(columnDefinition = " comment '물때'  ")
    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'물때', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  fishing_tide_time
    @Column(columnDefinition = " varchar(100)  comment '물때'  ")
    private String fishingTideTime;

    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'물고기길이', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  fish_length
    @Column(columnDefinition = " float comment '물고기길이'  ")
    private double fishLength;

    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'물고기무게', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  fish_weight
    @Column(columnDefinition = " float  comment '물고기무게'  ")
    private double fishWeight;

    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'낚시기법', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  fish_weight
    @Column(columnDefinition = "int   comment '낚시기법'  ")
    @Enumerated(EnumType.ORDINAL) //ORDINAL -> int로 할당 STRING -> 문자열로 할당
    private FishingTechnic fishingTechnic;

    //@Column(columnDefinition = " comment '미끼'  ")
    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'미끼', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  fishing_lure
    @Column(columnDefinition = " varchar(200) comment '미끼'  ")
    private String fishingLure;

    //위치 항목에 대해 선상인 경우 선상명, 갯바위인 경우 지역정보(xx시, xx군 면 등)
    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'낚시장소', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  fish_location
    @Column(columnDefinition = " varchar(50) comment '낚시장소'  ")
    private String fishingLocation;

    //위치 항목에 대해 선상인 경우 선상명, 갯바위인 경우 지역정보(xx시, xx군 면 등)
    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'낚시장소', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  fish_location
    @Column(columnDefinition = " varchar(200) comment '작성장소'  ")
    private String writeLocation;

    // EXEC sp_addextendedproperty 'MS_Description', N'위도', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  latitude
    //@Column(columnDefinition=" INT(11) NOT NULL COMMENT '0 for no action, 1 for executed, 2 for validated, 3 for aproved'  ")
    @Column(columnDefinition = "float   comment '위도'  ")
    private Long writeLatitude;


    // EXEC sp_addextendedproperty 'MS_Description', N'경도', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  longitude
    //@Column(columnDefinition=" INT(11) NOT NULL COMMENT '0 for no action, 1 for executed, 2 for validated, 3 for aproved'  ")
    @Column(columnDefinition = "float   comment '경도'  ")
    private Long writelongitude;


   /* // EXEC sp_addextendedproperty 'MS_Description', N'스크랩 사용자', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  fishing_rtvideos
    @OneToMany
    @JoinColumn(name = "fishing_diary_scrab_by" , columnDefinition = "bigint comment '스크랩 사용자'  ")
    @Builder.Default
    private List<Member> scrabMembers = new ArrayList<>();
*/


    //사진및 동영상은 File로.. 구분자(FilePublish) 조황일지
    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = " bigint not null comment '생성자' ")
    private Member createdBy;

    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="modified_by" ,  columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;


    //댓글은 Comment  구분장 dependentType 은 조화잉ㄹ지

}
