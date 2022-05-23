package kr.polymarket.domain.product.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import kr.polymarket.domain.product.document.Product;
import kr.polymarket.domain.product.dto.SearchWithPITResult;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.PointInTimeBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.functionScoreQuery;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductSearchCustomRepository implements ProductSearchRepository{

    private final RestHighLevelClient restHighLevelClient;
    private final ObjectMapper objectMapper;

    private static final String PIT_KEEP_ALIVE = "1m"; // pit 유지시간, 연장시간 기준, 1분

    /**
     * 상품 키워드로 elasticsearch에 검색하는 메소드
     * @param query
     * @param categoryId
     * @param page
     * @param pit
     * @return
     * @throws IOException
     */
    public SearchWithPITResult searchProductIdList(String query, Integer categoryId, int page, String pit) throws IOException {
        // score function filter: 오래된 문서일 수록 낮은 점수를 주도록 가중치 부여
        List<FunctionScoreQueryBuilder.FilterFunctionBuilder> filterFunctionBuilderList = List.of(
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        QueryBuilders.rangeQuery("create_date").gte("now-1d").lte("now"),
                        ScoreFunctionBuilders.weightFactorFunction(1.2f)
                ),
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        QueryBuilders.rangeQuery("create_date").gte("now-10d").lte("now-1d"),
                        ScoreFunctionBuilders.weightFactorFunction(1)
                ),
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        QueryBuilders.rangeQuery("create_date").gte("now-180d").lte("now-10d"),
                        ScoreFunctionBuilders.weightFactorFunction(0.8f)
                ),
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        QueryBuilders.rangeQuery("create_date").gte("now-1y").lte("now-180d"),
                        ScoreFunctionBuilders.weightFactorFunction(0.5f)
                ),
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        QueryBuilders.rangeQuery("create_date").lt("now-1y"),
                        ScoreFunctionBuilders.weightFactorFunction(0.2f)
                )
        );

        // 검색 쿼리 생성 및 검색요청
        BoolQueryBuilder boolQueryBuilder = boolQuery()
                .should(QueryBuilders.multiMatchQuery(query).field("product_title",2).field("product_content",1))
                .should(QueryBuilders.matchPhraseQuery("product_title", query).slop(1));
        if(categoryId != null) {
            boolQueryBuilder.filter(QueryBuilders.matchQuery("category_id", categoryId));
        }

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder
                .fetchSource(new String[]{"id"}, new String[]{})
                .query(
                    functionScoreQuery(
                            boolQueryBuilder,
                            filterFunctionBuilderList.toArray(new FunctionScoreQueryBuilder.FilterFunctionBuilder[0])
                    )
                    .boostMode(CombineFunction.MULTIPLY)
                )
                .from(page * PAGE_SIZE)
                .size(PAGE_SIZE);
        if(pit == null) {
            pit = getPointInTime();
        }
        final PointInTimeBuilder pointInTimeBuilder = new PointInTimeBuilder(pit);
        pointInTimeBuilder.setKeepAlive(PIT_KEEP_ALIVE);
        searchSourceBuilder.pointInTimeBuilder(pointInTimeBuilder);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        // 검색결과에서 상품 아이디 추출
        List<Long> retrievedProductIdList =  Arrays.stream(searchResponse.getHits().getHits())
                .filter(h -> h.getScore() > 0.0f)
                .map(SearchHit::getSourceAsString)
                .map(s -> new Gson().fromJson(s, Product.class).getId())
                .collect(Collectors.toList());

        return SearchWithPITResult.builder()
                .pit(pit)
                .retrievedProductIdList(retrievedProductIdList)
                .build();

    }

    /**
     * PIT 값을 얻어오는 메소드
     * @return
     * @throws IOException
     */
    public String getPointInTime() throws IOException {
        Request request = new Request("POST",INDEX_NAME + "/_pit");
        request.addParameter("keep_alive", PIT_KEEP_ALIVE);
        Response response = restHighLevelClient.getLowLevelClient().performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        PointInTimeResponse pointInTimeResponse = objectMapper.readValue(responseBody, PointInTimeResponse.class);
        return pointInTimeResponse.getId();
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    static class PointInTimeResponse {
        private String id;
    }
}
