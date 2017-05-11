package com.tqmall.mana.beans.BO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by huangzhangting on 16/12/14.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LicenseProvinceBO {
    public String license;
    public String firstLetter;
    public String province;
    public String cityName;
    public Integer cityId;
}
