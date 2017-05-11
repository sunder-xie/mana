package com.tqmall.mana.biz.manager.goods;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.goods.GoodsBasicBO;
import com.tqmall.mana.component.bean.HttpClientResult;
import com.tqmall.mana.component.exception.BusinessException;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.component.util.ResultUtil;
import com.tqmall.mana.component.util.http.HttpClientUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by zhouheng on 17/3/11.
 */
@Service
@Data
@Slf4j
public class GoodsConvertBizImpl implements GoodsConvertBiz {

    @Value("${search.domain}")
    private String searchDomain;

    private static final String GET_GOODS_URL = "/elasticsearch/goods/convert";

    @Override
    public Result<List<GoodsBasicBO>> getGoodsConvertList(String q) {
        if (StringUtils.isEmpty(q)) {
            return ResultUtil.errorResult("001", "request param is null!");
        }
        StringBuilder stringBuilder = new StringBuilder(searchDomain);
        stringBuilder.append(GET_GOODS_URL).append("?q="+q);
        log.info("http get goodsConvert start,param:{}",q);
        HttpClientResult result = HttpClientUtil.get(stringBuilder.toString());
        if (result == null || result.getStatus() != 200) {
            log.error("get goodsSn Info error,url:{} ,result:{}", stringBuilder.toString(), result);
            return ResultUtil.errorResult("002", "后去数据异常");
        }

        JsonNode jsonNode = JsonUtil.strToObject(result.getData(), JsonNode.class);
        JsonNode jsonNode1 = jsonNode.get("response");
        JsonNode jsonNode2 = jsonNode1.get("list");

        if (jsonNode2 != null) {
            List<GoodsBasicBO> list = packageJsonToGoodsBO(jsonNode2);
            return Result.wrapSuccessfulResult(list);
        }

        return ResultUtil.errorResult("003", "查询无数据");
    }

    /**
     * jsonNode to list
      * @param jsonNode
     * @return
     */
    private List<GoodsBasicBO> packageJsonToGoodsBO(JsonNode jsonNode){
        List<GoodsBasicBO> list = Lists.newArrayList();
        try {
            List<HashMap<String, String>> mapList = JsonUtil.jsonNodeToCollection(jsonNode, List.class, HashMap.class);
            if(mapList !=null ){
                int count = 0;
                for (Map map : mapList) {
                    GoodsBasicBO goodsBasicBO = new GoodsBasicBO();
                    String goodsSn = (String) map.get("goodsSn");
                    String goodsName = (String) map.get("goodsName");
                    goodsBasicBO.setGoodsSn(goodsSn);
                    goodsBasicBO.setGoodsName(goodsName);
                    list.add(goodsBasicBO);
                    count++;
                    if (count > 10) {
                        break;
                    }
                }
            }
            return list;
        }catch (Exception e){
            throw new BusinessException("内部数据转换异常");
        }
    }
}
