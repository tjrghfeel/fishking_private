package com.tobe.fishking.v2.entity.fishing;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.model.common.ShareStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


//@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "fishing_diary")
public class FishingDiary extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  id
    @Column(updatable = false, nullable = false, columnDefinition = "bigint  comment 'id' ")
    private Long id;

    // EXEC sp_addextendedproperty 'MS_Description', N'게시판그룹', 'USER', DBO, 'TABLE', orders, 'COLUMN',  fishing_ships
    @ManyToOne
    @JoinColumn(name = "board_id", columnDefinition = " bigint not null   comment '게시판'  ")
    private Board board;


    @Column(columnDefinition = "bigint null   comment '조행기/조행일지만'  ")
    @Enumerated(EnumType.ORDINAL)
    private FilePublish filePublish;

    //null이면 조행기, null이 아니면 조행일지
    // EXEC sp_addextendedproperty 'MS_Description', N'선상', 'USER', DBO, 'TABLE', ship, 'COLUMN',  ship
    @ManyToOne
    @JoinColumn(name = "fishing_diary_ship_id", columnDefinition = "bigint null   comment '선상'  ")
    private Ship ship;

    //null이면 조행일지, null이 아니면 조행기
    // EXEC sp_addextendedproperty 'MS_Description', N'글쓴이', 'USER', DBO, 'TABLE', ship, 'COLUMN',  ship
    @ManyToOne
    @JoinColumn(name = "fishing_diary_member_id", columnDefinition = "bigint not null   comment '글쓴이'  ")
    private Member member;


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
    private Double fishLength;

    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'물고기무게', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  fish_weight
    @Column(columnDefinition = " float  comment '물고기무게'  ")
    private Double fishWeight;

    @Column(columnDefinition = "varchar(200) comment '낚시기법'")
    private String fishingTechnic;

    @Column(columnDefinition = "varchar(200) comment '미끼'")
    private String fishingLure;

    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "int comment '선상/갯바위'")
    private FishingType fishingType;

    @ManyToMany(targetEntity = CommonCode.class)
    @JoinColumn(name = "fishing_diary_fishing_species", columnDefinition = " comment  '어종'  ")
    @Builder.Default
    private List<CommonCode> fishingSpecies = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id")
    @Builder.Default
    private Set<FishingDiaryFishingTechnics> fishingDiaryFishingTechnics = new LinkedHashSet<>();

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id")
    @Builder.Default
    private Set<FishingDiaryFishingLures> fishingDiaryFishingLures = new LinkedHashSet<>();

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
    private Double writeLatitude;


    // EXEC sp_addextendedproperty 'MS_Description', N'경도', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  longitude
    //@Column(columnDefinition=" INT(11) NOT NULL COMMENT '0 for no action, 1 for executed, 2 for validated, 3 for aproved'  ")
    @Column(columnDefinition = "float   comment '경도'  ")
    private Double writeLongitude;


    // EXEC sp_addextendedproperty 'MS_Description', N'스크랩 사용자', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  fishing_rtvideos
    @ManyToMany(targetEntity = Member.class)
    @JoinColumn(name = "fishing_diary_scrap_members", columnDefinition = "bigint not null   comment '스크랩 사용자'  ")
    //  @Builder.Default
    private final List<Member> scrapMembers = new ArrayList<>();


    //   @AttributeOverride(name = "shareCount", column = @Column(name = "SHARE"))
    @Embedded
    private ShareStatus status;

    @Column(columnDefinition = "bit not null default 0 comment '삭제여부'")
    private Boolean isDeleted;

    @Column(columnDefinition = "bit default 1 comment '숨김처리 여부. 글 자체가 없는것처럼 처리할때 사용. 관리자가 설정. '")
    private Boolean isActive;

    @Column(columnDefinition = "bit default 0 comment '내용 숨김처리 여부. 내용만 가릴때 사용. 관리자가 설정'")
    private Boolean isHidden;

/*
    @ManyToMany(targetEntity= FishingDiaryComment.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "fishing_diary_comments", columnDefinition = "comment '댓글' ")
    private List<FishingDiaryComment> comments = new ArrayList<>();
*/

    //사진및 동영상은 File로.. 구분자(FilePublish) 조황일지
    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name = "created_by", updatable = false, columnDefinition = " bigint not null comment '생성자' ")
    public Member createdBy;

    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', tide_journal, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name = "modified_by", columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    public Member modifiedBy;

    public FishingDiary(Board board, Ship ship, Member member, Goods goods, String title, String contents
            , String location, String fishingSpeciesName, String fishingDate, String fishingTideTime
            , double fishLength, double fishWeight, Set<FishingDiaryFishingTechnics> fishingDiaryFishingTechnics
            , Set<FishingDiaryFishingLures> fishingDiaryFishingLures
            , String fishingLocation, String writeLocation, Double writeLatitude
            , Double writeLongitude, Boolean isDeleted, Member createdBy, Member modifiedBy) {

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
        this.fishingDiaryFishingTechnics = fishingDiaryFishingTechnics;
        this.fishingDiaryFishingLures = fishingDiaryFishingLures;
        this.fishingLocation = fishingLocation;
        this.writeLocation = writeLocation;
        this.writeLatitude = writeLatitude;
        this.writeLongitude = writeLongitude;
        this.isDeleted = isDeleted;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
    }

    public FishingDiary() {

    }

    public FishingDiary(Long id, ShareStatus status) {
        this.id = id;
//        this.status = status;
        this.modifiedBy = modifiedBy;
    }


    public FishingDiary setUpdate(
            String title, String contents, String location, String fishingSpeciesName
            , String fishingDate, String fishingTideTime, double fishLength
            , double fishWeight, Set<FishingDiaryFishingTechnics> fishingDiaryFishingTechnics
            , Set<FishingDiaryFishingLures> fishingDiaryFishingLures, String fishingLocation, String writeLocation, Double writeLatitude, Double writelongitude, Member createdBy, Member modifiedBy) {

        this.title = title;
        this.contents = contents;
        this.location = location;
        this.fishingSpeciesName = fishingSpeciesName;
        this.fishingDate = fishingDate;
        this.fishingTideTime = fishingTideTime;
        this.fishLength = fishLength;
        this.fishWeight = fishWeight;
        this.fishingDiaryFishingTechnics = fishingDiaryFishingTechnics;
        this.fishingDiaryFishingLures = fishingDiaryFishingLures;
        this.fishingLocation = fishingLocation;
        this.writeLocation = writeLocation;
        this.writeLatitude = writeLatitude;
        this.writeLongitude = writelongitude;
        this.modifiedBy = modifiedBy;

        return this;
    }

    public void setFishingDiaryFishingTechnics(Set<FishingDiaryFishingTechnics> fishingDiaryFishingTechnics) {
        this.fishingDiaryFishingTechnics = fishingDiaryFishingTechnics;
    }

    public void setFishingDiaryFishingLures(Set<FishingDiaryFishingLures> fishingDiaryFishingLures) {
        this.fishingDiaryFishingLures = fishingDiaryFishingLures;
    }

//댓글은 Comment  구분장 dependentType 은 조화잉ㄹ지

    /*글쓰기 수정*/
    public FishingDiary modify(
            Ship ship, String title, String content, String fishSpecies, String fishingDate, String tideTime, String fishTechnic,
            String fishLure, FishingType fishingType, String fishingLocation, Double latitude, Double longitude, Member modifiedBy
    ){
        this.ship = ship;
        this.title = title;
        this.contents = content;
        this.fishingSpeciesName = fishSpecies;
        this.fishingDate = fishingDate;
        this.fishingTideTime = tideTime;
        this.fishingTechnic = fishTechnic;
        this.fishingLure = fishLure;
        this.fishingType = fishingType;
        this.fishingLocation = fishingLocation;
        this.writeLatitude = latitude;
        this.writeLongitude = longitude;
        this.modifiedBy = modifiedBy;
        return this;
    }

    public void delete(){this.isDeleted = true;}
    public void setActive(Boolean isActive){this.isActive = isActive;}
    public void setHide(Boolean isHidden){ this.isHidden = isHidden;}
}
