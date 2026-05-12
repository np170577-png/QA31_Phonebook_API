package restAssured;

import dto.ContactDTO;
import dto.ErrorDTO;
import dto.GetAllContactsDTO;
import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class getAllContactsTestRestAssured {

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibnA3Nzg4QGluYm94LnJ1IiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE3NzkyMDI5MjcsImlhdCI6MTc3ODYwMjkyN30.Rr_q3pVtLYXrpsg-xWVbvJxPY7fRcsWd9orXd7-JGwg";
    String endpoint = "contacts";

    @BeforeMethod
    public void preCondition() {
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";
    }

    @Test
    public void getAllContactsSuccess() {
        GetAllContactsDTO contactsDTO = given()
                .header("Authorization", token)
                .when()
                .get(endpoint)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .as(GetAllContactsDTO.class);
        List<ContactDTO> list = contactsDTO.getContacts();
        for (ContactDTO c : list) {
            System.out.println(c.getId());
            System.out.println(c.getEmail());
            System.out.println("======================");
            System.out.println("Size of List-->" + list.size());
        }
    }

    @Test
    public void getAllContactsWrongToken() {
        ErrorDTO errorDTO = given()
                .header("Authorization", "ghjsd")
                .when()
                .get(endpoint)
                .then()
                .extract().response().as(ErrorDTO.class);
        Assert.assertEquals(errorDTO.getError().toString(), "Unauthorized");
        Assert.assertTrue(errorDTO.getMessage().toString().contains("JWT strings must contain"));
        Assert.assertEquals(errorDTO.getStatus(), 401);
        System.out.println(errorDTO);
    }

    @Test
    public void getAllContactsWrongToken2() {

            given()
                    .header("Authorization", "ghjsd")
                    .when()
                    .get(endpoint)
                    .then()
                    .assertThat().statusCode(401)
                    .assertThat().body("message", containsString("JWT strings must contain"))
                    .assertThat().body("error",equalTo("Unauthorized"));

    }
}
