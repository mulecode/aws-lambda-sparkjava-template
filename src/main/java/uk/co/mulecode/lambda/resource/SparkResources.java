package uk.co.mulecode.lambda.resource;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.path;

import lombok.extern.log4j.Log4j2;
import uk.co.mulecode.lambda.controller.HelloController;
import uk.co.mulecode.lambda.service.LoggerService;

@Log4j2
public class SparkResources {

  public static final String CONTEXT_PATH = "/spark";
  public static final String APPLICATION_JSON = "application/json";

  private SparkResources() {
  }

  public static void defineResources() {
    before((req, res) -> res.type(APPLICATION_JSON));
    before("/*", LoggerService::logRequest);
    after("/*", LoggerService::logResponse);

    path(CONTEXT_PATH, () -> {
      get("/hello", HelloController.sayHello);
      get("/hallo", HelloController.sayHallo);
    });

  }
}
