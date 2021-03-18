package com.tobe.fishking.v2.model.admin.post;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.board.Tag;
import com.tobe.fishking.v2.enums.board.QuestionType;
import com.tobe.fishking.v2.enums.board.ReturnType;
import com.tobe.fishking.v2.enums.common.ChannelType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
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
    private String title;
    private String contents;
    private Long authorId;
    private String nickName;
    private String returnType;
    private String returnNoAddress;
    private String createdAt;
    private Boolean isSecret;
    private Long createdBy;
    private Long modifiedBy;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createDateStart;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createDateEnd;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate modifiedDateStart;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate modifiedDateEnd;
    private Boolean targetRole;
    private Boolean isReplied;

    private String sort;
}
