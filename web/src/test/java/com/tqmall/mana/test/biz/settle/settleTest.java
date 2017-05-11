package com.tqmall.mana.test.biz.settle;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.settle.SettleConfigBasicBO;
import com.tqmall.mana.beans.VO.settle.SettleConfigBasicVO;
import com.tqmall.mana.beans.VO.settle.SettleConfigItemVO;
import com.tqmall.mana.beans.param.settle.SettleConfigVOPageParam;
import com.tqmall.mana.biz.manager.settle.config.SettleConfigBiz;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.test.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouheng on 17/1/13.
 */
@Slf4j
public class settleTest extends BaseTest {

    @Autowired
    private SettleConfigBiz settleConfigBiz;

    private  HttpServletResponse response;

    private HttpServletRequest req;

    protected HttpSession session;




    @Test
    public void addSettleTest() {

        List<SettleConfigBasicVO> basicVOList = new ArrayList<>();

        SettleConfigBasicVO basicVO = new SettleConfigBasicVO();
        basicVO.setGroupName("zhh--交强险");
        basicVO.setInsuranceCompanyId(1);
        basicVO.setInsuranceCompanyName("安心保险");
        basicVO.setInsuranceType(1);
        basicVO.setCooperationMode(1);
        basicVO.setCalculateMode(2);
        List<SettleConfigItemVO> itemVOList = new ArrayList<>();
        SettleConfigItemVO itemVO = new SettleConfigItemVO();
        itemVO.setItemEndValue(new BigDecimal(5000.00));
        itemVO.setItemRate(new BigDecimal(0.30).setScale(2, BigDecimal.ROUND_HALF_UP));
        SettleConfigItemVO itemVO11 = new SettleConfigItemVO();
        itemVO11.setItemEndValue(new BigDecimal(15000.00));
        itemVO11.setItemRate(new BigDecimal(0.35).setScale(2, BigDecimal.ROUND_HALF_UP));
        SettleConfigItemVO itemVO1 = new SettleConfigItemVO();
        itemVO1.setItemEndValue(null);
        itemVO1.setItemRate(new BigDecimal(0.40).setScale(2, BigDecimal.ROUND_HALF_UP));
        itemVOList.add(itemVO11);
        itemVOList.add(itemVO);
        itemVOList.add(itemVO1);
        basicVO.setItemVOList(itemVOList);
        basicVOList.add(basicVO);

        SettleConfigBasicVO basicVO1 = new SettleConfigBasicVO();
        basicVO1.setGroupName("zhh--商业险");
        basicVO1.setInsuranceType(2);
        basicVO1.setCooperationMode(1);
        basicVO1.setCalculateMode(2);
        List<SettleConfigItemVO> itemVOList1 = new ArrayList<>();
        SettleConfigItemVO itemVO2 = new SettleConfigItemVO();
        itemVO2.setItemEndValue(new BigDecimal(5000.00));
        itemVO2.setItemRate(new BigDecimal(0.30).setScale(2, BigDecimal.ROUND_HALF_UP));
        SettleConfigItemVO itemVO3 = new SettleConfigItemVO();
        itemVO3.setItemEndValue(null);
        itemVO3.setItemRate(new BigDecimal(0.40).setScale(2, BigDecimal.ROUND_HALF_UP));
        itemVOList1.add(itemVO2);
        itemVOList1.add(itemVO3);
        basicVO1.setItemVOList(itemVOList1);
        basicVOList.add(basicVO1);

//        List<SettleConfigBasicBO> basicBOList = new ArrayList<>();
//        for (SettleConfigBasicVO basic : basicVOList) {
//            SettleConfigBasicBO basicBO = BdUtil.do2bo(basic,SettleConfigBasicBO.class);
//            List<SettleConfigItemBO> list = BdUtil.do2bo4List(basic.getItemVOList(),SettleConfigItemBO.class);
//            basicBO.setItemBOList(list);
//            basicBOList.add(basicBO);
//        }

//        String url = "http://localhost:8089/settle/addSettleConfig";
//        HttpClientResult result = HttpClientUtil.postJson(url,JsonUtil.objectToStr(basicVOList));
//        System.out.println(result);

//
        Result result = settleConfigBiz.addSettleConfigList(basicVOList);

        log.info("************json********" + JsonUtil.objectToStr(result));

    }


    @Test
    public void deleteSettleConfigTest() {

        Result result = settleConfigBiz.deleteSettleConfig(35);

        log.info("************json********" + JsonUtil.objectToStr(result));
    }

    @Test
    public void UpdateSettleConfigTest() {

        SettleConfigBasicVO basicVO = new SettleConfigBasicVO();
        basicVO.setId(36);
        basicVO.setGroupName("zhh--交强险1");
        basicVO.setInsuranceCompanyId(1);
        basicVO.setInsuranceCompanyName("安心保险");
        basicVO.setInsuranceType(1);
        basicVO.setCooperationMode(1);
        basicVO.setCalculateMode(2);
        List<SettleConfigItemVO> itemVOList = new ArrayList<>();
        SettleConfigItemVO itemVO = new SettleConfigItemVO();
        itemVO.setItemEndValue(new BigDecimal(5000.00));
        itemVO.setItemRate(new BigDecimal(0.30).setScale(2, BigDecimal.ROUND_HALF_UP));
        SettleConfigItemVO itemVO11 = new SettleConfigItemVO();
        itemVO11.setItemEndValue(new BigDecimal(15000.00));
        itemVO11.setItemRate(new BigDecimal(0.35).setScale(2, BigDecimal.ROUND_HALF_UP));
        SettleConfigItemVO itemVO1 = new SettleConfigItemVO();
        itemVO1.setItemEndValue(null);
        itemVO1.setItemRate(new BigDecimal(0.40).setScale(2, BigDecimal.ROUND_HALF_UP));
        itemVOList.add(itemVO11);
        itemVOList.add(itemVO);
        itemVOList.add(itemVO1);
        basicVO.setItemVOList(itemVOList);
//
        Result result = settleConfigBiz.updateSettleConfig(basicVO);

        log.info("************json********" + JsonUtil.objectToStr(result));
    }


    @Test
    public void getBasicBOPagingListTest() {

        SettleConfigVOPageParam voPageParam = new SettleConfigVOPageParam();
        PagingResult<SettleConfigBasicBO> pagingResult = settleConfigBiz.getBasicBOPagingList(voPageParam);
        log.info("************json********" + JsonUtil.objectToStr(pagingResult));

    }

    @Test
    public void exportSettleConfigListTest(){

        SettleConfigVOPageParam voPageParam = new SettleConfigVOPageParam();

        settleConfigBiz.exportSettleConfigList(response,voPageParam);

    }

}
