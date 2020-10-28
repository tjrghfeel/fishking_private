package com.tobe.fishking.v2.entity.board;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Tag")
@Table(name = "tag")
public class Tag extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // IDENTITY)
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    // EXEC sp_addextendedproperty 'MS_Description', N'태그명', 'USER', DBO, 'TABLE', tags, 'COLUMN',  trag
    @NaturalId
    @Column(columnDefinition = "varchar(200)   comment '태그명'")
    private String tagName;

    @ManyToMany(mappedBy = "tags")
    @Column(columnDefinition = "varchar(200)   comment '게시ID'")
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    //Getters and setters ommitted for brevity

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(tagName, tag.tagName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagName);
    }


    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', tags, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" , insertable= false ,  updatable= false , columnDefinition = "bigint  NOT NULL   comment '생성자'  ")
    private Member createdBy;

    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', tags, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" , insertable= false ,  updatable= false , columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;


}