package com.cy.store.service;
//import org.junit.runner.RunWith;

import com.cy.store.entity.Address;
import com.cy.store.entity.District;
import com.cy.store.service.ex.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

// @RunWith(SpringRunner.class)注解是一个测试启动器，可以加载Springboot测试注解
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DistrictServiceTests {
    @Autowired(required = false)
    private IDistrictService districtService;

    @Test
    public void getByParent(){
        List<District> list = districtService.getByParent("86");
        for (District d : list){
            System.out.println(d);
        }
    }
}