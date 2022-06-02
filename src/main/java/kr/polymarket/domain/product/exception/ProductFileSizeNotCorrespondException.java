package kr.polymarket.domain.product.exception;

import kr.polymarket.global.error.BusinessException;
import kr.polymarket.global.error.ErrorCode;

public class ProductFileSizeNotCorrespondException extends BusinessException {

    public ProductFileSizeNotCorrespondException() {
        super(ErrorCode.PRODUCTFILE_SIZE_NOT_CORRESPOND);
    }

    public ProductFileSizeNotCorrespondException(String message, ErrorCode errorCode) {
        super(message, ErrorCode.PRODUCTFILE_SIZE_NOT_CORRESPOND);
    }
}
