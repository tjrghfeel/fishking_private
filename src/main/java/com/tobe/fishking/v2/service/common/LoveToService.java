package com.tobe.fishking.v2.service.common;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.LoveTo;
import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.entity.fishing.FishingDiaryComment;
import com.tobe.fishking.v2.enums.common.TakeType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.MakeLoveToDto;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.LoveToRepository;
import com.tobe.fishking.v2.repository.fishking.FishingDiaryCommentRepository;
import com.tobe.fishking.v2.repository.fishking.FishingDiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoveToService {

    @Autowired
    LoveToRepository loveToRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    FishingDiaryRepository fishingDiaryRepository;
    @Autowired
    FishingDiaryCommentRepository fishingDiaryCommentRepository;

    /*좋아요 추가*/
    @Transactional
    public Boolean addLoveTo(MakeLoveToDto dto, String token) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        LoveTo preLoveTo = loveToRepository.findByLinkIdAndTakeTypeAndCreatedBy(
                dto.getLinkId(),TakeType.valueOf(dto.getTakeType()),member);
        if(preLoveTo==null) {

            /*조항일지, 유저조행기, 댓글에 좋아요를 할 경우, 해당 엔터티의 좋아요 카운트를 증가시켜준다. */
            if(dto.getTakeType().equals("fishingDiary") || dto.getTakeType().equals("fishingBlog")){
                FishingDiary fishingDiary = fishingDiaryRepository.findById(dto.getLinkId())
                        .orElseThrow(()->new ResourceNotFoundException("fishingDiary not found for this id :: "+dto.getLinkId()));
                fishingDiary.getStatus().plusLikeCount();
            }
            else if(dto.getTakeType().equals("comment")){
                FishingDiaryComment fishingDiaryComment = fishingDiaryCommentRepository.findById(dto.getLinkId())
                        .orElseThrow(()->new ResourceNotFoundException("fishingDiaryComment not found for this id :: "+dto.getLinkId()));
                fishingDiaryComment.plusLikeCount();
            }

            LoveTo loveTo = LoveTo.builder()
                    .linkId(dto.getLinkId())
                    .takeType(TakeType.valueOf(dto.getTakeType()))
                    .createdBy(member)
                    .build();
            loveToRepository.save(loveTo);

            return true;
        }
        else return false;
    }

    /*좋아요 삭제*/
    @Transactional
    public Boolean deleteLoveTo(MakeLoveToDto dto, String token) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        LoveTo preLoveTo = loveToRepository.findByLinkIdAndTakeTypeAndCreatedBy(
                dto.getLinkId(),TakeType.valueOf(dto.getTakeType()),member);
        /*해당 좋아요가 존재한다면,*/
        if(preLoveTo!=null){

            /*조항일지, 유저조행기, 댓글에 좋아요를 취소할 경우, 해당 엔터티의 좋아요 카운트를 감소시켜준다. */
            if(dto.getTakeType().equals("fishingDiary") || dto.getTakeType().equals("fishingBlog")){
                FishingDiary fishingDiary = fishingDiaryRepository.findById(dto.getLinkId())
                        .orElseThrow(()->new ResourceNotFoundException("fishingDiary not found for this id :: "+dto.getLinkId()));
                fishingDiary.getStatus().subLikeCount();
            }
            else if(dto.getTakeType().equals("comment")){
                FishingDiaryComment fishingDiaryComment = fishingDiaryCommentRepository.findById(dto.getLinkId())
                        .orElseThrow(()->new ResourceNotFoundException("fishingDiaryComment not found for this id :: "+dto.getLinkId()));
                fishingDiaryComment.subLikeCount();
            }

            loveToRepository.delete(preLoveTo);

            return true;
        }
        /*존재하지 않는다면,*/
        else return false;
    }
}
