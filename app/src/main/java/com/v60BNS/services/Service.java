package com.v60BNS.services;


import com.v60BNS.models.ReviewModels;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {


    @GET("place/details/json")
    Call<ReviewModels> getPlaceReview(@Query(value = "placeid") String placeid,
                                       @Query(value = "key") String key
    );

}