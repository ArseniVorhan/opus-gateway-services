package com.adeo.connector.opus.service.impl;

import com.adeo.connector.opus.*;
import com.adeo.connector.opus.gateway.ContentSet;
import com.adeo.connector.opus.gateway.OpusResponse;
import com.adeo.connector.opus.gateway.Ranking;
import com.adeo.connector.opus.service.OpusGatewayService;
import com.adeo.connector.opus.service.models.Context;
import com.adeo.connector.opus.service.models.FamilyAttribute;
import com.adeo.connector.opus.service.models.FamilySegment;
import com.adeo.connector.opus.service.models.FamilySort;
import com.adeo.connector.opus.utils.QueryBuilder;
import com.adobe.connector.services.OrchestratorService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by stievena on 28/10/16.
 */
@Component(immediate = true, label = "OPUS Gateway Service", description = "Expose the OPUS services")
@Service(OpusGatewayService.class)
public class OpusGatewayServiceImpl implements OpusGatewayService {

    private static final ContentSet EMPTY_CONTENT_SET = new ContentSet();

    private Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    @Reference
    private OrchestratorService orchestratorService;

    @Override
    public <T> T getProduct(String productId, List<Context> contexts, Class<T> modelClass) {
        ProductRequest request = new ProductRequest(modelClass, productId, QueryBuilder.buildContextsParam(contexts));
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0)
                : null;
    }

    @Override
    public <T> T getProduct(String productId, List<Context> contexts, List<String> masks, Class<T> modelClass) {
        String masksString = masks.stream().collect(Collectors.joining("%2C"));
        ProductWithMasksRequest request = new ProductWithMasksRequest(modelClass, productId, QueryBuilder.buildContextsParam(contexts), masksString);
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0)
                : null;
    }

    @Override
    public <T> T getFamily(String familyId, Class<T> modelClass) {
        FamilyRequest request = new FamilyRequest(modelClass, familyId);
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0)
                : null;
    }

    @Override
    public <T> List<T> getProducts(List<String> productIds, List<Context> contexts, Class<T> modelClass) {
        String query = productIds.stream().collect(Collectors.joining("%20OR%20"));
        ProductListRequest request = new ProductListRequest(modelClass, query, QueryBuilder.buildContextsParam(contexts));
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                && CollectionUtils.isNotEmpty(response.getResults().get(0).getResults())
                ? response.getResults().get(0).getResults()
                : Collections.emptyList();
    }

    @Override
    public <T> ContentSet<T> getProducts(String familyId, List<Context> context, int startFrom, int pageSize, List<FamilySegment[]> segments,
                                         List<FamilyAttribute> attributes, List<FamilySort> sorts, Class<T> modelClass) {
        FamilyProductsRequest request = new FamilyProductsRequest(modelClass, familyId, QueryBuilder.buildContextsParam(context), QueryBuilder.buildSegmentsFacetParam(segments), QueryBuilder.buildAttributesFacetParam(attributes),
                QueryBuilder.buildFilterParam(segments, attributes), QueryBuilder.buildSortsParam(sorts), Integer.toString(startFrom), Integer.toString(pageSize));
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0) : EMPTY_CONTENT_SET;
    }

    @Override
    public <T> List<T> getSegments(String familyId, Class<T> modelClass) {
        SegmentationRequest request = new SegmentationRequest(modelClass, familyId);
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && response.getResults() != null
                ? response.getResults()
                : Collections.emptyList();
    }

    @Override
    public <T> ContentSet<T> getStores(Class<T> modelClass) {
        StoreListRequest request = new StoreListRequest(modelClass);
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0) : EMPTY_CONTENT_SET;
    }

    @Override
    public <T> ContentSet<T> getStores(String region, Class<T> modelClass) {
        RegionalStoreListRequest request = new RegionalStoreListRequest(modelClass, region);
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0) : EMPTY_CONTENT_SET;
    }

    @Override
    public <T> T getStore(String storeId, Class<T> modelClass) {
        StoreRequest request = new StoreRequest(modelClass, storeId);
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0)
                : null;
    }

    @Override
    public <T> ContentSet<T> findEditorials(String keyword, String modelCode, Class<T> modelClass) {
        EditorialSearchRequest request = new EditorialSearchRequest(modelClass, keyword, modelCode);
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0) : EMPTY_CONTENT_SET;
    }

    @Override
    public <T> ContentSet<T> findProducts(String keyword, List<Context> contexts, int startFrom, int pageSize, Class<T> modelClass) {
        ProductSearchRequest request = new ProductSearchRequest(modelClass, keyword, QueryBuilder.buildContextsParam(contexts), String.valueOf(startFrom), String.valueOf(pageSize));
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0) : EMPTY_CONTENT_SET;
    }

    @Override
    public <T> ContentSet<T> findServices(String keyword, int startFrom, int pageSize, Class<T> modelClass) {
        ProductSearchRequest request = new ProductSearchRequest(modelClass, keyword, String.valueOf(startFrom), String.valueOf(pageSize));
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0) : EMPTY_CONTENT_SET;
    }

    @Override
    public <T> ContentSet<T> findFamily(String keyword, int startFrom, int pageSize, Class<T> modelClass) {
        FamilySearchResquest request = new FamilySearchResquest(modelClass, keyword, String.valueOf(startFrom), String.valueOf(pageSize));
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0) : EMPTY_CONTENT_SET;
    }

    @Override
    public <T> ContentSet<T> findSeries(String keyword, int startFrom, int pageSize, Class<T> modelClass) {
        SeriesSearchRequest request = new SeriesSearchRequest(modelClass, keyword, String.valueOf(startFrom), String.valueOf(pageSize));
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0) : EMPTY_CONTENT_SET;
    }

    @Override
    public <T> ContentSet<T> getRegions(String startFrom, String pageSize, Class<T> modelClass) {
        final RegionsRequest regionsRequest = new RegionsRequest(modelClass, startFrom, pageSize);
        final OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(regionsRequest);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0) : EMPTY_CONTENT_SET;
    }

    @Override
    public <T> T getRegion(String regionId, Class<T> modelClass) {
        final RegionRequest regionsRequest = new RegionRequest(modelClass, regionId);
        final OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(regionsRequest);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                && CollectionUtils.isNotEmpty(response.getResults().get(0).getResults())
                ? response.getResults().get(0).getResults().get(0)
                : null;
    }


    @Override
    public <T> T getFaq(String faqId, Class<T> modelClass) {
        GetFaqRequest request = new GetFaqRequest(modelClass, faqId);
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0)
                : null;
    }


    @Override
    public <T> T getService(String serviceId, Class<T> modelClass) {
        GetServiceRequest request = new GetServiceRequest(modelClass, serviceId);
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0)
                : null;
    }


    @Override
    public <T> T getHowTo(String serviceId, Class<T> modelClass) {
        GetHowToRequest request = new GetHowToRequest(modelClass, serviceId);
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0)
                : null;
    }

    @Override
    public <T> ContentSet<T> getProductCountsByBrand(List<String> brandNames, Class<T> modelClass) {
        String brandNamesString = brandNames.stream().collect(Collectors.joining("%20OR%20"));
        final ProductCountsBrandRequest request = new ProductCountsBrandRequest(modelClass, brandNamesString);
        final OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0) : EMPTY_CONTENT_SET;
    }

    @Override
    public <T> ContentSet<T> getProductsByBrand(String startFrom, String pageSize, List<String> brandNames, Class<T> modelClass) {
        String brandNamesString = brandNames.stream().collect(Collectors.joining("%2C"));
        final ProductSearchBrandRequest request = new ProductSearchBrandRequest(modelClass, startFrom, pageSize, brandNamesString);
        final OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0) : EMPTY_CONTENT_SET;
    }

    @Override
    public <T> ContentSet<T> getFamilies(String startFrom, String pageSize, Class<T> modelClass) {
        final FamiliesRequest familiesRequest = new FamiliesRequest(modelClass, startFrom, pageSize);
        final OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(familiesRequest);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0) : EMPTY_CONTENT_SET;
    }

    @Override
    public List<Ranking> getSortings(String familyId) {
        RankingListRequest request = new RankingListRequest(null, familyId);
        OpusResponse<Ranking> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && response.getResults() != null
                ? response.getResults()
                : Collections.emptyList();
    }

    @Override
    public <T> ContentSet<T> getSeries(String startFrom, String pageSize, Class<T> modelClass) {
        SeriesRequest request = new SeriesRequest(modelClass, startFrom, pageSize);
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0) : EMPTY_CONTENT_SET;
    }

    @Override
    public <T> T getSerie(String serieId, Class<T> modelClass) {
        SerieRequest request = new SerieRequest(modelClass, serieId);
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0)
                : null;
    }

    @Override
    public <T> ContentSet<T> getProductsInSeries(String seriesId, String startFrom, String pageSize, Class<T> modelClass) {
        ProductsInSeriesRequest request = new ProductsInSeriesRequest(modelClass, seriesId, startFrom, pageSize);
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0) : EMPTY_CONTENT_SET;
    }

    @Override
    public <T> ContentSet<T> getAllNetchandising(String startFrom, String pageSize, Class<T> modelClass) {
        AllNetchandisingRequest request = new AllNetchandisingRequest(modelClass, startFrom, pageSize);
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0) : EMPTY_CONTENT_SET;
    }

    @Override
    public <T> ContentSet<T> getNetchandisingContents(String netchandisingId, Class<T> modelClass) {
        NetchandisingContentsRequest request = new NetchandisingContentsRequest(modelClass, netchandisingId);
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0) : EMPTY_CONTENT_SET;
    }

    @Override
    public <T> ContentSet<T> getNetchandisingNodeContents(String netchandisingNodeId, Class<T> modelClass) {
        NetchandisingNodeContentsRequest request = new NetchandisingNodeContentsRequest(modelClass, netchandisingNodeId);
        OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0) : EMPTY_CONTENT_SET;
    }

    @Override
    public <T> T getNetchandisingNode(String netchandisingNodeId, Class<T> modelClass) {
        NetchandisingNodeRequest request = new NetchandisingNodeRequest(modelClass, netchandisingNodeId);
        OpusResponse<T> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0)
                : null;
    }

    
    @Override
    public <T> ContentSet<T> findFamily(String keyword, SearchFilterType filterType, Class<T> modelClass) {
    	FamilySearchResquest request = new FamilySearchResquest(modelClass, keyword, filterType.toString());
    	OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0) : EMPTY_CONTENT_SET;
    }
    
    @Override
    public <T> ContentSet<T> findSeries(String keyword, SearchFilterType filterType, Class<T> modelClass) {
    	SeriesSearchRequest request = new SeriesSearchRequest(modelClass, keyword, filterType.toString());
    	OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
        return response != null && CollectionUtils.isNotEmpty(response.getResults())
                ? response.getResults().get(0) : EMPTY_CONTENT_SET;
    }
    
    @Override
    public <T> ContentSet<T> getSearchSuggestions(String keyword, Class<T> modelClass) {
    	// Set default params
    	String fieldParam = "sf.productname";
    	String sizeParam = "1";
    	
    	OpusResponse<ContentSet<T>> response = executeSearchSuggestionsService(keyword, fieldParam, sizeParam, modelClass);
    	if(response.getResults().size() > 0) {
    		return response.getResults().get(0);
    	}
    	
    	//2nd attempt
    	fieldParam = "sf.productdesc";
    	response = executeSearchSuggestionsService(keyword, fieldParam, sizeParam, modelClass);
    	if(response.getResults().size() > 0) {
    		return response.getResults().get(0);
    	}
    	
    	//3rd attempt
    	fieldParam = "sf.brand";
    	response = executeSearchSuggestionsService(keyword, fieldParam, sizeParam, modelClass);
    	return response.getResults().get(0);
    }

	private <T> OpusResponse<ContentSet<T>> executeSearchSuggestionsService(String keyword, String fieldParam, String sizeParam,
			Class<T> modelClass) {
		SearchSuggetionRequest request = new SearchSuggetionRequest(modelClass, keyword, fieldParam, sizeParam);
    	OpusResponse<ContentSet<T>> response = (OpusResponse) orchestratorService.execute(request);
		return response;
	}

}
