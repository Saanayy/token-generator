package com.example.token.Utilities;

import com.example.token.Modals.LoginCredential;
import com.example.token.Modals.LoginResult;
import com.example.token.Modals.Pass;
import com.example.token.Modals.PassGenerationResult;
import com.example.token.Modals.RegistrationResult;
import com.example.token.Modals.SinglePassResult;
import com.example.token.Modals.User;
import com.example.token.Modals.UserPassesResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {

    SharedPrefManager sPref = SharedPrefManager.getInstance();

    @POST("/api/register")
    Call<RegistrationResult> register(@Body User user);

    @POST("/api/login")
    Call<LoginResult> login(@Body LoginCredential loginCredential);


    @POST("/api/generate_pass")
    Call<PassGenerationResult> createPass(@Header("x-access-tokens") String token, @Body Pass pass);

    @Headers("Content-Type: application/json")
    @GET("/api/user_passes")
    Call<UserPassesResult> getPasses(@Header("x-access-tokens") String token);

    @Headers("Content-Type: application/json")
    @POST("/api/police/get_pass")
    Call<SinglePassResult> getPass(@Body Map<String, String> body);

    @GET("/api/police/get_passes/{status}")
    Call<UserPassesResult> getStatusPass(@Path("status") int status);

    @Headers("Content-Type: application/json")
    @PUT("/api/police/validate_pass")
    Call<RegistrationResult> updateStatus(@Body Map<String, String> body);
//
//    @GET("/api/items")
//    Call<List<Item>> getItems(@QueryMap Map<String, String> map);
//
//    @GET("/api/employee/{email}")
//    Call<Employee> getEmployee(@Path("email") String email);
//
//    @POST("/api/employee/register")
//    Call<Employee> postEmployee(@Body Employee employee);
//
//    @GET("/api/employee/resendPasscode/{email}")
//    Call<String> getResend(@Path("email") String email);
//
//    @PUT("/api/items")
//    Call<Item> updateItem(@Body Item item);


}
