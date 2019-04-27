package com.hackathon.ui.home

import android.content.Context
import com.hackathon.data.model.Product
import com.hackathon.di.ILogger
import com.hackathon.di.module.SchedulersModule
import com.hackathon.domain.auth.AuthTask
import com.hackathon.lib.event.ObservableResult
import com.hackathon.lib.typing.Ok
import com.hackathon.ui.base.BaseViewModel
import java.util.Arrays.asList

class HomeViewModel(
        private val logger: ILogger,
        schedulersModule: SchedulersModule,
        private val authTask: AuthTask
) : BaseViewModel(schedulersModule) {
    val onLoggedOut = ObservableResult<Unit>()
    val onRecommendationsFetched = ObservableResult<List<Product>>()

    fun logout(context: Context) {
        authTask.logout(context)
        onLoggedOut.trigger(Ok(Unit))
    }

    fun fetchRecommendations() {
        val data = listOf(
                Product(_id = "1",
                        productId = 1,
                        productName = "Papatya Küresi",
                        tags = asList("Çiçek"),
                        imageUrl = "https://cdn03.ciceksepeti.com/cicek/at2091-1/L/papatya-kuresi-at2091-1-92.jpg",
                        currentPrice = "62,99TL",
                        price = "99,99TL",
                        taksit = "6 x 10,49 TL Taksit Seçeneği"),
                Product(_id = "2",
                        productId = 2,
                        productName = "Aşkın Simgesi Güller ve Papatyalar",
                        tags = asList("Çiçek"),
                        imageUrl = "https://cdn03.ciceksepeti.com/cicek/at2358-1/L/askin-simgesi-guller-ve-papatyalar-at2358-1-1.jpg",
                        currentPrice = "59,99TL",
                        price = "79,99TL",
                        taksit = "6 x 9,99 TL Taksit Seçeneği"),
                Product(_id = "3",
                        productId = 3,
                        productName = "Mutluluk Kutusu",
                        tags = asList("Çiçek"),
                        imageUrl = "https://cdn03.ciceksepeti.com/cicek/at2920-1/L/mutluluk-kutusu-at2920-1-636149066333279461.jpg",
                        currentPrice = "89,99TL",
                        price = "99,99TL",
                        taksit = "6 x 14,99 TL Taksit Seçeneği"),
                Product(_id = "4",
                        productId = 4,
                        productName = "Doğal Ahşap Kütükte Papatya Aranjmanı",
                        tags = asList("Çiçek"),
                        imageUrl = "https://cdn03.ciceksepeti.com/cicek/at3709-1/L/dogal-ahsap-kutukte-papatya-aranjmani-at3709-1-8d67d5ca3dc61be-69b93c3b.jpg",
                        currentPrice = "46,99TL",
                        price = "55,99TL",
                        taksit = "6 x 7,83 TL Taksit Seçeneği"),
                Product(_id = "5",
                        productId = 5,
                        productName = "Beyaz Papatyalar ve Çikolata",
                        tags = asList("Set"),
                        imageUrl = "https://cdn03.ciceksepeti.com/cicek/at3462-1/L/beyaz-papatyalar-ve-spesiyal-cikolata-at3462-1-8d44ea37d4682d7-3beae7ee.jpg",
                        price = "118,89TL",
                        currentPrice = "79,99TL",
                        taksit = "6 x 13,33 TL Taksit Seçeneği")
        )

        onRecommendationsFetched.trigger(Ok(data))
    }
}
