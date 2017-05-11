package com.tqmall.mana.test.server.insurance;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.client.beans.insurance.InsuranceDicDTO;
import com.tqmall.mana.client.service.insurance.InsuranceDicService;
import com.tqmall.mana.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by zxg on 17/1/18.
 * 11:41
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public class InsuranceDicServiceTest extends BaseTest {

    @Autowired
    private InsuranceDicService dicService;

    @Test
    public void testGetCooperationModeList(){
        Result<List<InsuranceDicDTO>> cooperationModeList = dicService.getCooperationModeList();
        System.out.println(cooperationModeList.toString());
    }

    @Test
    public void testGetSettleProjectList(){
        Result<List<InsuranceDicDTO>> settleProjectList = dicService.getSettleProjectList();
        System.out.println(settleProjectList.toString());
    }


}
