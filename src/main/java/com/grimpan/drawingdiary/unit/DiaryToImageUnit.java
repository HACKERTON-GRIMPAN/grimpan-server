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
public class DiaryToImageUnit {
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

    public String getFirstTokens(String content) {
        headers.clear();
        headers.add("Content-type","application/json; charset=utf-8");
        headers.add("Authorization", "Bearer "+ GPT_KEY);

        body.clear();
        body.put("model", "gpt-3.5-turbo");
        List<ChatGPTQuery> messages = new ArrayList<>();
        messages.add(ChatGPTQuery.builder()
                .role("system")
                .content("아래의 내용을 기반으로 생성 이미지 인공지능에게 넘겨줄거야. 그림을 그리는데 필요해 보이는 내용만 남기고 나머지는 다 지워줘. 영어로 바꿔주고 컴마(,)로 키워드만 띄워줘. 예를 틀면 팀빌딩, 연수원 건물, 책상, 의자, 많은 사람들, 피곤 이런 느낌으로 해줘. 영어로. 아래의 내용을 기반으로 생성 이미지 인공지능을 통해 그림을 그릴거야. 그에 맞게 프롬프트를 수정해줘.").build());
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
    return choices.get(0).getAsJsonObject().get("message").getAsJsonObject().get("content").getAsString();
    }

    public String getSecondTokens(String firstTokens) {
        headers.clear();
        headers.add("Content-type","application/json; charset=utf-8");
        headers.add("Authorization", "Bearer "+ GPT_KEY);

        body.clear();
        body.put("model", "gpt-3.5-turbo");
        List<ChatGPTQuery> messages = new ArrayList<>();
        messages.add(ChatGPTQuery.builder()
                .role("system")
                .content("You are an assistant who is good at creating prompts for image creation.").build());
        messages.add(ChatGPTQuery.builder()
                .role("assistant")
                .content(firstTokens).build());
        messages.add(ChatGPTQuery.builder()
                .role("user")
                .content("condense up to 4 outward descriptions to focus on nouns and adjectives separated by commas, ensuring that the total length does not exceed 200 characters.").build());

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
                + ", beautiful, illustrative style, colorful, diary-like concept";
    }

    public List<String> getImgNameList(String secondTokens) {
        headers.clear();
        headers.add("Content-type","application/json");

        body.clear();
        body.put("tokens", secondTokens);
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
}
