package com.umc.naoman.domain.photo.service;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FaceDetectionServiceImpl implements FaceDetectionService{
    @Value("${spring.lambda.function.name}")
    private String lambdaFunctionName;
    private final AWSLambda awsLambda;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Getter
    @Setter
    @AllArgsConstructor
    private class Body{
        private String name;
        private Long shareGroupId;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    private class PayLoad{
        private Body body;
    }

    @Override
    public void detectFaces(List<String> nameList, Long shareGroupId) {
        nameList.forEach(name->invokeLambda(name,shareGroupId));
    }

    private void invokeLambda(String name, Long shareGroupId) {
        InvokeRequest invokeRequest = createInvokeRequest(name, shareGroupId);
        awsLambda.invoke(invokeRequest);
    }

    private InvokeRequest createInvokeRequest(String name, Long shareGroupId) {
        PayLoad payLoad = new PayLoad(new Body(name,shareGroupId));
        String lambdaPayload = null;
        try {
            lambdaPayload = objectMapper.writeValueAsString(payLoad);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to create lambda payload");
        }
        return new InvokeRequest()
                .withFunctionName(lambdaFunctionName)
                .withPayload(lambdaPayload);
    }

}
