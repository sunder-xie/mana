//package com.tqmall.mana.web;
//
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
///**
// * Created by huangzhangting on 16/12/5.
// */
//@Controller
//public class GreetingController {
//    @RequestMapping("/helloSocket")
//    public String index(){
//        return "/hello/index";
//    }
//
//    @MessageMapping("/change-notice")
//    @SendTo("/topic/notice")
//    public String greeting(String value) {
//        return value;
//    }
//
//}
