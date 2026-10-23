package com.fdmgroup.spring_ai_basic_chat_Service.DocumentInjectionQnAadv.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fdmgroup.spring_ai_basic_chat_Service.DocumentInjectionQnAadv.DTO.ChatRequest;
import com.fdmgroup.spring_ai_basic_chat_Service.DocumentInjectionQnAadv.Service.CompanyCopilot;

@RestController 
@RequestMapping("/api/chat")
public class CompanyChatController {
    private final CompanyCopilot companyCopilot;

    public CompanyChatController(CompanyCopilot companyCopilot) {
        this.companyCopilot = companyCopilot;
    }

    @PostMapping
    public String ask(@RequestBody ChatRequest request) {
        return companyCopilot.ask(request.conversationId(), request.question());
    }

}
