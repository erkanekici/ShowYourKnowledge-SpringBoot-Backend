//package com.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.http.server.ServletServerHttpRequest;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.HandshakeInterceptor;
//import javax.servlet.http.HttpSession;
//import java.util.Map;
//
//public class HttpHandshakeInterceptor implements HandshakeInterceptor {
//
//
//    private static final Logger logger = LoggerFactory.getLogger(HttpHandshakeInterceptor.class);
//
//
//    @Override
//    public boolean beforeHandshake(ServerHttpRequest request,
//                                   ServerHttpResponse response,
//                                   WebSocketHandler wsHandler,
//                                   Map attributes) throws Exception
//    {
//        if (request instanceof ServletServerHttpRequest) {
//            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
//            HttpSession session = servletRequest.getServletRequest().getSession();
//            attributes.put("sessionId", session.getId());
//        }
//        return true;
//    }
//
//    public void afterHandshake(ServerHttpRequest request,
//                               ServerHttpResponse response,
//                               WebSocketHandler wsHandler,
//                               Exception ex)
//    {
//        logger.info("afterhs");
//    }
//}