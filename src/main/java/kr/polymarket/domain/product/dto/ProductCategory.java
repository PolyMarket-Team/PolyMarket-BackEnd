package kr.polymarket.domain.product.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductCategory {
    USED_CAR("C01", "중고차", 1),
    DIGITAL_DEVICE("C01", "디지털기기", 2),
    HOME_APPLIANCES("C02", "생활가전", 3),
    FURNITURE_INTERIOR("C03", "가구/인테리어", 4),
    INFANT_CHILD("C04", "유아동", 5),
    LIFE_PROCESSED_FOOD("C05", "생활가전", 6),
    CHILDREN_BOOKS("C06", "유아도서", 7),
    SPORTS_LEISURE("C07", "스포츠/레저", 8),
    WOMEN_ACCESSORIES("C08", "여성잡화", 9),
    WOMEN_CLOTHING("C09", "여성의류", 10),
    MENS_FASHION_ACCESSORIES("C10", "남성패션/잡화", 11),
    GAME_HOBBY("C11", "게임/취미", 12),
    BEAUTY("C12", "뷰티/미용", 13),
    PET_SUPPLIES("C13", "반려동물용품", 14),
    BOOK_TICKET_ALBUM("C14", "도서/티켓/음반", 15),
    PLANT("C15", "식물", 16),
    ETC("C16", "기타중고물품", 17)
    ;

    private final String code;
    private final String value;
    private final Integer id;

}