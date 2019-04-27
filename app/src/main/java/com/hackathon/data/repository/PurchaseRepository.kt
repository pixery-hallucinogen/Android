package com.hackathon.data.repository

import android.content.Context
import com.hackathon.Constants
import com.hackathon.R
import com.hackathon.data.api.ApiResult
import com.hackathon.data.api.PurchaseApi
import com.hackathon.data.error.ServerError
import com.hackathon.data.model.PurchaseRequest
import com.hackathon.di.ILogger
import com.hackathon.lib.typing.Ok
import com.hackathon.lib.typing.single
import com.hackathon.utils.PreferenceUtils
import com.hackathon.utils.get


class PurchaseRepository(
        private val context: Context,
        private val logger: ILogger,
        private val purchaseApi: PurchaseApi
) {
    fun purchase(productId: Int, storeId: Int, amount: Int): ApiResult<Unit> {
        val uid: Int? = PreferenceUtils.defaultPrefs(context)[Constants.UID]
        return if (uid != null && uid > 0) {
            purchaseApi.purchase(PurchaseRequest(uid, storeId, productId, amount)).flatMap {
                if (it.isSuccessful) {
                    Ok(Unit).single()
                } else {
                    ServerError(context.getString(R.string.errorOccurred)).toErr().single()
                }
            }
        } else {
            ServerError(context.getString(R.string.errorOccurred)).toErr().single()
        }
    }
}