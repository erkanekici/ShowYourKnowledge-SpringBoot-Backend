package com.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Map;

@Controller
@EnableScheduling
public class WebSocketController {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    private final SimpMessagingTemplate template;

    @Autowired
    WebSocketController(SimpMessagingTemplate template){
        this.template = template;
    }

    // @Scheduled(fixedDelay = 5000L)
    // @SendTo("/topic/pingpong")
    // public void sendPong() throws Exception {
    // this.template.convertAndSend(
    // "/topic/pingpong",
    // "pong (periodic)");
    // }

    @MessageMapping("/chat.sendMessage/{room}") //Js deki sendMessage URL i (karşilama urli)
    @SendTo("/topic/chat.addUser/{room}") //SocketClientJS deki topics (client tarafından abone olunan url, dönüş urli)
    //@SendTo("/topic/chat.public")
          public Map<String,String> sendMessage(
      @Payload Map<String,String> mesaj,
      @DestinationVariable String room)
    {
        logger.info("SEND" +new SimpleDateFormat("HH:mm:ss").format(new Date()));
        logger.info(mesaj.toString());
        return mesaj;
      }

    @MessageMapping("/chat.addUser/{room}")
    @SendTo("/topic/chat.addUser/{room}")
    public Map<String,String> addUser(@Payload Map<String,String> chatMessage,
               SimpMessageHeaderAccessor headerAccessor,
               //Principal user,
               @Header("simpSessionId") String sessionId)
    {
    // Add username in web socket session
    headerAccessor.getSessionAttributes().put("username", "abalakama");
    return chatMessage;

    //sendTo kullanmazsan:
    //template.convertAndSendToUser(chatMessage.getSender(), "/topic/public", chatMessage);
    //template.convertAndSend("/topic/public", new Gson().fromJson(chatMessage.getContent(), Map.class).get("name"));
  }



//  @MessageMapping("/chat.sendMessage")
//  @SendTo("/topic/chat.public")
//  public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
//    logger.info("SEND"+new SimpleDateFormat("HH:mm:ss").format(new Date()));
//    return chatMessage;
//  }

//  @MessageMapping("/chat.addUser")
//  @SendTo("/topic/public") //sendTo ile cliente donus yapılacak adresi belirtilir
//  //@SendToUser("/topic/public")
//  public ChatMessage addUser(@Payload ChatMessage chatMessage,
//                SimpMessageHeaderAccessor headerAccessor,
//                //Principal user,
//                @Header("simpSessionId") String sessionId)
//  {
//    // Add username in web socket session
//    headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//    return chatMessage;
//
//    //sendTo kullanmazsan:
//    //template.convertAndSendToUser(chatMessage.getSender(), "/topic/public", chatMessage);
//    //template.convertAndSend("/topic/public", new Gson().fromJson(chatMessage.getContent(), Map.class).get("name"));
//  }


  // Sonradan ekleme
  @MessageMapping("/message")
  @SendToUser("/queue/reply")
  public String processMessageFromClient(@Payload String message, Principal principal) throws Exception {
    //String name = new Gson().fromJson(message, Map.class).get("name").toString();
    //messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/reply", name);
    //return name;
    return "geldi";
  }

  @MessageExceptionHandler //throw any exceptions caused by STOMP to the end user on the /queue/errors destination.
  @SendToUser("/queue/errors")
  public String handleException(Throwable exception) {
    return exception.getMessage();
  }


  // Hack kontrolü için mevcut kupon sayısının yeni kaydedilecek değere oranını kontrol et
          //stompClient.ws.transportTimeoutId var işe yararsa
          //stomp.js i download ederek projeye ekle https://medium.com/@debanjanamaitra/stomp-js-in-5-minutes-30ebfb9d6e9a
}