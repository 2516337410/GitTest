package com.cy.store.mapper;
//import org.junit.runner.RunWith;

import com.cy.store.entity.Address;
import com.cy.store.entity.District;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

// @RunWith(SpringRunner.class)注解是一个测试启动器，可以加载Springboot测试注解
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DistrictMapperTests {
    @Autowired(required = false)
    private DistrictMapper districtMapper;

    @Test
    public void findByParent(){
        String parent = "110100";
        List<District> list = districtMapper.findByParent(parent);
        System.out.println("count=" + list.size());
        for (District district : list) {
            System.out.println(district);
        }
    }

    @Test
    public void findNameByCode(){
        String code = "610000";
        String name = districtMapper.findNameByCode(code);
        System.out.println(name);
    }
}