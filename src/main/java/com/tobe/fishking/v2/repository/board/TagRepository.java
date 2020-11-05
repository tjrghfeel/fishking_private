package com.tobe.fishking.v2.repository.board;

import com.tobe.fishking.v2.entity.board.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    //tagName으로 Tag검색 메소드.
    @Query("select t from Tag t where t.tagName = :tagName")
    Tag findByTagName(String tagName);


}
