package com.example.android.homely.SendNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA1mbyMq0:APA91bF46gmrGDdSsPkSfe6ASawMnrzXt8RiXH3ACZ5ByjJBXhw_appq51MfVT2fc73ciT1Ge6Sg83GgD_qTcWylCUcgRq4gT_tKOy5F9EF_zvhZmCyviaFLm6lTkk9g2ath7KOSEiIS"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);
}
