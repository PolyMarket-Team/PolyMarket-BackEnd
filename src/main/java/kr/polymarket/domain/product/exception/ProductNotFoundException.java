package kr.polymarket.domain.product.exception;

import kr.polymarket.global.error.BusinessException;
import kr.polymarket.global.error.ErrorCode;

public class ProductNotFoundException extends BusinessException {

    public ProductNotFoundException() {
        super(ErrorCode.PRODUCT_NOT_FOUND);
    }

    public ProductNotFoundException(String message, ErrorCode errorCode) {
        super(message, ErrorCode.PRODUCT_NOT_FOUND);
    }
}
