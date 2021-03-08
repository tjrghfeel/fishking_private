package com.tobe.fishking.v2.service.common;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Take;
import com.tobe.fishking.v2.enums.common.TakeType;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.TakeResponse;
import com.tobe.fishking.v2.model.common.DeletingTakeDto;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.TakeRepository;
import com.tobe.fishking.v2.repository.fishking.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TakeService {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TakeRepository takeRepository;
    @Autowired
    GoodsRepository goodsRepository;

    /*찜 추가 메소드. */
    @Transactional
    public Long addTake(Long linkId, String sessionToken) throws ResourceNotFoundException {

        Member member = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+sessionToken));

        Take take = Take.builder()
                .takeType(TakeType.ship)
                .linkId(linkId)
                .createdBy(member)
                .build();
        take = takeRepository.save(take);

        return take.getId();
    }

    /*찜 삭제 메소드.
    * 반환 : 삭제한 Take 엔터티의 id를 반환.  */
    @Transactional
    public Long deleteTake(Long takeId, Long linkId, String token) throws ResourceNotFoundException {
        /*삭제하려는 찜의 생성자인지 확인*/
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        Take take = null;
        if(takeId !=null) {
            take = takeRepository.findById(takeId)
                    .orElseThrow(() -> new ResourceNotFoundException("take not found for this id ::" + takeId));
        }
        else if(linkId!=null){
            take = takeRepository.findByCreatedByAndLinkIdAndTakeType(member, linkId, TakeType.ship);
        }
        else{ throw new RuntimeException("takeId와 linkId 둘 중 하나는 반드시 존재해야만 합니다."); }

        if(take.getCreatedBy()!=member){
            throw new RuntimeException("해당 찜의 삭제 권한이 없습니다.");
        }
        
        /*찜 삭제*/
        Long deletedTakeId = take.getId();
        takeRepository.delete(take);
        return deletedTakeId;
    }

    /*선상 낚시 찜 목록 조회
    * 반환 : */
    @Transactional
    public Page<TakeResponse> getFishingTypeFishTakeList(String fishingType, String sessionToken, int page) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+sessionToken));
        int fishingTypeOrdinal = FishingType.valueOf(fishingType).ordinal();
        Pageable pageable = PageRequest.of(page, 10);
        return goodsRepository.findTakeListAboutFishType(member, fishingTypeOrdinal, pageable);
    }

    /*선상, 갯바위 찜 개수 조회
    * 반환 : int[0] : 선상 찜 개수, int[1] : 갯바위 찜 개수 */
    @Transactional
    public int[] getTakeCount(String sessionToken) throws ResourceNotFoundException {
        int[] count = new int[2];
        Member member = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+sessionToken));

        count[0] = goodsRepository.findTakeCountAboutFishType(member, FishingType.ship);
        count[1] = goodsRepository.findTakeCountAboutFishType(member, FishingType.seaRocks);

        return count;
    }

}
