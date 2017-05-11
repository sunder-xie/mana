//package com.tqmall.mana.web.common.websocket;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.*;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
///**
// * Created by huangzhangting on 16/12/5.
// */
//
//@Slf4j
//@Component
//public class AppWebSocketHandler implements WebSocketHandler {
//    private static final List<WebSocketSession> sessionList = new ArrayList<>();
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
//        sessionList.add(webSocketSession);
//
//        webSocketSession.sendMessage(new TextMessage("你好啊"));
//        webSocketSession.sendMessage(new TextMessage("哈哈哈"));
//    }
//
//    @Override
//    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
//
//        webSocketSession.sendMessage(new TextMessage(new Date()+""));
//    }
//
//    @Override
//    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
//        if(webSocketSession.isOpen()){
//            webSocketSession.close();
//        }
//        sessionList.remove(webSocketSession);
//
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
//        sessionList.remove(webSocketSession);
//
//    }
//
//    @Override
//    public boolean supportsPartialMessages() {
//        return false;
//    }
//
//    /**
//     * 给所有在线用户发送消息
//     *
//     * @param message
//     */
//    public void sendMessageToUsers(TextMessage message) {
//        for (WebSocketSession session : sessionList) {
//            try {
//                if (session.isOpen()) {
//                    session.sendMessage(message);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//}
