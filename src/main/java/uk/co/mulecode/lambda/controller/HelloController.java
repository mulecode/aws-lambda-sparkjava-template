package uk.co.mulecode.lambda.controller;

import static uk.co.mulecode.lambda.util.JsonUtils.toJson;

import java.util.Map;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Route;

public class HelloController {

  public static final Route sayHello = (Request request, Response response) -> {
    var responseData = Map.of(
        "message", "hello"
    );
    response.status(HttpStatus.OK_200);
    return toJson(responseData);
  };

  public static final Route sayHallo = (Request request, Response response) -> {
    var responseData = Map.of(
        "message", "hallo"
    );
    response.status(HttpStatus.OK_200);
    return toJson(responseData);
  };
}
