// package com.fdmgroup.spring_ai_basic_chat_Service.Service;

// import java.util.List;
// import java.util.stream.Collectors;

// import org.springframework.ai.chat.client.ChatClient;
// import org.springframework.ai.document.Document;
// import org.springframework.ai.vectorstore.VectorStore;
// import org.springframework.stereotype.Service;

// @Service
// public class RagService {

//     private final ChatClient chatClient;
//     private final VectorStore vectorStore;

//     public RagService(ChatClient.Builder builder, VectorStore vectorStore) {
//         super();
//         this.chatClient = builder.build();
//         this.vectorStore = vectorStore;
//     }

//     public String ask(String question)
//     {
//         /*
//         17:52 List<Documents> documents essentially searches the vectorStore 
//         for documents that are semantically similar to the user's question.
//         18:21String context = documents.stream() this line will create a 
//         stream from the list of retrieved documents then map extracts the 
//         text contained from each document and collect combines all the 
//         retrieved text into a single string separated by new lines.
//         18:47 return chatClient.prompt() starts building a prompt request 
//         with an ai model, .system is saying answer only using the provided 
//         context system prompt gives instructions to the model helps prevent 
//         hallucinations and forces the model to answer only from retrieved 
//         documents.
//         19:12 then we provide the context and the question, then .call 
//         will send our completed prompt to the llm, .content will extract 
//         and returns only the text responses from ai model.
//          */
//         List<Document> documents = vectorStore.similaritySearch(question);
//         String context = documents.stream().map(Document::getText).collect(Collectors.joining("\n"));
//         return chatClient.prompt()
//         .system("""
//                 Answer only using the provided context.
//                 If the answer is not found, say 
//                 'I don't know'
//                 """)
//         .user("""
//                 context:
//                 %s
//                 question:
//                 %s
//                 """.formatted(context,question))
//         .call()
//         .content();
        
//     }

    
// }
