package com.adeo.connector.opus.service.impl;

import com.adeo.connector.opus.*;
import com.adeo.connector.opus.exception.OpusException;
import com.adeo.connector.opus.gateway.ContentSet;
import com.adeo.connector.opus.gateway.OpusResponse;
import com.adeo.connector.opus.gateway.Segment;
import com.adeo.connector.opus.gateway.SortingSet;
import com.adeo.connector.opus.model.OpusObject;
import com.adeo.connector.opus.models.Attribute;
import com.adeo.connector.opus.service.OpusGatewayService;
import com.adobe.connector.services.OrchestratorService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    private Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    @Reference
    private OrchestratorService orchestratorService;

    @Override
    public <T> T getProduct(String productId, String context, Class<T> modelClass) {
        ProductRequest request = new ProductRequest(modelClass, productId);
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> T getProduct(String productId, List<String> masks, String context, Class<T> modelClass) {
        String masksString = masks.stream().collect(Collectors.joining(","));
        ProductWithMasksRequest request = new ProductWithMasksRequest(modelClass, productId, masksString);
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> T getFamily(String familyId, Class<T> modelClass) {
        FamilyRequest request = new FamilyRequest(modelClass, familyId);
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> List<T> getProducts(List<String> productIds, Class<T> modelClass) {
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
    public <T> List<T> getSegments(String familyId, Class<T> modelClass) {
        SegmentationRequest request = new SegmentationRequest(modelClass, familyId);
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults();
    }

    @Override
    public <T> ContentSet<T> getStores(Class<T> modelClass) {
        StoreListRequest request = new StoreListRequest(modelClass);
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> ContentSet<T> getStores(String region, Class<T> modelClass) {
        RegionalStoreListRequest request = new RegionalStoreListRequest(modelClass, region);
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> T getStore(String storeId, Class<T> modelClass) {
        StoreRequest request = new StoreRequest(modelClass, storeId);
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> ContentSet<T> findEditorials(String keyword, String modelCode, Class<T> modelClass) {
        EditorialSearchRequest request = new EditorialSearchRequest(modelClass, keyword, modelCode);
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> ContentSet<T> findProducts(String keyword, String context, Class<T> modelClass) {
        ProductSearchRequest request = new ProductSearchRequest(modelClass, keyword, context);
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> ContentSet<T> findServices(String keyword, Class<T> modelClass) {
        ProductSearchRequest request = new ProductSearchRequest(modelClass, keyword);
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> ContentSet<T> getRegions(String startFrom, String pageSize, Class<T> modelClass) {
        final RegionsRequest regionsRequest = new RegionsRequest(modelClass, startFrom, pageSize);
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
    public void createFaq(OpusObject body) throws OpusException {
        PostFaqRequest request = new PostFaqRequest(null);
        request.setBody(gson.toJson(body));
        OpusResponse response = (OpusResponse) orchestratorService.execute(request);
        if (response.getStatus() >= 400) {
            throw new OpusException(response.getMessage());
        }
    }

    @Override
    public void updateFaq(String faqId, OpusObject body) throws OpusException {
        PutFaqRequest request = new PutFaqRequest(null, faqId);
        request.setBody(gson.toJson(body));
        OpusResponse response = (OpusResponse) orchestratorService.execute(request);
        if (response.getStatus() >= 400) {
            throw new OpusException(response.getMessage());
        }
    }

    @Override
    public void deleteFaq(String faqId) throws OpusException {
        DeleteFaqRequest request = new DeleteFaqRequest(null, faqId);
        OpusResponse response = (OpusResponse) orchestratorService.execute(request);
        if (response.getStatus() >= 400) {
            throw new OpusException(response.getMessage());
        }
    }

    @Override
    public <T> T getFaq(String faqId, Class<T> modelClass) {
        GetFaqRequest request = new GetFaqRequest(modelClass, faqId);
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public void createService(OpusObject body) throws OpusException {
        PostServiceRequest request = new PostServiceRequest(null);
        request.setBody(gson.toJson(body));
        OpusResponse response = (OpusResponse) orchestratorService.execute(request);
        if (response.getStatus() >= 400) {
            throw new OpusException(response.getMessage());
        }
    }

    @Override
    public void updateService(String serviceId, OpusObject body) throws OpusException {
        PutServiceRequest request = new PutServiceRequest(null, serviceId);
        request.setBody(gson.toJson(body));
        OpusResponse response = (OpusResponse) orchestratorService.execute(request);
        if (response.getStatus() >= 400) {
            throw new OpusException(response.getMessage());
        }
    }

    @Override
    public void deleteService(String serviceId) throws OpusException {
        DeleteServiceRequest request = new DeleteServiceRequest(null, serviceId);
        OpusResponse response = (OpusResponse) orchestratorService.execute(request);
        if (response.getStatus() >= 400) {
            throw new OpusException(response.getMessage());
        }
    }

    @Override
    public <T> T getService(String serviceId, Class<T> modelClass) {
        GetServiceRequest request = new GetServiceRequest(modelClass, serviceId);
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public void createHowTo(OpusObject body) throws OpusException {
        PostHowToRequest request = new PostHowToRequest(null);
        request.setBody(gson.toJson(body));
        OpusResponse response = (OpusResponse) orchestratorService.execute(request);
        if (response.getStatus() >= 400) {
            throw new OpusException(response.getMessage());
        }
    }

    @Override
    public void updateHowTo(String serviceId, OpusObject body) throws OpusException {
        PutHowToRequest request = new PutHowToRequest(null, serviceId);
        request.setBody(gson.toJson(body));
        OpusResponse response = (OpusResponse) orchestratorService.execute(request);
        if (response.getStatus() >= 400) {
            throw new OpusException(response.getMessage());
        }
    }

    @Override
    public void deleteHowTo(String serviceId) throws OpusException {
        DeleteHowToRequest request = new DeleteHowToRequest(null, serviceId);
        OpusResponse response = (OpusResponse) orchestratorService.execute(request);
        if (response.getStatus() >= 400) {
            throw new OpusException(response.getMessage());
        }
    }

    @Override
    public <T> T getHowTo(String serviceId, Class<T> modelClass) {
        GetHowToRequest request = new GetHowToRequest(modelClass, serviceId);
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public void createEditorialMedia(OpusObject body) throws OpusException {
        PostEditorialMediaRequest request = new PostEditorialMediaRequest(null);
        request.setBody(gson.toJson(body));
        OpusResponse response = (OpusResponse) orchestratorService.execute(request);
        if (response.getStatus() >= 400) {
            throw new OpusException(response.getMessage());
        }
    }

    @Override
    public void updateEditorialMedia(String editorialMediaId, OpusObject body) throws OpusException {
        PutEditorialMediaRequest request = new PutEditorialMediaRequest(null, editorialMediaId);
        request.setBody(gson.toJson(body));
        OpusResponse response = (OpusResponse) orchestratorService.execute(request);
        if (response.getStatus() >= 400) {
            throw new OpusException(response.getMessage());
        }
    }

 @Override
    public <T> ContentSet<T> getProductCountsByBrand(List<String> brandNames, Class modelClass) {
        String brandNamesString = brandNames.stream().collect(Collectors.joining("%20OR%20"));
        final ProductCountsBrandRequest request = new ProductCountsBrandRequest(modelClass, brandNamesString);
        final OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response.getResults().get(0);
    }

    @Override
    public <T> ContentSet<T> getProductsByBrand(String startFrom, String pageSize, List<String> brandNames, Class modelClass){
        String brandNamesString = brandNames.stream().collect(Collectors.joining("%2C"));
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
    
    @Override
    public SortingSet getSortings(String familyId, Class modelClass) {
    	SortingListRequest request = new SortingListRequest(modelClass, familyId);
    	OpusResponse<SortingSet> response = (OpusResponse) orchestratorService.execute(request);
    	return response.getResults().get(0);
    }
    
    @Override
    public <T> T getSeries(String seriesId, Class<T> modelClass) {
    	SeriesRequest request = new SeriesRequest(modelClass, seriesId);
    	OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
    	return response.getResults().get(0);
    }
    
    @Override
    public <T> ContentSet<T> getProductsInSeries(String seriesId, String pageSize, String startFrom, Class<T> modelClass) {
    	ProductsInSeriesRequest request = new ProductsInSeriesRequest(modelClass, seriesId, pageSize, startFrom);
    	OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
    	return response.getResults().get(0);
    }

}
