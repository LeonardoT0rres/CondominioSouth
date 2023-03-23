package service;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BaseService {

    public Response doPostRequest(Object body, String resource) {
        return
            given()
                .body(body)
            .when()
                .post(resource)
            ;
    }

    public Response doPutRequest(Object body, String id, String resource) {
        return
            given()
                .body(body)
                .pathParam("id", id)
            .when()
                .put(resource + "/{id}")
            ;
    }

    public Response doGetRequest(String resource) {
        return
            given()
            .when()
                .get(resource)
            ;
    }

    public Response doGetRequest(String id, String resource) {
        return
            given()
                .pathParam("id", id)
            .when()
                .get(resource + "/{id}")
            ;
    }

    public Response doDeleteRequest(String id, String resource) {
        return
            given()
                .pathParam("id", id)
            .when()
                .delete(resource + "/{id}")
            ;
    }
}
