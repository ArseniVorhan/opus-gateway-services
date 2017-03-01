package com.adeo.connector.opus.test;

import com.adeo.connector.opus.service.models.*;
import com.adeo.connector.opus.utils.QueryBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stievena on 27/02/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class UtilTest {
    @Test
    public void buildSegmentsFilterTest() {
        List<FamilySegment[]> segments = new ArrayList<>();
        segments.add(new FamilySegment[]{new FamilySegment("d91d347d-5397-4139-a6c2-ae2a9fc6f441_Opus_Segment", true),
                new FamilySegment("7b072c4c-708f-407a-bcbb-331a20dd9ad2_Opus_Segment", true)});
        segments.add(new FamilySegment[]{new FamilySegment("2b55f235-df07-4719-aa14-8133187900ec_Opus_Segment", false),
                new FamilySegment("37ebaad8-a740-43b4-b129-6d7adaba936c_Opus_Segment", false),
                new FamilySegment("a1b6556a-6da2-4abd-a748-be94afd6ea8c_Opus_Segment", false)});
        segments.add(new FamilySegment[]{new FamilySegment("134ffe8c-74f9-4b12-8ce0-233907d26523_Opus_Segment", true),
                new FamilySegment("9609cac7-eca2-4467-a89a-c6baa4ed4984_Opus_Segment", false)});
        String query = QueryBuilder.buildSegmentsFilterParam(segments);
        System.out.print(query);
        Assert.assertEquals("inContentSet:(d91d347d-5397-4139-a6c2-ae2a9fc6f441_Opus_Segment OR 7b072c4c-708f-407a-bcbb-331a20dd9ad2_Opus_Segment) AND inContentSet:(134ffe8c-74f9-4b12-8ce0-233907d26523_Opus_Segment)", query);
        query = QueryBuilder.buildSegmentsFilterParam(null);
        System.out.print(query);
        Assert.assertEquals("", query);
    }

    @Test
    public void buildSegmentsFacetTest() {
        List<FamilySegment[]> segments = new ArrayList<>();
        segments.add(new FamilySegment[]{new FamilySegment("d91d347d-5397-4139-a6c2-ae2a9fc6f441_Opus_Segment", true),
                new FamilySegment("7b072c4c-708f-407a-bcbb-331a20dd9ad2_Opus_Segment", true)});
        segments.add(new FamilySegment[]{new FamilySegment("2b55f235-df07-4719-aa14-8133187900ec_Opus_Segment", false),
                new FamilySegment("37ebaad8-a740-43b4-b129-6d7adaba936c_Opus_Segment", false),
                new FamilySegment("a1b6556a-6da2-4abd-a748-be94afd6ea8c_Opus_Segment", false)});
        segments.add(new FamilySegment[]{new FamilySegment("134ffe8c-74f9-4b12-8ce0-233907d26523_Opus_Segment", true),
                new FamilySegment("9609cac7-eca2-4467-a89a-c6baa4ed4984_Opus_Segment", false)});
        String query = QueryBuilder.buildSegmentsFacetParam(segments);
        System.out.print(query);
        Assert.assertEquals("d91d347d-5397-4139-a6c2-ae2a9fc6f441_Opus_Segment%2C7b072c4c-708f-407a-bcbb-331a20dd9ad2_Opus_Segment%2C2b55f235-df07-4719-aa14-8133187900ec_Opus_Segment%2C37ebaad8-a740-43b4-b129-6d7adaba936c_Opus_Segment%2Ca1b6556a-6da2-4abd-a748-be94afd6ea8c_Opus_Segment%2C134ffe8c-74f9-4b12-8ce0-233907d26523_Opus_Segment%2C9609cac7-eca2-4467-a89a-c6baa4ed4984_Opus_Segment", query);
        query = QueryBuilder.buildSegmentsFacetParam(null);
        System.out.print(query);
        Assert.assertEquals("", query);
    }

    @Test
    public void buildAttributesFilterTest() {
        List<FamilyAttribute> attributes = new ArrayList<>();
        attributes.add(new FamilyAttribute("color", new FixedValues("black", "white")));
        attributes.add(new FamilyAttribute("size", new IntervalValues("1", "10")));
        attributes.add(new FamilyAttribute("price", new FixedValues("back")));
        String query = QueryBuilder.buildAttributesFilterParam(attributes);
        System.out.print(query);
        Assert.assertEquals("@(color):(black OR white) AND @(size):[1 TO 10] AND @(price):(back)", query);
        query = QueryBuilder.buildAttributesFilterParam(null);
        System.out.print(query);
        Assert.assertEquals("", query);
    }

    @Test
    public void buildAttributesFacetTest() {
        List<FamilyAttribute> attributes = new ArrayList<>();
        attributes.add(new FamilyAttribute("color", new FixedValues("back", "white")));
        attributes.add(new FamilyAttribute("size", new FixedValues("big", "small")));
        attributes.add(new FamilyAttribute("price", new FixedValues("back")));
        String query = QueryBuilder.buildAttributesFacetParam(attributes);
        System.out.print(query);
        Assert.assertEquals("@(color)%2C@(size)%2C@(price)", query);
        query = QueryBuilder.buildAttributesFacetParam(null);
        System.out.print(query);
        Assert.assertEquals("", query);
    }

    @Test
    public void buildSortsTest() {
        List<FamilySort> sorts = new ArrayList<>();
        sorts.add(new FamilySort("color", FamilySort.Order.ASC));
        sorts.add(new FamilySort("size", FamilySort.Order.DESC));
        sorts.add(new FamilySort("price", FamilySort.Order.DESC));
        String query = QueryBuilder.buildSortsParam(sorts);
        System.out.print(query);
        Assert.assertEquals("@(color)%20asc%2C@(size)%20desc%2C@(price)%20desc", query);
        query = QueryBuilder.buildSortsParam(null);
        System.out.print(query);
        Assert.assertEquals("", query);
    }

    @Test
    public void buildContextsTest() {
        List<Context> contexts = new ArrayList<>();
        contexts.add(new Context("color", "45"));
        contexts.add(new Context("size", "20"));
        String query = QueryBuilder.buildContextsParam(contexts);
        System.out.print(query);
        Assert.assertEquals("color:45%2Csize:20", query);
        query = QueryBuilder.buildContextsParam(null);
        System.out.print(query);
        Assert.assertEquals("", query);
    }

    @Test
    public void buildFilterTest() {
        List<FamilySegment[]> segments = new ArrayList<>();
        segments.add(new FamilySegment[]{new FamilySegment("d91d347d-5397-4139-a6c2-ae2a9fc6f441_Opus_Segment", true),
                new FamilySegment("7b072c4c-708f-407a-bcbb-331a20dd9ad2_Opus_Segment", true)});
        segments.add(new FamilySegment[]{new FamilySegment("2b55f235-df07-4719-aa14-8133187900ec_Opus_Segment", false),
                new FamilySegment("37ebaad8-a740-43b4-b129-6d7adaba936c_Opus_Segment", false),
                new FamilySegment("a1b6556a-6da2-4abd-a748-be94afd6ea8c_Opus_Segment", false)});
        segments.add(new FamilySegment[]{new FamilySegment("134ffe8c-74f9-4b12-8ce0-233907d26523_Opus_Segment", true),
                new FamilySegment("9609cac7-eca2-4467-a89a-c6baa4ed4984_Opus_Segment", false)});

        List<FamilyAttribute> attributes = new ArrayList<>();
        attributes.add(new FamilyAttribute("color", new FixedValues("black", "white")));
        attributes.add(new FamilyAttribute("size", new IntervalValues("1", "10")));
        attributes.add(new FamilyAttribute("price", new FixedValues("back")));

        String query = QueryBuilder.buildFilterParam(segments, attributes);
        System.out.print(query);
        Assert.assertEquals("inContentSet:(d91d347d-5397-4139-a6c2-ae2a9fc6f441_Opus_Segment OR 7b072c4c-708f-407a-bcbb-331a20dd9ad2_Opus_Segment) AND inContentSet:(134ffe8c-74f9-4b12-8ce0-233907d26523_Opus_Segment) AND @(color):(black OR white) AND @(size):[1 TO 10] AND @(price):(back)", query);

        query = QueryBuilder.buildFilterParam(null, attributes);
        System.out.print(query);
        Assert.assertEquals("@(color):(black OR white) AND @(size):[1 TO 10] AND @(price):(back)", query);

        query = QueryBuilder.buildFilterParam(segments, null);
        System.out.print(query);
        Assert.assertEquals("inContentSet:(d91d347d-5397-4139-a6c2-ae2a9fc6f441_Opus_Segment OR 7b072c4c-708f-407a-bcbb-331a20dd9ad2_Opus_Segment) AND inContentSet:(134ffe8c-74f9-4b12-8ce0-233907d26523_Opus_Segment)", query);

        query = QueryBuilder.buildFilterParam(null, null);
        System.out.print(query);
        Assert.assertEquals("", query);
    }
}
