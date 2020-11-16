package com.tobe.fishking.v2.repository.common;


import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Coupon;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends BaseRepository<FileEntity, Long> {

    @Query("select a from FileEntity a where a.fileUrl = :fileUrl")
    public Optional<FileEntity> findByFileUrl(@Param("fileUrl") String fileUrl);

    public List<FileEntity> findByPidAndFilePublish(Long pid, FilePublish filePublish);
/*

    @Query(value = "select b.* " +
            "from member a, FileEntity b " +
            "where a.profile_image = b.pid " +
            "and concat(a.id, '') = :memberSeq)" +
            "and a.is_member = 1",
            nativeQuery = true)
    Optional<FileEntity> findProfileImageBySeqOrCode(int memberId);
*/


    @Query(value="SELECT a FROM FileEntity a , FishingDiary b  where a.pid = b.id and b.contents like CONCAT('%',:contents,'%')",
            countQuery = "SELECT count(a) FROM FileEntity , FishingDiary b  where a.pid = b.id and b.contents like CONCAT('%',:contents,'%')", nativeQuery = true)
    Page<FileEntity> findAllFilesWithPaginationNative(@Param("contents") String  contents, Pageable pageable);

/*
    @Query("select a from FileEntity a where a. = true ")
    FileEntity getDefaultProfile();*/
}
