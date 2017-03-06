package com.adeo.connector.opus.service.utils;

import com.adeo.connector.opus.service.models.Context;
import com.adeo.connector.opus.service.models.FamilyAttribute;
import com.adeo.connector.opus.service.models.FamilySegment;
import com.adeo.connector.opus.service.models.FamilySort;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by stievena on 27/02/2017.
 */
public class QueryBuilder {
    public static String buildSegmentsFilterParam(List<FamilySegment[]> segments) {
        return Optional.ofNullable(segments).orElse(Collections.emptyList()).stream()
                .map(s -> Stream.of(s)
                        .filter(FamilySegment::isEnabled)
                        .map(FamilySegment::getId)
                        .collect(Collectors.joining("%20OR%20", "inContentSet:(", ")")))
                .filter(s -> !s.equals("inContentSet:()"))
                .collect(Collectors.joining("%20AND%20"));
    }

    public static String buildSegmentsFacetParam(List<FamilySegment[]> segments) {
        return Optional.ofNullable(segments).orElse(Collections.emptyList()).stream()
                .flatMap(Stream::of)
                .map(FamilySegment::getId)
                .collect(Collectors.joining("%2C"));
    }

    public static String buildAttributesFilterParam(List<FamilyAttribute> attributes) {
        return Optional.ofNullable(attributes).orElse(Collections.emptyList()).stream().map(attr -> "@(" + attr.getName() + "):" + attr.getValues().formatValues()).collect(Collectors.joining("%20AND%20"));
    }

    public static String buildAttributesFacetParam(List<FamilyAttribute> attributes) {
        return Optional.ofNullable(attributes).orElse(Collections.emptyList()).stream()
                .map(attr -> "@(" + attr.getName() + ")")
                .collect(Collectors.joining("%2C"));
    }

    public static String buildSortsParam(List<FamilySort> sorts) {
        return Optional.ofNullable(sorts).orElse(Collections.emptyList()).stream()
                .map(sort -> "@(" + sort.getName() + ")%20" + sort.getOrder().name().toLowerCase())
                .collect(Collectors.joining("%2C"));
    }

    public static String buildContextsParam(List<Context> context) {
        return Optional.ofNullable(context).orElse(Collections.emptyList()).stream().map(ctx -> ctx.getName() + ":" + ctx.getValue()).collect(Collectors.joining("%2C"));
    }

    public static String buildFilterParam(List<FamilySegment[]> segments, List<FamilyAttribute> attributes) {
        return Stream.of(buildSegmentsFilterParam(segments), buildAttributesFilterParam(attributes)).filter(StringUtils::isNotBlank).collect(Collectors.joining("%20AND%20"));
    }
}
