package com.adeo.connector.opus.service.impl;

import com.adeo.connector.opus.*;
import com.adeo.connector.opus.gateway.ContentSet;
import com.adeo.connector.opus.gateway.OpusResponse;
import com.adeo.connector.opus.gateway.Segment;
import com.adeo.connector.opus.service.OpusGatewayService;
import com.adobe.connector.ConnectorResponse;
import com.adobe.connector.services.OrchestratorService;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by stievena on 28/10/16.
 */
@Component(immediate = true, label = "OPUS Gateway Service", description = "Expose the OPUS services")
@Service(OpusGatewayService.class)
public class OpusGatewayServiceImpl implements OpusGatewayService {

    private static final ContentSet EMPTY_CONTENT_SET = new ContentSet();
    private static final int MAX_PAGE_SIZE_OPUS = 500;

    @Reference
    private OrchestratorService orchestratorService;

    private static String createFilterQuery(Collection<String[]> segmentIds) {
        if (!segmentIds.isEmpty()) {
            return "inContentSet:" + segmentIds.stream()
                    .map(s -> Stream.of(s).collect(Collectors.joining(" OR ", "(", ")")))
                    .filter(s -> !"()".equals(s))
                    .collect(Collectors.joining(" AND "));
        }
        return "";
    }

    private static String createFacetsQuery(String[] segmentIds) {
        return Stream.of(segmentIds)
                .collect(Collectors.joining("&facet.contentSet="));

    }

    private static String createCollectStoresQuery(List<String> storeCodes) {
        return storeCodes.stream()
                .map(c -> c + "_Entity_pointOfSale")
                .collect(Collectors.joining(" OR "));
    }

    private static String createCollectProductsQuery(String... productCodes) {
        return Arrays.stream(productCodes)
                .map(s -> s + "_PimStd_Product")
                .collect(Collectors.joining(" "));
    }

    @Override
    public <T> T getProduct(String productId, String context, Class modelClass) {
        ProductRequest request = new ProductRequest(modelClass, productId);
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> T getProduct(String productId, List<String> masks, String context, Class modelClass) {
        String masksString = masks.stream().collect(Collectors.joining(","));
        ProductWithMasksRequest request = new ProductWithMasksRequest(modelClass, productId, masksString);
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> T getFamily(String familyId, Class modelClass) {
        FamilyRequest request = new FamilyRequest(modelClass, familyId);
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> List<T> getProducts(List<String> productIds, Class modelClass) {
        String query = productIds.stream().collect(Collectors.joining(" OR "));
        ProductListRequest request = new ProductListRequest(modelClass, query);
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0).getResults();
    }

    @Override
    public <T> List<T> getComparison(Class modelClass, String... productCodes) {
        final ComparisonRequest request = new ComparisonRequest(modelClass, createCollectProductsQuery(productCodes));
        final ConnectorResponse<ContentSet<T>> response = orchestratorService.execute(request);
        ContentSet<T> contentSet = response != null ? response.getResults().get(0) : EMPTY_CONTENT_SET;
        return contentSet.getResults();
    }

    @Override
    public <T> List<T> getShoppingListDetails(Class modelClass, String... productCodes) {
        final ShoppingListRequest request = new ShoppingListRequest(modelClass, createCollectProductsQuery(productCodes));
        final ConnectorResponse<ContentSet<T>> response = orchestratorService.execute(request);
        ContentSet<T> contentSet = response != null ? response.getResults().get(0) : EMPTY_CONTENT_SET;
        return contentSet.getResults();
    }

    @Override
    public <T> List<T> getSegments(String familyId, Class modelClass) {
        SegmentationRequest request = new SegmentationRequest(modelClass, familyId);
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response != null ? response.getResults() : Collections.emptyList();
    }

    public <T> ContentSet<T> getStoresContent(@Nonnull String familyId,
                                              @Nonnull List<String[]> storeSegmentIds,
                                              @Nonnull String[] allStoreSegmentIds,
                                              @Nonnull List<String> existingStoreIds, Class modelClass) {
        String defaultFilter = createFilterQuery(storeSegmentIds);
        String defaultFacets = createFacetsQuery(allStoreSegmentIds);
        String query = createCollectStoresQuery(existingStoreIds);
        final StoreListRequest request = new StoreListRequest(modelClass, familyId, defaultFilter, defaultFacets, query,
                String.valueOf(0), String.valueOf(MAX_PAGE_SIZE_OPUS));
        final ConnectorResponse<ContentSet<T>> response = orchestratorService.execute(request);
        return response != null ? response.getResults().get(0) : EMPTY_CONTENT_SET;
    }

    @Override
    public <T> T getStore(String storeId, Class modelClass) {
        StoreRequest request = new StoreRequest(modelClass, storeId);
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> ContentSet<T> findEditorials(String keyword, String modelCode, Class modelClass) {
        EditorialSearchRequest request = new EditorialSearchRequest(modelClass, keyword, modelCode);
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> ContentSet<T> findProducts(String keyword, String context, Class modelClass, int startFrom, int pageSize) {
        ProductSearchRequest request = new ProductSearchRequest(modelClass, keyword, context);
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> ContentSet<T> findServices(String keyword, Class modelClass) {
        ProductSearchRequest request = new ProductSearchRequest(modelClass, keyword);
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> ContentSet<T> getProductsContent(@Nonnull String familyId, String context,
                                                @Nonnull List<String[]> productSegmentIds,
                                                @Nonnull String[] allProductSegmentIds,
                                                int startFrom, int pageSize, String sortAttribute,
                                                boolean ascSorting, Class modelClass) {

        String defaultFilter = createFilterQuery(productSegmentIds);
        String defaultFacets = createFacetsQuery(allProductSegmentIds);
        String defaultSort = "";
        if (!StringUtils.isEmpty(sortAttribute)) {
            defaultSort = new StringBuilder("@(").append(sortAttribute).append(")%20").append(ascSorting ? "asc" : "desc").toString();
        }
        FamilyProductsRequest request = new FamilyProductsRequest(modelClass, familyId, defaultFilter, defaultFacets, defaultSort,
                String.valueOf(pageSize), String.valueOf(startFrom));
        final ConnectorResponse<ContentSet<T>> response = orchestratorService.execute(request);
        return response != null ? response.getResults().get(0) : EMPTY_CONTENT_SET;
    }
}
