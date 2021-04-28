package com.tobe.fishking.v2.model.admin.post;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.board.Tag;
import com.tobe.fishking.v2.enums.Constants;
import com.tobe.fishking.v2.enums.board.QuestionType;
import com.tobe.fishking.v2.enums.board.ReturnType;
import com.tobe.fishking.v2.enums.common.ChannelType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostSearchConditionDto {
    private Long id;
    private Long boardId;
    private Long parent_id;
    private String channelType;
    private String questionType;
//    @Size(max=100, message = " 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "")
    private String title;
//    @Size(max=2000, message = "내용은 2000자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING)
    private String contents;
    private Long authorId;
//    @Size(max=100)
//    @Pattern(regexp = Constants.STRING)
    private String nickName;
    private String returnType;
    private String returnNoAddress;
//    @Size(max=100)
//    @Pattern(regexp = Constants.STRING)
    private String createdAt;
    private Boolean isSecret;
    private Long createdBy;
    private Long modifiedBy;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdDateStart;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdDateEnd;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate modifiedDateStart;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate modifiedDateEnd;
    private String targetRole;
    private Boolean isReplied;

    private Integer pageCount=10;
//    @Size(max=100)
//    @Pattern(regexp = Constants.STRING)
    private String sort = "createdDate";
}
