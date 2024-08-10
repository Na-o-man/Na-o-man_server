package com.umc.naoman.domain.photo.service;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.InvocationType;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.naoman.domain.shareGroup.service.ShareGroupService;
import com.umc.naoman.global.error.BusinessException;
import com.umc.naoman.global.error.code.AwsLambdaErrorCode;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FaceDetectionServiceImpl implements FaceDetectionService{
    @Value("${spring.lambda.function.detect_face_photo}")
    private String detectFacePhotoLambda;
    @Value("${spring.lambda.function.join_share_group}")
    private String detectFaceShareGroupLambda;
    private final AWSLambda awsLambda;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ShareGroupService shareGroupService;

    @Getter
    @AllArgsConstructor
    private class DetectFacePhotoPayload {
        private List<String> nameList;
        private List<Long> memberIdList;
        private Long shareGroupId;
    }

    @Getter
    @AllArgsConstructor
    private class DetectFaceShareGroupPayload {
        private Long memberId;
        private Long shareGroupId;
    }

    @Override
    public void detectFaceUploadPhoto(List<String> photoNameList, Long shareGroupId) {
        List<Long> memberIdList = shareGroupService.findProfileListByShareGroupId(shareGroupId).stream()
                .map(profile -> profile.getMember().getId())
                .collect(Collectors.toList());
        DetectFacePhotoPayload payLoad = new DetectFacePhotoPayload(photoNameList,memberIdList,shareGroupId);
        String lambdaPayload = null;

        try {
            lambdaPayload = objectMapper.writeValueAsString(payLoad);
        } catch (JsonProcessingException e) {
            throw new BusinessException(AwsLambdaErrorCode.AWS_JsonProcessing_Exception,e);
        }
        InvokeRequest invokeRequest = new InvokeRequest()
                .withInvocationType(InvocationType.Event)
                .withFunctionName(detectFacePhotoLambda)
                .withPayload(lambdaPayload);

        awsLambda.invoke(invokeRequest);
    }

    @Override
    public void detectFaceJoinShareGroup(Long memberId, Long shareGroupId) {
        DetectFaceShareGroupPayload payLoad = new DetectFaceShareGroupPayload(memberId,shareGroupId);
        String lambdaPayload = null;

        try {
            lambdaPayload = objectMapper.writeValueAsString(payLoad);
        } catch (JsonProcessingException e) {
            throw new BusinessException(AwsLambdaErrorCode.AWS_JsonProcessing_Exception,e);
        }
        InvokeRequest invokeRequest = new InvokeRequest()
                .withInvocationType(InvocationType.Event)
                .withFunctionName(detectFaceShareGroupLambda)
                .withPayload(lambdaPayload);

        awsLambda.invoke(invokeRequest);
    }


}
