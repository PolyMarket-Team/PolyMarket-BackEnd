package kr.polymarket.domain.product.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SearchWithPITResult {

    private String pit;
    private List<Long> retrievedProductIdList;
}
