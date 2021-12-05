package com.operationsmiley.aarogyaecare.SendNotificationPack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAygxzMNE:APA91bGvDudr1QWJOzyuJvnEdJagVNqVCHx5Qc4MtvUO0vWVswAQG7oTgUwSaFq4wP6HoHoZfi0cXQBw80JjdxkOjTF2Q0ItzQ1kfTbm0TUe-yrvLhJBj6JtIO6hdJxPkpS_q8qDL5WR" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<com.operationsmiley.aarogyaecare.SendNotificationPack.MyResponse> sendNotifcation(@Body com.operationsmiley.aarogyaecare.SendNotificationPack.NotificationSender body);
}

