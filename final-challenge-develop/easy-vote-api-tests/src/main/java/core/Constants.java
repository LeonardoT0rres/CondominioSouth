package core;

import io.restassured.http.ContentType;

public interface Constants {

    String APP_BASE_URL = "https://fakerestapi.azurewebsites.net"; // arrumar com o correto para o projeto
    Integer APP_PORT = 443;
    String APP_BASE_PATH = "/api/v1"; // arrumar com o correto para o projeto

    ContentType APP_CONTENT_TYPE = ContentType.JSON;

    Long MAX_TIMEOUT = 5000L;
}
