package com.adeo.connector.opus;

import com.adeo.connector.opus.gateway.OpusRequest;

/**
 * Created by stievena on 29/09/16.
 */
public class ServiceSearchRequest extends OpusRequest {

    public ServiceSearchRequest(Class modelClass, String... arguments) {
        super(modelClass, arguments);
    }
}
