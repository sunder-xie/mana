package com.tqmall.mana.test;

import com.tqmall.mana.component.util.excel.PoiUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangzhangting on 16/12/26.
 */
@Slf4j
public class PoiTest {

    @Data
    class Person{
        private String name;
        private String mobile;

        public Person() {
        }

        public Person(String name, String mobile) {
            this.name = name;
            this.mobile = mobile;
        }
    }


    /** 导出简单bean封装的数据 */
    @Test
    public void testExportBean(){
        //excel表头
        String[] heads = new String[]{"姓名", "电话"};
        //实体类对应的属性名称
        String[] fields = new String[]{"name", "mobile"};
        //需要导出的数据
        Collection<Person> dataList = new ArrayList<>();
        dataList.add(new Person("张三", "123556"));
        dataList.add(new Person("李四", "1235235"));

        PoiUtil poiUtil = new PoiUtil();
        String filePath = "/Users/huangzhangting";
        String fileName = "poi测试-1";
        try {
            poiUtil.exportXlsxToSystem(fileName, filePath, heads, fields, dataList);
        } catch (Exception e) {
            log.error("export send log excel error", e);
        }

    }


    /** 导出map组装的数据 */
    @Test
    public void testExportMap(){
        //excel表头
        String[] heads = new String[]{"姓名", "电话"};

        //需要导出的数据，map的key，要跟表头保持一致
        Collection<Map<String, String>> dataList = new ArrayList<>();
        dataList.add(new HashMap<String, String>(){{
            put("姓名", "张三");
            put("电话", "123456");
        }});
        dataList.add(new HashMap<String, String>(){{
            put("姓名", "李四");
            put("电话", "456789");
        }});

        PoiUtil poiUtil = new PoiUtil();
        String filePath = "/Users/huangzhangting";
        String fileName = "poi测试-2";
        try {
            poiUtil.exportXlsxWithMapToSystem(fileName, filePath, heads, dataList);
        } catch (Exception e) {
            log.error("export send log excel error", e);
        }

    }

    @Test
    public void exportOffLineInsuranceList(){



    }
}
