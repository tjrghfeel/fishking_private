package com.tobe.fishking.v2.entity.common;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.Goods;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "review") //상품정보
public class Review extends BaseTime {

    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', review, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
    // @Column(columnDefinition = "comment 'id'  ")
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    // EXEC sp_addextendedproperty 'MS_Description', N'상품', 'USER', DBO, 'TABLE', review, 'COLUMN',  goods
    @ManyToOne/*(cascade=CascadeType.ALL)*/
    @JoinColumn(name = "review_good_id", columnDefinition = "bigint  not null comment '상품'  ")
    private Goods goods ;

    // EXEC sp_addextendedproperty 'MS_Description', N'회원', 'USER', DBO, 'TABLE', review, 'COLUMN',  member
    @ManyToOne/*(cascade=CascadeType.ALL)*/
    @JoinColumn(name = "member_id", columnDefinition = "bigint  not null comment '회원'  ")
    private Member member ;

   //fileEntity에 파일내용, 이미지 저장

    // EXEC sp_addextendedproperty 'MS_Description', N'전체평균평점 5점만점. 0/0.5/1....5', 'USER', DBO, 'TABLE', review, 'COLUMN',  total_avg_by_review
    @Column(nullable = false,  columnDefinition = "float  default 0.0 comment  '전체평균평점'  ")
    private Double totalAvgByReview;

    // EXEC sp_addextendedproperty 'MS_Description', N'손맛평점', 'USER', DBO, 'TABLE', review, 'COLUMN',  taste_by_review
    @Column(nullable = false,  columnDefinition = "float  default 0.0 comment  '손맛평점'  ")
    private Double tasteByReview;

    // EXEC sp_addextendedproperty 'MS_Description', N'서비스평점', 'USER', DBO, 'TABLE', review, 'COLUMN',  service_by_review
    @Column(nullable = false,  columnDefinition = "float  default 0.0 comment  '서비스평점'  ")
    private Double serviceByReview;
    // EXEC sp_addextendedproperty 'MS_Description', N'청결도평점', 'USER', DBO, 'TABLE', review, 'COLUMN',  clean_by_review
    @Column(nullable = false,  columnDefinition = "float  default 0.0 comment  '청결도평점'  ")
    private Double cleanByReview;

    @Column(nullable = false, columnDefinition = "varchar(2000) comment '내용'")
    private String content;

    @Column(nullable = false, columnDefinition = "varchar(10) comment '낚시 일자'")
    private String fishingDate;

    @Column(columnDefinition = "bit not null default 0 comment '삭제여부'")
    private Boolean isDeleted;

    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', review, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = " bigint not null  comment '생성자'")
    private Member createdBy;


    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', review, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" ,  columnDefinition = " bigint not null  comment '수정자'")
    private Member modifiedBy;

    public void modify(
        Double cleanScore, Double serviceScore, Double tasteScore, String content, Member modifiedBy
    ){
        this.cleanByReview = cleanScore; this.tasteByReview = tasteScore;
        this.serviceByReview = serviceScore; this.content = content;
        this.totalAvgByReview = (cleanScore + serviceScore + tasteScore)/3;
        this.modifiedBy = modifiedBy;
    }
    public void delete(){this.isDeleted=true;}
}
