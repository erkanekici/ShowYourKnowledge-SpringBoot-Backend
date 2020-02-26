package com.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic/", "/queue/"); //client ın abone olacagi dizinler
        config.setApplicationDestinationPrefixes("/app"); //client bu on dizin ile controllerdaki @MessageMapping-annotated metotların pathlerine erisir
        //config.setPathMatcher(new AntPathMatcher("."));
        //config.setUserDestinationPrefix("/secured/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // websocket endpointe ekstra guvenlik saglar. client sockete sadece bu dizin ile gelecek
                .setAllowedOrigins("*") //"mydomain.com"
                .withSockJS(); // sockJs ile websocket bağlantısı kesildiğinde veya websocket bağlantısı kurulamadığında, bağlantı HTTP'ye indirilir ve istemci ile sunucu arasındaki iletişim devam eder.

        // .setClientLibraryUrl("http://localhost:8080/myapp/js/sockjs-client.js");
        // .setStreamBytesLimit(512 * 1024)
        //.setHttpMessageCacheSize(1000)
        //.setDisconnectDelay(30 * 1000);
    }

}