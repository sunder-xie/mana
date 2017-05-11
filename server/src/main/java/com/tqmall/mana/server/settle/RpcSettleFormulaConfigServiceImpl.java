//package com.tqmall.mana.server.settle;
//
//import com.tqmall.core.common.entity.Result;
//import com.tqmall.mana.beans.entity.settle.SettleFeeFormulaConfigDO;
//import com.tqmall.mana.biz.manager.settle.config.SettleFormulaConfigBiz;
//import com.tqmall.mana.client.beans.settle.SettleFeeFormulaConfigDTO;
//import com.tqmall.mana.client.service.settle.RpcSettleFormulaConfigService;
//import com.tqmall.mana.component.enums.YesNoEnum;
//import com.tqmall.mana.component.util.BdUtil;
//import com.tqmall.mana.component.util.ResultUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by huangzhangting on 17/3/8.
// */
//public class RpcSettleFormulaConfigServiceImpl implements RpcSettleFormulaConfigService {
//    @Autowired
//    private SettleFormulaConfigBiz formulaConfigBiz;
//
//    @Override
//    public Result<List<SettleFeeFormulaConfigDTO>> getAllShowFormulas() {
//        List<SettleFeeFormulaConfigDTO> dtoList = new ArrayList<>();
//        List<SettleFeeFormulaConfigDO> list = formulaConfigBiz.getFormulaConfigDOList();
//        for(SettleFeeFormulaConfigDO formulaConfigDO : list){
//            if(YesNoEnum.YES.getCode().equals(formulaConfigDO.getIsShow())){
//                SettleFeeFormulaConfigDTO configDTO = BdUtil.do2bo(formulaConfigDO, SettleFeeFormulaConfigDTO.class);
//                dtoList.add(configDTO);
//            }
//        }
//        return ResultUtil.successResult(dtoList);
//    }
//}
