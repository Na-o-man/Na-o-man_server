package com.umc.naoman.domain.photo.elasticsearch.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PhotoEsClientRepositoryTest {
    @Autowired
    private PhotoEsClientRepository photoEsClientRepository;
    @Test
    public void test() {
        List<Long> val =  photoEsClientRepository.deletePhotoEsByFaceTag(1001L);

    }
}