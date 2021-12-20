/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.sunflower.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiManager {
    private val BASE_URL = "https://api.unsplash.com/"
    private var apiService : UnsplashService

    init {
        val logger = HttpLoggingInterceptor().apply {level = HttpLoggingInterceptor.Level.BASIC}
        val client = OkHttpClient.Builder().addInterceptor(logger).build()
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create()).build()
        apiService = retrofit.create(UnsplashService::class.java)
    }

    fun getApiService() : UnsplashService {
        return apiService
    }
}