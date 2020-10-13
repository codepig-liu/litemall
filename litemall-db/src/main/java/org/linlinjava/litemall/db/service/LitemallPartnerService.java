package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.db.dao.LitemallAdMapper;
import org.linlinjava.litemall.db.dao.LitemallPartnerMapper;
import org.linlinjava.litemall.db.domain.*;
import org.linlinjava.litemall.db.util.LatitudeUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LitemallPartnerService {
    @Resource
    private LitemallPartnerMapper partnerMapper;


    public List<LitemallPartner> queryIndex(String lat, String lng, Integer page, Integer limit) {
        LitemallPartnerSelect example = new LitemallPartnerSelect();
        example.setLatitude(lat);
        example.setLongitude(lng);
        PageHelper.startPage(page, limit);
        //example.or().andPositionEqualTo((byte) 1).andDeletedEqualTo(false).andEnabledEqualTo(true);
        List<LitemallPartner> partnerList = partnerMapper.selectPartnerDistance(example);
        partnerList = partnerList.stream().sorted(Comparator.comparing(LitemallPartner::getDistance)).collect(Collectors.toList());

        return partnerList;
    }

    public List<LitemallPartner> querySelective(String name, String telephone, String address, Integer page, Integer limit, String sort, String order) {
        LitemallPartnerExample example = new LitemallPartnerExample();
        LitemallPartnerExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        if (!StringUtils.isEmpty(telephone)) {
            criteria.andTelephoneLike("%" + telephone + "%");
        }
        if (!StringUtils.isEmpty(address)) {
            criteria.andAddressLike("%" + address + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }
        PageHelper.startPage(page, limit);
        return partnerMapper.selectByExample(example);
    }

    public int updateById(LitemallPartner partner) {
        Map<String, String> json = LatitudeUtils.getGeocoderLatitude(partner.getAddress());
        partner.setLatitude(json.get("lat"));
        partner.setLongitude(json.get("lng"));
        partner.setUpdateTime(LocalDateTime.now());
        return partnerMapper.updateByPrimaryKeySelective(partner);
    }

    public void deleteById(Integer id) {
        partnerMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(LitemallPartner partner) {
        Map<String, String> json = LatitudeUtils.getGeocoderLatitude(partner.getAddress());
        partner.setLatitude(json.get("lat"));
        partner.setLongitude(json.get("lng"));
        partner.setAddTime(LocalDateTime.now());
        partner.setUpdateTime(LocalDateTime.now());
        partnerMapper.insertSelective(partner);
    }

    public LitemallPartner findById(Integer id) {
        return partnerMapper.selectByPrimaryKey(id);
    }

}
