package com.program.aws;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.services.codedeploy.AmazonCodeDeployClient;
import com.amazonaws.services.codedeploy.model.BundleType;
import com.amazonaws.services.codedeploy.model.CreateDeploymentRequest;
import com.amazonaws.services.codedeploy.model.CreateDeploymentResult;
import com.amazonaws.services.codedeploy.model.RegisterApplicationRevisionRequest;
import com.amazonaws.services.codedeploy.model.RevisionLocation;
import com.amazonaws.services.codedeploy.model.RevisionLocationType;
import com.amazonaws.services.codedeploy.model.S3Location;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;;

public class S3CodeDeployEvent implements RequestHandler<S3Event, String> {

	public String handleRequest(S3Event s3Event, Context context) {
		LambdaLogger logger = context.getLogger();
		String bucket = null;
		String eTag = null;
		try {
			bucket = codeDeploy(s3Event, logger);
			eTag = s3Event.getRecords().get(0).getS3().getObject().geteTag();
			logger.log("received : " + eTag + "   " + bucket);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return String.valueOf(eTag + " " + bucket);
	}

	public String codeDeploy(S3Event tas3Event, LambdaLogger logger) throws InterruptedException {
		AmazonCodeDeployClient amazonCodeDeployClient = new AmazonCodeDeployClient(new StaticCredentialsProvider(
				new BasicAWSCredentials("AKIAJB2IT6DSGPF4QIUQ", "DGkvxF4skIq2BeLIzbIDwyqPaejYm+6l+Joenn+w")));

		RevisionLocation revision = new RevisionLocation();
		S3Location s3Location = new S3Location();
		s3Location.setETag(tas3Event.getRecords().get(0).getS3().getObject().geteTag());
		s3Location.setBucket(tas3Event.getRecords().get(0).getS3().getBucket().getName());
		s3Location.setKey(tas3Event.getRecords().get(0).getS3().getObject().getKey());
		s3Location.setBundleType(BundleType.Zip);
		revision.setS3Location(s3Location);
		revision.setRevisionType(RevisionLocationType.S3);
		System.out.println(revision.getS3Location().getBucket());
		amazonCodeDeployClient.registerApplicationRevision(
				new RegisterApplicationRevisionRequest().withApplicationName("sa").withRevision(revision));

		CreateDeploymentResult createDeploymentResult = amazonCodeDeployClient
				.createDeployment(new CreateDeploymentRequest().withDeploymentGroupName("sa").withApplicationName("sa").withRevision(revision));
		String deploymentId = createDeploymentResult.getDeploymentId();
		logger.log(deploymentId);
		// logger.log("RESULT :" + waitForDeployment(deploymentId, logger,
		// amazonCodeDeployClient));
		return revision.getS3Location().getBucket();
	}

	// private boolean waitForDeployment(String deploymentId, LambdaLogger
	// logger, AmazonCodeDeployClient codedeploy)
	// throws InterruptedException {
	//
	// GetDeploymentRequest deployInfoRequest = new GetDeploymentRequest();
	// deployInfoRequest.setDeploymentId(deploymentId);
	//
	// DeploymentInfo deployStatus =
	// codedeploy.getDeployment(deployInfoRequest).getDeploymentInfo();
	//
	// long startTimeMillis;
	// if (deployStatus == null || deployStatus.getStartTime() == null) {
	// startTimeMillis = new Date().getTime();
	// } else {
	// startTimeMillis = deployStatus.getStartTime().getTime();
	// }
	//
	// boolean success = true;
	// long pollingTimeoutMillis = 10L * 1000L;
	// long pollingFreqMillis = 10L * 1000L;
	//
	// while (deployStatus == null || deployStatus.getCompleteTime() == null) {
	//
	// if (deployStatus == null) {
	// logger.log("Deployment status: unknown.");
	// } else {
	// DeploymentOverview overview = deployStatus.getDeploymentOverview();
	// logger.log("Deployment status: " + deployStatus.getStatus() + ";
	// instances: " + overview);
	// }
	//
	// deployStatus =
	// codedeploy.getDeployment(deployInfoRequest).getDeploymentInfo();
	// Date now = new Date();
	//
	// if (now.getTime() - startTimeMillis >= pollingTimeoutMillis) {
	// logger.log("Exceeded maximum polling time of " + pollingTimeoutMillis + "
	// milliseconds.");
	// success = false;
	// break;
	// }
	//
	// Thread.sleep(pollingFreqMillis);
	// }
	//
	// logger.log("Deployment status: " + deployStatus.getStatus() + ";
	// instances: "
	// + deployStatus.getDeploymentOverview());
	//
	// if
	// (!deployStatus.getStatus().equals(DeploymentStatus.Succeeded.toString()))
	// {
	// logger.log("Deployment did not succeed. Final status: " +
	// deployStatus.getStatus());
	// success = false;
	// }
	//
	// return success;
	// }
}

