package com.skyscanner.repository;

interface ConfigGateway {

    fun getApiKey(): String
    fun getCreateSessionUrl(): String

}
