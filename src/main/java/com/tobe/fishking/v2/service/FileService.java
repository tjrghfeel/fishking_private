package com.tobe.fishking.v2.service;

import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.board.FileType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.FileDTO;
import com.tobe.fishking.v2.repository.FilesRepository;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.board.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FileService {
    @Autowired
    FilesRepository filesRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;

    @Transactional
    public Long storeFileEntity(FileDTO fileDTO) throws ResourceNotFoundException {
        Post post = postRepository.findById(fileDTO.getPostId())
                .orElseThrow(()->new ResourceNotFoundException("post not found for this id ::"+fileDTO.getPostId()));
        Member member = post.getAuthor();

        FileEntity fileEntity = FileEntity.builder()
                .filePublish(FilePublish.values()[fileDTO.getFilePublish()])
                .originalFile(fileDTO.getOriginalFile())
                .fileNo(fileDTO.getFileNo())
                .fileType(FileType.values()[fileDTO.getFileType()])
                .postId(post)
                .size(fileDTO.getSize())
                .fileUrl(fileDTO.getFileUrl())
                //.bid(post.getBoard())
//                .caption()
//                .pid()
                .createdBy(member)
                .modifiedBy(member)
                .locations("sampleLocation")
                .build();
        fileEntity = filesRepository.save(fileEntity);

        return fileEntity.getId();
    }
}
