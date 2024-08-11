package com.umc.naoman.domain.photo.elasticsearch.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import com.umc.naoman.global.error.BusinessException;
import com.umc.naoman.global.error.code.ElasticsearchErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FaceVectorClientRepository {
    private final ElasticsearchClient elasticsearchClient;

    //photoName으로 face_vectors 삭제
    public void deleteFaceVectorsByPhotoName(List<String> photoNameList){
        List<FieldValue> fieldValuePhotoNameList = photoNameList.stream()
                .map(FieldValue::of)
                .toList();
        try {
            elasticsearchClient.deleteByQuery(delete -> delete
                    .index("face_vectors")
                    .query(q -> q
                            .terms(t -> t
                                    .field("name")
                                    .terms(te -> te.value(fieldValuePhotoNameList))
                            )
                    )
            );
        } catch (IOException e){
            throw new BusinessException(ElasticsearchErrorCode.ELASTICSEARCH_IOEXCEPTION, e);
        }
    }
}
