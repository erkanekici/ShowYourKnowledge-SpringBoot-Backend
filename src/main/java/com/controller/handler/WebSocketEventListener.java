package com.controller.handler;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.LinkedList;
import java.util.Map;

@Component
public class WebSocketEventListener {

  private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

  @Autowired
  private SimpMessageSendingOperations messagingTemplate;

  @EventListener
  public void handleWebSocketConnectListener(SessionConnectedEvent event) {
    logger.info("NEW CONNECTION");
    try {
      StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
      GenericMessage message = (GenericMessage) headers.getMessageHeaders().get("simpConnectMessage");
      Map collect = (Map) message.getHeaders().get("nativeHeaders");
      LinkedList topics = (LinkedList) collect.get("topic");
      String topic = topics.get(0).toString();
      String sessionId = headers.getMessageHeaders().get("simpSessionId").toString();
//    headerAccessor.addNativeHeader("user","erko");

      JSONObject jsonObject=new JSONObject();
      jsonObject.accumulate("msg", "Ben geldim");
      //messagingTemplate.convertAndSend("/topic/chat.addUser/room1", jsonObject.toString());
      messagingTemplate.convertAndSend(topic, jsonObject.toString());
    }catch(Exception e) {
      logger.error("Websocket Connection Error: ");
      e.printStackTrace();
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request", e);
    }

  }

  @EventListener
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

    String username = (String) headerAccessor.getSessionAttributes().get("username");
    if(username != null) {
      logger.info("DISCONNECTION : " + username);
    }
      JSONObject jsonObject=new JSONObject();
      jsonObject.accumulate("msg", "ÇIKTI");

      //messagingTemplate.convertAndSend("/topic/public", jsonObject.toString());
      messagingTemplate.convertAndSend("/topic/chat.addUser/room1", jsonObject.toString());

  }

//  @EventListener
//  public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
//    logger.info("Received a new web socket connection");
//    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//    logger.info(headerAccessor.getSessionAttributes().get("sessionId").toString());
//
//  }
//
//  @EventListener
//  public void handleWebSocketUnsubscribeListener(SessionUnsubscribeEvent event) {
//    logger.info("Received a new web socket connection");
//    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//
//  }
}