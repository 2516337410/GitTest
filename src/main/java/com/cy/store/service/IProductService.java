package com.cy.store.service;

import com.cy.store.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IProductService {
    /**
     * 查询热销商品的前四名
     * @return 热销商品前四名的集合
     */
    List<Product> findHotList();

    Product findById(Integer id);
}
