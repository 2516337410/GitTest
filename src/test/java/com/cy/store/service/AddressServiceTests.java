package com.cy.store.service;
//import org.junit.runner.RunWith;

import com.cy.store.entity.Address;
import com.cy.store.entity.User;
import com.cy.store.mapper.AddressMapper;
import com.cy.store.service.ex.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

// @RunWith(SpringRunner.class)注解是一个测试启动器，可以加载Springboot测试注解
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AddressServiceTests {
    @Autowired(required = false)
    private IAddressService addressService;

    @Test
    public void addNewAddress(){
        try {
            Integer uid = 11;
            String username = "管理员";
            Address address = new Address();
            address.setName("男朋友");
            address.setPhone("17858805555");
            address.setAddress("雁塔区小寨华旗");
            addressService.addNewAddress(uid, username, address);
            System.out.println("OK.");
        } catch (ServiceException e) {
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void setDefault(){
        Integer uid = 11;
        Integer aid = 4;
        String username = "小红";
        addressService.setDefault(uid,aid,username);
    }

    @Test
    public void delete() {
        try {
            Integer aid = 3;
            Integer uid = 11;
            String username = "明明";
            addressService.delete(aid, uid, username);
            System.out.println("OK.");
        } catch (ServiceException e) {
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
    }
}