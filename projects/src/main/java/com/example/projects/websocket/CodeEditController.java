package com.example.projects.websocket;

import com.example.projects.dto.CodeEditMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CodeEditController {

    @MessageMapping("/edit") // client sends to /app/edit
    @SendTo("/topic/updates") // server broadcasts to /topic/updates
    public CodeEditMessage broadcastChange(CodeEditMessage message) {
        System.out.println("Received edit: " + message);
        return message; // Automatically broadcast to all subscribers
    }
}
