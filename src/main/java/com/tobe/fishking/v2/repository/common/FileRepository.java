package com.tobe.fishking.v2.repository.common;


import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.board.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

    @Query("select a from FileEntity a where a.fileUrl = :fileUrl")
    public Optional<FileEntity> findByFileUrl(String fileUrl);

    public List<FileEntity> findByPostId(Post post);

    @Query(value = "select b.* " +
            "from member a, file b " +
            "where a.profile_image = b.temp_file_seq " +
            "and (a.alt_user_code = :memberSeq or concat(a.member_seq, '') = :memberSeq)" +
            "and a.is_member = 1",
    nativeQuery = true)
    Optional<FileEntity> findProfileImageBySeqOrCode(String memberSeq);
/*
    @Query("select a from FileEntity a where a. = true ")
    FileEntity getDefaultProfile();*/
}
