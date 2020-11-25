package com.tobe.fishking.v2.entity.fishing;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.board.Tag;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.fishing.FishingTechnic;
import com.tobe.fishking.v2.model.common.ShareStatus;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


//@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "fishing_diary")
public class FishingDiary extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // IDENTITY)
    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  id
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    // EXEC sp_addextendedproperty 'MS_Description', N'게시판그룹', 'USER', DBO, 'TABLE', orders, 'COLUMN',  fishing_ships
    @ManyToOne
    @JoinColumn(name = "board_id", columnDefinition = " bigint not null   comment '게시판'  ")
    private Board board;


    @Column(columnDefinition = "bigint null   comment '조행기/조행일지만'  ")
    @Enumerated(EnumType.ORDINAL)
    private FilePublish  filePublish;

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
    private Long writeLongitude;


    // EXEC sp_addextendedproperty 'MS_Description', N'스크랩 사용자', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  fishing_rtvideos
    @ManyToMany(targetEntity = Member.class)
    @JoinColumn(name = "fishing_diary_scrap_by" , columnDefinition = "bigint not null   comment '스크랩 사용자'  ")
    //  @Builder.Default
    private final List<Member> scrapMembers = new ArrayList<>();

    @AttributeOverride(name="shareCount", column=@Column(name = "SHARE"))
    private ShareStatus status;

/*
    @ManyToMany(targetEntity= FishingDiaryComment.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "fishing_diary_comments", columnDefinition = "comment '댓글' ")
    private List<FishingDiaryComment> comments = new ArrayList<>();
*/

    @Column(columnDefinition = "bit not null default 1 comment '활성화 여부' ")
    private boolean isActive;

    //사진및 동영상은 File로.. 구분자(FilePublish) 조황일지
    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = " bigint not null comment '생성자' ")
    public Member createdBy;

    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="modified_by" ,  columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    public Member modifiedBy;

    public FishingDiary(Board board, Ship ship, Member member, Goods goods, String title, String contents
            , String location, String fishingSpeciesName, String fishingDate, String fishingTideTime
            , double fishLength, double fishWeight, FishingTechnic fishingTechnic
            , String fishingLure, String fishingLocation, String writeLocation, Long writeLatitude
            , Long writeLongitude, Member createdBy, Member modifiedBy) {

        this.board = board;
        this.ship = ship;
        this.member = member;
        this.goods = goods;
        this.title = title;
        this.contents = contents;
        this.location = location;
        this.fishingSpeciesName = fishingSpeciesName;
        this.fishingDate = fishingDate;
        this.fishingTideTime = fishingTideTime;
        this.fishLength = fishLength;
        this.fishWeight = fishWeight;
        this.fishingTechnic = fishingTechnic;
        this.fishingLure = fishingLure;
        this.fishingLocation = fishingLocation;
        this.writeLocation = writeLocation;
        this.writeLatitude = writeLatitude;
        this.writeLongitude = writeLongitude;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
    }

    public FishingDiary() {

    }

    public FishingDiary(Long id,  ShareStatus status  ) {
        this.id = id;
        this.status = status;
        this.modifiedBy = modifiedBy;
    }


    public FishingDiary setUpdate(String title, String contents, String location, String fishingSpeciesName, String fishingDate, String fishingTideTime, double fishLength, double fishWeight, FishingTechnic fishingTechnic, String fishingLure, String fishingLocation, String writeLocation, Long writeLatitude, Long writelongitude, Member createdBy, Member modifiedBy) {

        this.title = title;
        this.contents = contents;
        this.location = location;
        this.fishingSpeciesName = fishingSpeciesName;
        this.fishingDate = fishingDate;
        this.fishingTideTime = fishingTideTime;
        this.fishLength = fishLength;
        this.fishWeight = fishWeight;
        this.fishingTechnic = fishingTechnic;
        this.fishingLure = fishingLure;
        this.fishingLocation = fishingLocation;
        this.writeLocation = writeLocation;
        this.writeLatitude = writeLatitude;
        this.writeLongitude = writelongitude;
        this.modifiedBy = modifiedBy;

        return this;
    }





//댓글은 Comment  구분장 dependentType 은 조화잉ㄹ지

}
