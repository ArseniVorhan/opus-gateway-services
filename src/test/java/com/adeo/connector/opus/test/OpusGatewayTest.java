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
        mappings.add("com.adeo.connector.opus.FamilyProductsRequest:/business/v2/families/{0}/contentSet/contents?filter={1}&facet.contentSet={2}&mode=mask&mask=StaticMask,Characteristcs&expand=attributes&sort={3}&pageSize={4}&startFrom={5}:ContentSetProcessor");
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
        List<CriterionModelTest> response = service.getSegments("d9446ea6-86fe-421e-ad4f-102fcb0365c3_Opus_Family", SegmentModelTest.class);
        Assert.assertEquals(2, response.size());
    }

    @Test
    public void testFamilyProducts() {
        OpusGatewayService service = context.getService(OpusGatewayService.class);
        List<Segment[]> segments = new ArrayList<>();
        segments.add(new Segment[]{new Segment("b9735e4b-3628-49d4-951a-bd792a038fe2_Opus_Segment", true), new Segment("bd7be009-8904-4d5c-9b1a-21db361cbb30_Opus_Segment", true)});
        segments.add(new Segment[]{new Segment("3620845f-5b8d-4ffc-90c4-d815cf801b7b_Opus_Segment", false), new Segment("12427141-2a81-43ce-a184-e5159cd8fdff_Opus_Segment", false)});
        ContentSet<ProductModelTest> response = service.getProducts("d9446ea6-86fe-421e-ad4f-102fcb0365c3_Opus_Family", null, 20, 1, segments, "letterRange", false, ProductModelTest.class);
        Assert.assertEquals(1893, response.getTotalCount());

        segments = new ArrayList<>();
        segments.add(new Segment[]{new Segment("b9735e4b-3628-49d4-951a-bd792a038fe2_Opus_Segment", true), new Segment("bd7be009-8904-4d5c-9b1a-21db361cbb30_Opus_Segment", false)});
        segments.add(new Segment[]{new Segment("3620845f-5b8d-4ffc-90c4-d815cf801b7b_Opus_Segment", false), new Segment("12427141-2a81-43ce-a184-e5159cd8fdff_Opus_Segment", false)});
        response = service.getProducts("d9446ea6-86fe-421e-ad4f-102fcb0365c3_Opus_Family", null, 20, 1, segments, "letterRange", true, ProductModelTest.class);
        Assert.assertEquals(1512, response.getTotalCount());

        segments = new ArrayList<>();
        segments.add(new Segment[]{new Segment("b9735e4b-3628-49d4-951a-bd792a038fe2_Opus_Segment", false), new Segment("bd7be009-8904-4d5c-9b1a-21db361cbb30_Opus_Segment", true)});
        segments.add(new Segment[]{new Segment("3620845f-5b8d-4ffc-90c4-d815cf801b7b_Opus_Segment", false), new Segment("12427141-2a81-43ce-a184-e5159cd8fdff_Opus_Segment", false)});
        response = service.getProducts("d9446ea6-86fe-421e-ad4f-102fcb0365c3_Opus_Family", null, 20, 1, segments, "letterRange", true, ProductModelTest.class);
        Assert.assertEquals(381, response.getTotalCount());

        segments = new ArrayList<>();
        segments.add(new Segment[]{new Segment("b9735e4b-3628-49d4-951a-bd792a038fe2_Opus_Segment", true), new Segment("bd7be009-8904-4d5c-9b1a-21db361cbb30_Opus_Segment", true)});
        segments.add(new Segment[]{new Segment("3620845f-5b8d-4ffc-90c4-d815cf801b7b_Opus_Segment", true), new Segment("12427141-2a81-43ce-a184-e5159cd8fdff_Opus_Segment", false)});
        response = service.getProducts("d9446ea6-86fe-421e-ad4f-102fcb0365c3_Opus_Family", null, 20, 1, segments, "letterRange", true, ProductModelTest.class);
        Assert.assertEquals(0, response.getTotalCount());
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
        Assert.assertEquals(32, response.getTotalCount());
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
