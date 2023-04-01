package com.cy.store.service;

import com.cy.store.vo.CartVO;

import java.util.List;

public interface ICartService {
    void addToCart(Integer uid, Integer pid, Integer amount, String username);

    List<CartVO> getVOByUid(Integer uid);

    Integer addNum(Integer cid, Integer uid, String username);

    List<CartVO> getVOByCid(Integer uid, Integer[] cids);
}
