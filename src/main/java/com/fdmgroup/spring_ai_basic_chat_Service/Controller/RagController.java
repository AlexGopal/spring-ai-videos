// package com.fdmgroup.spring_ai_basic_chat_Service.Controller;

// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import com.fdmgroup.spring_ai_basic_chat_Service.Service.RagService;

// @RestController
// @RequestMapping("/rag")
// public class RagController {
//     private final RagService ragService;

//     public RagController(RagService ragService) {
//         this.ragService = ragService;
//     }

//     @GetMapping("/ask")
//     public String ask(@RequestParam String question) {
//         return ragService.ask(question);
//     }

    
// }
