package com.v60BNS.services;


import com.v60BNS.models.ExpertModel;
import com.v60BNS.models.MessageDataModel;
import com.v60BNS.models.NearbyStoreDataModel;
import com.v60BNS.models.PlaceGeocodeData;
import com.v60BNS.models.PostModel;
import com.v60BNS.models.ReviewModels;
import com.v60BNS.models.RoomModelID;
import com.v60BNS.models.StoryModel;
import com.v60BNS.models.UserModel;

import org.androidannotations.annotations.rest.Post;

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

    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);

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

    @Multipart
    @POST("api/apiStories")
    Call<ResponseBody> addstory(@Header("Authorization") String user_token,
                                @Part MultipartBody.Part logo


    );

    @GET("api/apiStories")
    Call<StoryModel> getStories(@Query("pagination") String pagination,
                                @Query("type") String type
    );

    @GET("api/apiPosts")
    Call<PostModel> getposts(@Query("pagination") String pagination,
                             @Query("user_id") String user_id
    );

    @GET("api/myPosts")
    Call<PostModel> getmyposts(@Query("pagination") String pagination,
                               @Query("user_id") String user_id
    );

    @Multipart
    @POST("api/apiPosts")
    Call<ResponseBody> addpost(@Header("Authorization") String user_token,
                               @Part("ar_desc") RequestBody ar_desc,
                               @Part("place_id") RequestBody place_id,
                               @Part("address") RequestBody address,
                               @Part("latitude") RequestBody latitude,
                               @Part("longitude") RequestBody longitude,

                               @Part MultipartBody.Part logo


    );

    @FormUrlEncoded
    @POST("api/apiPosts/love")
    Call<ResponseBody> likepost(
            @Header("Authorization") String Authorization,
            @Field("post_id") String post_id)
            ;
    @GET("api/currentUser")
    Call<UserModel> getprofile(
            @Header("Authorization") String Authorization,
            @Field("phone") String phone);
    @GET("api/experts")
    Call<ExpertModel> getExperts(@Query("pagination") String pagination
    );

    @POST("api/chatRoom/get")
    Call<RoomModelID> getchatroom(
            @Header("Authorization") String Authorization,
            @Field("from_user_id") String from_user_id,
            @Field("to_user_id") String to_user_id
            );
    @FormUrlEncoded
    @POST("api/single-chat-room")
    Call<MessageDataModel> getRoomMessages(@Field("user_id") int user_id,
                                           @Field("room_id") int room_id,
                                           @Field("page") int page
    );
}