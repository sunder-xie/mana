//package com.tqmall.mana.web.common.websocket;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;
//import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
//import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
//
//
///**
// * Created by huangzhangting on 16/12/5.
// */
//
//@Slf4j
//@Configuration
//@EnableWebMvc
//@EnableWebSocket
//public class WebSocketConf extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
//    @Autowired
//    private AppWebSocketHandler appWebSocketHandler;
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//
//        registry.addHandler(appWebSocketHandler, "/webSocketServer");
//        registry.addHandler(appWebSocketHandler, "/webSocketServer/sockjs").setAllowedOrigins("*").withSockJS();
//    }
//
//}
