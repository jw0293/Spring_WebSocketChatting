package com.spring_chatting.chat.dto;

import com.spring_chatting.chat.domain.service.ChatService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
public class ChatRoom {

    private String roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId, String name){
        this.roomId = roomId;
        this.name = name;
    }

    public void handleActions(WebSocketSession session, ChatMessage message, ChatService chatService){
        if(message.getType().equals(ChatMessage.MessageType.ENTER)){
            sessions.add(session);
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }
        sendMessage(message, chatService);
    }

    public <T> void sendMessage(T message, ChatService chatService){
        sessions.parallelStream().forEach(session -> chatService.sendMessage(session, message));
    }

}
