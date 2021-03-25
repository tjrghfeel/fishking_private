package com.tobe.fishking.v2.service.board;

import com.tobe.fishking.v2.addon.UploadService;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Comment;
import com.tobe.fishking.v2.entity.common.Event;
import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.entity.fishing.FishingDiaryComment;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.fishing.DependentType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.board.CommentDtoForPage;
import com.tobe.fishking.v2.model.board.CommentPageDto;
import com.tobe.fishking.v2.model.board.DeleteCommentDto;
import com.tobe.fishking.v2.model.fishing.DeleteFishingDiaryCommentDto;
import com.tobe.fishking.v2.model.fishing.FishingDiaryCommentDtoForPage;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.board.CommentRepository;
import com.tobe.fishking.v2.repository.common.EventRepository;
import com.tobe.fishking.v2.repository.common.FileRepository;
import com.tobe.fishking.v2.repository.fishking.FishingDiaryCommentRepository;
import com.tobe.fishking.v2.repository.fishking.FishingDiaryRepository;
import com.tobe.fishking.v2.service.auth.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {


    @Autowired
    CommentRepository commentRepository;
    @Autowired
    FishingDiaryRepository fishingDiaryRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    FileRepository fileRepository;
    @Autowired
    Environment env;
    @Autowired
    UploadService uploadService;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    MemberService memberService;

    /*댓글 작성*/
    @Transactional
    public Long makeComment(
            DependentType dependentType, Long linkId, Long parentId, String content, Long fileId, String token
    ) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        if(dependentType==DependentType.event ){
            Event event = eventRepository.findById(linkId)
                    .orElseThrow(()->new ResourceNotFoundException("event not found for this id :: "+linkId));

            if(parentId!=0) {
                if (!commentRepository.existsById(parentId)) {
                    throw new RuntimeException("parentId에 해당하는 댓글이 존재하지 않습니다.");
                }
            }

            Comment comment = Comment.builder()
                    .dependentType(DependentType.event)
                    .linkId(event.getId())
                    .parentId(parentId)
                    .contents(content)
                    .likeCount(0)
                    .isDeleted(false)
                    .createdBy(member)
                    .modifiedBy(member)
                    .build();
            comment = commentRepository.save(comment);

            if(fileId!=null) {
                FileEntity fileEntity = fileRepository.findById(fileId)
                        .orElseThrow(() -> new ResourceNotFoundException("file not found for this id :: " + fileId));
                fileEntity.saveTemporaryFile(comment.getId());
            }

            event.getStatus().plusCommentCount();

            return comment.getId();
        }
        else{
            throw new RuntimeException("이벤트에 대한 댓글만 가능합니다");
        }
    }

    /*댓글 수정*/
    @Transactional
    public Boolean modifyComment(
            Long commentId, String content, Long fileId, String token
    ) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new ResourceNotFoundException("comment not found for this id :: "+commentId));

        if(member != comment.getCreatedBy() && member.getRoles()!= Role.admin){
            throw new ResourceNotFoundException("해당 댓글에 대한 수정권한이 없습니다.");
        }

        comment.modify(content);
        commentRepository.save(comment);

        List<FileEntity> preFileList = fileRepository.findByPidAndFilePublishAndIsDelete(commentId, FilePublish.commonComment,false);
        for(int i=0; i<preFileList.size(); i++){
            preFileList.get(i).setIsDelete(true);
        }
        if(fileId!=null){
            FileEntity file = fileRepository.findById(fileId)
                    .orElseThrow(()->new ResourceNotFoundException("comment not found for this id :: "+fileId));
            file.saveTemporaryFile(commentId);
        }
        return true;
    }

    /*댓글 삭제*/
    @Transactional
    public Boolean deleteComment(DeleteCommentDto dto, String token) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        Comment comment = commentRepository.findById(dto.getCommentId())
                .orElseThrow(()->new ResourceNotFoundException("comment not found for this id :: "+dto.getCommentId()));

        if(comment.getCreatedBy()!=member && member.getRoles() != Role.admin ){
            throw new RuntimeException("해당 댓글에 대한 삭제 권한이 없습니다");
        }

        comment.delete();

        List<FileEntity> preFileList = fileRepository.findByPidAndFilePublishAndIsDelete(dto.getCommentId(), FilePublish.commonComment,false);
        for(int i=0; i<preFileList.size(); i++){
            preFileList.get(i).setIsDelete(true);
        }

        /*원글의 댓글수 필드 감소*/
        if(comment.getDependentType() == DependentType.event ){
            Event event = eventRepository.findById(comment.getLinkId())
                    .orElseThrow(()->new ResourceNotFoundException("event not found for this id :: "+comment.getLinkId()));
            event.getStatus().subCommentCount();
        }

        return true;
    }

    /*댓글 목록 가져오기*/
    @Transactional
    public CommentPageDto getCommentList(Long linkId, DependentType dependentType, String token) throws ResourceNotFoundException {
        CommentPageDto result = null;
        Integer commentCount =0;
        String title = null;
        Long memberId = null;
        Boolean isManager = false;
        if(token !=null) {
            Member member = memberRepository.findBySessionToken(token)
                    .orElseThrow(() -> new ResourceNotFoundException("member not found for this token :: " + token));
            memberId = member.getId();
            if(member.getRoles() == Role.admin){isManager= true;}
        }
        if(dependentType==DependentType.event){
            Event event = eventRepository.findById(linkId)
                    .orElseThrow(()->new ResourceNotFoundException("event not found for this id :: "+linkId));
            title = event.getTitle();
        }
        String path = env.getProperty("file.downloadUrl");

        //댓글목록 가져옴.
        List<CommentDtoForPage> parentCommentList = commentRepository.getCommentList(linkId, dependentType,0L,memberId,path);
        commentCount += parentCommentList.size();
        //각 댓글들에 대해, 대댓글 목록 가져와 저장.
        for(int i=0; i<parentCommentList.size(); i++){
            CommentDtoForPage parentComment = parentCommentList.get(i);
            List<CommentDtoForPage> childCommentList = commentRepository.getCommentList(
                    linkId,dependentType,parentComment.getCommentId(),memberId,path);
            commentCount += childCommentList.size();
            parentComment.setChildList(childCommentList);
        }
        result = CommentPageDto.builder()
                .title(title)
                .isManager(isManager)
                .commentCount(commentCount)
                .commentList(parentCommentList)
                .build();

        return result;
    }

    //숨김처리
    @Transactional
    public Boolean hideComment(Long id, String active, String token) throws ResourceNotFoundException {
        Member member = memberService.getMemberBySessionToken(token);
        Boolean isActive = null;
        if(active.equals("true")){isActive=true;}
        else if(active.equals("false")){isActive = false; }

        if(member.getRoles()==Role.admin){
            Comment comment = commentRepository.findById(id)
                    .orElseThrow(()->new ResourceNotFoundException("comment not found for this id :: "+id));
            comment.setActive(isActive);
            return true;
        }
        else return false;
    }
}
