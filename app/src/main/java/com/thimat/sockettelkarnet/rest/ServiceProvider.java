package com.thimat.sockettelkarnet.rest;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thimat.sockettelkarnet.rest.network.UTCDateTypeAdapter;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.realm.RealmObject;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ServiceProvider {
    public final static int WEBSERVICE_GENERAL = 1;
    public final static int WEBSERVICE_GETWAY = 2;
    private Retrofit mRetrofitClient;
    private final OperationService opService;
    private final Gson gson;

    public ServiceProvider(int serviceType,int timeout) {
        gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .registerTypeAdapter(Date.class, new UTCDateTypeAdapter())
                .setLenient()
                .create();

// Serialize a Realm object to a JSON string
//        String json = gson.toJson(realm.where(Person.class).findFirst());
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(Date.class, new UTCDateTypeAdapter())
//                .create();
//        OkHttpClient okHttpClient = new OkHttpClient();
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(timeout, TimeUnit.SECONDS)
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .build();
        if (serviceType == WEBSERVICE_GENERAL) {
            mRetrofitClient = new Retrofit.Builder()
                    .baseUrl(ClientConfigs.REST_API_BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        opService = mRetrofitClient.create(OperationService.class);

    }

    public Retrofit getmRetrofitClient() {
        return mRetrofitClient;
    }

    public OperationService getOpService() {
        return opService;
    }

//    public String realmToJson(TblOrganization result) {
//        String json = gson.toJson(result);
//        return json;
//    }
}

