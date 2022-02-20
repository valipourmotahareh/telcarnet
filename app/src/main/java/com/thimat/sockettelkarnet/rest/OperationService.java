package com.thimat.sockettelkarnet.rest;

import com.thimat.sockettelkarnet.models.GetLatLonCar;
import com.thimat.sockettelkarnet.models.ListCarModel;
import com.thimat.sockettelkarnet.models.ListNewsModel;
import com.thimat.sockettelkarnet.models.ListagentModel;
import com.thimat.sockettelkarnet.models.ListguideModel;
import com.thimat.sockettelkarnet.models.ReadTicketModel;
import com.thimat.sockettelkarnet.models.Responce;
import com.thimat.sockettelkarnet.models.contactListModel;
import com.thimat.sockettelkarnet.models.offline;
import com.thimat.sockettelkarnet.models.online;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface OperationService {
    //-------------------------------login-----------------------------
    @GET("act/{u}/{p}")
    Call<ListCarModel> loginUser(@Path("u") String u, @Path("p") String p);
    //------------------------------ offline----------------------------
    @Headers("Content-Type: application/json")
    @POST("offline2")
    Call<offline> GetOffline(@Body String jsonString);
    //------------------------------ online-----------------------------
    @GET("online/{id}")
    Call<online> GetOnline(@Path("id") int person_id);
    //----------------------------------------------- send GetDistributionPathPromotersLog
    @GET("GetDistributionPathPromotersLog/{promoterid}/{apikey}")
    Call<GetLatLonCar> postDistributionPathPromotersLog(@Path("promoterid") int promoterid, @Path("apikey") String apikey);
    //---------------------------------------------- delete car
    @GET("delet/{codecar}/{parameter}")
    Call<Responce> delete(@Path("codecar") int codecar, @Path("parameter") int parameter);
    //---------------------------------------------- rename car-----------
    @Headers("Accept: application/json")
    @POST("rename")
    Call<Responce> rename(@Body String jsonstring);
    //---------------------------------------------- get news------------------
    @GET("news")
    Call<ListNewsModel> GetNews();
    //--------------------------------------------- get agent-----------------
    @GET("agent")
    Call<ListagentModel> GetAgent();
    //---------------------------------------------- get guide-----------------
    @GET("guide")
    Call<ListguideModel> GetGuide();
    //---------------------------------------------- get contact---------------
    @GET("contact")
    Call<contactListModel> Getcontact();
    //------------------------------------------------ send ticket
    @Headers("Accept: application/json")
    @POST("ticket/write")
    Call<Responce> WriteTicket(@Body String jsonstring);
    //------------------------------------------------ send ticket
    @Headers("Accept: application/json")
    @POST("ticket/rewrite")
    Call<Responce> rewriteTicket(@Body String jsonstring);
    //------------------------------------------------- close ticket
    @GET("ticket/close/{id_ticket}")
    Call<Responce> closeticket(@Path("id_ticket") int id_ticket);
    //------------------------------------------------- check ticket
    @GET("ticket/check/{id_ticket}")
    Call<Responce> checkticket(@Path("id_ticket") int id_ticket);
    //-----------------------------------------------------
    @GET("ticket/read/{person_id}/{cod-ticket}")
    Call<ReadTicketModel> ReadTicket(@Path("person_id") int person_id,@Path("cod-ticket") int cod_ticket);
    //------------------------------------------------------Save_car
    @Headers("Accept: application/json")
    @POST("save_car")
    Call<Responce> Savecar(@Body String jsonString);
    //------------------------------------------------------- update
    @GET(ClientConfigs.versionURL)
    @Streaming
    Call<ResponseBody> downloadFile();
}