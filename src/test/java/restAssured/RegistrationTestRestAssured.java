package restAssured;

import dto.AuthRequestDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;


public class RegistrationTestRestAssured {

    String endpoint = "user/registration/usernamepassword";

    @BeforeMethod
    public void preCondition() {
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";
    }

    @Test
    public void registrationSuccess() {
        int i = (int) ((System.currentTimeMillis() / 1000) % 3600);

        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("toni" + i + "@gmail.com").password("Tton123456$").build();

        String token = given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .path("token");
        System.out.println(token);
    }

    @Test
    public void registrationWrongEmail() {
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("toniGmail.com")
                .password("Tton123456$")
                .build();

        given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.username", containsString("must be a well-formed email address"));

    }

    @Test
    public void registrationWrongPassword() {
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("toni@gmail.com")
                .password("WadiNis")
                .build();

        given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.password", containsString("At least 8 characters; Must contain at least 1 uppercase letter, 1 lowercase letter, and 1 number; "));

    }

    @Test
    public void registrationRegisteredUser() {
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("np7788@inbox.ru").password("WadiNisnas8#").build();
        given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(409)
                .assertThat().body("message",containsString("User already exists"));
    }
}
