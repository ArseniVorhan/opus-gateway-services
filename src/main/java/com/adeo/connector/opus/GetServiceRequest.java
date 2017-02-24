package com.adeo.connector.opus;

import com.adeo.connector.opus.gateway.OpusRequest;

/**
 * Created by stievena on 25/01/2017.
 */
public class GetServiceRequest extends OpusRequest {
    public GetServiceRequest(Class modelClass, String... parameters) {
        super(modelClass, parameters);
    }
}
