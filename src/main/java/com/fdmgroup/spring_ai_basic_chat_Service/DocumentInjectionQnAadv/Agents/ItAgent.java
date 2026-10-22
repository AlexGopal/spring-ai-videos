package com.fdmgroup.spring_ai_basic_chat_service.DocumentInjectionQnAadv.Agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import com.fdmgroup.spring_ai_basic_chat_service.DocumentInjectionQnAadv.Tools.ItTools;

@Service
public class ItAgent {
    private final ChatClient chatClient;

    public ItAgent(ChatClient.Builder builder, VectorStore itVectorStore, ChatMemory chatMemory, ItTools itTools) {
        this.chatClient = builder
                .defaultSystem(""" 
                        You are an IT support specialist.
                        
                        """)
                .defaultTools(itTools)
                .defaultAdvisors(
                            MessageChatMemoryAdvisor
                            .builder(chatMemory)
                            .build(),
                            QuestionAnswerAdvisor.builder(itVectorStore)
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
