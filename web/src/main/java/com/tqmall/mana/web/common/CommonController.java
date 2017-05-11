package com.tqmall.mana.web.common;

import com.tqmall.athena.domain.result.carcategory.CarCategoryDTO;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.insurance.InsuranceCompanyBO;
import com.tqmall.mana.beans.VO.CarBrandSelectVO;
import com.tqmall.mana.biz.manager.common.CommonBiz;
import com.tqmall.mana.biz.manager.insurance.InsuranceBiz;
import com.tqmall.mana.component.bean.ConstantBean;
import com.tqmall.mana.component.util.ResultUtil;
import com.tqmall.mana.external.dubbo.athena.ExtCarService;
import com.tqmall.mana.external.dubbo.insurance.ExtInsuranceCompanyService;
import com.tqmall.mana.external.dubbo.stall.ExtRegionService;
import com.tqmall.tqmallstall.domain.result.RegionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by huangzhangting on 16/12/7.
 */
@Controller
@RequestMapping("common")
public class CommonController extends BaseController {
    @Value("${send.coupon.key}")
    private String sendCouponKey;

    @Autowired
    private ExtCarService extCarService;
    @Autowired
    private ExtRegionService extRegionService;
    @Autowired
    private ExtInsuranceCompanyService extInsuranceCompanyService;
    @Autowired
    private InsuranceBiz insuranceBiz;
    @Autowired
    private CommonBiz commonBiz;


    @RequestMapping("unauthorized")
    public ModelAndView unauthorized() {
        return new ModelAndView("unauthorized");
    }

    @RequestMapping("checkSendCouponKey")
    @ResponseBody
    public Result checkSendCouponKey(String key) {
        if (sendCouponKey.equals(key)) {
            session.setAttribute(ConstantBean.SEND_COUPON_KEY, key);
            return ResultUtil.successResult(1);
        }
        return ResultUtil.errorResult("", "访问密码不正确");
    }

    /**
     * 查询下级车型接口，默认查询品牌
     */
    @RequestMapping("carListByPid")
    @ResponseBody
    public List<CarCategoryDTO> carListByPid(Integer pid) {
        return extCarService.getCarListByPid(pid);
    }

    /**
     * 车品牌选择
     */
    @RequestMapping("carBrandSelect")
    @ResponseBody
    public CarBrandSelectVO carBrandSelect() {
        return commonBiz.getCarBrandSelectVO();
    }

    /**
     * 查询下级地区接口，默认查询中国的省份
     */
    @RequestMapping("regionListByPid")
    @ResponseBody
    public List<RegionDTO> regionListByPid(Integer pid) {
        return extRegionService.getRegionByPid(pid);
    }

    /** 查询车牌前缀，默认查询省份 */
//    @RequestMapping("licensePreList")
//    @ResponseBody
//    public List<LicenseProvinceBO> licensePreList(Integer cityId){
//        return extLegendService.getLicensePreList(cityId);
//    }

    /**
     * 查询保险公司列表
     */
    @RequestMapping("insuranceCompanyList")
    @ResponseBody
    public List<InsuranceCompanyBO> insuranceCompanyList() {
        return insuranceBiz.getAllCompanyList();
    }

}
