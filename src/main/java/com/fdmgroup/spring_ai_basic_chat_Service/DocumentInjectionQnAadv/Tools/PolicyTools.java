package com.fdmgroup.spring_ai_basic_chat_service.DocumentInjectionQnAadv.Tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component 
public class PolicyTools {
    @Tool(description = "Get latest company policy version")
    public String latestPolicyVersion() {
        return "Remote work policy v3.2";
    }
}
