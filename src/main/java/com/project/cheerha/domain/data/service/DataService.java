package com.project.cheerha.domain.data.service;

import com.project.cheerha.common.exception.BaseException;
import com.project.cheerha.common.exception.ErrorCode;
import com.project.cheerha.domain.data.entity.Data;
import com.project.cheerha.domain.data.repository.DataRepository;
import java.util.Base64;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataService {

    private final DataRepository dataRepository;

    public String urlFindById(Long id) {
        Data data = dataRepository.findById(id).orElseThrow();
        String url = data.getUrl();

        if (!url.startsWith("http")) {
            url = "https://" + url;
        }

        System.out.println("🔍 최종 리다이렉트 URL: " + url);
        data.upCnt(); //todo:아직 혹시모를 cnt 관련 예외처리 하나도 안 했음.
        dataRepository.save(data);
        return url;
    }
}
