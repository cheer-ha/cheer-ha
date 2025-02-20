package com.project.cheerha.domain.jobopening.service;

import com.project.cheerha.domain.jobopening.entity.JobOpening;
import com.project.cheerha.domain.keyword.entity.JobOpeningKeyword;
import com.project.cheerha.domain.keyword.entity.Keyword;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class JobOpeningKeywordFactory {

    private static final Random random = new Random();

    //jobOpeningGenerator 의 로직을 그대로 사용하므로, 상수를 static 으로 두지 않음
    public static List<JobOpeningKeyword> createRandomKeywordList(JobOpening jobOpening) {
        int keywordCount = 45;
        int numKeywords = random.nextInt(6) + 5;
        Set<Long> keywordSet = new HashSet<>();
        while (keywordSet.size() < numKeywords) {
            keywordSet.add((long) (random.nextInt(keywordCount) + 1));
        }

        return keywordSet.stream()
                .map(id -> JobOpeningKeyword.toEntity(jobOpening, Keyword.toEntity(id)))
                .toList();
    }
}
