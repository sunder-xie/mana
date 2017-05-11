package com.tqmall.mana.web.common.convert;

import com.tqmall.mana.component.util.DateUtil;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * Created by huangzhangting on 16/12/27.
 */
public class DateConvert implements Converter<String, Date> {
    @Override
    public Date convert(String s) {
        return DateUtil.stringToDate(s, DateUtil.yyyy_MM_dd);
    }
}
