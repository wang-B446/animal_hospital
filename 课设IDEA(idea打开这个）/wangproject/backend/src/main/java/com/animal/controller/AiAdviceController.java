package com.animal.controller;

import com.animal.common.Result;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai-advice")
public class AiAdviceController {

    @Value("${deepseek.api-key:}")
    private String deepseekApiKey;

    @Value("${deepseek.api-url:https://api.deepseek.com/chat/completions}")
    private String deepseekApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public Result<String> getAdvice(@RequestBody Map<String, String> request) {
        String symptom = request.get("symptom");
        String animalType = request.get("animalType");
        String animalName = request.get("animalName");

        if (symptom == null || symptom.trim().isEmpty()) {
            return Result.error("请输入动物的症状信息");
        }

        try {
            String prompt = buildPrompt(animalName, animalType, symptom);
            String response = callDeepSeekApi(prompt);
            String advice = parseResponse(response);
            return Result.success(advice);
        } catch (Exception e) {
            return Result.error("AI建议获取失败：" + e.getMessage());
        }
    }

    private String buildPrompt(String animalName, String animalType, String symptom) {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一位专业的宠物医生AI助手。请根据以下动物的病情信息，提供专业的医疗建议。\n\n");
        sb.append("【动物信息】\n");
        if (animalName != null && !animalName.trim().isEmpty()) {
            sb.append("动物名称：").append(animalName).append("\n");
        }
        if (animalType != null && !animalType.trim().isEmpty()) {
            sb.append("动物种类：").append(animalType).append("\n");
        }
        sb.append("症状描述：").append(symptom).append("\n\n");
        sb.append("请从以下几个方面给出建议：\n");
        sb.append("1. 可能的病因分析\n");
        sb.append("2. 建议的治疗方案\n");
        sb.append("3. 护理注意事项\n");
        sb.append("4. 何时需要立即就医\n\n");
        sb.append("请用简洁专业的中文回答，字数控制在300字以内。");
        return sb.toString();
    }

    private String callDeepSeekApi(String prompt) throws Exception {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("model", "deepseek-chat");

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        messages.add(userMessage);
        bodyMap.put("messages", messages);

        bodyMap.put("max_tokens", 1000);
        bodyMap.put("temperature", 0.7);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + deepseekApiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(bodyMap, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                deepseekApiUrl,
                HttpMethod.POST,
                entity,
                String.class
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("DeepSeek API调用失败，状态码：" + response.getStatusCodeValue());
        }

        return response.getBody();
    }

    private String parseResponse(String responseBody) throws Exception {
        JsonNode root = objectMapper.readTree(responseBody);
        JsonNode choices = root.path("choices");
        if (choices.isArray() && choices.size() > 0) {
            return choices.get(0).path("message").path("content").asText("无法获取建议，请稍后重试");
        }
        return "无法获取建议，请稍后重试";
    }
}
