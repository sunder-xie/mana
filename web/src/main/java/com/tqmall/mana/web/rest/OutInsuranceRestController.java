package com.tqmall.mana.web.rest;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.outInsurance.PingAnBO;
import com.tqmall.mana.beans.entity.insurance.OtherCityDO;
import com.tqmall.mana.beans.entity.insurance.OtherInsuranceRelationDO;
import com.tqmall.mana.biz.manager.outInsurance.OtherInsuranceRelationBiz;
import com.tqmall.mana.biz.util.OtherCityUtils;
import com.tqmall.mana.biz.util.OtherRelationUtils;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.dao.mapper.insurance.OtherCityDOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by zxg on 16/11/30.
 * 10:07
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@RestController
@RequestMapping("outInsuranceRest")
public class OutInsuranceRestController {

    @Autowired
    private OtherCityDOMapper otherCityDOMapper;

    @Autowired
    private OtherInsuranceRelationBiz otherInsuranceRelationBiz;

    @RequestMapping(value = "refreshOutCity", method = RequestMethod.GET)
    public Result refreshOutCity(){
        List<OtherCityDO> otherCityDOList = otherCityDOMapper.selectAll();
        OtherCityUtils.setMap(otherCityDOList);

        return Result.wrapSuccessfulResult(OtherCityUtils.getMap());
    }

    @RequestMapping(value = "refreshOutRelation", method = RequestMethod.GET)
    public Result refreshOutRelation(){
        // 设置 安心对应的其他车险的字段
        List<OtherInsuranceRelationDO> list = otherInsuranceRelationBiz.getAllRelation();
        OtherRelationUtils.setInsuranceRelationDOMap(list);

        return Result.wrapSuccessfulResult(OtherRelationUtils.getMap());
    }

    @RequestMapping(value = "getPingAnCity", method = RequestMethod.GET)
    public Result getPingAn(PingAnBO pingAnBO){

        return Result.wrapSuccessfulResult("success");
    }

    @RequestMapping(value = "test", method = RequestMethod.POST)
    public Result test(String cities){
        List<Object> list = JsonUtil.strToList(cities,Object.class);

        return Result.wrapSuccessfulResult("success");
    }

}
