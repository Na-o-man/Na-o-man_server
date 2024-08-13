package com.umc.naoman.domain.photo.elasticsearch.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.umc.naoman.domain.photo.elasticsearch.document.PhotoEs;
import com.umc.naoman.domain.photo.entity.Photo;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PhotoEsClientRepository {
    private final ElasticsearchClient elasticsearchClient;
    private final FaceVectorClientRepository faceVectorClientRepository;

    //사진 업로드 시 ES에 벌크로 업로드
    public void savePhotoBulk(List<Photo> photoList) {
        List<PhotoEs> photoEsList = photoList.stream()
                .map(photo -> PhotoEs.builder()
                        .rdsId(photo.getId())
                        .shareGroupId(photo.getShareGroup().getId())
                        .faceTag(new ArrayList<>())
                        .downloadTag(new ArrayList<>())
                        .url(photo.getUrl())
                        .name(photo.getName())
                        .createdAt(esTimeFormat(photo.getCreatedAt()))
                        .build())
                .toList();
        BulkRequest.Builder bulkBuilder = new BulkRequest.Builder();
        for (PhotoEs photoEs : photoEsList) {
            bulkBuilder.operations(op -> op
                    .index(idx -> idx
                            .index("photos_es")
                            .routing(photoEs.getShareGroupId().toString())
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

    //특정 공유 그룹의 모든 사진 검색
    public Page<PhotoEs> findPhotoEsByShareGroupId(Long shareGroupId, Pageable pageable) {
        SearchResponse<PhotoEs> response = null;

        pageable.getPageNumber();
        try {
            response = elasticsearchClient.search(s -> s
                            .index("photos_es")
                            .routing(shareGroupId.toString())
                            .from(getFrom(pageable))
                            .size(pageable.getPageSize())
                            .sort(sort -> sort
                                    .field(f -> f
                                            .field("createdAt")
                                            .order(SortOrder.Desc)))
                            .query(q -> q
                                    .term(t -> t
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

    //특정 공유 그룹의 얼굴이 태그된 사진 검색
    public Page<PhotoEs> findPhotoEsByShareGroupIdAndFaceTag(Long shareGroupId, Long faceTag, Pageable pageable) {
        SearchResponse<PhotoEs> response = null;
        try {
            response = elasticsearchClient.search(s -> s
                            .index("photos_es")
                            .routing(shareGroupId.toString())
                            .from(getFrom(pageable))
                            .size(pageable.getPageSize())
                            .sort(sort -> sort
                                    .field(f -> f
                                            .field("createdAt")
                                            .order(SortOrder.Desc)))
                            .query(q -> q
                                    .bool(b -> b
                                            .must(m -> m
                                                    .term(t -> t
                                                            .field("shareGroupId")
                                                            .value(shareGroupId)
                                                    )
                                            )
                                            .must(m -> m
                                                    .term(t -> t
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

    //특정 공유 그룹의 얼굴이 태그되지 않은 사진 검색
    public Page<PhotoEs> findPhotoEsByShareGroupIdAndNotFaceTag(Long shareGroupId, Pageable pageable) {
        SearchResponse<PhotoEs> response = null;
        try {
            response = elasticsearchClient.search(s -> s
                            .index("photos_es")
                            .routing(shareGroupId.toString())
                            .from(getFrom(pageable))
                            .size(pageable.getPageSize())
                            .sort(sort -> sort
                                    .field(f -> f
                                            .field("createdAt")
                                            .order(SortOrder.Desc)))
                            .query(q -> q
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
        } catch (IOException e) {
            throw new BusinessException(ElasticsearchErrorCode.ELASTICSEARCH_IOEXCEPTION, e);
        }

        return toPagePhotoEs(response.hits().hits(), pageable);
    }

    // rdsId로 ES에서 사진 삭제
    public void deletePhotoEsByRdsId(List<Long> rdsIdList, Long shareGroupId) {
        List<FieldValue> fieldValueList = rdsIdList.stream()
                .map(FieldValue::of)
                .toList();
        try {
            elasticsearchClient.deleteByQuery(d -> d
                    .index("photos_es")
                    .routing(shareGroupId.toString())
                    .query(q -> q
                            .terms(t -> t
                                    .field("rdsId")
                                    .terms(te -> te.value(fieldValueList))
                            )
                    )
            );
        } catch (IOException e) {
            throw new BusinessException(ElasticsearchErrorCode.ELASTICSEARCH_IOEXCEPTION, e);
        }
    }

    //특정 회원의 얼굴이 태그된 사진 삭제 -> 해당 사진에서 감지된 얼굴벡터도 함께 삭제  return : 삭제된 사진의 rdsId
    public List<Long> deletePhotoEsByFaceTag(Long memberId) {
        SearchResponse<PhotoEs> response = null;
        List<Long> rdsIdList = new ArrayList<>();
        List<String> photoNameList = new ArrayList<>();
        try {
            response = elasticsearchClient.search(s -> s
                            .index("photos_es")
                            .from(0)
                            .size(5000)
                            .query(q -> q
                                    .term(t -> t
                                            .field("faceTag")
                                            .value(FieldValue.of(memberId))
                                    )
                            ),
                    PhotoEs.class);
            elasticsearchClient.deleteByQuery(d -> d
                    .index("photos_es")
                    .query(q -> q
                            .term(t -> t
                                    .field("faceTag")
                                    .value(FieldValue.of(memberId))
                            )
                    )
            );
            rdsIdList = response.hits().hits().stream()
                    .map(hit -> hit.source().getRdsId())
                    .toList();
            photoNameList = response.hits().hits().stream()
                    .map(hit -> hit.source().getName())
                    .toList();
        } catch (IOException e) {
            throw new BusinessException(ElasticsearchErrorCode.ELASTICSEARCH_IOEXCEPTION, e);
        }
        //삭제된 사진에서 감지된 얼굴 벡터 삭제
        faceVectorClientRepository.deleteFaceVectorsByPhotoName(photoNameList);
        return rdsIdList;
    }

    //특정 공유 그룹의 사진 삭제 -> 해당 사진에서 감지된 얼굴벡터도 함께 샥제 return : 삭제된 사진의 rdsId
    public List<Long> deletePhotoEsByShareGroupIdList(List<Long> shareGroupIdList) {
        List<FieldValue> fieldValueShareGroupList = shareGroupIdList.stream()
                .map(FieldValue::of)
                .toList();
        SearchResponse<PhotoEs> response = null;
        List<Long> rdsIdList = new ArrayList<>();
        List<String> photoNameList = new ArrayList<>();
        try {
            response = elasticsearchClient.search(s -> s
                            .index("photos_es")
                            .from(0)
                            .size(5000)
                            .query(q -> q
                                    .terms(t -> t
                                            .field("shareGroupId")
                                            .terms(te -> te.value(fieldValueShareGroupList))
                                    )
                            ),
                    PhotoEs.class);
            elasticsearchClient.deleteByQuery(d -> d
                    .index("photos_es")
                    .query(q -> q
                            .terms(t -> t
                                    .field("shareGroupId")
                                    .terms(te -> te.value(fieldValueShareGroupList))
                            )
                    )
            );
            rdsIdList = response.hits().hits().stream()
                    .map(hit -> hit.source().getRdsId())
                    .toList();
            photoNameList = response.hits().hits().stream()
                    .map(hit -> hit.source().getName())
                    .toList();
        } catch (IOException e) {
            throw new BusinessException(ElasticsearchErrorCode.ELASTICSEARCH_IOEXCEPTION, e);
        }
        //삭제된 사진에서 감지된 얼굴 벡터 삭제
        faceVectorClientRepository.deleteFaceVectorsByPhotoName(photoNameList);
        return rdsIdList;
    }

    //특정 사진에 특정 맴버 다운로드 태그 추가
    public void addDownloadTag(List<PhotoEs> photoEs, Long memberId){
        List<FieldValue> fieldValueList = photoEs.stream()
                .map(photo -> FieldValue.of(photo.getName()))
                .toList();
        String routing = photoEs.get(0).getShareGroupId().toString();
        Map<String, JsonData> params = new HashMap<>();
        params.put("memberId", JsonData.of(memberId));
        try {
            elasticsearchClient.updateByQuery(u -> u
                    .index("photos_es")
                    .routing(routing)
                    .query(q -> q
                            .terms(t -> t
                                    .field("name")
                                    .terms(te -> te.value(fieldValueList))
                            )
                    )
                    .script(s -> s
                            .inline(i->i
                                    .source("if (!ctx._source.downloadTag.contains(params.memberId)) { " +
                                            "ctx._source.downloadTag.add(params.memberId); }")
                                    .lang("painless")
                                    .params(params)
                            )

                    )
            );
        } catch (IOException e) {
            throw new BusinessException(ElasticsearchErrorCode.ELASTICSEARCH_IOEXCEPTION, e);
        }
    }

    String esTimeFormat(LocalDateTime localDateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(dateTimeFormatter);
    }

    private Page<PhotoEs> toPagePhotoEs(List<Hit<PhotoEs>> hits, Pageable pageable) {
        List<PhotoEs> photoEsList = hits.stream().map(Hit::source).collect(Collectors.toList());
        return new PageImpl<>(photoEsList, pageable, hits.size());
    }

    private int getFrom(Pageable pageable) {
        return pageable.getPageNumber() * pageable.getPageSize();
    }
}
