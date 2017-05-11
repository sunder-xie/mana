package com.tqmall.mana.test.others;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.*;

/**
 * Created by zxg on 17/2/7.
 * 14:27
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public class JodaTimeTest {

    @Test
    public void JodaTimeMaximumTest(){
        DateTime resultTime = new DateTime();
        resultTime = resultTime.minusMonths(1);
        resultTime = resultTime.dayOfMonth().withMaximumValue().secondOfDay().withMaximumValue();
        System.out.println(resultTime.toString("yyyy-MM-dd HH:mm:ss"));

    }

    public static void main(String[] args){
        Set set = new TreeSet();
        set.add("2");
        set.add(null);
        set.add("1");
        Iterator it = set.iterator();
        while (it.hasNext())
            System.out.print(it.next() + "");

        Map map = new Hashtable();
    }
}
