package com.v60BNS.services;


import com.v60BNS.models.NearbyStoreDataModel;
import com.v60BNS.models.ReviewModels;
import com.v60BNS.models.UserModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {


    @GET("place/details/json")
    Call<NearbyStoreDataModel> getPlaceReview(@Query(value = "placeid") String placeid,
                                       @Query(value = "key") String key
    );

    @GET("place/nearbysearch/json")
    Call<NearbyStoreDataModel> getNearbyStores(@Query(value = "location") String location,
                                               @Query(value = "radius") int radius,
                                               @Query(value = "type") String type,
                                               @Query(value = "language") String language,
                                               @Query(value = "key") String key
    );
    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> login(@Field("phone_code") String phone_code,
                          @Field("phone") String phone

    );
    @Multipart
    @POST("api/register")
    Call<UserModel> signUpWithImagewithoutlogo(@Part("name") RequestBody name,
                                    @Part("email") RequestBody email,
                                    @Part("phone_code") RequestBody phone_code,
                                    @Part("phone") RequestBody phone,
                                    @Part("user_type") RequestBody user_type,
                                    @Part("software_type") RequestBody software_type,
                                    @Part MultipartBody.Part banner


    );
    @Multipart
    @POST("api/register")
    Call<UserModel> signUpWithImagewithout(@Part("name") RequestBody name,
                                               @Part("email") RequestBody email,
                                               @Part("phone_code") RequestBody phone_code,
                                               @Part("phone") RequestBody phone,
                                               @Part("user_type") RequestBody user_type,
                                               @Part("software_type") RequestBody software_type,
                                               @Part MultipartBody.Part banner,
                                               @Part MultipartBody.Part logo


    );
    @FormUrlEncoded
    @POST("api/logout")
    Call<ResponseBody> logout(@Header("Authorization") String user_token,
                              @Field("user_id") String user_id


    );
}