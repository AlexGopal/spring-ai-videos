// package com.fdmgroup.spring_ai_basic_chat_Service.Configuration;

// import java.beans.BeanProperty;
// import java.util.List;

// import org.springframework.ai.document.Document;
// import org.springframework.ai.vectorstore.VectorStore;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// @Configuration
// public class KnowledgeBaseLoader {
//     @Bean
//     /*
//     9:49 CommandLineRunner loadKnowledgeBase(VectorStore vectorStore) basically creates a 
//     commandlinerunner bean, this bean will run automatically after our 
//     springapplication starts successfully, 
//     spring also injects a vectorstore bean into this method
//     so this code executes at application start up and then list document 
//     creates 3 spring ai document objects, each document is a piece of knowledge 
//     that will be stored in the vector database vectorstore.add(docs) 
//     will convert the text into embeddings using the configured embedding model, 
//     those embeddings are stored in the configured vector database here 
//     our database is simple vector store, the original text is stored along 
//     with the vectors
//      */
//     CommandLineRunner loadKnowledgeBase(VectorStore vectorStore) {
//         return args -> {
//             List<Document> docs = List.of(
//                 new Document("""
//                     Skills Lab is Microsoft's learning organization.
//                     """),

//                 new Document("""
//                     Nikita Chitre is a Skills Lab Coach.
//                     """),

//                 new Document("""
//                     Spring AI integrates LLMs with Spring applications.
//                     """)
//             );

//             vectorStore.add(docs);

//             System.out.println("Knowledge Base Loaded");
//         };
//     }
// }
