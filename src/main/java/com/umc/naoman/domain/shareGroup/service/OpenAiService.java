package com.umc.naoman.domain.shareGroup.service;

import java.util.List;

public interface OpenAiService {
    String generateGroupName(String place, List<String> meetingTypes);
}
