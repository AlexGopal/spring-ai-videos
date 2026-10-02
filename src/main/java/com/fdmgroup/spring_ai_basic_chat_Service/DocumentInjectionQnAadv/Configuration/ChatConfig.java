package com.fdmgroup.spring_ai_basic_chat_Service.DocumentInjectionQnAadv.Configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

    @Bean
    public ChatClient chatClient(
            ChatClient.Builder builder,
            VectorStore vectorStore) {

        /*
         * defaultAdvisors:
         * - Advisors run automatically on every ChatClient request.
         * - You can configure multiple advisors.
         *
         * QuestionAnswerAdvisor:
         * - One type of Advisor specifically used for RAG.
         * - Searches the VectorStore for relevant document chunks.
         * - Adds those chunks as context before the prompt goes to the LLM.
         *
         * Flow:
         * Question -> QuestionAnswerAdvisor -> VectorStore
         *          -> relevant chunks -> LLM -> answer
         */
        // for more info go up one level to spring_ai_modules and go to 
        // spring-ai-question-answer-advisor-notes.md
        return builder
            .defaultAdvisors(
                QuestionAnswerAdvisor.builder(vectorStore)
                    .build() // Builds the QuestionAnswerAdvisor
            )
            .build(); // Builds the final ChatClient
    }
}