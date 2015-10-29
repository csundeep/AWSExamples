package com.program.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNSRecord;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

public class SNSLambdaEvent implements RequestHandler<SNSEvent, String> {
	
	public String handleRequest(SNSEvent snsEvent, Context context) {
		LambdaLogger logger = context.getLogger();
		String fileName = null;
		try {
			SNSRecord record = snsEvent.getRecords().get(0);
			String msg = record.getSNS().getMessage();
			JSONObject json = new JSONObject(msg);
			logger.log(msg);
			JSONObject s3 = json.getJSONArray("Records").getJSONObject(0).getJSONObject("s3");
			fileName = s3.getJSONObject("object").getString("key");
			logger.log("The fileName is :::::::::::::::::::::" + fileName);
		} catch (JSONException e) {
			logger.log(e.getMessage());
		}
		return fileName;
	}
}
