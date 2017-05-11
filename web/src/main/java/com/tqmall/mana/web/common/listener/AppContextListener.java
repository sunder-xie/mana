package com.tqmall.mana.web.common.listener;

import com.tqmall.mana.beans.entity.insurance.OtherCityDO;
import com.tqmall.mana.beans.entity.insurance.OtherInsuranceRelationDO;
import com.tqmall.mana.biz.manager.outInsurance.OtherInsuranceRelationBiz;
import com.tqmall.mana.biz.util.OtherCityUtils;
import com.tqmall.mana.biz.util.OtherRelationUtils;
import com.tqmall.mana.dao.mapper.insurance.OtherCityDOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zxg on 16/12/2.
 * 14:02
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Slf4j
@Component
public class AppContextListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private OtherInsuranceRelationBiz otherInsuranceRelationBiz;
    @Autowired
    private OtherCityDOMapper otherCityDOMapper;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
        ApplicationContext applicationContext = event.getApplicationContext().getParent();
        if(applicationContext == null){//root application context 没有parent，他就是老大.
            //需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
            log.info("1=====start]");


        }else{
            //自己的 projectName-servlet context（作为root application context的子容器）
            log.info("2=====start init ]");
            // 设置 安心对应的其他车险的字段
            List<OtherInsuranceRelationDO> list = otherInsuranceRelationBiz.getAllRelation();
            OtherRelationUtils.setInsuranceRelationDOMap(list);
            //设置外部的城市站
            List<OtherCityDO> otherCityDOList = otherCityDOMapper.selectAll();
            OtherCityUtils.setMap(otherCityDOList);
        }

    }
}
