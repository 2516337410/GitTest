package com.cy.store.service.impl;

import com.cy.store.controller.ex.AddressCountLimitException;
import com.cy.store.entity.Address;
import com.cy.store.mapper.AddressMapper;
import com.cy.store.service.IAddressService;
import com.cy.store.service.IDistrictService;
import com.cy.store.service.ex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl implements IAddressService {
    @Autowired(required = false)
    private AddressMapper addressMapper;
    @Autowired
    private IDistrictService districtService;
    @Value("${user.address.max-count}")
    private Integer maxCount;
    @Override
    public void addNewAddress(Integer uid, String username, Address address) {
        Integer count = addressMapper.countByUid(uid);
        if(count >= maxCount){
            throw new AddressCountLimitException("用户收货地址超限");
        }
        // 对address对象中的数据进行补全：省市区
        String ProvinceName = districtService.getNameByCode(address.getProvinceCode());
        String CityName = districtService.getNameByCode(address.getCityCode());
        String AreaName = districtService.getNameByCode(address.getAreaCode());

        address.setProvinceName(ProvinceName);
        address.setCityName(CityName);
        address.setAreaName(AreaName);
        address.setUid(uid);
        Integer isDefault = count == 0 ? 1 : 0;
        address.setIsDefault(isDefault);
        address.setCreatedUser(username);
        address.setCreatedTime(new Date());
        address.setModifiedUser(username);
        address.setModifiedTime(new Date());
        Integer rows = addressMapper.insert(address);
        if (rows != 1){
            throw new InsertException("插入用户收入地址时产生未知的异常");
        }
    }

    @Override
    public List<Address> getByUid(Integer uid) {
        List<Address> list = addressMapper.findByUid(uid);
        for(Address address : list){
            address.setProvinceCode(null);
            address.setCityCode(null);
            address.setAreaCode(null);
            address.setZip(null);
            address.setTel(null);
            address.setCreatedUser(null);
            address.setCreatedTime(null);
            address.setModifiedUser(null);
            address.setModifiedTime(null);
        }
        return list;
    }

    @Override
    public void setDefault(Integer uid, Integer aid, String username) {
        Address result = addressMapper.findByAid(aid);
        if(result == null){
            throw new AddressNotFoundException("收货地址信息不存在");
        }
        if(!result.getUid().equals(uid)){
            throw new AccessDeniedException("非法数据访问");
        }
        Integer rows = addressMapper.updateNonDefaultByUid(uid);
        if(rows < 1){
            throw new UpdateException("更新数据时产生未知异常");
        }
        rows = addressMapper.updateDefaultByAid(aid,username,new Date());
        if(rows != 1){
            throw new UpdateException("更新数据时产生未知异常");
        }
    }

    @Override
    public void delete(Integer aid, Integer uid, String username) {
        Address result = addressMapper.findByAid(aid);
        if(result == null){
            throw new AddressNotFoundException("收货地址信息不存在");
        }
        if(!result.getUid().equals(uid)){
            throw new AccessDeniedException("非法数据访问");
        }

        Integer rows = addressMapper.deleteByAid(aid);
        if(rows != 1){
            throw new DeleteException("用户删除数据时产生未知的异常");
        }
        Integer count = addressMapper.countByUid(uid);
        if(count == 1){
            return;
        }
        if(result.getIsDefault() == 0){
            return;
        }
        Address address  = addressMapper.findLastModified(uid);
        rows = addressMapper.updateDefaultByAid(address.getAid(), username,new Date());
        if(rows != 1){
            throw new UpdateException("更新数据时产生未知异常");
        }
    }

    @Override
    public Address getByAid(Integer aid, Integer uid) {
        Address result = addressMapper.findByAid(aid);
        if(result == null){
            throw new AddressNotFoundException("收货地址数据不存在");
        }
        if(!result.getUid().equals(uid)){
            throw new AccessDeniedException("非法数据访问");
        }
        result.setProvinceCode(null);
        result.setCityCode(null);
        result.setAreaCode(null);
        result.setCreatedUser(null);
        result.setCreatedTime(null);
        result.setModifiedUser(null);
        result.setModifiedTime(null);
        return result;
    }
}
