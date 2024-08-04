package com.umc.naoman.domain.photo.elasticsearch.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.umc.naoman.domain.photo.elasticsearch.document.PhotoEs;
import com.umc.naoman.global.error.BusinessException;
import com.umc.naoman.global.error.code.ElasticsearchErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PhotoEsClientRepository {
    private final ElasticsearchClient elasticsearchClient;

    public void savePhotoBulk(List<String> url, List<String> nameList, Long shareGroupId) {
        List<PhotoEs> photoEsList = new ArrayList<>();
        for(int i=0; i<url.size(); i++){
            PhotoEs photoEs = PhotoEs.builder()
                    .shareGroupId(shareGroupId)
                    .url(url.get(i))
                    .name(nameList.get(i))
                    .createdAt(esTimeFormat(LocalDateTime.now()))
                    .build();
            photoEsList.add(photoEs);
        }
        BulkRequest.Builder bulkBuilder = new BulkRequest.Builder();
        for(PhotoEs photoEs :photoEsList){
            bulkBuilder.operations(op ->op
                    .index(idx -> idx
                            .index("photos_es")
                            .routing(shareGroupId.toString())
                            .document(photoEs)
                    )
            );
        }
        try {
            BulkResponse result = elasticsearchClient.bulk(bulkBuilder.build());
        } catch (IOException e) {
            throw new BusinessException(ElasticsearchErrorCode.ELASTICSEARCH_IOEXCEPTION, e);
        }
    }

    public Page<PhotoEs> findPhotoEsByShareGroupId(Long shareGroupId, Pageable pageable) {
        SearchResponse<PhotoEs> response = null;

        pageable.getPageNumber();
        try{
            response = elasticsearchClient.search(s->s
                            .index("photos_es")
                            .routing(shareGroupId.toString())
                            .from(getFrom(pageable))
                            .size(pageable.getPageSize())
                            .sort(sort -> sort
                                        .field(f -> f
                                                .field("createdAt")))
                            .query(q->q
                                    .term(t->t
                                            .field("shareGroupId")
                                            .value(shareGroupId)
                                    )
                            ),
                    PhotoEs.class
            );
        } catch (IOException e) {
            throw new BusinessException(ElasticsearchErrorCode.ELASTICSEARCH_IOEXCEPTION, e);
        }
        return toPagePhotoEs(response.hits().hits(), pageable);
    }

    public Page<PhotoEs> findPhotoEsByShareGroupIdAndFaceTag(Long shareGroupId,Long faceTag, Pageable pageable) throws IOException{
        SearchResponse<PhotoEs> response = null;
        try{
            response = elasticsearchClient.search(s->s
                            .index("photos_es")
                            .routing(shareGroupId.toString())
                            .from(getFrom(pageable))
                            .size(pageable.getPageSize())
                            .sort(sort -> sort
                                    .field(f -> f
                                            .field("createdAt")))
                            .query(q->q
                                    .bool(b->b
                                            .must(m->m
                                                    .term(t->t
                                                            .field("shareGroupId")
                                                            .value(shareGroupId)
                                                    )
                                            )
                                            .must(m->m
                                                    .term(t->t
                                                            .field("faceTag")
                                                            .value(faceTag)
                                                    )
                                            )
                                    )
                            ),
                    PhotoEs.class
            );
        } catch (IOException e) {
            throw new BusinessException(ElasticsearchErrorCode.ELASTICSEARCH_IOEXCEPTION, e);
        }

        return toPagePhotoEs(response.hits().hits(), pageable);
    }

    public Page<PhotoEs> findPhotoEsByShareGroupIdAndNotFaceTag(Long shareGroupId, Pageable pageable){
        SearchResponse<PhotoEs> response = null;
        try{
            response = elasticsearchClient.search(s->s
                            .index("photos_es")
                            .routing(shareGroupId.toString())
                            .from(getFrom(pageable))
                            .size(pageable.getPageSize())
                            .sort(sort -> sort
                                    .field(f -> f
                                            .field("createdAt")))
                            .query(q->q
                                    .bool(b -> b
                                            .must(m -> m
                                                    .term(t -> t
                                                            .field("shareGroupId")
                                                            .value(shareGroupId)
                                                    )
                                            )
                                            .mustNot(mn -> mn
                                                    .exists(e -> e
                                                            .field("faceTag")
                                                    )
                                            )
                                    )
                            ),
                    PhotoEs.class
            );
        }catch (IOException e){
            throw new BusinessException(ElasticsearchErrorCode.ELASTICSEARCH_IOEXCEPTION, e);
        }

        return toPagePhotoEs(response.hits().hits(), pageable);
    }

    String esTimeFormat(LocalDateTime localDateTime){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(dateTimeFormatter);
    }

    private Page<PhotoEs> toPagePhotoEs(List<Hit<PhotoEs>> hits, Pageable pageable){
        List<PhotoEs> photoEsList = new ArrayList<>();
        hits.forEach(hit->{
            photoEsList.add(hit.source());
        });
        return new PageImpl<>(photoEsList, pageable, hits.size());
    }

    private int getFrom(Pageable pageable){
        return pageable.getPageNumber() * pageable.getPageSize();
    }
}
