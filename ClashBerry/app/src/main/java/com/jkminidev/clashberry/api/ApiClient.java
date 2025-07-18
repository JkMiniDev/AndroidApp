package com.jkminidev.clashberry.api;

import com.google.gson.Gson;
import com.jkminidev.clashberry.models.ErrorResponse;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    
    // TODO: Replace with your actual backend URL after hosting
    private static final String BASE_URL = "https://your-backend-url.com/";
    
    private static Retrofit retrofit = null;
    private static ClashApiService clashApiService = null;

    public static ClashApiService getClashApiService() {
        if (clashApiService == null) {
            clashApiService = getRetrofitInstance().create(ClashApiService.class);
        }
        return clashApiService;
    }

    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ErrorResponse parseErrorResponse(Response<?> response) {
        try {
            if (response.errorBody() != null) {
                String errorBody = response.errorBody().string();
                Gson gson = new Gson();
                return gson.fromJson(errorBody, ErrorResponse.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Return default error response
        return new ErrorResponse("unknown_error", "An unknown error occurred", null);
    }

    // Method to update base URL after backend is hosted
    public static void updateBaseUrl(String newBaseUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(newBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        clashApiService = null; // Reset service to use new retrofit instance
    }
}