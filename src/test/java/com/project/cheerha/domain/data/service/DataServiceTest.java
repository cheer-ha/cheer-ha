package com.project.cheerha.domain.data.service;

import com.project.cheerha.domain.data.entity.Data;
import com.project.cheerha.domain.data.repository.DataRepository;
import com.project.cheerha.domain.keyword.repository.KeywordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@SpringBootTest
public class DataServiceTest {

    @Autowired
    private DataService dataService;

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private KeywordRepository keywordRepository;

    @Test
    public void testAddDummyData() {
        // 더미 데이터를 추가합니다.
        dataService.addDummyData();

        // 데이터베이스에서 Data 엔티티를 조회합니다.
        List<Data> dataList = dataRepository.findAll();

        // 데이터가 존재하는지 확인합니다.
        assertThat(dataList).isNotEmpty();

        // 최소 1개 이상의 데이터가 존재해야 함
        assertThat(dataList.size()).isGreaterThan(0);
    }
}