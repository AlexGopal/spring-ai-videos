// package com.fdmgroup.spring_ai_basic_chat_Service.Controller;

// import com.fdmgroup.spring_ai_basic_chat_Service.Service.ChatService;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// @RestController
// @RequestMapping("/api")
// public class ChatController {

//     private final ChatService chatService;

//     public ChatController(ChatService chatService) {
//         this.chatService = chatService;
//     }

//     @GetMapping("/ask")
//     public String ask(@RequestParam("message") String message) {
//         return chatService.ask(message);
//     }
// }
