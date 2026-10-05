package com.fdmgroup.spring_ai_basic_chat_Service.DocumentInjectionQnAadv.Configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

    @Bean
    public ChatClient chatClient(
            ChatClient.Builder builder,
            VectorStore vectorStore,
            ChatMemory chatMemory) {
                //chatMemory stores the conversation history
                // to maintain chatcontext

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
                //this creates an advisor that injects chat history into prompts
                MessageChatMemoryAdvisor.builder(chatMemory)
                    .build(),
                QuestionAnswerAdvisor.builder(vectorStore)
                    .searchRequest(
                        SearchRequest.builder()
                        .topK(5)
                        .similarityThreshold(0.5)
                        .build()
                        // v4 .searchrequest is optional but it used to control how
                        // vector search behaves, so how many documents to retrieve
                        // and how relevent they must be
                        // the threshhold is the minimum amount so it can return minimum
                        // or greater
                        // 6:16 after doing the configuration we need a
                        // conversation id,
                        /*
                        conversation id will uniquely identify a chat session, 
                        ensures memories are separated between users, so user 
                        a gets memory a, user b gets memory b but everyone 
                        shares the same knowledge base
                        */
                        )
                    .build() // Builds the QuestionAnswerAdvisor
            )
            .build(); // Builds the final ChatClient
    }
}