package com.tobe.fishking.v2.service.board;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Accuse;
import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.enums.fishing.EntityType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.board.AddAccuseDto;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.board.AccuseRepository;
import com.tobe.fishking.v2.repository.fishking.FishingDiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccuseService {

    @Autowired
    AccuseRepository accuseRepository;
    @Autowired
    FishingDiaryRepository fishingDiaryRepository;
    @Autowired
    MemberRepository memberRepository;

    /*신고하기*/
    @Transactional
    public Long addAccuse(AddAccuseDto dto, String token) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));

        /*신고 대상이 실제로 있는지 확인*/
        /*조항일지, 유저조행기인경우*/
        if(dto.getTargetType().equals("fishingDiary")){
            FishingDiary fishingDiary = fishingDiaryRepository.findById(dto.getLinkId())
                    .orElseThrow(()->new ResourceNotFoundException("fishingDiary not found for this id :: "+dto.getLinkId()));
        }
        /*댓글인경우*/
        else if(dto.getTargetType().equals("comment")){

        }

        Accuse accuse = Accuse.builder()
                .targetType(EntityType.valueOf(dto.getTargetType()))
                .linkId(dto.getLinkId())
                .createdBy(member)
                .build();
        accuse = accuseRepository.save(accuse);
        return accuse.getId();
    }
}
