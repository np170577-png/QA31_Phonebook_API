package okhttp;

import com.google.gson.Gson;
import dto.ContactDTO;
import dto.MessageDTO;
import dto.ErrorDTO;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class DeleteContactByIdOkHttp {

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibnA3Nzg4QGluYm94LnJ1IiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE3Nzg4MzY3NjAsImlhdCI6MTc3ODIzNjc2MH0.OF0xZXLvltN2LYymo1R98S3NFu3bebbxitPzPFiDxqw";
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    String id;
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");


    @BeforeMethod

    public void precondition() throws IOException {

        int i = new Random().nextInt(1000) + 1000;
        ContactDTO contactDTO = ContactDTO.builder()
                .name("Lusia")
                .lastName("Tur")
                .email("lusia"+i+"@gmail.com")
                .phone("1029384"+i)
                .address("DC")
                .description("nobody knows")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(contactDTO), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization", token).build();

        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        MessageDTO messageDTO = gson.fromJson(response.body().string(), MessageDTO.class);
        String message = messageDTO.getMessage();
        System.out.println(message);
        String [] all = message.split(": ");
        id = all[1];
        System.out.println(id);

    }

    @Test

    public void deleteContactByIdSuccess() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/" + id)
                .delete()
                .addHeader("Authorization", token)
                .build();

        Response response = client.newCall(request).execute();

        Assert.assertEquals(response.code(), 200);
        MessageDTO dto = gson.fromJson(response.body().string(), MessageDTO.class);
        Assert.assertEquals(response.message(), "OK");
    }

    @Test
    public void deleteContactByIdWrongToken() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/05906431-8d54-47a8-91b2-a85f1ff5bdcb")
                .delete()
                .addHeader("Authorization", "asd;lfkj")
                .build();

        Response response = client.newCall(request).execute();

        Assert.assertEquals(response.code(), 401);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getError(), "Unauthorized");
    }

    @Test

    public void deleteContactByIdNotFound() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/" +123)
                .delete()
                .addHeader("Authorization", token)
                .build();

        Response response = client.newCall(request).execute();

        Assert.assertEquals(response.code(), 400);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getError(), "Bad Request");
        Assert.assertEquals(errorDTO.getMessage(), "Contact with id: 123 not found in your contacts!");
    }
}
