package okhttp;

import com.google.gson.Gson;
import dto.AuthRequestDTO;
import dto.AuthResponseDTO;
import dto.ErrorDTO;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;

public class RegistrationTestsOkHttp {

    Gson gson = new Gson();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Test
    public void registrationSuccess() throws IOException {

        int i = (int)(System.currentTimeMillis()/1000)%3600;

        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("masha"+i+"@gmail.com")
                .password("Mash123456$").build();

        RequestBody requestBody = RequestBody.create(gson.toJson(auth),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(),200);
        AuthResponseDTO responseDto=
                gson.fromJson(response.body().string(),AuthResponseDTO.class);
        String  token = responseDto.getToken();
        System.out.println(token);
    }

    @Test
    public void registrationWrongEmail() throws IOException {
        int i = (int) (System.currentTimeMillis()/1000)%3600;
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("Masha"+i+".gmail.com")
                .password("Masha123456$").build();

        RequestBody body = RequestBody.create(gson.toJson(auth),JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);
        ErrorDTO error = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(error.getStatus(),400);
        Assert.assertEquals(error.getError(), "Bad Request");
        Assert.assertEquals(error.getMessage().toString(), "{username=must be a well-formed email address}");
        Assert.assertEquals(error.getPath(), "/v1/user/registration/usernamepassword");
    }

    @Test
    public void registrationWrongPassword() throws IOException {

        int i = (int) (System.currentTimeMillis()/100)%3600;
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("stom"+i+"@gmail.com")
                .password("Rgda")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(auth), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(),ErrorDTO.class);
        Assert.assertEquals(errorDTO.getError(), "Bad Request");
        Assert.assertEquals(errorDTO.getMessage().toString(), "{password= At least 8 characters; Must contain at least 1 uppercase letter, 1 lowercase letter, and 1 number; Can contain special characters [@$#^&*!]}");
        Assert.assertEquals(errorDTO.getPath(), "/v1/user/registration/usernamepassword");
    }

    @Test
    public void registrationRegisteredUser() throws IOException {

        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("np7788@inbox.ru")
                .password("WadiNisnas8#")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(auth), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body).build();
        Response response = client.newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),409);
        ErrorDTO error = gson.fromJson(response.body().string(), ErrorDTO.class);
        System.out.println(error);
        Assert.assertEquals(error.getError(), "Conflict");
        Assert.assertEquals(error.getStatus(), 409);
        Assert.assertEquals(error.getMessage().toString(),"User already exists");

    }
}