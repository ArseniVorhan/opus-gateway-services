package com.adeo.connector.opus.test;

import com.adeo.connector.opus.gateway.ContentSet;
import com.adeo.connector.opus.gateway.OpusGateway;
import com.adeo.connector.opus.gateway.OpusGatewayMappings;
import com.adeo.connector.opus.gateway.Segment;
import com.adeo.connector.opus.gateway.processors.ContentSetProcessor;
import com.adeo.connector.opus.gateway.processors.ModelTypeProcessor;
import com.adeo.connector.opus.gateway.processors.SegmentationProcessor;
import com.adeo.connector.opus.models.Attribute;
import com.adeo.connector.opus.service.OpusGatewayService;
import com.adeo.connector.opus.service.impl.OpusGatewayServiceImpl;
import com.adeo.connector.opus.test.models.CriterionModelTest;
import com.adeo.connector.opus.test.models.FamilyTest;
import com.adeo.connector.opus.test.models.ProductModelTest;
import com.adeo.connector.opus.test.models.RegionTest;
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
import java.util.Arrays;

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
        mappings.add("com.adeo.connector.opus.FamilyProductsRequest:/business/v2/families/{0}/contentSet/contents?facet.contentSet={1}&facet.attribute={2}&filter={3}&mode=mask&mask=StaticMask,Characteristcs&expand=attributes&sort={4}&startFrom={5}&pageSize={6}:ContentSetProcessor");
        //testProducts
        mappings.add("com.adeo.connector.opus.ProductListRequest:/business/v2/products?query={0}&mode=mask&mask=StaticMask,Characteristcs&expand=attributes:ContentSetProcessor");
        mappings.add("com.adeo.connector.opus.RegionsRequest:/business/v2/Region?startFrom={0}&pageSize={1}&mode=mask&mask=RegionMask&expand=attributes:ContentSetProcessor");
        mappings.add("com.adeo.connector.opus.RegionRequest:/business/v2/Region?filter=%40(regionId3)%3D{0}&mode=mask&mask=RegionMask&expand=attributes:ContentSetProcessor");
        mappings.add("com.adeo.connector.opus.ProductSearchBrandRequest:/business/v2/products?startFrom={0}&pageSize={1}&facet.field=%40(377%40PimFeat)&filter=%40(377%40PimFeat)%3D({2})&facet.field=inContentSet&facet.pattern=.%2AFamily&mode=mask&mask=StaticMask&expand=attributes:ContentSetProcessor");
        mappings.add("com.adeo.connector.opus.ProductCountsBrandRequest:/business/v2/products?filter=%40(377%40PimFeat)%3A({0})&facet.field=%40(377%40PimFeat)&pageSize=0:ContentSetProcessor");
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

        List<Attribute> attributes = new ArrayList<>();
        String[] values = {"SENSEA"};
        attributes.add(new Attribute("377%40PimFeat", values));

        ContentSet<ProductModelTest> response = service.getProducts("ef058db2-3427-4264-9709-41fa0628e4b7_Opus_Family", null, 20, 1, null, attributes,
                "letterRange", false, ProductModelTest.class);
        Assert.assertEquals(189, response.getTotalCount());

        List<Segment[]> segments = new ArrayList<>();
        segments.add(new Segment[]{new Segment("d91d347d-5397-4139-a6c2-ae2a9fc6f441_Opus_Segment", true),
                new Segment("7b072c4c-708f-407a-bcbb-331a20dd9ad2_Opus_Segment", false)});
        segments.add(new Segment[]{new Segment("2b55f235-df07-4719-aa14-8133187900ec_Opus_Segment", false),
                new Segment("37ebaad8-a740-43b4-b129-6d7adaba936c_Opus_Segment", false),
                new Segment("a1b6556a-6da2-4abd-a748-be94afd6ea8c_Opus_Segment", false)});
        segments.add(new Segment[]{new Segment("134ffe8c-74f9-4b12-8ce0-233907d26523_Opus_Segment", true),
                new Segment("9609cac7-eca2-4467-a89a-c6baa4ed4984_Opus_Segment", false)});
        response = service.getProducts("ef058db2-3427-4264-9709-41fa0628e4b7_Opus_Family", null, 1, 20,
                segments, null, "letterRange", false, ProductModelTest.class);
        Assert.assertEquals(1, response.getTotalCount());


        attributes = new ArrayList<>();
        values = new String[]{"SENSEA", "NO NAME"};
        attributes.add(new Attribute("377%40PimFeat", values));
        response = service.getProducts("ef058db2-3427-4264-9709-41fa0628e4b7_Opus_Family", null, 1, 20,
                segments, attributes, null, false, ProductModelTest.class);
        Assert.assertEquals(1, response.getTotalCount());
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
    public void testProductCountsByBrand() {
        String[] brands = {"STANDERS", "AXTON", "SENSEA"};
        Map<String, Integer> productCounts = new HashMap<>();
        productCounts.put("STANDERS", 533);
        productCounts.put("SENSEA", 190);
        productCounts.put("AXTON", 154);
        ContentSet contentSet = opusGatewayService.getProductCountsByBrand(Arrays.asList(brands), ProductModelTest.class);
        Assert.assertEquals(productCounts, contentSet.getFacets());
    }

    @Test
    public void testSearchProductsByBrandGroupedByFamily() {
        String[] brands = {"INSPIRE", "SENSEA"};
        ContentSet contentSet = opusGatewayService.getProductsByBrand("1", "10", Arrays.asList(brands), ProductModelTest.class);
        Map<String, Integer> familyCounts = new HashMap<>();
        familyCounts.put("8f4b391d-00d1-4150-a55a-00a761520e1d_Opus_Family", 7);
        familyCounts.put("ef058db2-3427-4264-9709-41fa0628e4b7_Opus_Family", 30);
        Assert.assertEquals(familyCounts, contentSet.getFacets());
    }

    @Test
    public void testGetFamilies() {
        ContentSet<FamilyTest> contentSet = opusGatewayService.getFamilies("1", "10", FamilyTest.class);
        Assert.assertEquals(12, contentSet.getTotalCount());
        Assert.assertEquals("products for comparison", contentSet.getResults().get(0).getName());
    }
}
