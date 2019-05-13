package com.skyscanner.repository.impl

import android.content.Context
import com.skyscanner.repository.ConfigGateway
import javax.inject.Inject

class ConfigGatewayImpl @Inject constructor(context: Context) : ConfigGateway {

    private val apiKey = context.getString(R.string.API_KEY)
    private val createSessionUrl = context.getString(R.string.API_CREATE_SESSION)

    override fun getApiKey(): String {
        return apiKey
    }

    override fun getCreateSessionUrl(): String {
        return createSessionUrl
    }

}