package com.tobe.fishking.v2.service.fishking;

import com.tobe.fishking.v2.addon.UploadService;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.auth.RegistrationToken;
import com.tobe.fishking.v2.entity.board.Comment;
import com.tobe.fishking.v2.entity.common.Alerts;
import com.tobe.fishking.v2.entity.common.CodeGroup;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.entity.fishing.FishingDiaryComment;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.common.AlertType;
import com.tobe.fishking.v2.enums.fishing.DependentType;
import com.tobe.fishking.v2.enums.fishing.EntityType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.exception.ServiceLogicException;
import com.tobe.fishking.v2.model.fishing.*;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.AlertsRepository;
import com.tobe.fishking.v2.repository.common.CodeGroupRepository;
import com.tobe.fishking.v2.repository.common.CommonCodeRepository;
import com.tobe.fishking.v2.repository.common.FileRepository;
import com.tobe.fishking.v2.repository.fishking.FishingDiaryCommentRepository;
import com.tobe.fishking.v2.repository.fishking.FishingDiaryRepository;
import com.tobe.fishking.v2.service.FishkingScheduler;
import com.tobe.fishking.v2.service.auth.MemberService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class FishingDiaryCommentService {

    @Autowired
    FishingDiaryCommentRepository commentRepository;
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
    MemberService memberService;
    @Autowired
    CommonCodeRepository commonCodeRepository;
    @Autowired
    CodeGroupRepository codeGroupRepository;
    @Autowired
    AlertsRepository alertsRepository;
    @Autowired
    FishkingScheduler fishkingScheduler;

    /*?????? ??????*/
    @Transactional
    public Long makeFishingDiaryComment(
            DependentType dependentType, Long linkId, Long parentId, String content, Long fileId, String token
    ) throws ResourceNotFoundException, IOException, ServiceLogicException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        if(dependentType==DependentType.fishingBlog || dependentType==DependentType.fishingDiary){
            FishingDiary fishingDiary = fishingDiaryRepository.findById(linkId)
                    .orElseThrow(()->new ResourceNotFoundException("fishingDiary not found for this id :: "+linkId));

            if(parentId!=0) {
                if (!commentRepository.existsById(parentId)) {
                    throw new ServiceLogicException("parentId??? ???????????? ????????? ???????????? ????????????.");
                }
            }

            FishingDiaryComment comment = FishingDiaryComment.builder()
                    .fishingDiary(fishingDiary)
                    .dependentType(DependentType.valueOf(fishingDiary.getFilePublish().getKey()))
                    .linkId(fishingDiary.getId())
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

            fishingDiary.getStatus().plusCommentCount();

            //????????? ??????????????? ???????????? ?????????.
            Member receiver = fishingDiary.getMember();
            //???????????? ?????? ????????????????????? ????????????.
            CodeGroup alertSetCodeGroup = codeGroupRepository.findByCode("alertSet");
            CommonCode fishingDiaryAlertSetCommonCode = commonCodeRepository.findByCodeGroupAndCode(alertSetCodeGroup, "fishingDiary");
            if(receiver.hasAlertSetCode(fishingDiaryAlertSetCommonCode.getCode())) {
                Set<RegistrationToken> registrationTokenList = receiver.getRegistrationTokenList();
                String alertTitle = "??? ??????";
                String fishingDiaryTitle = (fishingDiary.getTitle().length() > 10)? fishingDiary.getTitle().substring(0,10)+"..." : fishingDiary.getTitle();
                String sentence = "'?????? : "+fishingDiaryTitle+"'??? ????????? ?????????????????????.";

                Alerts alerts = Alerts.builder()
                        .alertType(AlertType.fishingDiary)
                        .entityType(EntityType.fishingDiary)
                        .pid(comment.getId())
                        .content(null)
                        .sentence(sentence)
                        .isRead(false)
                        .isSent(false)
                        .receiver(receiver)
                        .alertTime(LocalDateTime.now())
                        .createdBy(receiver)
                        .type("c")
                        .build();

                alerts = alertsRepository.save(alerts);

                for (RegistrationToken item: registrationTokenList) {
                    fishkingScheduler.sendPushAlert(alertTitle, sentence, alerts, item.getToken());
                }
            }

            return comment.getId();
        }
        else{
            throw new ServiceLogicException("??????????????? ?????????????????? ?????? ????????? ???????????????");
        }
    }

    /*?????? ??????*/
    @Transactional
    public Boolean modifyFishingDiaryComment(
            Long commentId, String content, Long fileId, String token
    ) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        FishingDiaryComment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new ResourceNotFoundException("comment not found for this id :: "+commentId));

        if(member != comment.getCreatedBy() && member.getRoles()!=Role.admin){
            throw new ResourceNotFoundException("?????? ????????? ?????? ??????????????? ????????????.");
        }

        comment.modify(content);
        commentRepository.save(comment);

        List<FileEntity> preFileList = fileRepository.findByPidAndFilePublishAndIsDelete(commentId, FilePublish.comment,false);
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

    /*?????? ??????*/
    @Transactional
    public Boolean deleteFishingDiaryComment(DeleteFishingDiaryCommentDto dto, String token) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        FishingDiaryComment comment = commentRepository.findById(dto.getCommentId())
                .orElseThrow(()->new ResourceNotFoundException("comment not found for this id :: "+dto.getCommentId()));

        if(comment.getCreatedBy()!=member && member.getRoles() != Role.admin ){
            throw new RuntimeException("?????? ????????? ?????? ?????? ????????? ????????????");
        }

        comment.delete();

        List<FileEntity> preFileList = fileRepository.findByPidAndFilePublishAndIsDelete(dto.getCommentId(), FilePublish.comment,false);
        for(int i=0; i<preFileList.size(); i++){
            preFileList.get(i).setIsDelete(true);
        }

        /*????????? ????????? ?????? ??????*/
        if(comment.getDependentType() == DependentType.fishingBlog || comment.getDependentType() == DependentType.fishingDiary){
            FishingDiary fishingDiary = fishingDiaryRepository.findById(comment.getLinkId())
                    .orElseThrow(()->new ResourceNotFoundException("fishingDiary not found for this id :: "+comment.getLinkId()));
            fishingDiary.getStatus().subCommentCount();
        }

        return true;
    }

    /*?????? ?????? ????????????*/
    @Transactional
    public FishingDiaryCommentPageDto getCommentList(Long fishingDiaryId, String token) throws ResourceNotFoundException {
        FishingDiaryCommentPageDto result = null;
        int commentCount = 0;
        Member member =null;
        Boolean isManager = false;
        if(token!=null) {
            member = memberRepository.findBySessionToken(token)
                    .orElseThrow(() -> new ResourceNotFoundException("member not found for this token :: " + token));
            if(member.getRoles() == Role.admin){ isManager = true; }
        }


        FishingDiary fishingDiary = fishingDiaryRepository.findById(fishingDiaryId)
                .orElseThrow(()->new ResourceNotFoundException("fishingDiary not found for this id :: "+fishingDiaryId));
        String path = env.getProperty("file.downloadUrl");

        //???????????? ?????????.
        List<FishingDiaryCommentDtoForPage> parentCommentList = commentRepository.getCommentList(fishingDiaryId,0L,member,path);
        commentCount += parentCommentList.size();
        //??? ???????????? ??????, ????????? ?????? ????????? ??????.
        for(int i=0; i<parentCommentList.size(); i++){
            FishingDiaryCommentDtoForPage parentComment = parentCommentList.get(i);
            List<FishingDiaryCommentDtoForPage> childCommentList = commentRepository.getCommentList(
                    fishingDiaryId,parentComment.getCommentId(),member,path);
            commentCount += childCommentList.size();
            parentComment.setChildList(childCommentList);
        }

        result = FishingDiaryCommentPageDto.builder()
                .commentList(parentCommentList)
                .commentCount(commentCount)
                .isManager(isManager)
                .title(fishingDiary.getTitle())
                .build();
        return result;
    }

    //????????????
    @Transactional
    public Boolean hideFishingDiaryComment(Long id, String active, String token) throws ResourceNotFoundException {
        Member member = memberService.getMemberBySessionToken(token);
        Boolean isActive = null;
        if(active.equals("true")){isActive=true;}
        else if(active.equals("false")){isActive = false; }

        if(member.getRoles()==Role.admin){
            FishingDiaryComment comment = commentRepository.findById(id)
                    .orElseThrow(()->new ResourceNotFoundException("comment not found for this id :: "+id));
            comment.setActive(isActive);
            return true;
        }
        else return false;
    }
}
