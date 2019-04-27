package com.hackathon.data.repository

import android.content.Context
import com.hackathon.Constants
import com.hackathon.R
import com.hackathon.data.api.ApiResult
import com.hackathon.data.api.CommentApi
import com.hackathon.data.error.LoginError
import com.hackathon.data.error.LoginErrorType
import com.hackathon.data.error.ServerError
import com.hackathon.data.model.GetCommentRequest
import com.hackathon.data.model.GetCommentResponse
import com.hackathon.di.ILogger
import com.hackathon.lib.typing.Ok
import com.hackathon.lib.typing.single
import com.hackathon.utils.PreferenceUtils
import com.hackathon.utils.get


class CommentRepository(
        private val context: Context,
        private val logger: ILogger,
        private val commentApi: CommentApi
) {
    fun getComments(productId: Int, storeId: Int, shelfId: Int): ApiResult<GetCommentResponse> {
        val uid: Int? = PreferenceUtils.defaultPrefs(context)[Constants.UID]
        return if (uid != null && uid > 0) {
            commentApi.getComments(GetCommentRequest(uid, storeId, productId, shelfId)).flatMap {
                if (it.isSuccessful && it.body() is GetCommentResponse) {
                    val response = it.body() as GetCommentResponse
                    Ok(response).single()
                } else {
                    ServerError(context.getString(R.string.errorOccurred)).toErr().single()
                }
            }
        } else {
            ServerError(context.getString(R.string.errorOccurred)).toErr().single()
        }


    }
}