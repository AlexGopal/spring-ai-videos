package com.fdmgroup.spring_ai_basic_chat_service.DocumentInjectionQnAadv.Tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class ItTools {
    
    @Tool(description = "Reset user password")
    public String resetPassword(String email) {
        return "Password reset email send to" + email;
    }

    @Tool(description = "Check VPN status")
    public String vpnStatus() {
        return "VPN service is healthy";
    }
}
