package com.example.admin.Utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
//  use this url for the deployed url
//public static final String BASE_URL = "http://202.56.13.210:5000/";

//    //    use this url when the IP is static. Sanay
    public static final String BASE_URL = "http://192.168.43.159:5000/";



//        use this url when the IP is static. Suraj
//      public static final String BASE_URL = "http://192.168.43.193:5000/";

    //    This is the singleton pattern for declaring the instance of retrofit.
    public static Retrofit retrofit = null;


    public static Retrofit getApiClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(builder.build())
                    .addConverterFactory(GsonConverterFactory.create(gson))

                    .build();
        }

        return retrofit;
    }

}



