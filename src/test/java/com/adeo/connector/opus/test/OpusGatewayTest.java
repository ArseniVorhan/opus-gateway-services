package com.adeo.connector.opus.test;

import com.adeo.connector.opus.gateway.ContentSet;
import com.adeo.connector.opus.gateway.OpusGateway;
import com.adeo.connector.opus.gateway.OpusGatewayMappings;
import com.adeo.connector.opus.gateway.Segment;
import com.adeo.connector.opus.gateway.processors.ContentSetProcessor;
import com.adeo.connector.opus.gateway.processors.ModelTypeProcessor;
import com.adeo.connector.opus.gateway.processors.SegmentationProcessor;
import com.adeo.connector.opus.service.OpusGatewayService;
import com.adeo.connector.opus.service.impl.OpusGatewayServiceImpl;
import com.adeo.connector.opus.test.models.CriterionModelTest;
import com.adeo.connector.opus.test.models.FamilyTest;
import com.adeo.connector.opus.test.models.ProductModelTest;
import com.adeo.connector.opus.test.models.RegionTest;
import com.adeo.connector.opus.test.models.SegmentModelTest;
import com.adobe.connector.gateway.connection.http.HttpEndpointConnector;
import com.adobe.connector.gateway.connection.http.OkHttpEndpointClient;
import com.adobe.connector.services.OrchestratorService;
import com.adobe.connector.services.impl.DefaultExecutionPlanBuilder;
import com.adobe.connector.services.impl.DefaultOrchestratorService;
import com.adobe.connector.services.impl.ExecutionPlanFactoryImpl;
import com.google.common.collect.ImmutableMap;
import org.apache.sling.testing.mock.osgi.junit.OsgiContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class OpusGatewayTest {

    @Rule
    public final OsgiContext context = new OsgiContext();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private OrchestratorService orchestratorService = new DefaultOrchestratorService();

    private OpusGatewayService opusGatewayService = new OpusGatewayServiceImpl();

    private OpusGateway opusGateway = new OpusGateway();

    private OpusGatewayMappings opusGatewayMappings = new OpusGatewayMappings();

    private List<String> mappings = new ArrayList<>();

    @Before
    public void setUp() throws Exception {

        //testProduct
        mappings.add("com.adeo.connector.opus.ProductRequest:/business/v2/products/{0}?mode=mask&mask=StaticMask,Characteristcs&expand=attributes:ModelTypeProcessor");
        //testSegments
        mappings.add("com.adeo.connector.opus.SegmentationRequest:/business/v2/families/{0}?expand=subContents(depth%3A2)&mode=mask&mask=WebtopList:SegmentationProcessor");
        //testFamilyProducts
        mappings.add("com.adeo.connector.opus.FamilyProductsRequest:/business/v2/families/{0}/contentSet/contents?facet.contentSet={1}&facet.attribute=={2}&filter={3}&mode=mask&mask=StaticMask,Characteristcs&expand=attributes&sort={4}&startFrom={5}&pageSize={6}:ContentSetProcessor");
        //testProducts
        mappings.add("com.adeo.connector.opus.ProductListRequest:/business/v2/products?query={0}&mode=mask&mask=StaticMask,Characteristcs&expand=attributes:ContentSetProcessor");
        mappings.add("com.adeo.connector.opus.RegionsRequest:/business/v2/Region?startFrom={0}&pageSize={1}&mode=mask&mask=RegionMask&expand=attributes:ContentSetProcessor");
        mappings.add("com.adeo.connector.opus.RegionRequest:/business/v2/Region?filter=%40(regionId3)%3D{0}&mode=mask&mask=RegionMask&expand=attributes:ContentSetProcessor");
        mappings.add("com.adeo.connector.opus.ProductSearchBrandRequest:/business/v2/products?startFrom=1&pageSize=0&facet.field=%40(377%40PimFeat)&filter=%40(377%40PimFeat)%3D({0})&facet.field=inContentSet&facet.pattern=.%2AFamily&mode=mask&mask=StaticMask&expand=attributes:ContentSetProcessor");
        mappings.add("com.adeo.connector.opus.FamiliesRequest:/business/v2/families?mode=mask&mask=family&startFrom={0}&pageSize={1}:ContentSetProcessor");


        context.registerInjectActivateService(new OkHttpEndpointClient());
        context.registerInjectActivateService(new HttpEndpointConnector());
        context.registerInjectActivateService(new DefaultExecutionPlanBuilder());

        context.registerInjectActivateService(opusGateway, ImmutableMap.<String, Object>builder()
                .put("gateway.name", "opusGateway")
                .put("opus.url.domain", "opus-core.adobe.demo.web.opus.webadeo.net")
                .put("opus.url.context", "")
                .put("opus.url.scheme", "http")
                .put("opus.auth.username", "wikeo")
                .put("opus.auth.password", "oekiw")
                .build());

        context.registerInjectActivateService(opusGatewayMappings, ImmutableMap.<String, Object>builder()
                .put("gateway.name", "opusGateway")
                .put("request.mappings", mappings.toArray())
                .build());

        context.registerInjectActivateService(new ModelTypeProcessor());
        context.registerInjectActivateService(new ContentSetProcessor());
        context.registerInjectActivateService(new SegmentationProcessor());

        context.registerInjectActivateService(new ExecutionPlanFactoryImpl(), ImmutableMap.<String, Object>builder()
                .put("gateway.name", "opusGateway")
                .put("request", "com.adeo.connector.opus.gateway.OpusRequest")
                .build());

        context.registerInjectActivateService(orchestratorService);

        context.registerInjectActivateService(opusGatewayService);

    }

    @Test
    public void testProduct() {
        OpusGatewayService service = context.getService(OpusGatewayService.class);
        ProductModelTest response = service.getProduct("11639491_refproduct_Product", null, ProductModelTest.class);
        Assert.assertEquals("11639491_refproduct_Product", response.getId());
        Assert.assertEquals("SURMES", response.getTitle());
        Assert.assertEquals(3, response.getCharacteristic().size());
    }

    @Test
    public void testSegments() {
        OpusGatewayService service = context.getService(OpusGatewayService.class);
        List<CriterionModelTest> response = service.getSegments("b55f25d8-111a-4a1b-92bf-2e20bd4fd2f1_Opus_Family", CriterionModelTest.class);
        Assert.assertEquals(6, response.size());
        Assert.assertEquals("NO NAME", response.get(0).getSegments().get(0).getName());
    }

    @Test
    public void testFamilyProducts() {
        OpusGatewayService service = context.getService(OpusGatewayService.class);
        String[] attributes = new  String[]{"377%40PimFeat"};
        Map<String, String> attributeValues = new HashMap<>();
        attributeValues.put("377%40PimFeat", "SENSEA");
        ContentSet<ProductModelTest> response = service.getProducts("b55f25d8-111a-4a1b-92bf-2e20bd4fd2f1_Opus_Family", null, 1, 20,
                null, null, attributes, attributeValues, "letterRange", false, ProductModelTest.class);
        Assert.assertEquals(4, response.getTotalCount());


        List<String[]> segmentIds = new ArrayList<>();
        segmentIds.add(new String[]{"bea1abfc-9508-4652-b27b-924444d79834_Opus_Segment", "6b547233-5778-409a-9e77-558f0b956329_Opus_Segment"});
        segmentIds.add(new String[]{"40731da4-f98b-44b1-8791-3556cb048c14_Opus_Segment"});
        String[] allSegmentIds = new String[]{"10a4dda4-17fe-49e2-8663-1d72419c5315_Opus_Segment",
                "47c07a3b-dfb0-45d1-a83f-d7266c63631d_Opus_Segment", "226463c4-da91-4e20-bff4-6e808585a596_Opus_Segment",
                "bea1abfc-9508-4652-b27b-924444d79834_Opus_Segment", "ffab6a7e-ebe1-4504-b978-091dcf28ab91_Opus_Segment",
                "6b547233-5778-409a-9e77-558f0b956329_Opus_Segment", "40731da4-f98b-44b1-8791-3556cb048c14_Opus_Segment"};
        response = service.getProducts("b55f25d8-111a-4a1b-92bf-2e20bd4fd2f1_Opus_Family", null, 1, 20,
                segmentIds, allSegmentIds, null, null, "letterRange", false, ProductModelTest.class);
        Assert.assertEquals(19, response.getTotalCount());


        attributeValues = new HashMap<>();
        attributeValues.put("377%40PimFeat", "SENSEA");
        attributeValues.put("377%40PimFeat", "NO NAME");
        response = service.getProducts("b55f25d8-111a-4a1b-92bf-2e20bd4fd2f1_Opus_Family", null, 1, 20,
                segmentIds, allSegmentIds, attributes, attributeValues, null, false, ProductModelTest.class);
        Assert.assertEquals(16, response.getTotalCount());
    }

    @Test
    public void testProducts() {
        List<String> ids = new ArrayList<>();
        ids.add("11639491_refproduct_Product");
        ids.add("13054147_refproduct_Product");
        List<ProductModelTest> response = opusGatewayService.getProducts(ids, ProductModelTest.class);
        Assert.assertEquals(2, response.size());
    }

    @Test
    public void testRegions() {
        ContentSet<RegionTest> response = opusGatewayService.getRegions("1", "500", RegionTest.class);
        Assert.assertEquals(30, response.getTotalCount());
    }

    @Test
    public void testRegion() {
        RegionTest region = opusGatewayService.getRegion("2393_Opus_Region", RegionTest.class);
        Assert.assertEquals("2393", region.getRegionId());
    }

    @Test
    public void testSearchProductsByBrand() {
        String[] brands = {"INSPIRE", "LEXMAN", "SENSEA"};
        ContentSet contentSet = opusGatewayService.getProductsByBrand(brands, ProductModelTest.class);
        Assert.assertEquals(39, contentSet.getTotalCount());
    }

    @Test
    public void testSearchProductsByBrandGroupedByFamily() {
        String[] brands = {"INSPIRE", "SENSEA"};
        ContentSet contentSet = opusGatewayService.getProductsByBrand(brands, ProductModelTest.class);
        Map<String, Integer> familyCounts = new HashMap<>();
        familyCounts.put("b72fdee1-3c9a-42ab-bdd8-8c6831bdedc6_Opus_Family", 5);
        Assert.assertEquals(familyCounts, contentSet.getFacets());
    }

    @Test
    public void testGetFamilies() {
        ContentSet<FamilyTest> contentSet = opusGatewayService.getFamilies("1", "10", FamilyTest.class);
        Assert.assertEquals(9, contentSet.getTotalCount());
        Assert.assertEquals("products for comparison", contentSet.getResults().get(0).getName());
    }
}
