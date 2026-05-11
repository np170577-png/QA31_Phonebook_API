package okhttp;

import com.google.gson.Gson;
import dto.*;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class AddNewContactTestsOkHttp {

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibnA3Nzg4QGluYm94LnJ1IiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE3Nzg4MzY3NjAsImlhdCI6MTc3ODIzNjc2MH0.OF0xZXLvltN2LYymo1R98S3NFu3bebbxitPzPFiDxqw";
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");

    @Test
    public void addNewContactTestSuccess() throws IOException {
        int i = (int) (System.currentTimeMillis() / 100) % 3600;
        ContactDTO contact = ContactDTO.builder()
                .name("Lusia")
                .lastName("Tur")
                .email("lusia" + i + "@gmail.com")
                .phone("1029384" + i)
                .address("DC")
                .description("nobody knows")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(contact), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization", token)
                .build();

        Response response = client.newCall(request).execute();

        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);
        MessageDTO message = gson.fromJson(response.body().string(), MessageDTO.class);
        Assert.assertTrue(message.getMessage().contains("Contact was added!"));
        Assert.assertTrue(message.getMessage().contains("ID"));

    }

    @Test
    public void addContactTestEmptyName() throws IOException {

        ContactDTO contact = ContactDTO.builder()
                .lastName("Tur")
                .email("lusia@gmail.com")
                .phone("10293844732")
                .address("DC")
                .description("nobody knows")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(contact), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization", token)
                .build();

        Response response = client.newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);

        ErrorDTO error = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertTrue(error.getMessage()
                .toString().contains("name=must not be blank"));

    }

    @Test
    public void addNewContactEmptyLastName() throws IOException {

        ContactDTO contact = ContactDTO.builder()
                .name("Lusia")
                .email("lusia@gmail.com")
                .phone("10293843957")
                .address("DC")
                .description("nobody knows")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(contact), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization", token)
                .build();

        Response response = client.newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);
        ErrorDTO error = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertTrue(error.getMessage().toString()
                .contains("lastName=must not be blank"));

    }

    @Test
    public void addNewContactWrongEmail() throws IOException {

        ContactDTO contact = ContactDTO.builder()
                .name("Lusia")
                .lastName("Tur")
                .email("lusiagmail.com")
                .phone("10293842345")
                .address("DC")
                .description("nobody knows")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(contact), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization", token)
                .build();

        Response response = client.newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);
        ErrorDTO error = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertTrue(error.getMessage().toString().contains("email=must be a well-formed email address"));

    }

    @Test
    public void addNewContactEmptyEmailBug() throws IOException {

        ContactDTO contact = ContactDTO.builder()
                .name("Lusia")
                .lastName("Tur")
                .email("")
                .phone("10293842345")
                .address("DC")
                .description("nobody knows")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(contact), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization", token)
                .build();

        Response response = client.newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);
        ErrorDTO error = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertTrue(error.getMessage().toString().contains("email=must be a well-formed email address"));

    }

    @Test
    public void addNewContactWrongPhone() throws IOException {
        ContactDTO contact = ContactDTO.builder()
                .name("Lusia")
                .lastName("Tur")
                .email("lusia@gmail.com")
                .phone("1029384")
                .address("DC")
                .description("nobody knows")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(contact), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization", token)
                .build();

        Response response = client.newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);

        ErrorDTO error = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertTrue(error.getMessage().toString().contains("Phone number must contain only digits! And length min 10, max 15!"));
    }

    @Test
    public void addNewContactEEmptyPhoneBug() throws IOException {
        ContactDTO contact = ContactDTO.builder()
                .name("Lusia")
                .lastName("Tur")
                .email("lusia@gmail.com")
                .address("DC")
                .description("nobody knows")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(contact), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization", token)
                .build();

        Response response = client.newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);

        ErrorDTO error = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertTrue(error.getMessage().toString().contains("Phone number must contain only digits! And length min 10, max 15!"));
    }

    @Test
    public void addNewContactEmptyAddress() throws IOException {

        ContactDTO contact = ContactDTO.builder()
                .name("Lusia")
                .lastName("Tur")
                .email("lusia@gmail.com")
                .phone("1029384890")
                .description("nobody knows")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(contact), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization", token)
                .build();

        Response response = client.newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);

        ErrorDTO error = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertTrue(error.getMessage().toString().contains("address=must not be blank"));

    }

    @Test
    public void addNewContactUnauthorized() throws IOException {

        ContactDTO contactDto = ContactDTO.builder()
                .name("Jenny")
                .lastName("Wolf")
                .email("jenny@mail.com")
                .phone("9870070077")
                .address("NY")
                .description("Friend").build();

        RequestBody body = RequestBody.create(gson.toJson(contactDto), JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization", "hhyyyyt")
                .build();

        Response response = client.newCall(request).execute();

        ErrorDTO error = gson.fromJson(response.body().string(), ErrorDTO.class);
        System.out.println(error.getMessage());
        Assert.assertTrue(error.getMessage().toString().contains("JWT strings must contain"));
        Assert.assertEquals(response.code(), 401);
        Assert.assertFalse(response.isSuccessful());

    }

    @Test
    public void addNewContactDuplicateBug() throws IOException {
        ContactDTO contact = ContactDTO.builder()
                .name("Jason")
                .lastName("Momoa")
                .phone("12245630679")
                .email("momoa@gmail.com")
                .address("Washington, DC")
                .description("All fields")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(contact), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization", token)
                .build();

        Response response = client.newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 409);
        Assert.assertEquals(response.code(), 200);

        ErrorDTO error = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertTrue(error.getMessage().toString().contains("User already exists"));
    }

}
