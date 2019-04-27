package com.hackathon.di.module

import android.content.Context
import android.util.Log
import com.hackathon.Constants
import com.hackathon.R
import com.hackathon.data.api.PostApi
import com.hackathon.data.api.UserApi
import com.hackathon.utils.PreferenceUtils
import com.hackathon.utils.get
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*

class RetrofitClient(
        private val context: Context
) {
    fun userApi(): UserApi = getRetrofit().create(UserApi::class.java)
    fun postApi(): PostApi = getRetrofit().create(PostApi::class.java)

    private fun getRetrofit(): Retrofit {

        return Retrofit.Builder()
                .baseUrl(context.getString(R.string.api_url))
                .client(getUnsafeOkHttpClient())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    fun getUnsafeOkHttpClient(): OkHttpClient {
        try {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }

                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                }
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            return OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(object : Interceptor {
                        override fun intercept(chain: Interceptor.Chain): Response {
                            val token: String? = PreferenceUtils.defaultPrefs(context)[Constants.JWT]
                            Log.d("Pixery", token)
                            if (!token.isNullOrEmpty()) {
                                val request = chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build()
                                return chain.proceed(request)
                            }
                            return chain.proceed(chain.request())
                        }
                    })
                    .hostnameVerifier(object : HostnameVerifier {
                        override fun verify(hostname: String, session: SSLSession): Boolean {
                            return true
                        }
                    }).build()

        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}