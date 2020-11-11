package com.tobe.fishking.v2.repository.common;


import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends BaseRepository<FileEntity, Long> {

    @Query("select a from FileEntity a where a.fileUrl = :fileUrl")
    public Optional<FileEntity> findByFileUrl(String fileUrl);


    /*@Query(value = "select b.* " +
            "from member a, FileEntity b " +
            "where a.profile_image = b.file_seq " +
            "and (a.alt_user_code = :memberSeq or concat(a.member_seq, '') = :memberSeq)" +
            "and a.is_member = 1",
            nativeQuery = true)
    Optional<FileEntity> findProfileImageBySeqOrCode(String memberSeq);
    */

    //joing fetch는 paging이 안되서 totallimit 삭제
    @Query(value = "SELECT distinct fe FROM FileEntity AS fe "
            + "JOIN FishingDiary AS fd ON fe.pid = fd.id "
            + "WHERE fd.contents like CONCAT('%',:contents,'%')")
    Page<FileEntity> findAllFileLikeFishingDiaryContents(@Param("contents") String contents, Pageable pageable);

/*
    @Query(value = "SELECT distinct fe FROM FileEntity AS fe "
            + "JOIN FishingDiary AS fd ON fe.pid = fd.id "
            + "WHERE fd.contents like CONCAT('%',:contents,'%')")
    Optional<List<FileEntity>> findAllFileLikeFishingDiaryContents(@Param("contents") String contents, Pageable pageable);*/




/*
    @Query("select a from FileEntity a where a. = true ")
    FileEntity getDefaultProfile();*/
}
