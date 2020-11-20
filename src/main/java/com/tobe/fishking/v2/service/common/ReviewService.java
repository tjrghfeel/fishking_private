package com.tobe.fishking.v2.service.common;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.ReviewDTOForList;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ReviewService {

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    MemberRepository memberRepository;

    /*마이메뉴 - 내글관리 - 리뷰에서 내 리뷰목록 가져오는 메소드*/
    /*@Transactional
    public Map<String, Object> getReviewList(Long memberId) throws ResourceNotFoundException {
        Map<String, Object> map = new HashMap<String, Object>();
        Page<ReviewDTOForList> myReviewList = null;
        List<String> fileList = new LinkedList<String>();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id ::"+memberId));

        *//*리포지토리로부터 리뷰리스트DTO 받아오기*//*
        myReviewList = reviewRepository.findMyReviewList(member);
        map.put("page", myReviewList);

        *//*리뷰에 포함된 파일 가져와서 리뷰리스트DTO에 저장해주기*//*
        myReviewList.
                *//*파일을 어떻게보내야할지?
                * interface형 dto에는 repository에서 받아오는거말고 따로 set이 안된다.
                * 근데 Paging처리하려면 interface형으로 dto받아와야하는데..
                * 쿼리상으로 한번에 여러개의 파일을 받아올방법이 있는지 모르겠다. *//*
    }*/


}
