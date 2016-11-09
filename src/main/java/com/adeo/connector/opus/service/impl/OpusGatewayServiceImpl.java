package com.adeo.connector.opus.service.impl;

import com.adeo.connector.opus.*;
import com.adeo.connector.opus.gateway.ContentSet;
import com.adeo.connector.opus.gateway.OpusResponse;
import com.adeo.connector.opus.gateway.Segment;
import com.adeo.connector.opus.service.OpusGatewayService;
import com.adobe.connector.services.OrchestratorService;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by stievena on 28/10/16.
 */
@Component(immediate = true, label = "OPUS Gateway Service", description = "Expose the OPUS services")
@Service(OpusGatewayService.class)
public class OpusGatewayServiceImpl implements OpusGatewayService {

    @Reference
    private OrchestratorService orchestratorService;

    @Override
    public <T> T getProduct(String productId, String context) {
        ProductRequest request = new ProductRequest(productId);
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> List<T> getProducts(List<String> productIds) {
        String query = productIds.stream().collect(Collectors.joining(" OR "));
        ProductListRequest request = new ProductListRequest(query);
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0).getResults();
    }

    @Override
    public <T> ContentSet<T> getProducts(String familyId, String context, int pageSize, int startFrom, List<Segment[]> segments, String orderingAttribute, boolean ascOrdering) {
        String defaultFilter = "";
        String defaultFacets = "";
        if (segments != null) {
            StringBuilder contentSet = new StringBuilder("inContentSet:");
            contentSet.append(segments.stream().map(s -> Stream.of(s).filter(Segment::isEnabled).map(Segment::getId).collect(Collectors.joining(" OR ", "(", ")"))).filter(s -> !s.equals("()")).collect(Collectors.joining(" AND ")));
            defaultFilter = contentSet.toString();
            defaultFacets = segments.stream().flatMap(segmentList -> Stream.of(segmentList)).map(Segment::getId).collect(Collectors.joining("&facet.contentSet="));
        }
        String defaultSort = "";
        if (!StringUtils.isEmpty(orderingAttribute)) {
            defaultSort = new StringBuilder("@(").append(orderingAttribute).append(")%20").append(ascOrdering ? "asc" : "desc").toString();
        }
        FamilyProductsRequest request = new FamilyProductsRequest(familyId, defaultFilter, defaultFacets, defaultSort, Integer.toString(pageSize), Integer.toString(startFrom));
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> List<T> getSegments(String familyId) {
        SegmentationRequest request = new SegmentationRequest(familyId);
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults();
    }

    @Override
    public <T> ContentSet<T> getStores() {
        StoreListRequest request = new StoreListRequest();
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> ContentSet<T> getStores(String region) {
        RegionalStoreListRequest request = new RegionalStoreListRequest(region);
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> T getStore(String storeId) {
        ProductRequest request = new ProductRequest(storeId);
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> ContentSet<T> findEditorials(String keyword, String modelCode) {
        EditorialSearchRequest request = new EditorialSearchRequest(keyword, modelCode);
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> ContentSet<T> findProducts(String keyword, String context) {
        ProductSearchRequest request = new ProductSearchRequest(keyword, context);
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> ContentSet<T> findServices(String keyword) {
        ProductSearchRequest request = new ProductSearchRequest(keyword);
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }
}
