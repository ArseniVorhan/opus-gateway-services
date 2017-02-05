package com.adeo.connector.opus.service;


import com.adeo.connector.opus.exception.OpusException;
import com.adeo.connector.opus.gateway.ContentSet;
import com.adeo.connector.opus.gateway.Segment;
import com.adeo.connector.opus.model.OpusObject;

import java.util.List;

/**
 * Business class to use the OPUS gateway services
 */
public interface OpusGatewayService {

    /**
     * Get a product instance based on its identifier.
     * OSGi configuration pattern: com.adeo.connector.opus.ProductRequest:/business/v2/products/{0}?mode=mask&mask=MyMask&expand=attributes:ModelTypeProcessor
     *
     * @param productId  The unique identifier of the product.
     * @param context    The context to filter contextualized attributes.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected.
     * @return a product instance.
     */
    <T> T getProduct(String productId, String context, Class<T> modelClass);

    /**
     * Get a product instance based on its identifier.
     * OSGi configuration pattern: com.adeo.connector.opus.ProductWithMasksRequest:/business/v2/products/{0}?mode=mask&mask={1}&expand=attributes:ModelTypeProcessor
     *
     * @param productId  The unique identifier of the product.
     * @param masks      The list of masks.
     * @param context    The context to filter contextualized attributes.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected.
     * @return a product instance.
     */
    <T> T getProduct(String productId, List<String> masks, String context, Class<T> modelClass);

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
     * OSGi configuration pattern: com.adeo.connector.opus.ProductListRequest:/business/v2/products?query={0}&mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor
     *
     * @param productIds List of products identifiers.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected.
     * @return The list of product instances.
     */
    <T> List<T> getProducts(List<String> productIds, Class<T> modelClass);

    /**
     * Get a list of product instances for a family. The result is based on a given segmentation.
     * OSGi configuration pattern: com.adeo.connector.opus.FamilyProductsRequest:/business/v2/families/{0}/contentSet/contents?filter={1}&facet.contentSet={2}&mode=mask&mask=MyMask&expand=attributes&sort={3}&pageSize={4}&startFrom={5}:ContentSetProcessor
     *
     * @param familyId      The unique identifier of the family.
     * @param context       The context to filter contextualized attributes.
     * @param pageSize      The number of products to get.
     * @param startFrom     The starting index in the the product result list. Used in conjunction with pageSize it allow paginating the results.
     * @param segments      The segments used by the family.
     * @param sortAttribute The attribute used for sorting results.
     * @param ascSorting    The order of the sorting. If true, the order is ascending. If false, the order id descending.
     * @param modelClass    The model class used to parse the OPUS response.
     * @param <T>           The model class expected.
     * @return The list of product instances.
     */
    <T> ContentSet<T> getProducts(String familyId, String context, int pageSize, int startFrom, List<Segment[]> segments, String sortAttribute, boolean ascSorting, Class<T> modelClass);

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
     * OSGi configuration pattern: com.adeo.connector.opus.EditorialSearchRequest:/business/v2/editorials?filter=keyword%3D({0})%20AND%20modelCode%3D{1}&mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor
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
     * OSGi configuration pattern: com.adeo.connector.opus.ProductSearchRequest:/business/v2/products?query=keyword%3D({0})?mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor
     *
     * @param keyword    The keyword used for the search.
     * @param context    The context to filter contextualized attributes.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected.
     * @return The product instances.
     */
    <T> ContentSet<T> findProducts(String keyword, String context, Class<T> modelClass);


    /**
     * Find a list of services based on a keyword.
     * OSGi configuration pattern: com.adeo.connector.opus.ServiceSearchRequest:/business/v2/services?query=keyword%3D({0})?mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor
     *
     * @param keyword    The keyword used for the search.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected.
     * @return The product instances.
     */
    <T> ContentSet<T> findServices(String keyword, Class<T> modelClass);

    /**
     * Get list of regions
     *
     * @param startFrom  number of start page.
     * @param pageSize   count of products on one page.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected.
     * @return ContentSet with all regions
     */
    <T> ContentSet<T> getRegions(String startFrom, String pageSize, Class<T> modelClass);

    /**
     * @param regionId id of region.
     * @param <T>      The model class expected. The model class has to match the OSGi configuration.
     * @return a region instance.
     */
    <T> T getRegion(String regionId, Class modelClass);

    /**
     * Post a new FAQ to OPUS
     * OSGi configuration pattern: com.adeo.connector.opus.PostFaqRequest:/business/v2/editorials:GsonProcessor:POST
     *
     * @param body JSON body of the POST request.
     * @throws OpusException
     */
    void createFaq(OpusObject body) throws OpusException;

    /**
     * Update a FAQ in OPUS
     * OSGi configuration pattern: com.adeo.connector.opus.PutFaqRequest:/business/v2/editorials/{0}?diff=true&sources=AEM:GsonProcessor:PUT
     *
     * @param faqId The unique identifier of the FAQ.
     * @param body  JSON body of the PUT request.
     * @throws OpusException
     */
    void updateFaq(String faqId, OpusObject body) throws OpusException;

    /**
     * Delete a FAQ in OPUS
     * OSGi configuration pattern: com.adeo.connector.opus.DeleteFaqRequest:/business/v2/editorials/{0}:GsonProcessor:DELETE
     *
     * @param faqId The unique identifier of the FAQ.
     * @throws OpusException
     */
    void deleteFaq(String faqId) throws OpusException;

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
     * Post a new Service to OPUS
     * OSGi configuration pattern: com.adeo.connector.opus.PostServiceRequest:/business/v2/services:GsonProcessor:POST
     *
     * @param body JSON body of the POST request.
     * @throws OpusException
     */
    void createService(OpusObject body) throws OpusException;

    /**
     * Update a Service in OPUS
     * OSGi configuration pattern: com.adeo.connector.opus.PutServiceRequest:/business/v2/services/{0}?diff=true&sources=AEM:GsonProcessor:PUT
     *
     * @param serviceId The unique identifier of the Service.
     * @param body      JSON body of the PUT request.
     * @throws OpusException
     */
    void updateService(String serviceId, OpusObject body) throws OpusException;

    /**
     * Delete a Service in OPUS
     * OSGi configuration pattern: com.adeo.connector.opus.DeleteServiceRequest:/business/v2/services/{0}:GsonProcessor:DELETE
     *
     * @param serviceId The unique identifier of the Service.
     * @throws OpusException
     */
    void deleteService(String serviceId) throws OpusException;

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
     * Post a new How To to OPUS
     * OSGi configuration pattern: com.adeo.connector.opus.PostHowToRequest:/business/v2/editorials:GsonProcessor:POST
     *
     * @param body JSON body of the POST request.
     * @throws OpusException
     */
    void createHowTo(OpusObject body) throws OpusException;

    /**
     * Update a How To in OPUS
     * OSGi configuration pattern: com.adeo.connector.opus.PutHowToRequest:/business/v2/editorials/{0}?diff=true&sources=AEM:GsonProcessor:PUT
     *
     * @param serviceId The unique identifier of the How To.
     * @param body      JSON body of the PUT request.
     * @throws OpusException
     */
    void updateHowTo(String serviceId, OpusObject body) throws OpusException;

    /**
     * Delete a How To in OPUS
     * OSGi configuration pattern: com.adeo.connector.opus.DeleteHowToRequest:/business/v2/editorials/{0}:GsonProcessor:DELETE
     *
     * @param serviceId The unique identifier of the How To.
     * @throws OpusException
     */
    void deleteHowTo(String serviceId) throws OpusException;

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
     * Post a new Editorial Media in OPUS
     * OSGi configuration pattern: com.adeo.connector.opus.PostEditorialMediaRequest:/business/v2/editorialMedias:GsonProcessor:POST
     *
     * @param body JSON body of the POST request.
     * @throws OpusException
     */
    void createEditorialMedia(OpusObject body) throws OpusException;

    /**
     * Update a new Editorial Media in OPUS
     * OSGi configuration pattern: com.adeo.connector.opus.PutEditorialMediaRequest:/business/v2/editorialMedias/{0}?diff=true&sources=AEM:GsonProcessor:PUT
     *
     * @param body JSON body of the PUT request.
     * @throws OpusException
     */
    void updateEditorialMedia(String editorialMediaId, OpusObject body) throws OpusException;
}
