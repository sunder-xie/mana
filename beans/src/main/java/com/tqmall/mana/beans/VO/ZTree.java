package com.tqmall.mana.beans.VO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangzhangting on 16/12/19.
 */
@Data
public class ZTree {
    private Integer id;
    private Integer pid;
    private String name;
    private Boolean open;
    private Boolean checked;
    private List<ZTree> children;

    public ZTree(){
        this.open = true;
        this.checked = false;
        this.children = new ArrayList<>();
    }

    public ZTree(Boolean open){
        this.open = open;
        this.checked = false;
        this.children = new ArrayList<>();
    }

    public ZTree(Boolean open, Boolean checked){
        this.open = open;
        this.checked = checked;
        this.children = new ArrayList<>();
    }

}
