package com.adeo.connector.opus;

import com.adeo.connector.opus.gateway.OpusRequest;

/**
 * Created by stievena on 29/09/16.
 */
public class StoreRequest extends OpusRequest {

    public StoreRequest(Class modelClass, String... arguments) {
        super(modelClass, arguments);
    }
}
