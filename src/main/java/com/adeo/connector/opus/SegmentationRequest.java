package com.adeo.connector.opus;

import com.adeo.connector.opus.gateway.OpusRequest;

/**
 * Created by stievena on 14/10/16.
 */
public class SegmentationRequest extends OpusRequest {
    public SegmentationRequest(Class modelClass, String... arguments) {
        super(modelClass, arguments);
    }
}
