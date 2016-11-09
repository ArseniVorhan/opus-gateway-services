package com.adeo.connector.opus.service;


import com.adeo.connector.opus.gateway.ContentSet;
import com.adeo.connector.opus.gateway.Segment;

import java.util.List;

/**
 * Business class to use the OPUS gateway services
 */
public interface OpusGatewayService {

    /**
     * Get a product instance
     * OSGi configuration: com.adeo.connector.opus.ProductRequest:/business/v2/products/{0}?mode=mask&mask=MyMask&expand=attributes:ModelTypeProcessor:com.adeo.connector.opus.models.MyProductModel
     *
     * @param productId The unique identifier of the product.
     * @param context   The context to filter contextualized attributes.
     * @param <T>       The model class expected. The model class has to match the OSGi configuration.
     * @return a product instance.
     */
    <T> T getProduct(String productId, String context);

    /**
     * Get a list of product instances based on identifiers
     * OSGi configuration: com.adeo.connector.opus.ProductListRequest:/business/v2/products?query={0}&mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor:com.adeo.connector.opus.models.MyProductModel
     *
     * @param productIds List of products identifiers.
     * @param <T>        The model class expected. The model class has to match the OSGi configuration.
     * @return The list of product instances matching the order of the identifiers given in parameter.
     */
    <T> List<T> getProducts(List<String> productIds);

    /**
     * Get a list of product instances based on segmentation
     *
     * @param familyId       The unique identifier of the family
     * @param context        The contec
     * @param pageSize
     * @param startFrom
     * @param segments
     * @param orderAttribute
     * @param ascOrdering
     * @param <T>
     * @return
     */
    <T> ContentSet<T> getProducts(String familyId, String context, int pageSize, int startFrom, List<Segment[]> segments, String orderAttribute, boolean ascOrdering);

    <T> List<T> getSegments(String familyId);

    <T> ContentSet<T> getStores();

    <T> ContentSet<T> getStores(String region);

    <T> T getStore(String storeId);

    <T> ContentSet<T> findEditorials(String keyword, String modelCode);

    <T> ContentSet<T> findProducts(String keyword, String context);

    <T> ContentSet<T> findServices(String keyword);


}
