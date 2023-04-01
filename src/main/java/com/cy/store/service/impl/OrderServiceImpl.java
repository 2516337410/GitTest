package com.cy.store.service.impl;

import com.cy.store.entity.Address;
import com.cy.store.entity.Order;
import com.cy.store.entity.OrderItem;
import com.cy.store.mapper.OrderMapper;
import com.cy.store.service.IAddressService;
import com.cy.store.service.ICartService;
import com.cy.store.service.IOrderService;
import com.cy.store.service.ex.InsertException;
import com.cy.store.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired(required = false)
    private OrderMapper orderMapper;

    @Autowired(required = false)
    private IAddressService addressService;

    @Autowired(required = false)
    private ICartService cartService;

    @Override
    public Order create(Integer aid, Integer uid, String username, Integer[] cids) {
        List<CartVO> list = cartService.getVOByCid(uid,cids);
        // 计算产品的总价
        Long totalPrice = 0L;
        for (CartVO cartVO : list){
            totalPrice += cartVO.getRealPrice() * cartVO.getNum();
        }
        Address address = addressService.getByAid(aid,uid);
        Order order = new Order();
        order.setUid(uid);
        order.setRecvName(address.getName());
        order.setRecvPhone(address.getPhone());
        order.setRecvProvince(address.getProvinceName());
        order.setRecvCity(address.getCityName());
        order.setRecvArea(address.getAreaName());
        order.setRecvAddress(address.getAddress());
        order.setStatus(0);
        order.setTotalPrice(totalPrice);
        order.setOrderTime(new Date());
        order.setCreatedUser(username);
        order.setCreatedTime(new Date());
        order.setModifiedUser(username);
        order.setModifiedTime(new Date());
        Integer rows = orderMapper.insertOrder(order);
        if(rows != 1){
            throw new InsertException("插入数据异常");
        }

        for (CartVO cartVO : list){
            OrderItem orderItem = new OrderItem();
            orderItem.setOid(order.getOid());
            orderItem.setPid(cartVO.getPid());
            orderItem.setTitle(cartVO.getTitle());
            orderItem.setPrice(cartVO.getRealPrice());
            orderItem.setImage(cartVO.getImage());
            orderItem.setNum(cartVO.getNum());
            orderItem.setCreatedUser(username);
            orderItem.setCreatedTime(new Date());
            orderItem.setModifiedUser(username);
            orderItem.setModifiedTime(new Date());
            rows = orderMapper.insertOrderItem(orderItem);
            if(rows != 1){
                throw new InsertException("插入数据异常");
            }
        }

        return order;
    }
}
