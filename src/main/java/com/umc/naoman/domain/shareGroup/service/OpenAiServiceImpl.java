package com.umc.naoman.domain.shareGroup.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OpenAiServiceImpl implements OpenAiService {

    private final OpenAiChatModel chatModel;

    public String generateGroupName(String place, List<String> meetingTypes) {
        String prompt = String.format("장소: %s, 모임 종류: %s \n위 키워드를 참고해 재미있는 그룹 이름을 공백 포함 10자~20자 사이로 하나만 지어줘. 큰따옴표는 제외하고 보여줘.",
                place, String.join(", ", meetingTypes));
        String response = chatModel.call(prompt);
        return removeQuotes(response);
    }

    private String removeQuotes(String response) {
        return response.replace("\"", ""); // 모든 큰따옴표 제거
    }
}