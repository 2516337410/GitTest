package com.cy.store.controller;

import com.cy.store.entity.Order;
import com.cy.store.service.IOrderService;
import com.cy.store.util.JsonResult;
import jakarta.servlet.http.HttpSession;
import jdk.security.jarsigner.JarSigner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orders")
public class OrderController extends BaseController{
    @Autowired
    private IOrderService orderService;

    @RequestMapping("create")
    public JsonResult<Order> create(Integer aid, Integer[] cids, HttpSession session){
        Order data = orderService.create(aid,getuidFromSession(session),getUsernameFromSession(session),cids);
        return new JsonResult<>(OK,data);
    }
}
