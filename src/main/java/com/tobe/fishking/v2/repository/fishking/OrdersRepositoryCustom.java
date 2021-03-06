package com.tobe.fishking.v2.repository.fishking;

import com.querydsl.core.Tuple;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.entity.fishing.OrderDetails;
import com.tobe.fishking.v2.entity.fishing.Orders;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.model.fishing.SearchOrdersDTO;
import com.tobe.fishking.v2.model.response.OrderDetailResponse;
import com.tobe.fishking.v2.model.response.OrderListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface OrdersRepositoryCustom {
    List<Orders> getCheckOrders();
    Page<OrderListResponse> searchOrders(SearchOrdersDTO dto, Long memberId, Pageable pageable, String keys);
    OrderDetailResponse orderDetail(Long orderId);
    List<OrderListResponse> getBookRunning(Long memberId);
    List<OrderListResponse> getBookConfirm(Long memberId);
    List<Tuple> getStatus(Long memberId, int type);
    List<Orders> getOrderByStatus(String date, OrderStatus status);
    List<Orders> getOrderByStatusForScheduler(String date, String time, OrderStatus status);
    List<OrderDetails> getNextOrders(Integer personnel, Goods good, String fishingDate);
    Integer getPersonnelByFishingDate(Goods good, String fishingDate);
}
