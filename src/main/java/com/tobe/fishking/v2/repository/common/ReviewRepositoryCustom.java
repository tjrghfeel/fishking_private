package com.tobe.fishking.v2.repository.common;


import com.tobe.fishking.v2.model.common.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {
    Page<ReviewResponse> getShipReviews(Long shipId, Pageable pageable);
}
