package com.cy.store.service;
//import org.junit.runner.RunWith;

import com.cy.store.entity.Order;
import com.cy.store.service.ex.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

// @RunWith(SpringRunner.class)注解是一个测试启动器，可以加载Springboot测试注解
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class OrderServiceTests {
    @Autowired(required = false)
    private IOrderService orderService;

    @Test
    public void create() {
        try {
            Integer aid = 7;
            Integer[] cids = {1, 2, 4, 5};
            Integer uid = 11;
            String username = "红红";
            Order order = orderService.create(aid, uid, username, cids);
            System.out.println(order);
        } catch (ServiceException e) {
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
    }
}