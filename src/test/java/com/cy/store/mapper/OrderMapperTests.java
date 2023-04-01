package com.cy.store.mapper;
//import org.junit.runner.RunWith;

import com.cy.store.entity.Cart;
import com.cy.store.entity.Order;
import com.cy.store.entity.OrderItem;
import com.cy.store.vo.CartVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

// @RunWith(SpringRunner.class)注解是一个测试启动器，可以加载Springboot测试注解
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class OrderMapperTests {
    @Autowired(required = false)
    private OrderMapper orderMapper;

    @Test
    public void insertOrder() {
        Order order = new Order();
        order.setUid(11);
        order.setRecvName("胡代金");
        order.setRecvPhone("1122334455");
        Integer rows = orderMapper.insertOrder(order);
        System.out.println("rows=" + rows);
    }

    @Test
    public void insertOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setOid(1);
        orderItem.setPid(10000008);
        orderItem.setTitle("戴尔Dell 燃700R1605经典版银色");
        Integer rows = orderMapper.insertOrderItem(orderItem);
        System.out.println("rows=" + rows);
    }
}