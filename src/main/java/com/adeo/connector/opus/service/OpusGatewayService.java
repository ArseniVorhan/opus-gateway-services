package com.adeo.connector.opus.service;


import com.adeo.connector.opus.gateway.ContentSet;
import com.adeo.connector.opus.gateway.Segment;

import javax.annotation.Nonnull;
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
     * @param modelClass The model class used to parse the OPUS response.
     * @param context    The context to filter contextualized attributes.
     * @param <T>        The model class expected. The model class has to match the OSGi configuration.
     * @return a product instance.
     */
    <T> T getProduct(String productId, String context, Class modelClass);

    /**
     * Get a product instance based on its identifier.
     * OSGi configuration pattern: com.adeo.connector.opus.ProductWithMasksRequest:/business/v2/products/{0}?mode=mask&mask={1}&expand=attributes:ModelTypeProcessor
     *
     * @param productId  The unique identifier of the product.
     * @param masks      The list of masks.
     * @param context    The context to filter contextualized attributes.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected. The model class has to match the OSGi configuration.
     * @return a product instance.
     */
    <T> T getProduct(String productId, List<String> masks, String context, Class modelClass);

    /**
     * Get a family instance based on its identifier
     * OSGi configuration pattern: com.adeo.connector.opus.FamilyRequest:/business/v2/families/{0}?mode=mask&mask=MyMask&expand=attributes:ModelTypeProcessor
     *
     * @param familyId   The unique identifier of the family.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected. The model class has to match the OSGi configuration.
     * @return a family instance.
     */
    <T> T getFamily(String familyId, Class modelClass);

    /**
     * Get a list of product instances based on identifiers. The order of products result will match the identifiers order.
     * OSGi configuration pattern: com.adeo.connector.opus.ProductListRequest:/business/v2/products?query={0}&mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor
     *
     * @param productIds List of products identifiers.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected. The model class has to match the OSGi configuration.
     * @return The list of product instances.
     */
    <T> List<T> getProducts(List<String> productIds, Class modelClass);

    /**
     *
     * @param modelClass The model class used to parse the OPUS response.
     * @param productCodes product IDs
     * @param <T> The model class expected. The model class has to match the OSGi configuration.
     * @return list of products for comparison.
     */
    <T> List<T> getComparison(Class modelClass, String... productCodes);

    /**
     *
     * @param modelClass The model class used to parse the OPUS response.
     * @param productCodes product IDs
     * @param <T> The model class expected. The model class has to match the OSGi configuration.
     * @return list of Shopping List Details.
     */
    <T> List<T> getShoppingListDetails(Class modelClass, String... productCodes);

    /**
     * Get a list of segments for a given family.
     * OSGi configuration pattern: com.adeo.connector.opus.SegmentationRequest:/business/v2/families/{0}?expand=subContents(depth%3A2)&mode=mask&mask=MyMask:SegmentationProcessor
     *
     * @param familyId   The unique identifier of the family.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected. The model class has to match the OSGi configuration.
     * @return The list of segment instance.
     */
    <T> List<T> getSegments(String familyId, Class modelClass);

    /**
     *
     * @param familyId stores family id in OPUS
     * @param storeSegmentIds checked segment IDs
     * @param allStoreSegmentIds IDs of all segments of current family. Needed to get count of stores for each segment.
     * @param existingStoreIds IDs of stores that exist in AEM
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T> The model class expected. The model class has to match the OSGi configuration.
     * @return The product instances.
     */
    <T> ContentSet<T> getStoresContent(@Nonnull String familyId, @Nonnull List<String[]> storeSegmentIds,
                                       @Nonnull String[] allStoreSegmentIds,
                                       @Nonnull List<String> existingStoreIds, Class modelClass);
    /**
     * Get a store based on its identifier.
     * OSGi configuration pattern: com.adeo.connector.opus.StoreRequest:/business/v2/pointsOfSale/{0}?mode=mask&mask=MyMask&expand=attributes:ModelTypeProcessor
     *
     * @param storeId    The unique identifier of the store.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected. The model class has to match the OSGi configuration.
     * @return The store instance.
     */
    <T> T getStore(String storeId, Class modelClass);

    /**
     * Find a list of editorials based on a keyword and an editorial model.
     * OSGi configuration pattern: com.adeo.connector.opus.EditorialSearchRequest:/business/v2/editorials?filter=keyword%3D({0})%20AND%20modelCode%3D{1}&mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor
     *
     * @param keyword    The keyword used for the search.
     * @param modelCode  The unique identifier of the model.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected. The model class has to match the OSGi configuration.
     * @return The editorial instances.
     */
    <T> ContentSet<T> findEditorials(String keyword, String modelCode, Class modelClass);

    /**
     * Find a list of products based on a keyword.
     * OSGi configuration pattern: com.adeo.connector.opus.ProductSearchRequest:/business/v2/products?query=keyword%3D({0})?mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor
     *
     * @param keyword    The keyword used for the search.
     * @param context    The context to filter contextualized attributes.
     * @param modelClass The model class used to parse the OPUS response.
     * @param startFrom pagination parameter.
     * @param pageSize pagination parameter.
     * @param <T>        The model class expected. The model class has to match the OSGi configuration.
     * @return The product instances.
     */
    <T> ContentSet<T> findProducts(String keyword, String context, Class modelClass, int startFrom, int pageSize);


    /**
     * Find a list of services based on a keyword.
     * OSGi configuration pattern: com.adeo.connector.opus.ServiceSearchRequest:/business/v2/services?query=keyword%3D({0})?mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor
     *
     * @param keyword    The keyword used for the search.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T>        The model class expected. The model class has to match the OSGi configuration.
     * @return The product instances.
     */
    <T> ContentSet<T> findServices(String keyword, Class modelClass);

    /**
     * Finds a list of products based on checked segmentIds and facets with counts of of products for each segment.
     *
     * @param familyId id of product family in OPUS
     * @param context    The context to filter contextualized attributes.
     * @param productSegmentIds checked segment IDs
     * @param allProductSegmentIds IDs of all segments of current family. Needed to get count of products for each segment.
     * @param startFrom pagination parameter.
     * @param pageSize pagination parameter.
     * @param modelClass The model class used to parse the OPUS response.
     * @param <T> The model class expected. The model class has to match the OSGi configuration.
     * @return The product instances.
     */
    <T> ContentSet<T> getProductsContent(@Nonnull String familyId, String context, @Nonnull List<String[]> productSegmentIds, @Nonnull String[] allProductSegmentIds,
                                         int startFrom, int pageSize, Class modelClass);
}
