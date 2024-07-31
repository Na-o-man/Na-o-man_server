package com.umc.naoman.domain.photo.elasticsearch.repository;

import com.umc.naoman.domain.photo.elasticsearch.document.SamplePhotoVector;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SamplePhotoVectorRepository extends ElasticsearchRepository<SamplePhotoVector,String> {
}
