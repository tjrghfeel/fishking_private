package com.tobe.fishking.v2.entity.board;


import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.enums.board.QuestionType;
import com.tobe.fishking.v2.enums.board.ReturnType;
import com.tobe.fishking.v2.enums.common.ChannelType;
import com.tobe.fishking.v2.model.board.UpdatePostDTO;
import com.tobe.fishking.v2.service.StringConverter;
import lombok.*;
import org.aspectj.weaver.patterns.TypePatternQuestions;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// @NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@Builder
@AllArgsConstructor
@Entity(name = "Post")
@Table(name = "post")
public class Post extends BaseTime {

    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', banner, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    // EXEC sp_addextendedproperty 'MS_Description', N'게시판그룹', 'USER', DBO, 'TABLE', orders, 'COLUMN',  fishing_ships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", columnDefinition = " bigint not null   comment '게시판그룹'  ")
    private Board board;

    // EXEC sp_addextendedproperty 'MS_Description', N'상위id', 'USER', DBO, 'TABLE', post, 'COLUMN',  parent_id
    // @Column(columnDefinition ="INT NULL")
    @Column(nullable = true, columnDefinition ="INT  comment '상위 ID' ")
    private Long parent_id;

    // EXEC sp_addextendedproperty 'MS_Description', N'작성자이름', 'USER', DBO, 'TABLE', post, 'COLUMN',  writer_name
    @Column(columnDefinition = "int comment '채널타입' ", nullable = false)
    @Enumerated(EnumType.ORDINAL) //ORDINAL -> int로 할당 STRING -> 문자열로 할당
    private ChannelType channelType;


    // EXEC sp_addextendedproperty 'MS_Description', N'작성자이름', 'USER', DBO, 'TABLE', post, 'COLUMN',  writer_name
    @Column(columnDefinition = "int comment '문의유형' ", nullable = false)
    @Enumerated(EnumType.ORDINAL) //ORDINAL -> int로 할당 STRING -> 문자열로 할당
    private QuestionType questionType;


    // EXEC sp_addextendedproperty 'MS_Description', N'제목', 'USER', DBO, 'TABLE', post, 'COLUMN',  title
    @Column(nullable = false , columnDefinition = " varchar(100) comment '제목' ")
    private String title;
    
    // EXEC sp_addextendedproperty 'MS_Description', N'내용', 'USER', DBO, 'TABLE', post, 'COLUMN',  content
    @Column(columnDefinition = "varchar(2000) comment '내용' ", nullable = false)
    private String contents;

    // EXEC sp_addextendedproperty 'MS_Description', N'작성자', 'USER', DBO, 'TABLE', post, 'COLUMN',  member_id
    @ManyToOne(targetEntity= Member.class, fetch=FetchType.LAZY) // (1)
    @JoinColumn(columnDefinition = "int null comment '작성자' ")
    private Member author;

    // EXEC sp_addextendedproperty 'MS_Description', N'작성자이름', 'USER', DBO, 'TABLE', post, 'COLUMN',  writer_name
//    @Convert(converter = StringConverter.class)
    @Column(columnDefinition = "varchar(200)  comment '작성자' ",  nullable = false)   //1:1문의
    private String authorName;


    // EXEC sp_addextendedproperty 'MS_Description', N'답변방법', 'USER', DBO, 'TABLE', post, 'COLUMN',  writer_name
    @Column(columnDefinition = "int  comment '답변방법' ", nullable = false)
    private ReturnType returnType;


    // EXEC sp_addextendedproperty 'MS_Description', N'답변주소', 'USER', DBO, 'TABLE', post, 'COLUMN',  writer_name
    @Column(columnDefinition = "varchar(200)  comment '답변주소'  ", nullable = false)
    private String returnNoAddress;

    // EXEC sp_addextendedproperty 'MS_Description', N'작성위치', 'USER', DBO, 'TABLE', post, 'COLUMN',  created_at
    @Column(columnDefinition = "varchar(200)  comment '작성위치' ", nullable = false)
    private String createdAt;

    @Transient // DB에 영향을 미치지 않는다.
    @Column(columnDefinition = "varchar(200)  comment '좋아요수' ")
    private Integer likeCount;

    @Column(columnDefinition = "bit default 0  comment '비밀글여부' ")
    private Boolean isSecret;

    @ManyToMany(targetEntity= Tag.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "post_tags", columnDefinition = "comment '태그' ")
    @Builder.Default
    private List<Tag> tags = new ArrayList<>();

    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', post, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" , updatable= false , columnDefinition  = "bigint  NOT NULL   comment '생성자'  ")
    private Member createdBy;

    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', post, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" , columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;

    @Column(columnDefinition = "bit comment '회원에 대한 글이면 true, 업주에 대한 글이면 false.' ")
    private Boolean targetRole;

    @Column(columnDefinition = "bit comment '1:1문의의 경우 답변여부'")
    private Boolean isReplied;

    @Column(columnDefinition = "bit comment '삭제여부'")
    private Boolean isDeleted;

//    @Column(columnDefinition = "date null comment '공지사항의 중요공지 등록 기간 시작일'")
//    private LocalDate noticeStartDate;
    @Column(columnDefinition = "date null comment '공지사항의 중요공지 등록 기간 종료일'")
    private LocalDate noticeEndDate;

    public Post(){

    }
    //Getters and setters ommitted for brevity
    public void addTag(Tag tag) {
        tags.add(tag);
        //    tag.getPosts().add(this);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
        //   tag.getPosts().remove(this);
    }

    public Post(Member member, Board board, String authorName , String title, String contents) {
        this.createdBy = member;
        this.modifiedBy = member;
        this.board = board;
        this.authorName = authorName;
        this.title = title;
        this.contents = contents;
    }

    //Post 수정시에, UpdatePostDTO로부터 엔터티 정보 수정하는 메소드.
    public void updatePost(UpdatePostDTO postDTO){
        channelType = ChannelType.valueOf(postDTO.getChannelType());
        title = postDTO.getTitle();
        contents = postDTO.getContents();
        returnType = ReturnType.valueOf(postDTO.getReturnType());
        returnNoAddress = postDTO.getReturnNoAddress();
        createdAt = postDTO.getCreatedAt();
        questionType = QuestionType.valueOf(postDTO.getQuestionType());
        isSecret = postDTO.getIsSecret();
        noticeEndDate = postDTO.getNoticeEndDate();
    }

    public void delete(){this.isDeleted = true;}
    public void setChannelType(ChannelType channelType){this.channelType = channelType;}
}
