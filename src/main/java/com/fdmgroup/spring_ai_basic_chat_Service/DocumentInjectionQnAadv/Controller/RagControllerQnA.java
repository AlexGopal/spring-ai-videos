package com.fdmgroup.spring_ai_basic_chat_Service.DocumentInjectionQnAadv.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fdmgroup.spring_ai_basic_chat_Service.DocumentInjectionQnAadv.Service.RagServiceQnA;


@RestController
@RequestMapping("/api/ragQnA")
public class RagControllerQnA {

    private final RagServiceQnA ragServiceQnA;

    public RagControllerQnA(RagServiceQnA ragServiceQnA) {
        super();
        this.ragServiceQnA = ragServiceQnA;
    }

    @GetMapping
    public String ask(@RequestParam String question) {
        return ragServiceQnA.ask(question);
    }


    
}
