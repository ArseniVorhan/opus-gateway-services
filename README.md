# OPUS Gateway Services


## Summary

The purpose of this bundle is to provide convenient methods to call OPUS services

--------------------------------------------

### T getProduct(String productId, String context)

**Description:** Get a product instance based on its identifier.

**OSGi configuration pattern:** com.adeo.connector.opus.ProductRequest:/business/v2/products/{0}?mode=mask&mask=MyMask&expand=attributes:ModelTypeProcessor:com.adeo.connector.opus.models.MyProductModel

### List<T> getProducts(List<String> productIds)

**Description:** Get a list of product instances based on identifiers. The order of products result will match the identifiers order.

**OSGi configuration pattern:** com.adeo.connector.opus.ProductRequest:/business/v2/products/{0}?mode=mask&mask=MyMask&expand=attributes:ModelTypeProcessor:com.adeo.connector.opus.models.MyProductModel

### ContentSet<T> getProducts(String familyId, String context, int pageSize, int startFrom, List<Segment[]> segments, String sortAttribute, boolean ascSorting)

**Description:** Get a list of product instances for a family. The result is based on a given segmentation.

**OSGi configuration pattern:** com.adeo.connector.opus.FamilyProductsRequest:/business/v2/families/{0}/contentSet/contents?filter={1}&facet.contentSet={2}&mode=mask&mask=MyMask&expand=attributes&sort={3}&pageSize={4}&startFrom={5}:ContentSetProcessor:com.adeo.connector.opus.models.MyProductModel

### List<T> getSegments(String familyId)

**Description:** Get a list of segments for a given family.

**OSGi configuration pattern:** com.adeo.connector.opus.SegmentationRequest:/business/v2/families/{0}?expand=subContents(depth%3A2)&mode=mask&mask=MyMask:SegmentationProcessor:com.adeo.connector.opus.models.MyCriterionModel

### ContentSet<T> getStores()

**Description:** Get a list of stores.

**OSGi configuration pattern:** com.adeo.connector.opus.StoreListRequest:/business/v2/pointsOfSale?mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor:com.adeo.connector.opus.models.MyStoreModel

### ContentSet<T> getStores(String region)

**Description:** Get a store based on its identifier.

**OSGi configuration pattern:** com.adeo.connector.opus.StoreRequest:/business/v2/pointsOfSale/{0}?mode=mask&mask=MyMask&expand=attributes:ModelTypeProcessor:com.adeo.connector.opus.models.MyStoreModel

### ContentSet<T> findEditorials(String keyword, String modelCode)

**Description:** Find a list of editorials based on a keyword and an editorial model.

**OSGi configuration pattern:** com.adeo.connector.opus.EditorialSearchRequest:/business/v2/editorials?filter=keyword%3D'{'{0}'}'%20AND%20modelCode%3D{1}&mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor:com.adeo.connector.opus.models.MyEditorialModel

### ContentSet<T> findProducts(String keyword, String context)

**Description:** Find a list of products based on a keyword.

**OSGi configuration pattern:** com.adeo.connector.opus.ProductSearchRequest:/business/v2/products?query=keyword%3D'{'{0}'}'?mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor:com.adeo.connector.opus.models.MyProductModel

### ContentSet<T> findServices(String keyword)

**Description:** Find a list of services based on a keyword.

**OSGi configuration pattern:** com.adeo.connector.opus.ServiceSearchRequest:/business/v2/services?query=keyword%3D'{'{0}'}'?mode=mask&mask=MyMask&expand=attributes:ContentSetProcessor:com.adeo.connector.opus.models.MyServiceModel
