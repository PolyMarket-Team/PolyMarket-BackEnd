package kr.polymarket.domain.product.exception;

import kr.polymarket.global.error.BusinessException;
import kr.polymarket.global.error.ErrorCode;

public class SearchNotFoundException extends BusinessException {

    public SearchNotFoundException() {
        super(ErrorCode.SEARCH_NOT_FOUND);
    }

    public SearchNotFoundException(String message, ErrorCode errorCode) {
        super(message, ErrorCode.SEARCH_NOT_FOUND);
    }
}
