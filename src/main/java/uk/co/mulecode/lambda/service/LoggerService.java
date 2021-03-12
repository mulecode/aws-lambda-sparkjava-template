package uk.co.mulecode.lambda.service;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;

@Slf4j
public class LoggerService {

  public static void logRequest(Request request, Response response) {

    Map<String, Object> content = new HashMap<>();
    content.put("method", request.requestMethod());
    content.put("url", request.url());
    content.put("query", request.queryString());
    content.put("headers", request.headers());

    log.info("Request received: {}", content);
  }

  public static void logResponse(Request request, Response response) {

    Map<String, Object> content = new HashMap<>();
    content.put("method", request.requestMethod());
    content.put("url", request.url());
    content.put("query", request.queryString());
    content.put("status", response.status());
    content.put("body", response.body());

    log.info("Response processed: {}", content);
  }

}
