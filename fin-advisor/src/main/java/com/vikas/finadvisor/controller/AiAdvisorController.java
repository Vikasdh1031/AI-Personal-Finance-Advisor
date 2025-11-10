package com.vikas.finadvisor.controller;

import com.vikas.finadvisor.service.AiAdvisorService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*") // Allow requests from any frontend
public class AiAdvisorController {

    private final AiAdvisorService aiService;

    // ✅ Explicit constructor (fixes IntelliJ red line)
    public AiAdvisorController(AiAdvisorService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/advice")
    public Map<String, String> getAdvice(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        String reply = aiService.generateAdvice(question);
        return Map.of("reply", reply);
    }

    @GetMapping("/test")
    public Map<String, String> test() {
        return Map.of("status", "AI endpoint active ✅");
    }
}
