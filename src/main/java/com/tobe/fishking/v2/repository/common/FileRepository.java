package com.tobe.fishking.v2.repository.common;


import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.board.FileType;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends BaseRepository<FileEntity, Long> {

    @Query("select a from FileEntity a where a.fileUrl = :fileUrl")
    public Optional<FileEntity> findByFileUrl(@Param("fileUrl") String fileUrl);

    public List<FileEntity> findByPidAndFilePublish(Long pid, FilePublish filePublish);
    public List<FileEntity> findByPidAndFilePublishAndFileType(Long pid, FilePublish filePublish, FileType fileType);

    @Query("select a from FileEntity a where a.filePublish = :filePublish and  a.isRepresent = :isRepresent")
    public Optional<FileEntity> findFileEntityByAndFilePublish(FilePublish filePublish, Boolean isRepresent );

    public FileEntity findTop1ByPidAndFilePublishAndIsRepresent(Long pid, FilePublish filePublish, Boolean represent);

    @Query("select f from FileEntity f where f.id in :idList")
    List<FileEntity> findAllById(@Param("idList") Long[] id);

        // public List<FileEntity> findByPostId(Post post);
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
