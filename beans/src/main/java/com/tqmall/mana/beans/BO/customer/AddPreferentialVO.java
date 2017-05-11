package com.tqmall.mana.beans.BO.customer;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * Created by zhouheng on 16/12/28.
 */
@Data
public class AddPreferentialVO {

    private List<Integer> vehicleIdList;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date sendDate;

    private String preferentialContent;

    private Integer preferentialType;

    private Integer preferentialNum;


}
