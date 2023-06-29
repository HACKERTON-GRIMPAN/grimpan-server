package com.grimpan.drawingdiary.controller;

import com.grimpan.drawingdiary.unit.DiaryToImageUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryToImageUnit gptUnit;

    @GetMapping("/test")
    public List<String> getasdf() {
        String test = "일기 - 2023년 6월 29일" +
                "오늘은 아름다운 여름날씨에 감사하며 하루를 시작했습니다. 창문을 열고 상쾌한 바람이 들어오면서 싱그러운 느낌이 전해졌어요. 오늘은 나른한 주말 아침을 보내기로 마음먹었기 때문에 부지런히 일어나지 않았습니다." +
                "아침 식사 후에는 햇빛이 내리쬐는 정원에서 산책을 나섰어요. 꽃들이 피어 있는 모습이 화려하게 펼쳐져 있었고, 작은 나무들도 싱그러운 녹색으로 가득 차 있었습니다. 신선한 공기와 새들의 지저귐 속에서 나는 일상의 스트레스를 잠시 잊을 수 있었습니다." +
                "오후에는 좋아하는 미술관에 방문했습니다. 이번 주제로 열린 르누아르의 전시회를 즐겼는데, 작품 하나하나가 화려하고 감각적인 느낌을 주었습니다. 르누아르의 그림에서는 아름다운 여름 풍경과 사람들의 모습이 살아 숨쉬듯 그려져 있었어요. 그 작품들을 감상하면서 나는 자유와 창의력의 중요성을 느낄 수 있었습니다." +
                "저녁에는 친구들과 함께 맛있는 음식을 먹으며 소소한 대화를 나눴어요. 서로의 이야기를 듣고 웃음 소리가 끊이지 않았습니다. 친구들과 함께하는 시간은 항상 나에게 큰 힘이 되어줍니다. 이런 소중한 순간들을 함께 나눌 수 있다는 것에 감사함을 느낍니다." +
                "하루가 저물어가는 저녁이 되었습니다. 이렇게 행복하고 평온한 하루를 보내고 있는 지금, 일기를 작성하며 내 마음을 정리하고자 합니다. 오늘은 정말로 특별하고 아름다운 하루였습니다. 이 순간들을 기억에 담아두고, 내일도 새로운 일상을 즐기기로 다짐합니다.";

        String firstTokens = gptUnit.getFirstTokens(test);
        String secondTokens = gptUnit.getSecondTokens(firstTokens);
        return gptUnit.getImgNameList(secondTokens);
    }
}
