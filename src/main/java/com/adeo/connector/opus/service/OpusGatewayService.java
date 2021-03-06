package com.adeo.connector.opus.service;


import com.adeo.connector.opus.gateway.ContentSet;
import com.adeo.connector.opus.gateway.Ranking;
import com.adeo.connector.opus.service.models.Context;
import com.adeo.connector.opus.service.models.FamilyAttribute;
import com.adeo.connector.opus.service.models.FamilySegment;
import com.adeo.connector.opus.service.models.FamilySort;

import java.util.List;

/**
 * Business class to use the OPUS gateway services
 */
public interface OpusGatewayService {

    /**
     * Get a product instance based on its identifier.
     * OSGi configuration pattern: com.adeo.connector.opus.ProductRequest:/business/v2/products/{0}?context={1}&mode=mask&mask=MyMask&expand=attributes:ModelTypeProcessor
     *
     * @param productId  The unique identifier of the product.
     * @param contexts   The context to filter contextualized attributes.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected.
     * @return a product instance.
     */
    <T> T getProduct(String productId, List<Context> contexts, Class<T> modelClass);

    /**
     * Get a product instance based on its identifier.
     * OSGi configuration pattern: com.adeo.connector.opus.ProductWithMasksRequest:/business/v2/products/{0}?context={1}&mode=mask&mask={2}&expand=attributes:ModelTypeProcessor
     *
     * @param productId  The unique identifier of the product.
     * @param masks      The list of masks.
     * @param contexts   The context to filter contextualized attributes.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected.
     * @return a product instance.
     */
    <T> T getProduct(String productId, List<Context> contexts, List<String> masks, Class<T> modelClass);

    /**
     * Get a family instance based on its identifier
     * OSGi configuration pattern: com.adeo.connector.opus.FamilyRequest:/business/v2/families/{0}?mode=mask&mask=MyMask&expand=attributes:ModelTypeProcessor
     *
     * @param familyId   The unique identifier of the family.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected.
     * @return a family instance.
     */
    <T> T getFamily(String familyId, Class<T> modelClass);

    /**
     * Get a list of product instances based on identifiers. The order of products result will match the identifiers order.
     * OSGi configuration pattern: com.adeo.connector.opus.ProductListRequest:/business/v2/products?query={0}&context={1}&mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor
     *
     * @param productIds List of products identifiers.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected.
     * @return The list of product instances.
     */
    <T> List<T> getProducts(List<String> productIds, List<Context> contexts, Class<T> modelClass);

    /**
     * Get a list of product instances for a family. The result is based on a given segmentation.
     * OSGi configuration pattern: com.adeo.connector.opus.FamilyProductsRequest:/business/v2/families/{0}/contentSet/contents?context={1}&facet.contentSet={2}&facet.attribute={3}&filter={4}&mode=mask&mask=MyMask&expand=attributes&sort={5}&startFrom={6}&pageSize={7}:ContentSetProcessor
     *
     * @param <T>        The model class expected. The model class has to match the OSGi configuration.
     * @param familyId   The unique identifier of the family.
     * @param contexts   The context to filter contextualized attributes.
     * @param startFrom  The starting index in the the product result list. Used in conjunction with pageSize it allow paginating the results.
     * @param pageSize   The number of products to get.
     * @param segments   The segments used by the family.
     * @param attributes Names and values of attributes. Needed for filtering.
     * @param sorts      The sorts used for sorting results.
     * @param modelClass The model class used to parse the OPUS response.
     * @return The list of product instances.
     */
    <T> ContentSet<T> getProducts(String familyId, List<Context> contexts, int startFrom, int pageSize, List<FamilySegment[]> segments,
                                  List<FamilyAttribute> attributes, List<FamilySort> sorts, Class<T> modelClass);

    /**
     * Get a list of segments for a given family.
     * OSGi configuration pattern: com.adeo.connector.opus.SegmentationRequest:/business/v2/families/{0}?expand=subContents(depth%3A2)&mode=mask&mask=MyMask:SegmentationProcessor
     *
     * @param familyId   The unique identifier of the family.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected.
     * @return The list of segment instance.
     */
    <T> List<T> getSegments(String familyId, Class<T> modelClass);

    /**
     * Get a list of stores.
     * OSGi configuration pattern: com.adeo.connector.opus.StoreListRequest:/business/v2/pointsOfSale?mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor
     *
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected.
     * @return The list of store instances.
     */
    <T> ContentSet<T> getStores(Class<T> modelClass);

    /**
     * Get a list list of stores based on a given region.
     * OSGi configuration pattern: com.adeo.connector.opus.RegionalStoreListRequest:/business/v2/pointsOfSale?filter=@(regionId):{0}&mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor
     *
     * @param region     The unique identifier of the region.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected.
     * @return The list of store instances.
     */
    <T> ContentSet<T> getStores(String region, Class<T> modelClass);

    /**
     * Get a store based on its identifier.
     * OSGi configuration pattern: com.adeo.connector.opus.StoreRequest:/business/v2/pointsOfSale/{0}?mode=mask&mask=MyMask&expand=attributes:ModelTypeProcessor
     *
     * @param storeId    The unique identifier of the store.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected.
     * @return The store instance.
     */
    <T> T getStore(String storeId, Class<T> modelClass);

    /**
     * Find a list of editorials based on a keyword and an editorial model.
     * OSGi configuration pattern: com.adeo.connector.opus.EditorialSearchRequest:/business/v2/editorials?filter=keyword%3A({0})%20AND%20modelCode%3D{1}&mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor
     *
     * @param keyword    The keyword used for the search.
     * @param modelCode  The unique identifier of the model.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected.
     * @return The editorial instances.
     */
    <T> ContentSet<T> findEditorials(String keyword, String modelCode, Class<T> modelClass);

    /**
     * Find a list of products based on a keyword.
     * OSGi configuration pattern: com.adeo.connector.opus.ProductSearchRequest:/business/v2/products?query=keyword%3A({0})&facet.field=inContentSet&facet.pattern=.*Family&context={1}&startFrom={2}&pageSize={3}&mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor
     *
     * @param keyword    The keyword used for the search.
     * @param contexts   The context to filter contextualized attributes.
     * @param startFrom  The starting index in the the product result list. Used in conjunction with pageSize it allow paginating the results.
     * @param pageSize   The number of products to get.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected.
     * @return The product instances.
     */
    <T> ContentSet<T> findProducts(String keyword, List<Context> contexts, int startFrom, int pageSize, Class<T> modelClass);


    /**
     * Find a list of services based on a keyword.
     * OSGi configuration pattern: com.adeo.connector.opus.ServiceSearchRequest:/business/v2/services?query=keyword%3A({0})&startFrom={1}&pageSize={2}&mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor
     *
     * @param keyword    The keyword used for the search.
     * @param startFrom  The starting index in the the product result list. Used in conjunction with pageSize it allow paginating the results.
     * @param pageSize   The number of products to get.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected.
     * @return The product instances.
     */
    <T> ContentSet<T> findServices(String keyword, int startFrom, int pageSize, Class<T> modelClass);

    /**
     * Find a list of Families based on keyword.
     * OSGi configuration pattern: com.adeo.connector.opus.FamilySearchResquest:/business/v2/families?query=keyword%3A({0})&startFrom={1}&pageSize={2}&mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor
     *
     * @param keyword    The keyword used to search
     * @param startFrom  The starting index in the the product result list. Used in conjunction with pageSize it allow paginating the results.
     * @param pageSize   The number of products to get.
     * @param modelClass The model class used to parse the OPUS response.
     * @return ContentSet instance containing all the results.
     */
    <T> ContentSet<T> findFamily(String keyword, int startFrom, int pageSize, Class<T> modelClass);

    /**
     * Find a list of Families based on keyword.
     * OSGi configuration pattern: com.adeo.connector.opus.SeriesSearchRequest:/business/v2/series?query=keyword%3A({0})&startFrom={1}&pageSize={2}&mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor
     *
     * @param keyword    The keyword used to search
     * @param startFrom  The starting index in the the product result list. Used in conjunction with pageSize it allow paginating the results.
     * @param pageSize   The number of products to get.
     * @param modelClass The model class used to parse the OPUS response.
     * @return ContentSet instance containing all the results.
     */
    <T> ContentSet<T> findSeries(String keyword, int startFrom, int pageSize, Class<T> modelClass);

    /**
     * Get a list list of all regions.
     * OSGi configuration pattern: com.adeo.connector.opus.RegionsRequest:/business/v2/Region?startFrom={0}&pageSize={1}&mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor
     *
     * @param startFrom  number of start page.
     * @param pageSize   count of products on one page.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected.
     * @return ContentSet with all regions
     */
    <T> ContentSet<T> getRegions(String startFrom, String pageSize, Class<T> modelClass);

    /**
     * Get a region based on its identifier.
     * OSGi configuration pattern: com.adeo.connector.opus.RegionRequest:/business/v2/Region?filter=%40(regionId3)%3D{0}&mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor
     *
     * @param regionId id of region.
     * @param <T>      The model class expected. The model class has to match the OSGi configuration.
     * @return a region instance.
     */
    <T> T getRegion(String regionId, Class<T> modelClass);


    /**
     * Get a FAQ instance based on its identifier
     * OSGi configuration pattern: com.adeo.connector.opus.GetFaqRequest:/business/v2/editorials/{0}:ModelTypeProcessor
     *
     * @param faqId      The unique identifier of the FAQ.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected.
     * @return a faq instance
     */
    <T> T getFaq(String faqId, Class<T> modelClass);


    /**
     * Get a Service instance based on its identifier
     * OSGi configuration pattern: com.adeo.connector.opus.GetServiceRequest:/business/v2/services/{0}:ModelTypeProcessor
     *
     * @param serviceId  The unique identifier of the Service.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected.
     * @return a Service instance
     */
    <T> T getService(String serviceId, Class<T> modelClass);


    /**
     * Get a How To instance based on its identifier
     * OSGi configuration pattern: com.adeo.connector.opus.GetHowToRequest:/business/v2/editorials/{0}:ModelTypeProcessor
     *
     * @param serviceId  The unique identifier of the How To.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected.
     * @return a How To instance
     */
    <T> T getHowTo(String serviceId, Class<T> modelClass);


    /**
     * Get a contentSet with counts of products for each brand.
     * OSGi configuration pattern: com.adeo.connector.opus.ProductCountsBrandRequest:/business/v2/products?filter=%40(377%40PimFeat)%3A({0})&facet.field=%40(377%40PimFeat)&pageSize=0:ContentSetProcessor
     *
     * @param brandNames list of brand names.
     * @param <T>        The model class expected. The model class has to match the OSGi configuration.
     * @return ContentSet with all regions
     */
    <T> ContentSet<T> getProductCountsByBrand(List<String> brandNames, Class<T> modelClass);

    /**
     * Get a list of products by brands.
     * OSGi configuration pattern: com.adeo.connector.opus.ProductSearchBrandRequest:/business/v2/products?startFrom={0}&pageSize={1}&facet.field=%40(377%40PimFeat)&filter=%40(377%40PimFeat)%3D({2})&facet.field=inContentSet&facet.pattern=.%2AFamily&mode=mask&mask=StaticMask&expand=attributes:ContentSetProcessor
     *
     * @param brandNames list of brand names.
     * @param <T>        The model class expected. The model class has to match the OSGi configuration.
     * @return ContentSet with all regions
     */
    <T> ContentSet<T> getProductsByBrand(String startFrom, String pageSize, List<String> brandNames, Class<T> modelClass);

    /**
     * Get a list list of all families.
     * OSGi configuration pattern: com.adeo.connector.opus.FamiliesRequest:/business/v2/families?mode=mask&mask=MyMask&startFrom={0}&pageSize={1}:ContentSetProcessor
     *
     * @param startFrom  number of start page.
     * @param pageSize   count of products on one page.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected. The model class has to match the OSGi configuration.
     * @return ContentSet with all families
     */
    <T> ContentSet<T> getFamilies(String startFrom, String pageSize, Class<T> modelClass);

    /**
     * Get a list of Sortings for a particular family
     * OSGi configuration pattern: com.adeo.connector.opus.RankingListRequest:/business/v2/families/{0}/contentSet/ranking:RankingProcessor
     *
     * @param familyId The unique identifier of the family
     * @return {@link Ranking} will all the ranking items
     */
    List<Ranking> getSortings(String familyId);

    /**
     * Get a Serie instance based on its identifier
     * OSGi configuration pattern: com.adeo.connector.opus.SerieRequest:/business/v2/series/{0}:ModelTypeProcessor
     *
     * @param serieId    Unique identifier fo the Series
     * @param modelClass Class used to parse the OPUS Json
     * @return Instance of the Series request
     */
    <T> T getSerie(String serieId, Class<T> modelClass);

    /**
     * Get a Series instance based on its identifier
     * OSGi configuration pattern: com.adeo.connector.opus.SeriesRequest:/business/v2/series?startFrom={0}&pageSize={1}:ContentSetProcessor
     *
     * @param modelClass Class used to parse the OPUS Json
     * @return Instance of the Series request
     */
    <T> ContentSet<T> getSeries(String startFrom, String pageSize, Class<T> modelClass);

    /**
     * Get List of Products in a Series.
     * OSGi configuration pattern: com.adeo.connector.opus.ProductInSeriesRequest:/business/v2/series/{0}/contentSet/contents?startFrom={1}&pageSize={2}&expand=links:ContentSetProcessor
     *
     * @param seriesId   Series Id under which products are required
     * @param startFrom  number of start page.
     * @param pageSize   count of products on one page.
     * @param modelClass Class used to parse the OPUS Json
     * @return ContentSet with all Products
     */
    <T> ContentSet<T> getProductsInSeries(String seriesId, String startFrom, String pageSize, Class<T> modelClass);

    /**
     * Get all Netchandising contents.
     * OSGi configuration pattern: com.adeo.connector.opus.AllNetchandisingRequest:/business/v2/netchandisings?startFrom={0}&pageSize={1}:ContentSetProcessor
     *
     * @param startFrom  number of start page.
     * @param pageSize   count of products on one page.
     * @param modelClass The model class used to parse the OPUS response.
     * @return ContentSet with all Netchandising Content
     */
    <T> ContentSet<T> getAllNetchandising(String startFrom, String pageSize, Class<T> modelClass);

    /**
     * Get Sub Contents of a Netchandising
     * OSGi configuration pattern: com.adeo.connector.opus.NetchandisingContentsRequest:/business/v2/netchandisings/{0}/nodes:ContentSetProcessor
     *
     * @param netchandisingId Id of the Netchandising
     * @param modelClass      The model class used to parse the OPUS response.
     * @return ContentSet containing all the sub nodes of the Netchandising
     */
    <T> ContentSet<T> getNetchandisingContents(String netchandisingId, Class<T> modelClass);

    /**
     * Get Sub Contents of a Netchandising Node
     * OSGi configuration pattern: com.adeo.connector.opus.NetchandisingNodeContentsRequest:/business/v2/netchandisingNodes/{0}/nodes:ContentSetProcessor
     *
     * @param netchandisingNodeId Id of the NetchandisingNode
     * @param modelClass          The model class used to parse the OPUS response.
     * @return ContentSet containing all the sub nodes of the NetchandisingNode
     */
    <T> ContentSet<T> getNetchandisingNodeContents(String netchandisingNodeId, Class<T> modelClass);

    /**
     * Get a Netchandising Node
     * OSGi configuration pattern: com.adeo.connector.opus.NetchandisingNodeRequest:/business/v2/netchandisingNodes/{0}:ModelTypeProcessor
     *
     * @param netchandisingNodeId Id of the NetchandisingNode
     * @param modelClass          The model class used to parse the OPUS response.
     * @return an instance of NetchandisingNode
     */
    <T> T getNetchandisingNode(String netchandisingNodeId, Class<T> modelClass);

    /**
     * Find search suggestions based on keywords.
     * OSGi Configuration pattern: com.adeo.connector.opus.SearchSuggestionRequest:/search/v2/suggest/phrase?input={0}&field={1}&size={2}:SuggestionProcessor
     *
     * @param input the input used to search
     * @param field the field used to search in
     * @param size  the size of the expected response
     * @return a list of words
     */
    List<String> getSearchSuggestions(String input, String field, int size);

}
