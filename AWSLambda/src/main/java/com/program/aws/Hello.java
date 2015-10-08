package com.program.aws;

import com.amazonaws.services.lambda.runtime.Context; 
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.S3Event;

public class Hello {
    public String myHandler(S3Event s3Event, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("received : " + s3Event.getRecords().get(0).getS3().getObject().getKey());
        return String.valueOf(s3Event.getRecords().get(0).getS3().getObject().getKey());
    }
}
