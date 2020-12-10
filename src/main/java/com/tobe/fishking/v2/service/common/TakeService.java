package com.tobe.fishking.v2.service.common;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Take;
import com.tobe.fishking.v2.enums.common.TakeType;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.TakeResponse;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.TakeRepository;
import com.tobe.fishking.v2.repository.fishking.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public Long addTake(Long linkId, int takeType, Long memberId) throws ResourceNotFoundException {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id ::"+memberId));

        Take take = Take.builder()
                .takeType(TakeType.values()[takeType])
                .linkId(linkId)
                .createdBy(member)
                .build();
        take = takeRepository.save(take);

        return take.getId();
    }

    /*찜 삭제 메소드.
    * 반환 : 삭제한 Take 엔터티의 id를 반환.  */
    @Transactional
    public Long deleteTake(Long takeId) throws ResourceNotFoundException {
        Take take = takeRepository.findById(takeId)
                .orElseThrow(()->new ResourceNotFoundException("take not found for this id ::"+takeId));
        Long deletedTakeId = take.getId();
        takeRepository.delete(take);
        return deletedTakeId;
    }

    /*선상 낚시 찜 목록 조회
    * 반환 : */
    @Transactional
    public Page<TakeResponse> getFishingTypeFishTakeList(int fishingType, Long memberId, int page) throws ResourceNotFoundException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id ::"+memberId));

        Pageable pageable = PageRequest.of(page, 10);
        return goodsRepository.findTakeListAboutFishType(member, fishingType,pageable);
    }

    /*선상, 갯바위 찜 개수 조회
    * 반환 : int[0] : 선상 찜 개수, int[1] : 갯바위 찜 개수 */
    @Transactional
    public int[] getTakeCount(Long memberId) throws ResourceNotFoundException {
        int[] count = new int[2];
        /*Member 조회*/
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id ::"+memberId));

        /*Member로*/
        count[0] = goodsRepository.findTakeCountAboutFishType(member, FishingType.ship);
        count[1] = goodsRepository.findTakeCountAboutFishType(member, FishingType.sealocks);

        return count;
    }

}
