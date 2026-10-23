package com.fdmgroup.spring_ai_basic_chat_Service.DocumentInjectionQnAadv.Service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class RagServiceQnA {
    private final ChatClient chatClient;

    public RagServiceQnA(ChatClient chatClient) {
        super();
        this.chatClient = chatClient;
    }

    public String ask(String conversationId, String question) {
        // .user(question) adds ONLY the original user's question.
        // When .call() executes, QuestionAnswerAdvisor:
        // 1. Uses the original question to search the VectorStore.
        // 2. Gets back the relevant chunks.
        // 3. Adds those chunks as context to create the augmented prompt.
        // 4. Sends that augmented prompt (question + chunks) to the LLM.
        // So: original question -> retrieve chunks -> augmented prompt -> LLM
        return chatClient.prompt()
            .user(question)
            // v4 8:42 passes run time parameters to the 
            // configured advisor
            // ChatMemory.CONVERSATION_ID key is used by chat memory to identify
            // a specific conversation,
            // ChatMemory.CONVERSATION_ID is the key which spring ai defines for us
            // conversationId is the value from the user
            // we're basically telling spring ai to use this conversation id when
            // executing the advisor for this specific request
            .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId))
            // .call() will send the prompt with memory and retrieved context to llm
            // and executes it as well
            .call()
            .content();
    }

    
}
