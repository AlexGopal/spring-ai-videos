package com.fdmgroup.spring_ai_basic_chat_Service.DocumentInjectionQnAadv.Service;

import org.springframework.stereotype.Service;

import com.fdmgroup.spring_ai_basic_chat_Service.DocumentInjectionQnAadv.Agents.AgentType;
import com.fdmgroup.spring_ai_basic_chat_Service.DocumentInjectionQnAadv.Agents.HrAgent;
import com.fdmgroup.spring_ai_basic_chat_Service.DocumentInjectionQnAadv.Agents.ItAgent;
import com.fdmgroup.spring_ai_basic_chat_Service.DocumentInjectionQnAadv.Agents.PolicyAgent;
import com.fdmgroup.spring_ai_basic_chat_Service.DocumentInjectionQnAadv.Agents.RouterAgent;

@Service
public class CompanyCopilot {
    private final HrAgent hrAgent;
    private final ItAgent itAgent;
    private final PolicyAgent policyAgent;
    private final RouterAgent routerAgent;

    public CompanyCopilot(HrAgent hrAgent, ItAgent itAgent, PolicyAgent policyAgent, RouterAgent routerAgent) {
        this.hrAgent = hrAgent;
        this.itAgent = itAgent;
        this.policyAgent = policyAgent;
        this.routerAgent = routerAgent;
    }
    
    public String ask(String conversationId, String question) {
        AgentType agentType = routerAgent.route(question);

        if(agentType == AgentType.HR) {
            return hrAgent.answer(question, conversationId);
        }
        if(agentType == AgentType.IT) {
            return itAgent.answer(question, conversationId);
        }
        if(agentType == AgentType.POLICY) {
            return policyAgent.answer(question, conversationId);
        }

        throw new IllegalStateException("Unexpected agent type" + agentType);
    }
    
}
