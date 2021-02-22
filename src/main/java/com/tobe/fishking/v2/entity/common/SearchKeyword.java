package com.tobe.fishking.v2.entity.common;

import com.tobe.fishking.v2.entity.BaseTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "searchKeyword")
public class SearchKeyword extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // IDENTITY //mssql
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(100) comment '검색어' ")
    private String searchKeyword;

    @Column(nullable = false, columnDefinition = "bigint default 0 comment '검색 수' ")
    private Long count;

    @Column(columnDefinition = "bit default 0 comment '인기' ")
    private Boolean popular;

    @Column(columnDefinition = "bit default 0 comment 'new' ")
    private Boolean newPopular;

    @Builder
    public SearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
        this.count = 1L;
        this.popular = false;
        this.newPopular = false;
    }

    public void updateCount() {
        this.count += 1;
    }

    public void isPopular() {
        this.popular = true;
    }

    public void isNotPopular() {
        this.popular = true;
    }

    public void isNew() {
        this.newPopular = true;
    }

    public void isNotNew() {
        this.newPopular = false;
    }
}
