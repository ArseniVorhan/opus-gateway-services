package com.adeo.connector.opus;

import com.adeo.connector.opus.gateway.OpusRequest;

/**
 * Created by stievena on 25/01/2017.
 */
public class DeleteServiceRequest extends OpusRequest {
    public DeleteServiceRequest(Class modelClass, String... parameters) {
        super(modelClass, parameters);
    }
}
