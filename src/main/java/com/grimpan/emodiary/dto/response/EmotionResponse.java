package com.grimpan.emodiary.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmotionResponse {
    private String userName;
    private List<Integer> emotionScore;
    private List<Double> emotionDistribution;
}
