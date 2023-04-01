package com.cy.store.service;

import com.cy.store.entity.Address;

import java.util.List;

public interface IAddressService {
    /**
     * 创建新的收货地址
     * @param uid 当前登录的用户的id
     * @param username 当前登录的用户名
     * @param address 用户提交的收货地址数据
     */
    void addNewAddress(Integer uid, String username, Address address);

    List<Address> getByUid(Integer uid);

    void setDefault(Integer uid,Integer aid,String username);

    void delete(Integer aid, Integer uid, String username);

    Address getByAid(Integer aid, Integer uid);
}
