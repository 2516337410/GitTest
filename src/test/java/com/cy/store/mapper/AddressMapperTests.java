package com.cy.store.mapper;
//import org.junit.runner.RunWith;

import com.cy.store.entity.Address;
import com.cy.store.entity.User;
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
public class AddressMapperTests {
    @Autowired(required = false)
    private AddressMapper addressMapper;

    @Test
    public void insert(){
        Address address = new Address();
        address.setUid(11);
        address.setName("女盆友");
        address.setPhone("17858802974");
        address.setAddress("雁塔区小寨赛格");
        Integer rows = addressMapper.insert(address);
        System.out.println("rows=" + rows);
    }

    @Test
    public void countByUid(){
        Integer uid = 11;
        Integer count = addressMapper.countByUid(uid);
        System.out.println("count=" + count);
    }

    @Test
    public void findByUid(){
        Integer uid = 11;
        List<Address> list = addressMapper.findByUid(uid);
        System.out.println(list);
    }

    @Test
    public void updateNonDefaultByUid() {
        Integer uid = 11;
        Integer rows = addressMapper.updateNonDefaultByUid(uid);
        System.out.println("rows=" + rows);
    }

    @Test
    public void updateDefaultByAid() {
        Integer aid = 5;
        String modifiedUser = "管理员";
        Date modifiedTime = new Date();
        Integer rows = addressMapper.updateDefaultByAid(aid, modifiedUser, modifiedTime);
        System.out.println("rows=" + rows);
    }

    @Test
    public void findByAid() {
        Integer aid = 4;
        Address result = addressMapper.findByAid(aid);
        System.out.println(result);
    }

    @Test
    public void deleteByAid(){
        addressMapper.deleteByAid(4);
    }

    @Test
    public void findLastModified() {
        Integer uid = 11;
        Address result = addressMapper.findLastModified(uid);
        System.out.println(result);
    }
}