package com.umc.naoman.domain.photo.service;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.naoman.global.error.BusinessException;
import com.umc.naoman.global.error.code.AwsLambdaErrorCode;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FaceDetectionServiceImpl implements FaceDetectionService{
    @Value("${spring.lambda.function.detect_face}")
    private String detectFaceDummyLambda;
    private final AWSLambda awsLambda;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Getter
    @Setter
    @AllArgsConstructor
    private class PayLoad{
        private Body body;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private class Body{
        private List<String> nameList;
        private List<Long> memberIdList;
        private Long shareGroupId;
    }

    @Override
    @Async
    public void detectFace(List<String> nameList, Long shareGroupId) {
        List<Long> memberIdList = null; //TODO: shareGroupId로 memberIdList 조회하는 로직 추가
        PayLoad payLoad = new PayLoad(new Body(nameList,memberIdList,shareGroupId));
        String lambdaPayload = null;

        try {
            lambdaPayload = objectMapper.writeValueAsString(payLoad);
        } catch (JsonProcessingException e) {
            throw new BusinessException(AwsLambdaErrorCode.AWS_JsonProcessing_Exception,e);
        }
        InvokeRequest invokeRequest = new InvokeRequest()
                .withFunctionName(detectFaceDummyLambda)
                .withPayload(lambdaPayload);

        awsLambda.invoke(invokeRequest);
    }
}
