package restAssured;

import dto.ContactDTO;
import dto.MessageDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class DeleteContactByIdRestAssured {


    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibnA3Nzg4QGluYm94LnJ1IiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE3Nzk0MjYyOTAsImlhdCI6MTc3ODgyNjI5MH0.h9DxhIYQLmLENXdjMP-YcY3aik4S6Xk7J_H_XwxJnQs";
    String id;

    @BeforeMethod
    public void preCondition() {
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";
 int i = new Random().nextInt(1000)+1000;
        ContactDTO contactDTO = ContactDTO.builder()
                .name("Lusia")
                .lastName("Tur")
                .email("lusia"+i+"@gmail.com")
                .phone("102938"+i)
                .address("Tel-Aviv")
                .description("nobody knows")
                .build();

        String message = given()
                .body(contactDTO)
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(200)
                .extract().path("message");

        String[] all = message.split(" ");
        id = all[4];

    }

    @Test
    public void deleteContactByIDSuccess(){
        MessageDTO messageDTO =   given()
                .header("Authorization", token)
                .when()
                .delete("contacts/"+id)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .as(MessageDTO.class);
        Assert.assertEquals(messageDTO.getMessage(), "Contact was deleted!");
    }

    @Test
    public void deleteContactByIDSuccess2(){
        given()
                .header("Authorization", token)
                .when()
                .delete("contacts/"+id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message", containsString("Contact was deleted!"));
    }

    @Test
    public void deleteContactByIdWrongToken(){
        given()
                .header("Authorization", "asdlfkj")
                .when()
                .delete("contacts/"+id)
                .then()
                .assertThat().statusCode(401);
    }
}
