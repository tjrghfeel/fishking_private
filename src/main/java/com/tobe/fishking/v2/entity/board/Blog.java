package com.tobe.fishking.v2.entity.board;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import lombok.*;
import org.springframework.data.annotation.Transient;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
@Entity(name = "Blog")
@Table(name = "blog")
public class Blog extends BaseTime {
    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', blog

    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', blog, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // IDENTITY)
// @Column(columnDefinition = "comment 'id'  ")
    private int id;


    // EXEC sp_addextendedproperty 'MS_Description', N'사진 찍은 위치', 'USER', DBO, 'TABLE', blog, 'COLUMN',  location
    @Column(columnDefinition = "varchar(200)  comment '사진 찍은 위치'  ")
    private String location; //사진 찍은 위치 (로마)

    // EXEC sp_addextendedproperty 'MS_Description', N'사진 설명', 'USER', DBO, 'TABLE', blog, 'COLUMN',  caption
    @Column(columnDefinition = "varchar(1000)  comment '사진 설명'  ")
    private String caption; // 사진 설명
    // EXEC sp_addextendedproperty 'MS_Description', N'포스팅 사진 경로+이름', 'USER', DBO, 'TABLE', blog, 'COLUMN',  post_image
    @Column(columnDefinition = "varchar(200)  comment '포스팅 사진 경로+이름'  ")
    private String postImage; //포스팅 사진 경로+이름

//    // (1) Like List
//    @OneToMany(mappedBy = "blog")
//    private List<LoveTo> loveTo = new ArrayList<>();
//
//    // (2) Tag List
//    @OneToMany(mappedBy = "blog")
//    @JsonManagedReference
//    private List<Tag> tags = new ArrayList<>();

    @Transient // DB에 영향을 미치지 않는다.
    private int likeCount;

    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', blog, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" , insertable= false ,  updatable= false , columnDefinition = "bigint  NOT NULL   comment '생성자'  ")
    private Member createdBy;

    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', blog, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" , insertable= false ,  updatable= false , columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;


}
