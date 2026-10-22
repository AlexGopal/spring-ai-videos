package com.fdmgroup.spring_ai_basic_chat_service.DocumentInjectionQnAadv.Tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.fdmgroup.spring_ai_basic_chat_service.DocumentInjectionQnAadv.Service.LeaveService;

@Component
public class HrTools {
    private final  LeaveService leaveService;

    public HrTools(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @Tool(description = "Get employee leave balance")
    public String getLeaveBalance(String employeeId) {
        return "Balance" + leaveService.getBalance(employeeId);
    }
    
}
