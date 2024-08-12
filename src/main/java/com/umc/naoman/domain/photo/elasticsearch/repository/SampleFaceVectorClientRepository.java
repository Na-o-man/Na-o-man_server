package com.umc.naoman.domain.photo.elasticsearch.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import com.umc.naoman.global.error.BusinessException;
import com.umc.naoman.global.error.code.ElasticsearchErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
@RequiredArgsConstructor
public class SampleFaceVectorClientRepository {
    private final ElasticsearchClient elasticsearchClient;

    public void deleteSampleFaceVectorByMemberId(Long memberId) {
        try {
            elasticsearchClient.deleteByQuery(d -> d
                    .index("sample_face_vectors")
                    .query(q -> q
                            .term(t -> t
                                    .field("memberId")
                                    .value(FieldValue.of(memberId))
                            )
                    )
            );
        } catch (IOException e){
            throw new BusinessException(ElasticsearchErrorCode.ELASTICSEARCH_IOEXCEPTION, e);
        }
    }
}
