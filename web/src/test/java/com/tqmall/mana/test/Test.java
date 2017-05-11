package com.tqmall.mana.test;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by huangzhangting on 17/3/29.
 */
public class Test {
    public static void main(String[] args){
        EnumTest enumTest = EnumTest.BLUE;
        testEnum(enumTest);


        BigDecimal result = new BigDecimal(100);
        BigDecimal rate = new BigDecimal(0.00);
        System.out.println(rate.doubleValue()>0);
        BigDecimal settleBaseAmount = BigDecimal.ZERO.equals(rate)?null:result.divide(rate, 2, RoundingMode.HALF_UP);
        System.out.println(settleBaseAmount);
    }
    public static void testEnum(EnumTest enumTest){
        switch (enumTest){
            case GREEN:
                System.out.println("is green");
                break;
            case BLUE:
                System.out.println("is blue");
                break;
            case RED:
                System.out.println("is red");
                break;
            default:
                System.out.println("unknown color");
                break;
        }
    }

    public static void test_string(String str){
        switch (str){
            case "a":
                System.out.println("a");
                break;
            case "b":
                System.out.println("b");
                break;
            default:
                System.out.println("什么鬼");
                break;
        }
    }


}
