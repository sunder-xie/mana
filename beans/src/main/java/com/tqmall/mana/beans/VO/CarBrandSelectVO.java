package com.tqmall.mana.beans.VO;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 车品牌选择返回对象
 * Created by huangzhangting on 16/12/23.
 */
@Data
public class CarBrandSelectVO {
    private List<String> firstWordList; //首字母筛选
    private Map<String, List<CarBrandVO>> dataMap; //具体数据，已首字符最为key
}
