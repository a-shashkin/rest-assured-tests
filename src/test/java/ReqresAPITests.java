import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresAPITests {

    @Test
    void getListUsersTest() {
        Integer response =
            get("https://reqres.in/api/users?page=2").
                    then().
                    extract().path("total");

        assertEquals(12, response);
    }

    @Test
    void getSingleUserTest() {
        Response response =
                get("https://reqres.in/api/users/2").
                        then().
                        extract().response();

        String name = response.path("data.first_name") + " " + response.path("data.last_name");
        Integer id = response.path("data.id");
        String email = response.path("data.email");

        assertEquals("Janet Weaver", name);
        assertEquals(2, id);
        assertEquals("janet.weaver@reqres.in", email);
    }

    @Test
    void getResourceList() {
        Response response =
                get("https://reqres.in/api/unknown").
                        then().
                        extract().response();

        Integer page = response.path("page");
        Integer per_page = response.path("per_page");
        Integer total = response.path("total");
        Integer total_pages = response.path("total_pages");

        assertEquals(1, page);
        assertEquals(6, per_page);
        assertEquals(12, total);
        assertEquals(2, total_pages);
    }

    @Test
    void registerUserTest() {
        Map<String, String> jsonBody = new HashMap<>();
        jsonBody.put("email", "eve.holt@reqres.in");
        jsonBody.put("password", "pistol");

        Response response =
        given().
                header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36").
                contentType(ContentType.JSON).
                body(jsonBody).
        when().
                post("https://reqres.in/api/register").
        then().
                statusCode(200).
                extract().response();

        Integer id = response.path("id");
        String token = response.path("token");

        assertEquals(4, id);
        assertEquals("QpwL5tke4Pnpja7X4", token);
    }

    @Test
    void loginUserTest() {
        Map<String, String> jsonBody = new HashMap<>();
        jsonBody.put("email", "eve.holt@reqres.in");
        jsonBody.put("password", "cityslicka");

        Response response =
        given().
                header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                           "(KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36").
                contentType("application/json").
                body(jsonBody).
        when().
                post("https://reqres.in/api/login").
        then().
                statusCode(200).
                extract().response();

        String token = response.path("token");

        assertEquals("QpwL5tke4Pnpja7X4", token);
    }
}