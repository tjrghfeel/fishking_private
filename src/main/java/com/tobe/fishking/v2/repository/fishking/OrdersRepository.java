package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.entity.fishing.Orders;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.model.fishing.OrdersDetailDto;
import com.tobe.fishking.v2.model.fishing.OrdersDtoForPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

    /*member가 한 예약 건수를 주문진행상태에 따라 검색*/
    int countByOrderStatusAndCreatedBy(OrderStatus orderStatus, Member member);
    /*마이메뉴 > 현재 나의 예약 건수 반환을 위해 orderStatus가 '예약취소', '출조완료'상태인것을 제외한 order들의 개수를 카운트*/
    @Query(value = "" +
            "select count(o.id) " +
            "from orders o " +
            "where o.created_by = :memberId " +
            "and o.is_pay = true ",
            nativeQuery = true
    )
    int countCurrentMyOrders(@Param("memberId") Long memberId);

    /*나의 예약내역 리스트 출력 메소드
    * - 주문상태에 따라 검색한경우 사용된다. */
    @Query(value = "select " +
            "   o.id id, " +
            "   g.id goodsId, " +
            "   s.id shipId, " +
            "   if(s.profile_image is null, '', concat('/resource', s.profile_image)) shipImageUrl , " +
//            "   (select f.file_url from files f " +
//            "   where f.file_publish = 0 and f.pid = s.id and f.is_represent = 1) shipImageFileUrl, " +
//            "   (select f.thumbnail_file from files f " +
//            "   where f.file_publish = 0 and f.pid = s.id and f.is_represent = 1) shipImageFileName, " +
            "   s.ship_name shipName, " +
            "   s.fishing_type fishingType, " +
            "   s.address sigungu, " +
//            "   s.distance distance, " +
            "   o.order_status ordersStatus, " +
            "   replace(o.fishing_date, '-', '') fishingDate, " +
            "   g.fishing_start_time fishingStartTime, " +
            "   o.orders_num ordersNum " +
            "from orders o, ship s, goods g " +
            "where o.created_by = :member " +
            "   and o.goods = g.id " +
            "   and g.goods_ship_id = s.id " +
            "   and o.order_status = :orderStatus " +
            "   and o.is_pay = true " +
            "order by g.fishing_date desc ",
            countQuery = "select o.id " +
                    "from orders o, ship s, goods g " +
                    "where o.created_by = :member " +
                    "   and o.goods = g.id " +
                    "   and g.goods_ship_id = s.id " +
                    "   and o.order_status = :orderStatus " +
                    "order by g.fishing_date desc ",
            nativeQuery = true
    )
    Page<OrdersDtoForPage> findByCreatedByAndOrderStatus(
            @Param("member") Member member,
            @Param("orderStatus") int orderStatus,
            Pageable pageable);

    /*나의 예약내역 리스트 출력 메소드2
    * - 모든 주문상태의 예약리스트를 보여준다. 정렬순서는 OrderStatus의 ORDINAL값 오름차순. */
    @Query(value = "select " +
            "   o.id id, " +
            "   g.id goodsId, " +
            "   s.id shipId, " +
            "   if(s.profile_image is null, '',concat('/resource', s.profile_image)) shipImageUrl , " +
//            "   (select f.file_url from files f " +
//            "   where f.file_publish = 0 and f.pid = s.id and f.is_represent = 1) shipImageFileUrl, " +
//            "   (select f.thumbnail_file from files f " +
//            "   where f.file_publish = 0 and f.pid = s.id and f.is_represent = 1) shipImageFileName, " +
            "   s.ship_name shipName, " +
            "   s.fishing_type fishingType, " +
            "   s.address sigungu, " +
//            "   s.distance distance, " +
            "   o.order_status ordersStatus, " +
            "   replace(o.fishing_date, '-', '') fishingDate, " +
            "   g.fishing_start_time fishingStartTime, " +
            "   o.orders_num ordersNum " +
            "from orders o, ship s, goods g " +
            "where o.created_by = :member " +
            "   and o.goods = g.id " +
            "   and g.goods_ship_id = s.id " +
            "   and o.is_pay = true " +
            "order by o.order_status asc, g.fishing_date desc ",
            countQuery = "select o.id " +
                    "from orders o, ship s, goods g " +
                    "where o.created_by = :member " +
                    "   and o.goods = g.id " +
                    "   and g.goods_ship_id = s.id " +
                    "order by o.order_status asc, g.fishing_date desc ",
            nativeQuery = true
    )
    Page<OrdersDtoForPage> findByCreatedByOrderByOrderStatus(
            @Param("member") Member member,
            Pageable pageable);

    @Query("select o from Orders o where o.orderNumber = :orderNumber")
    Orders getOrdersByOrderNumber(String orderNumber);
}
