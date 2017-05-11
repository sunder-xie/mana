package com.tqmall.mana.biz.manager.outInsurance;

import com.tqmall.mana.beans.entity.insurance.OtherInsuranceRelationDO;
import com.tqmall.mana.dao.mapper.OtherInsuranceRelationDOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zxg on 16/12/2.
 * 12:13
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Service
public class OtherInsuranceRelationBizImpl implements OtherInsuranceRelationBiz {
    @Autowired
    private OtherInsuranceRelationDOMapper otherInsuranceRelationDOMapper;
    @Override
    public List<OtherInsuranceRelationDO> getAllRelation() {

        return otherInsuranceRelationDOMapper.selectAll();
    }
}
