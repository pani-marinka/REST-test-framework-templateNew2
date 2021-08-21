package client;

import config.ServiceConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import service.GenreService2;

import java.util.logging.Logger;

import static io.restassured.RestAssured.*;

public class HttpClient {
    private static final Logger LOG = Logger.getLogger(GenreService2.class.getName());

    public static Response get(String endpoint) {
        return HttpClient.sendRequest(Method.GET, endpoint);
    }

    public static Response post(String endpoint, String body) {
        return HttpClient.sendRequest(Method.POST, endpoint, body);
    }

    public static Response put(String endpoint, String body) {
        return HttpClient.sendRequest(Method.PUT, endpoint, body);
    }

    public static Response delete(String endpoint) {
        return HttpClient.sendRequest(Method.DELETE, endpoint);
    }

    private static Response sendRequest(Method method, String endpoint) {

        return HttpClient.sendRequest(method, endpoint, null);
    }

//    private static Response sendRequest(Method method, String endpoint, String body) {
//        String url = ServiceConfig.HOST + endpoint;
//        RequestSpecification spec = given();
//        LOG.info(spec.get().getBody().print());
//        if (body != null) spec.body(body);
//        Response response = spec.request(method, url);
//        LOG.info(response.getBody().print());
//        return response;
//    }

    private static Response sendRequest(Method method, String endpoint, String body) {
        String url = ServiceConfig.HOST + endpoint;
        LOG.info(method + " " + url);
        RequestSpecification spec = given()
                .contentType(ContentType.JSON);
        if (body != null) spec.body(body);
        Response response = spec.request(method, url);

        LOG.info(response.getBody().print());
        return response;
    }

    private static Response sendRequestNew(Method method, String endpoint, String body) {
        RestAssured.baseURI = ServiceConfig.HOST; //+ endpoint;
        RequestSpecification spec = RestAssured.given();
        spec.header("Content-Type", "application/json");
        if (body != null) spec.body(body);
        //Response response = spec2.request(method, url);
        return spec.request(method, endpoint);
    }
}
