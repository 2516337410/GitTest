package com.cy.store.service;


import com.cy.store.entity.Address;
import com.cy.store.entity.Order;

public interface IOrderService {
    Order create(Integer aid, Integer uid, String username, Integer[] cids);
}
