package restAssured;

import dto.AuthRequestDTO;
import dto.AuthResponseDTO;
import dto.ErrorDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class LoginTestsRestAssured {

    String endpoint = "user/login/usernamepassword";

    @BeforeMethod

    public void precondition() {
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";
    }

    @Test
    public void loginSuccess() {
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("np7788@inbox.ru")
                .password("WadiNisnas8#")
                .build();

        AuthResponseDTO response =
                given()
                        .body(auth)
                        .contentType("application/json")
                        .when()
                        .post(endpoint)
                        .then()
                        .assertThat().statusCode(200)
                        .extract().response().as(AuthResponseDTO.class);
        System.out.println(response.getToken());
    }

    @Test
    public void loginWrongEmail(){
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("np7788inbox.ru")
                .password("WadiNisnas8#")
                .build();

        ErrorDTO error =
                given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(401)
                .extract().response().as(ErrorDTO.class);
        Assert.assertEquals(error.getMessage(), "Login or Password incorrect");
        Assert.assertEquals(error.getError(), "Unauthorized");
        Assert.assertEquals(error.getStatus(), 401);

    }

    @Test
    public void loginWrongEmailFormat(){
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("np7788inbox.ru")
                .password("WadiNisnas8#")
                .build();


                given()
                        .body(auth)
                        .contentType(ContentType.JSON)
                        .when()
                        .post(endpoint)
                        .then()
                        .assertThat().statusCode(401)
                        .assertThat().body("message", containsString("Login or Password incorrect"))
                        .assertThat().body("path", equalTo("/v1/user/login/usernamepassword"))
                        .assertThat().body("error", containsString("Unauthorized"));

    }

    @Test
    public void loginWrongPasswordFormat(){
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("np7788@inbox.ru")
                .password("WadiNis")
                .build();


        given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message", containsString("Login or Password incorrect"))
                .assertThat().body("path", equalTo("/v1/user/login/usernamepassword"))
                .assertThat().body("error", containsString("Unauthorized"));

    }

    @Test
    public void loginUnregisteredUserFormat(){
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("np7@inbox.ru")
                .password("123456")
                .build();


        given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message", containsString("Login or Password incorrect"))
                .assertThat().body("path", equalTo("/v1/user/login/usernamepassword"))
                .assertThat().body("error", containsString("Unauthorized"));

    }
}
