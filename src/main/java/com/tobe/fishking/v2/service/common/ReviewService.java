package com.tobe.fishking.v2.service.common;

import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Review;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.DeleteReviewDto;
import com.tobe.fishking.v2.model.common.ModifyReviewDto;
import com.tobe.fishking.v2.model.common.ReviewDto;
import com.tobe.fishking.v2.model.common.WriteReviewDto;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.FileRepository;
import com.tobe.fishking.v2.repository.common.ReviewRepository;
import com.tobe.fishking.v2.repository.fishking.GoodsRepository;
import com.tobe.fishking.v2.repository.fishking.ShipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final GoodsRepository goodsRepository;
    private final FileRepository fileRepository;
    private final ShipRepository shipRepository;

    /*리뷰 작성*/
    @Transactional
    public Long writeReview(WriteReviewDto dto, String token) throws ResourceNotFoundException {
        Goods goods = goodsRepository.findById(dto.getGoodsId())
                .orElseThrow(()->new ResourceNotFoundException("goods not found for this id :: "+dto.getGoodsId()));
        Member author = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        Ship ship = goods.getShip();

        /*리뷰저장*/
        Review review = Review.builder()
                .goods(goods)
                .member(author)
                .totalAvgByReview((dto.getCleanScore()+dto.getServiceScore()+dto.getTasteScore())/3)
                .tasteByReview(dto.getTasteScore())
                .serviceByReview(dto.getServiceScore())
                .cleanByReview(dto.getCleanScore())
                .content(dto.getContent())
                .createdBy(author)
                .modifiedBy(author)
                .build();
        review = reviewRepository.save(review);

        /*ship에 리뷰 평점 적용*/
        ship.applyReviewGrade(dto.getTasteScore(),dto.getCleanScore(),dto.getServiceScore());

        /*이미지 파일들 저장*/
        if(dto.getFileList().length>0){
            Long[] fileIdList = dto.getFileList();

            for(int i=0; i<fileIdList.length; i++){
                Long fileId = fileIdList[i];
                FileEntity fileEntity = fileRepository.findById(fileId)
                        .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+fileId));
                fileEntity.saveTemporaryFile(review.getId());
            }
        }

        return review.getId();
    }

    /*리뷰 수정*/
    @Transactional
    public Boolean modifyReview(ModifyReviewDto dto, String token) throws ResourceNotFoundException {
        Review review = reviewRepository.findById(dto.getReviewId())
                .orElseThrow(()->new ResourceNotFoundException("review not found for this id :: "+dto.getReviewId()));
        Goods goods = review.getGoods();
        Member author = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        Ship ship = goods.getShip();

        if(review.getMember()!=author && author.getRoles()!=Role.admin){
            throw new RuntimeException("해당 리뷰에 대한 수정 권한이 없습니다.");
        }

        /*리뷰수정.*/
        review.modify(dto.getCleanScore(),dto.getServiceScore(),dto.getTasteScore(),dto.getContent(),author);
        review = reviewRepository.save(review);

        /*ship에 리뷰 평점 적용*/
        ship.applyReviewGrade(dto.getTasteScore(),dto.getCleanScore(),dto.getServiceScore());

        /*이미지 파일들 수정*/
        List<FileEntity> preFileList = fileRepository.findByPidAndFilePublish(review.getId(), FilePublish.review);
        for(int i=0; i<preFileList.size(); i++){
            preFileList.get(i).setIsDelete(true);
        }
        if(dto.getFileList().length>0){
            Long[] fileIdList = dto.getFileList();

            for(int i=0; i<fileIdList.length; i++){
                Long fileId = fileIdList[i];
                FileEntity fileEntity = fileRepository.findById(fileId)
                        .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+fileId));
                fileEntity.saveTemporaryFile(review.getId());
            }
        }

        return true;
    }

    /*리뷰 삭제*/
    @Transactional
    public Boolean deleteReview(DeleteReviewDto dto, String token) throws ResourceNotFoundException {
        /*본인의 리뷰인지 확인*/
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found this token :: "+token));
        Review review = reviewRepository.findById(dto.getReviewId())
                .orElseThrow(()->new ResourceNotFoundException("review not found for this id :: "+dto.getReviewId()));
        if(review.getMember()!=member && member.getRoles()!= Role.admin){
            throw new RuntimeException("해당 리뷰의 삭제 권한이 없습니다.");
        }

        /*삭제*/
        List<FileEntity> fileList = fileRepository.findByPidAndFilePublish(review.getId(), FilePublish.review);
        for(int i=0; i<fileList.size(); i++){
            fileRepository.delete(fileList.get(i));
        }

        reviewRepository.delete(review);
        return true;
    }





}
