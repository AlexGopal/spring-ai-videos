package com.fdmgroup.spring_ai_basic_chat_Service.Configuration;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
7:00 an embeddingmodel bean is injected into the configuration class 
and an embeddingmodel converts text into numerical vectors and 
these vectors are stored in simplevector store and are used 
during similarity search
*/
@Configuration
public class VectorStoreConfiguration {

    @Bean
    VectorStore vectorStore(EmbeddingModel embeddingModel) {
        return SimpleVectorStore.builder(embeddingModel)
        .build();
    }
}
