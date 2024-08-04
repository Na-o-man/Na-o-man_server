package com.umc.naoman.domain.photo.elasticsearch.repository;

import com.umc.naoman.domain.photo.elasticsearch.document.SampleFaceVector;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleFaceVectorRepository extends ElasticsearchRepository<SampleFaceVector,String> {
}
