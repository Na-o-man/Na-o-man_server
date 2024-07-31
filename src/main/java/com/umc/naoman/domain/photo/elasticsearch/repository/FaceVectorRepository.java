package com.umc.naoman.domain.photo.elasticsearch.repository;

import com.umc.naoman.domain.photo.elasticsearch.document.FaceVector;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaceVectorRepository extends ElasticsearchRepository<FaceVector,String> {
}
