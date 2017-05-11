package com.tqmall.mana.dao.mapper.sms;

import com.tqmall.mana.beans.entity.sms.ManaSmsTemplateDO;

import java.util.List;

public interface ManaSmsTemplateDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ManaSmsTemplateDO record);

    ManaSmsTemplateDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ManaSmsTemplateDO record);

    List<ManaSmsTemplateDO> selectAll();

}