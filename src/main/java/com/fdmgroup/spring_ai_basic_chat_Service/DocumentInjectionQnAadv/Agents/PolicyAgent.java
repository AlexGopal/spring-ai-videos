package com.fdmgroup.spring_ai_basic_chat_Service.DocumentInjectionQnAadv.Agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import com.fdmgroup.spring_ai_basic_chat_Service.DocumentInjectionQnAadv.Tools.PolicyTools;

@Service
public class PolicyAgent {
     private final ChatClient chatClient;

    public PolicyAgent(ChatClient.Builder builder, VectorStore policyVectorStore, ChatMemory chatMemory, PolicyTools policyTools) {
        this.chatClient = builder
                .defaultSystem(""" 
                        You are a company policy specialist.
                        
                        """)
                .defaultTools(policyTools)
                .defaultAdvisors(
                            MessageChatMemoryAdvisor
                            .builder(chatMemory)
                            .build(),
                            QuestionAnswerAdvisor.builder(policyVectorStore)
                            .build())
                .build();
    }

    public String answer(String question, String conversationId) {
        return chatClient.prompt()
                .user(question)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }

}
