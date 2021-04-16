package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.entity.fishing.Orders;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.model.admin.OrderManageDtoForPage;
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
public interface OrdersRepository extends JpaRepository<Orders, Long>, OrdersRepositoryCustom {

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
            "   s.profile_image shipImageUrl, " +
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
            "   o.order_number ordersNum " +
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
            "   s.profile_image shipImageUrl , " +
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
            "   o.order_number ordersNum " +
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

    //관리자 - 주문 목록 검색
    @Query(
            value = "select " +
                    "   o.id orderId, " +
                    "   od.id orderDetailId, " +
                    "   o.created_date orderDate, " +
                    "   o.fishing_date fishingDate, " +
                    "   o.total_amount totalAmount, " +
                    "   o.discount_amount discountAmount, " +
                    "   o.payment_amount paymentAmount, " +
                    "   o.is_pay isPay, " +
                    "   o.pay_method payMethod, " +
                    "   o.order_status orderStatus, " +
                    "   o.order_number orderNumber, " +
                    "   o.trade_number tradeNumber, " +
//                    "   o.confirm_number confirmNumber, " +
                    "   g.id goodsId, " +
                    "   g.name goodsName, " +
                    "   o.cancel_date cancelDate, " +
                    "   o.cancel_number cancelNumber, " +
                    "   m.id memberId, " +
                    "   m.member_name memberName, " +
                    "   s.id shipId, " +
                    "   s.ship_name shipName, " +
                    "   c.id companyId, " +
                    "   c.company_name companyName, " +
                    "   c.member_id shipownerId " +
                    "from orders o join orders_details od on o.id = od.order_detail_orders_id join goods g on o.goods=g.id " +
                    "   join member m on o.created_by=m.id join ship s on g.goods_ship_id = s.id join company c on s.company_id=c.id " +
                    "where " +
                    "   if(:orderId is null, true, o.id=:orderId) " +
                    "   and if(:orderDetailId is null, true, od.id=:orderDetailId) " +
                    "   and if(:orderDateStart is null, true, o.order_date >= :orderDateStart) " +
                    "   and if(:orderDateEnd is null, true, o.order_date <= :orderDateEnd) " +
                    "   and if(:fishingDateStart is null, true, o.fishing_date >= :fishingDateStart) " +
                    "   and if(:fishingDateEnd is null, true, o.fishing_date <= :fishingDateEnd) " +
                    "   and if(:totalAmountStart is null, true, o.total_amount >= :totalAmountStart) " +
                    "   and if(:totalAmountEnd is null, true, o.total_amount <= :totalAmountEnd) " +
                    "   and if(:discountAmountStart is null, true, o.discount_amount >= :discountAmountStart) " +
                    "   and if(:discountAmountEnd is null, true, o.discount_amount <= :discountAmountEnd) " +
                    "   and if(:paymentAmountStart is null, true, o.payment_amount >= :paymentAmountStart) " +
                    "   and if(:paymentAmountEnd is null, true, o.payment_amount <= :paymentAmountEnd) " +
                    "   and if(:isPay is null, true, o.is_pay = :isPay) " +
                    "   and if(:payMethod is null, true, o.pay_method = :payMethod) " +
                    "   and if(:orderStatus is null, true, o.order_status = :orderStatus) " +
                    "   and if(:goodsName is null, true, g.name like %:goodsName%) " +
                    "   and if(:memberName is null, true, m.member_name like %:memberName%) " +
                    "   and if(:memberAreaCode is null, true, m.areacode like %:memberAreaCode%) " +
                    "   and if(:memberLocalNumber is null, true, m.localnumber like %:memberLocalNumber%) " +
                    "   and if(:shipName is null, true, s.ship_name like %:shipName%) " +
                    "order by o.created_date desc",
            countQuery = "select o.id " +
                    "from orders o join orders_details od on o.id = od.order_detail_orders_id join goods g on o.goods=g.id " +
                    "   join member m on o.created_by=m.id join ship s on g.goods_ship_id = s.id join company c on s.company_id=c.id " +
                    "where " +
                    "   if(:orderId is null, true, o.id=:orderId) " +
                    "   and if(:orderDetailId is null, true, od.id=:orderDetailId) " +
                    "   and if(:orderDateStart is null, true, o.order_date >= :orderDateStart) " +
                    "   and if(:orderDateEnd is null, true, o.order_date <= :orderDateEnd) " +
                    "   and if(:fishingDateStart is null, true, o.fishing_date >= :fishingDateStart) " +
                    "   and if(:fishingDateEnd is null, true, o.fishing_date <= :fishingDateEnd) " +
                    "   and if(:totalAmountStart is null, true, o.total_amount >= :totalAmountStart) " +
                    "   and if(:totalAmountEnd is null, true, o.total_amount <= :totalAmountEnd) " +
                    "   and if(:discountAmountStart is null, true, o.discount_amount >= :discountAmountStart) " +
                    "   and if(:discountAmountEnd is null, true, o.discount_amount <= :discountAmountEnd) " +
                    "   and if(:paymentAmountStart is null, true, o.payment_amount >= :paymentAmountStart) " +
                    "   and if(:paymentAmountEnd is null, true, o.payment_amount <= :paymentAmountEnd) " +
                    "   and if(:isPay is null, true, o.is_pay = :isPay) " +
                    "   and if(:payMethod is null, true, o.pay_method = :payMethod) " +
                    "   and if(:orderStatus is null, true, o.order_status = :orderStatus) " +
                    "   and if(:goodsName is null, true, g.name like %:goodsName%) " +
                    "   and if(:memberName is null, true, m.member_name like %:memberName%) " +
                    "   and if(:memberAreaCode is null, true, m.areacode like %:memberAreaCode%) " +
                    "   and if(:memberLocalNumber is null, true, m.localnumber like %:memberLocalNumber%) " +
                    "   and if(:shipName is null, true, s.ship_name like %:shipName%) " +
                    "order by o.created_date desc",
            nativeQuery = true
    )
    Page<OrderManageDtoForPage> getOrderList(
        @Param("orderId") Long orderId,
        @Param("orderDetailId") Long orderDetailId,
        @Param("orderDateStart") String orderDateStart,
        @Param("orderDateEnd") String orderDateEnd,
        @Param("fishingDateStart") String fishingDateStart,
        @Param("fishingDateEnd") String fishingDateEnd,
        @Param("totalAmountStart") Integer totalAmountStart,
        @Param("totalAmountEnd") Integer totalAmountEnd,
        @Param("discountAmountStart") Integer discountAmountStart,
        @Param("discountAmountEnd") Integer discountAmountEnd,
        @Param("paymentAmountStart") Integer paymentAmountStart,
        @Param("paymentAmountEnd") Integer paymentAmountEnd,
        @Param("isPay") Boolean isPay,
        @Param("payMethod") Integer payMethod,
        @Param("orderStatus") Integer orderStatus,
        @Param("goodsName") String goodsName,
        @Param("memberName") String memberName,
        @Param("memberAreaCode") String memberAreaCode,
        @Param("memberLocalNumber") String memberLocalNumber,
        @Param("shipName") String shipName,
        Pageable pageable
    );
}
