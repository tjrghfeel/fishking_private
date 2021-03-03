package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.fishing.Orders;

import java.util.List;

public interface OrdersRepositoryCustom {
    List<Orders> getCheckOrders();
}
