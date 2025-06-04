package usth.intern.notifts.data.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Streaming
import usth.intern.notifts.domain.NotificationContent

interface WavApiService {
    @POST("/tts-service")
    @Streaming
    suspend fun downloadWav(@Body notification: NotificationContent): Response<ResponseBody>
}