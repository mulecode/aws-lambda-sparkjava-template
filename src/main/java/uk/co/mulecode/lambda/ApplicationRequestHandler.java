package uk.co.mulecode.lambda;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.model.HttpApiV2ProxyRequest;
import com.amazonaws.serverless.proxy.spark.SparkLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.extern.log4j.Log4j2;
import spark.Spark;
import uk.co.mulecode.lambda.resource.SparkResources;

@Log4j2
public class ApplicationRequestHandler implements RequestStreamHandler {

  private static SparkLambdaContainerHandler<HttpApiV2ProxyRequest, AwsProxyResponse> handler;

  static {
    try {
      handler = SparkLambdaContainerHandler.getHttpApiV2ProxyHandler();
      SparkResources.defineResources();
      Spark.awaitInitialization();
    } catch (ContainerInitializationException e) {
      throw new RuntimeException("Could not initialize Spark container", e);
    }
  }

  @Override
  public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
    handler.proxyStream(input, output, context);
  }
}
