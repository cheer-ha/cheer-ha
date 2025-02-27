package com.project.cheerha.common.exception.data;

import com.project.cheerha.common.exception.base.BaseException;

public class ElasticsearchQueryException extends BaseException {

    public ElasticsearchQueryException(DataErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode.getStatus());
    }
}
