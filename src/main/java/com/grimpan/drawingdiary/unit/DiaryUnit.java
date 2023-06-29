package com.grimpan.drawingdiary.unit;

import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiaryUnit {
    @Value("${gpt.url}")
    private String GPT_URL;
    @Value("${gpt.key}")
    private String GPT_KEY;
    @Value("${karlo.url}")
    private String KARLO_URL;
    @Value("${karlo.key}")
    private String KARLO_KEY;

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final HttpHeaders headers = new HttpHeaders();
    private static final JSONObject body = new JSONObject();

    public String getTokenByDiary(String content) {
        headers.clear();
        headers.add("Content-type","application/json; charset=utf-8");
        headers.add("Authorization", "Bearer "+ GPT_KEY);

        body.clear();
        body.put("model", "gpt-3.5-turbo");
        List<ChatGPTQuery> messages = new ArrayList<>();
        messages.add(ChatGPTQuery.builder()
                .role("system")
                .content("Please adjust the AI that generates images to make it draw pictures well by making expressions, such as adjectives, more dramatic. Leave only the content necessary for drawing in the text and discard the rest.").build());
        messages.add(ChatGPTQuery.builder()
                .role("user")
                .content(content.replace("\n", " ")).build());
        messages.add(ChatGPTQuery.builder()
                .role("user")
                .content("Please adjust the AI that generates images to make it draw pictures well by making expressions, such as adjectives, more dramatic. Leave only the content necessary for drawing in the text and discard the rest.").build());
        messages.add(ChatGPTQuery.builder()
                .role("user")
                .content("condense up to 4 outward descriptions to focus on nouns and adjectives separated by commas, ensuring that the total length does not exceed 150 characters.").build());

        body.put("messages", messages);
        HttpEntity<?> request = new HttpEntity<String>(body.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                GPT_URL,
                HttpMethod.POST,
                request,
                String.class
        );

        JsonArray choices = (JsonArray) JsonParser.parseString(Objects.requireNonNull(response.getBody())).getAsJsonObject().get("choices");
        return choices.get(0).getAsJsonObject().get("message").getAsJsonObject().get("content").getAsString()
                + ", beautiful, Hand-drawn, Pastel, Warm, Colored pencils, Soft, by Oscar-Claude Monet";
    }

    public List<String> getImgNameList(String tokens) {
        headers.clear();
        headers.add("Content-type","application/json");

        body.clear();
        body.put("tokens", tokens);
        body.put("key", KARLO_KEY);

        HttpEntity<?> request = new HttpEntity<String>(body.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                KARLO_URL,
                HttpMethod.POST,
                request,
                String.class
        );

        JsonArray names = (JsonArray) JsonParser.parseString(Objects.requireNonNull(response.getBody())).getAsJsonObject().get("names");

        List<String> imgNames = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            imgNames.add(names.get(i).getAsJsonObject().get("name").getAsString());
        }

        return imgNames;
    }

    public Integer getEmotionScore(String content) {
        headers.clear();
        headers.add("Content-type","application/json; charset=utf-8");
        headers.add("Authorization", "Bearer "+ GPT_KEY);

        body.clear();
        body.put("model", "gpt-3.5-turbo");
        List<ChatGPTQuery> messages = new ArrayList<>();
        messages.add(ChatGPTQuery.builder()
                .role("system")
                .content("너는 감정을 분석해주는 역할이야. 주어진 일기 내용을 기반으로 어떤 긍정적이고 좋은 감정을 보이는지 평가해줘. 너무 심하게 부정적이고 우울하면 0, 5, 엄청 긍적적이면 10 쪽으로 출력해줘. 즉 감정을 0,1,2,3,4,5,6,7,8,9,10으로 0~10사이 정수로 출력해줘. 글은 필요 없고 숫자만 출력해줘.").build());
        messages.add(ChatGPTQuery.builder()
                .role("user")
                .content(content.replace("\n", " ")).build());

        body.put("messages", messages);
        HttpEntity<?> request = new HttpEntity<String>(body.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                GPT_URL,
                HttpMethod.POST,
                request,
                String.class
        );

        JsonArray choices = (JsonArray) JsonParser.parseString(Objects.requireNonNull(response.getBody())).getAsJsonObject().get("choices");
        return choices.get(0).getAsJsonObject().get("message").getAsJsonObject().get("content").getAsInt();
    }
}
