/*
 * Copyright 2020 Google LLC
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

package com.google.samples.apps.sunflower.data

import android.util.Log
import com.google.samples.apps.sunflower.api.UnsplashService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import retrofit2.HttpException
import java.io.IOException

@ExperimentalCoroutinesApi
class UnsplashRepository (private val service: UnsplashService) {

    private val searchResult = ConflatedBroadcastChannel<UnsplashSearchResult>()

    @FlowPreview
    suspend fun getSearchResultStream(query: String): Flow<UnsplashSearchResult> {
        requestData(query)
        return searchResult.asFlow()
    }

    private suspend fun requestData(query: String) {
        try {
            val response = service.searchPhotos(query)
            searchResult.offer(UnsplashSearchResult.Success(response))
        } catch (exception: IOException) {
            searchResult.offer(UnsplashSearchResult.Error(exception))
        } catch (exception: HttpException) {
            searchResult.offer(UnsplashSearchResult.Error(exception))
        }
    }
}
