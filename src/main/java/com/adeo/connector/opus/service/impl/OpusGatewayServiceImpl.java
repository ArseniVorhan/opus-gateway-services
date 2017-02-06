package com.adeo.connector.opus.service.impl;

import com.adeo.connector.opus.*;
import com.adeo.connector.opus.gateway.ContentSet;
import com.adeo.connector.opus.gateway.OpusResponse;
import com.adeo.connector.opus.gateway.Segment;
import com.adeo.connector.opus.models.Attribute;
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
    public <T> ContentSet<T> getProducts(String familyId, String context, int startFrom, int pageSize, List<Segment[]> segments,
                                         List<Attribute> attributes, String sortAttribute, boolean ascSorting, Class modelClass) {
        String defaultFacets = "";
        String defaultAttributes = "";
        String filterSegments = "";
        String filterAttributes = "";
        String defaultSort = "";

        if (segments != null) {
            StringBuilder contentSet = new StringBuilder("inContentSet:");
            contentSet.append(segments.stream()
                    .map(s -> Stream.of(s)
                            .filter(Segment::isEnabled)
                            .map(Segment::getId)
                            .collect(Collectors.joining(" OR ", "(", ")")))
                    .filter(s -> !s.equals("()"))
                    .collect(Collectors.joining(" AND ")));
            filterSegments = contentSet.toString();
            defaultFacets = segments.stream()
                    .flatMap(Stream::of)
                    .map(Segment::getId)
                    .collect(Collectors.joining("%2C"));
        }

        if (attributes != null){
            defaultAttributes = attributes.stream()
                    .map(attribute -> "@(" + attribute.getName() + ")")
                    .collect(Collectors.joining("&facet.attribute="));
        }

        if (attributes != null) {
            filterAttributes = attributes.stream()
                    .map(attribute -> "@(" + attribute.getName() + ")%3A" + Stream.of(attribute.getValues()).collect(Collectors.joining("%2C")))
                    .collect(Collectors.joining("&filter="));
        }

        String filter = Stream.of(filterAttributes, filterSegments)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.joining(" AND "));

        if (!StringUtils.isEmpty(sortAttribute)) {
            defaultSort = new StringBuilder("@(").append(sortAttribute).append(")%20").append(ascSorting ? "asc" : "desc").toString();
        }
        FamilyProductsRequest request = new FamilyProductsRequest(modelClass, familyId, defaultFacets, defaultAttributes,
                filter, defaultSort, Integer.toString(pageSize), Integer.toString(startFrom));
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> List<T> getSegments(String familyId, Class modelClass) {
        SegmentationRequest request = new SegmentationRequest(modelClass, familyId);
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults();
    }

    @Override
    public <T> ContentSet<T> getStores(Class modelClass) {
        StoreListRequest request = new StoreListRequest(modelClass);
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> ContentSet<T> getStores(String region, Class modelClass) {
        RegionalStoreListRequest request = new RegionalStoreListRequest(modelClass, region);
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
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
    public <T> ContentSet<T> findProducts(String keyword, String context, Class modelClass) {
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
    public <T> ContentSet<T> getRegions(String startFrom, String pageSize, Class modelClass) {
        final RegionsRequest regionsRequest = new RegionsRequest(modelClass ,startFrom, pageSize);
        final OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(regionsRequest);
        return response.getResults().get(0);
    }

    @Override
    public <T> T getRegion(String regionId, Class modelClass) {
        final RegionRequest regionsRequest = new RegionRequest(modelClass, regionId);
        final OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(regionsRequest);
        ContentSet<T> contentSet = response.getResults().get(0);
        return !contentSet.getResults().isEmpty() ? contentSet.getResults().get(0) : null;
    }

    @Override
    public <T> ContentSet<T> getProductsByBrand(String startFrom, String pageSize, String[] brandNames, Class modelClass){
        String brandNamesString = Stream.of(brandNames).collect(Collectors.joining("%2C"));
        final ProductSearchBrandRequest request = new ProductSearchBrandRequest(modelClass, startFrom, pageSize, brandNamesString);
        final OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> ContentSet<T> getFamilies(String startFrom, String pageSize, Class modelClass) {
        final FamiliesRequest familiesRequest = new FamiliesRequest(modelClass, startFrom, pageSize);
        final OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(familiesRequest);
        return response.getResults().get(0);
    }
}
