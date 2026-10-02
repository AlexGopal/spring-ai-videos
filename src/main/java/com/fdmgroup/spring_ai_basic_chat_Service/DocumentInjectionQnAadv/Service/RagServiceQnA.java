package com.fdmgroup.spring_ai_basic_chat_Service.DocumentInjectionQnAadv.Service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class RagServiceQnA {
    private final ChatClient chatClient;

    public RagServiceQnA(ChatClient chatClient) {
        super();
        this.chatClient = chatClient;
    }

    public String ask(String question) {
        // .user(question) adds ONLY the original user's question.
        // When .call() executes, QuestionAnswerAdvisor:
        // 1. Uses the original question to search the VectorStore.
        // 2. Gets back the relevant chunks.
        // 3. Adds those chunks as context to create the augmented prompt.
        // 4. Sends that augmented prompt (question + chunks) to the LLM.
        // So: original question -> retrieve chunks -> augmented prompt -> LLM
        return chatClient.prompt()
            .user(question)
            .call()
            .content();
    }

    
}
