package com.adeo.connector.opus.service;


import com.adeo.connector.opus.gateway.ContentSet;
import com.adeo.connector.opus.gateway.Segment;

import java.util.List;

/**
 * Business class to use the OPUS gateway services
 */
public interface OpusGatewayService {

    /**
     * Get a product instance based on its identifier.
     * OSGi configuration pattern: com.adeo.connector.opus.ProductRequest:/business/v2/products/{0}?mode=mask&mask=MyMask&expand=attributes:ModelTypeProcessor:com.adeo.connector.opus.models.MyProductModel
     *
     * @param productId The unique identifier of the product.
     * @param context   The context to filter contextualized attributes.
     * @param <T>       The model class expected. The model class has to match the OSGi configuration.
     * @return a product instance.
     */
    <T> T getProduct(String productId, String context);

    /**
     * Get a product instance based on its identifier.
     * OSGi configuration pattern: com.adeo.connector.opus.ProductWithMasksRequest:/business/v2/products/{0}?mode=mask&mask={1}&expand=attributes:ModelTypeProcessor:com.adeo.connector.opus.models.MyProductModel
     *
     * @param productId The unique identifier of the product.
     * @param masks     The list of masks.
     * @param context   The context to filter contextualized attributes.
     * @param <T>       The model class expected. The model class has to match the OSGi configuration.
     * @return a product instance.
     */
    <T> T getProduct(String productId, List<String> masks, String context);

    /**
     * Get a family instance based on its identifier
     * OSGi configuration pattern: com.adeo.connector.opus.FamilyRequest:/business/v2/families/{0}?mode=mask&mask=MyMask&expand=attributes:ModelTypeProcessor:com.adeo.connector.opus.models.MyFamilyModel
     *
     * @param familyId The unique identifier of the family.
     * @param <T>      The model class expected. The model class has to match the OSGi configuration.
     * @return a family instance.
     */
    <T> T getFamily(String familyId);

    /**
     * Get a list of product instances based on identifiers. The order of products result will match the identifiers order.
     * OSGi configuration pattern: com.adeo.connector.opus.ProductListRequest:/business/v2/products?query={0}&mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor:com.adeo.connector.opus.models.MyProductModel
     *
     * @param productIds List of products identifiers.
     * @param <T>        The model class expected. The model class has to match the OSGi configuration.
     * @return The list of product instances.
     */
    <T> List<T> getProducts(List<String> productIds);

    /**
     * Get a list of product instances for a family. The result is based on a given segmentation.
     * OSGi configuration pattern: com.adeo.connector.opus.FamilyProductsRequest:/business/v2/families/{0}/contentSet/contents?filter={1}&facet.contentSet={2}&mode=mask&mask=MyMask&expand=attributes&sort={3}&pageSize={4}&startFrom={5}:ContentSetProcessor:com.adeo.connector.opus.models.MyProductModel
     *
     * @param familyId      The unique identifier of the family.
     * @param context       The context to filter contextualized attributes.
     * @param pageSize      The number of products to get.
     * @param startFrom     The starting index in the the product result list. Used in conjunction with pageSize it allow paginating the results.
     * @param segments      The segments used by the family.
     * @param sortAttribute The attribute used for sorting results.
     * @param ascSorting    The order of the sorting. If true, the order is ascending. If false, the order id descending.
     * @param <T>           The model class expected. The model class has to match the OSGi configuration.
     * @return The list of product instances.
     */
    <T> ContentSet<T> getProducts(String familyId, String context, int pageSize, int startFrom, List<Segment[]> segments, String sortAttribute, boolean ascSorting);

    /**
     * Get a list of segments for a given family.
     * OSGi configuration pattern: com.adeo.connector.opus.SegmentationRequest:/business/v2/families/{0}?expand=subContents(depth%3A2)&mode=mask&mask=MyMask:SegmentationProcessor:com.adeo.connector.opus.models.MyCriterionModel
     *
     * @param familyId The unique identifier of the family.
     * @param <T>      The model class expected. The model class has to match the OSGi configuration.
     * @return The list of segment instance.
     */
    <T> List<T> getSegments(String familyId);

    /**
     * Get a list of stores.
     * OSGi configuration pattern: com.adeo.connector.opus.StoreListRequest:/business/v2/pointsOfSale?mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor:com.adeo.connector.opus.models.MyStoreModel
     *
     * @param <T> The model class expected. The model class has to match the OSGi configuration.
     * @return The list of store instances.
     */
    <T> ContentSet<T> getStores();

    /**
     * Get a list list of stores based on a given region.
     * OSGi configuration pattern: com.adeo.connector.opus.RegionalStoreListRequest:/business/v2/pointsOfSale?filter=@(regionId):{0}&mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor:com.adeo.connector.opus.models.MyStoreModel
     *
     * @param region The unique identifier of the region.
     * @param <T>    The model class expected. The model class has to match the OSGi configuration.
     * @return The list of store instances.
     */
    <T> ContentSet<T> getStores(String region);

    /**
     * Get a store based on its identifier.
     * OSGi configuration pattern: com.adeo.connector.opus.StoreRequest:/business/v2/pointsOfSale/{0}?mode=mask&mask=MyMask&expand=attributes:ModelTypeProcessor:com.adeo.connector.opus.models.MyStoreModel
     *
     * @param storeId The unique identifier of the store.
     * @param <T>     The model class expected. The model class has to match the OSGi configuration.
     * @return The store instance.
     */
    <T> T getStore(String storeId);

    /**
     * Find a list of editorials based on a keyword and an editorial model.
     * OSGi configuration pattern: com.adeo.connector.opus.EditorialSearchRequest:/business/v2/editorials?filter=keyword%3D'{'{0}'}'%20AND%20modelCode%3D{1}&mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor:com.adeo.connector.opus.models.MyEditorialModel
     *
     * @param keyword   The keyword used for the search.
     * @param modelCode The unique identifier of the model.
     * @param <T>       The model class expected. The model class has to match the OSGi configuration.
     * @return The editorial instances.
     */
    <T> ContentSet<T> findEditorials(String keyword, String modelCode);

    /**
     * Find a list of products based on a keyword.
     * OSGi configuration pattern: com.adeo.connector.opus.ProductSearchRequest:/business/v2/products?query=keyword%3D'{'{0}'}'?mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor:com.adeo.connector.opus.models.MyProductModel
     *
     * @param keyword The keyword used for the search.
     * @param context The context to filter contextualized attributes.
     * @param <T>     The model class expected. The model class has to match the OSGi configuration.
     * @return The product instances.
     */
    <T> ContentSet<T> findProducts(String keyword, String context);


    /**
     * Find a list of services based on a keyword.
     * OSGi configuration pattern: com.adeo.connector.opus.ServiceSearchRequest:/business/v2/services?query=keyword%3D'{'{0}'}'?mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor:com.adeo.connector.opus.models.MyServiceModel
     *
     * @param keyword The keyword used for the search.
     * @param <T>     The model class expected. The model class has to match the OSGi configuration.
     * @return The product instances.
     */
    <T> ContentSet<T> findServices(String keyword);


}
