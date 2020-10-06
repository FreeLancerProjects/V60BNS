package com.v60BNS.services;


import com.v60BNS.models.Add_Order_Model;
import com.v60BNS.models.ExpertModel;
import com.v60BNS.models.MessageDataModel;
import com.v60BNS.models.MessageModel;
import com.v60BNS.models.CategoryDataModel;
import com.v60BNS.models.NearbyStoreDataModel;
import com.v60BNS.models.NotificationDataModel;
import com.v60BNS.models.OrderDataModel;
import com.v60BNS.models.OrderModel;
import com.v60BNS.models.PlaceGeocodeData;
import com.v60BNS.models.PlaceMapDetailsData;
import com.v60BNS.models.PostModel;
import com.v60BNS.models.ProductModel;
import com.v60BNS.models.ReviewModels;
import com.v60BNS.models.SingleProductModel;
import com.v60BNS.models.SliderModel;
import com.v60BNS.models.RoomModelID;
import com.v60BNS.models.SettingModel;
import com.v60BNS.models.StoryModel;
import com.v60BNS.models.UserModel;
import com.v60BNS.models.UserRoomModelData;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {

    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );

    @GET("place/details/json")
    Call<NearbyStoreDataModel> getPlaceReview(@Query(value = "placeid") String placeid,
                                              @Query(value = "key") String key,
                                              @Query(value = "language") String language

                                              );

    @GET("place/nearbysearch/json")
    Call<NearbyStoreDataModel> getNearbyStores(@Query(value = "location") String location,
                                               @Query(value = "radius") int radius,
                                               @Query(value = "type") String type,
                                               @Query(value = "language") String language,
                                               @Query(value = "key") String key
    );
    @GET("place/nearbysearch/json")
    Call<NearbyStoreDataModel> getNearbySearchStores(@Query(value = "location") String location,
                                                     @Query(value = "radius") int radius,
                                                     @Query(value = "name") String query,
                                                     @Query(value = "language") String language,
                                                     @Query(value = "key") String key
    );
    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);

    @FormUrlEncoded
    @POST("api/firebase-tokens")
    Call<ResponseBody> updateToken(
            @Field("user_id") int user_id,
            @Field("firebase_token") String firebase_token,
            @Field("software_type") String software_type
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
    Call<PostModel> getmyposts(
            @Header("Authorization") String Authorization,
            @Query("pagination") String pagination,
            @Query("user_id") String user_id,
            @Query("page") int page
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

    @GET("api/sliders")
    Call<SliderModel> getSliders(@Query("pagination") String pagination);

    @FormUrlEncoded
    @POST("api/apiPosts/love")
    Call<ResponseBody> likepost(
            @Header("Authorization") String Authorization,
            @Field("post_id") String post_id)
            ;

    @GET("api/currentUser")
    Call<UserModel> getprofile(
            @Header("Authorization") String Authorization)
            ;

    @GET("api/experts")
    Call<ExpertModel> getExperts(@Query("pagination") String pagination
    );

    @FormUrlEncoded
    @POST("api/chatRoom/get")
    Call<RoomModelID> getchatroom(
            @Header("Authorization") String Authorization,
            @Field("from_user_id") String from_user_id,
            @Field("to_user_id") String to_user_id
    );

    @FormUrlEncoded
    @POST("api/single-chat-room")
    Call<MessageDataModel> getRoomMessages(
            @Header("Authorization") String Authorization,
            @Field("room_id") int room_id,
            @Field("page") int page
    );

    @FormUrlEncoded
    @POST("api/message/send")
    Call<MessageModel> sendChatMessage(
            @Header("Authorization") String Authorization,
            @Field("room_id") int room_id,
            @Field("from_user_id") int from_user_id,
            @Field("to_user_id") int to_user_id,
            @Field("message_kind") String message_kind,
            @Field("message") String message,
            @Field("date") long date


    );

    @GET("api/app/info")
    Call<SettingModel> getSetting(

    );

    @Multipart
    @POST("api/profile/update")
    Call<UserModel> editClientProfileWithoutImage(
            @Header("Authorization") String Authorization,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email
    );

    @GET("api/categories")
    Call<CategoryDataModel> getMainCategory(@Query("pagination") String pagination);


    @POST("api/products")
    Call<ProductModel> getProduct(
            @Query("pagination") String pagination,
            @Query("category_id") String category_id,
            @Query("limit_per_page") String limit_per_page,
            @Query("page") int page


    );

    @Multipart
    @POST("api/profile/update")
    Call<UserModel> editClientProfileWithImage(
            @Header("Authorization") String Authorization,
            @Part MultipartBody.Part logo,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email


    );

    @GET("api/my-notifications")
    Call<NotificationDataModel> getNotification(
            @Header("Authorization") String Authorization,
            @Header("lang") String lang,
            @Query("page") int page
    );

    @FormUrlEncoded
    @POST("api/single-product")
    Call<SingleProductModel> getSingleProduct(@Field("product_id") String product_id);

    @POST("api/order/creating")
    Call<ResponseBody> accept_orders(
            @Header("Authorization") String Authorization,
            @Body Add_Order_Model add_order_model);

    @GET("api/orders/current")
    Call<OrderDataModel> getcurrentOrders(
            @Header("Authorization") String Authorization,
            @Query("pagination") String pagination,
            @Query("page") int page
    );


    @GET("api/orders/previous")
    Call<OrderDataModel> getfinshiorders(
            @Header("Authorization") String Authorization,
            @Query("pagination") String pagination,
            @Query("page") int page
    );

    @FormUrlEncoded
    @POST("api/single-order")
    Call<OrderModel> getorderdetials(
            @Header("Authorization") String Authorization,
            @Field("order_id") String order_id

    );

    @FormUrlEncoded
    @POST("api/firebase/token/delete")
    Call<ResponseBody> deltePhoneToken(
            @Header("Authorization") String Authorization,
            @Field("firebase_token") String firebase_token,
            @Field("user_id") int user_id

    );

    @FormUrlEncoded
    @POST("api/my-chat-rooms")
    Call<UserRoomModelData> getRooms(
            @Header("Authorization") String Authorization,

            @Field("user_id") int user_id,
            @Field("page") int page
    );
    @FormUrlEncoded
    @POST("api/MakeComment")
    Call<ResponseBody> AddComment(
            @Header("Authorization") String Authorization,
            @Field("post_id") String post_id,
            @Field("comment") String comment
    );
}
