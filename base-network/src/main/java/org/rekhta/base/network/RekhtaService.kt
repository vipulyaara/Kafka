package org.rekhta.base.network

import okhttp3.ResponseBody
import org.rekhta.base.network.model.auth.AuthLoginRequestBody
import org.rekhta.base.network.model.auth.AuthResponseContainer
import org.rekhta.base.network.model.auth.AuthSignupRequestBody
import org.rekhta.base.network.model.content.CommentResponseContainer
import org.rekhta.base.network.model.content.ContentResponseContainer
import org.rekhta.base.network.model.content.ContentTypeResponseContainer
import org.rekhta.base.network.model.content.type.AppContentTypeResponse
import org.rekhta.base.network.model.contentdetail.ContentCountResponseContainer
import org.rekhta.base.network.model.contentdetail.ContentDetailResponseContainer
import org.rekhta.base.network.model.contentdetail.RelatedContentResponseContainer
import org.rekhta.base.network.model.favorite.FavoriteResponseContainer
import org.rekhta.base.network.model.favorite.SpecificFavoriteBody
import org.rekhta.base.network.model.favorite.SpecificFavoriteResponseContainer
import org.rekhta.base.network.model.home.HomepageResponseContainer
import org.rekhta.base.network.model.poet.PoetResponseContainer
import org.rekhta.base.network.model.poet.detail.PoetDetailResponseContainer
import org.rekhta.base.network.model.search.ExploreResponseContainer
import org.rekhta.base.network.model.search.SearchResponse
import org.rekhta.base.network.model.tags.TagResponseContainer
import org.rekhta.base.network.model.word.WordMeaningResponseContainer
import retrofit2.Call
import retrofit2.http.*

interface RekhtaService {
//    https://world-api.azureedge.net/api/v5/shayari/SearchContentByTypePageWise?
//    // type=1&pageIndex=2&lang=1&keyword=dard

//    https://world-api.azureedge.net/api/v5/shayari/
//    // GetRekhtaDictionaryMeanings?keyword=khak

//    https://world-api.azureedge.net/api/v5/shayari/GetContentTypeTabByType?targetType=1&targetIdSlug=b29b62bb-2452-4e81-8118-6c21dfa64d4f&lang=1

//    https://world-api.azureedge.net/api/v5/shayari/GetT20
//    https://world-api.azureedge.net/api/v5/shayari/GetTagsListWithTrendingTag?lang=1
//    https://world-api.azureedge.net/api/v5/shayari/GetOccasionList?pageIndex=1&lang=1
//    https://world-api.azureedge.net/api/v5/shayari/GetCollectionListByCollectionType?collectionType=1&contentTypeId=43d60a15-0b49-4caf-8b74-0fcdddeb9f83&pageIndex=1&lang=1&keyword=

//    https://world-api.azureedge.net/api/v5/shayari/GetAudioListByPoetIdWithPaging?poetId=c5097803-df0b-44eb-91fc-b20413837614&pageIndex=1&keyword=
//    https://world.rekhta.org/api/v5/shayari/GetAllFavoriteListWithPaging?lastFetchDate=&pageIndex=1&keyword=&favType=3

//    https://world-api.azureedge.net/api/v5/shayari/GetCoupletListWithPaging?poetId=&targetId=2c3a2883-b2e7-4323-b135-18a66a4584bf&pageIndex=1&contentTypeId=f722d5dc-45da-41ec-a439-900df702a3d6&keyword=
//
//    https://world-api.azureedge.net/api/v5/shayari/GetContentListWithPaging
//    ?poetId=&targetId=2eab8a3f-093c-4d15-bcea-aa3d28a543b5&pageIndex=1&
//    contentTypeId=c54c4d8b-7e18-4f70-8312-e1c2cc028b0b&sortBy=0&keyword=
//    https://world-api.azureedge.net/api/v5/shayari/GetCollectionListByCollectionType?collectionType=1&pageIndex=1&contentTypeId=43d60a15-0b49-4caf-8b74-0fcdddeb9f83&lang=1&keyword=

    @POST("GetHomePageCollection")
    suspend fun getHomePageResponse(
        @Query("lang") lang: Int,
        @Query("lastFetchDate") lastFetchDate: String?
    ): HomepageResponseContainer

    @POST("GetPoetCompleteProfile")
    fun getPoetProfile(
        @Query("poetId") poetId: String?,
        @Query("lang") lang: Int
    ): Call<PoetDetailResponseContainer>

    @JvmSuppressWildcards
    @POST("GetContentListWithPaging")
    suspend fun getPagedContentList(@QueryMap params: Map<String, Any?>): ContentResponseContainer

    @JvmSuppressWildcards
    @POST("GetCoupletListWithPaging")
    suspend fun getSherListWithPaging(@QueryMap params: Map<String, Any?>): ContentResponseContainer

    @JvmSuppressWildcards
    @POST("GetShayariImagesWithSearch")
    suspend fun getImageShayriWithPaging(@QueryMap params: Map<String, Any?>): ContentResponseContainer

    @JvmSuppressWildcards
    @POST("GetAudioListByPoetIdWithPaging")
    suspend fun getAudiosWithPaging(@QueryMap params: Map<String, Any?>): ContentResponseContainer

    @JvmSuppressWildcards
    @POST("GetPoetsListWithPaging")
    suspend fun getPoetListWithPaging(@QueryMap params: Map<String, Any?>): PoetResponseContainer

    @POST("GetPoetsListWithPaging")
    suspend fun getPoetListWithPaging(
        @Query("targetId") targetId: String?,
        @Query("lastFetchDate") lastFetchDate: String?,
        @Query("keyword") keyword: String?,
        @Query("pageIndex") pageIndex: Int
    ): PoetResponseContainer

    @JvmSuppressWildcards
    @POST
    suspend fun getFavorites(
        @Url url: String,
        @QueryMap params: Map<String, Any?>
    ): ContentResponseContainer

    @JvmSuppressWildcards
    @POST
    suspend fun fetchFavorites(
        @Url url: String,
        @QueryMap params: Map<String, Any?>
    ): FavoriteResponseContainer

    @JvmSuppressWildcards
    @POST
    suspend fun getSpecificFavorite(
        @Url url: String,
        @Body body: SpecificFavoriteBody
    ): SpecificFavoriteResponseContainer

    @POST
    suspend fun getAllFavorites(
        @Query("lastFetchDate") lastFetchDate: String,
        @Query("pageIndex") pageIndex: Int,
        @Query("keyword") keyword: String,
        @Query("favType") favType: String?,
    ): FavoriteResponseContainer

    @JvmSuppressWildcards
    @POST("GetShayariImagesWithSearch")
    suspend fun getShayariImages(@QueryMap params: Map<String, Any?>): ContentResponseContainer

    @GET("GetContentById")
    suspend fun getContentDetailById(
        @Query("contentId") contentId: String,
        @Query("lang") lang: Int
    ): ContentDetailResponseContainer

    @GET("GetBottomContentByIdSlug")
    suspend fun getRelatedContent(
        @Query("contentId") contentId: String,
        @Query("lang") lang: Int,
        @Query("listSlug") slug: String
    ): RelatedContentResponseContainer

    @GET("GetCountingSummaryByTargetId")
    fun getContentCounts(@Query("targetId") targetId: String): Call<ContentCountResponseContainer>

    @POST("GetWordMeaning")
    fun getWordMeaning(@Query("word") wordId: String): Call<WordMeaningResponseContainer>

    @POST("GetExplore")
    fun getExplorePage(@Query("lang") lang: Int): Call<ExploreResponseContainer>

    @POST("SearchAll")
    suspend fun searchAll(
        @Query("keyword") keyword: String,
        @Query("lang") lang: Int,
        @Query("pageIndex") pageIndex: Int,
    ): SearchResponse

    @POST("SearchAllLoadOnDemand")
    suspend fun searchAllOnDemand(
        @Query("keyword") keyword: String,
        @Query("lang") lang: Int,
        @Query("pageIndex") pageIndex: Int,
    ): SearchResponse

    @GET("GetContentTypeTabByType")
    fun getContentTabsByType(
        @Query("lang") lang: Int,
        @Query("targetIdSlug") targetIdSlug: String
    ): Call<ContentTypeResponseContainer>

    @POST
    suspend fun login(
        @Url url: String,
        @Body authLoginRequestBody: AuthLoginRequestBody
    ): AuthResponseContainer

    @POST
    suspend fun signup(
        @Url url: String,
        @Body requestBody: AuthSignupRequestBody
    ): AuthResponseContainer

    @POST("GetContentTypeList")
    fun getContentTypes(
        @Query("lastFetchDate") lastFetchDate: String
    ): Call<AppContentTypeResponse>

    @POST
    suspend fun markFavorite(
        @Url url: String,
        @Query("targetId") targetId: String,
        @Query("language") language: Int,
        @Query("favType") favType: Int,
    ): ResponseBody

    @POST
    suspend fun removeFavorite(
        @Url url: String,
        @Query("targetId") targetId: String,
        @Query("language") language: Int,
        @Query("favType") favType: Int,
    ): ResponseBody
//    https://world.rekhta.org/api/v5/shayari/GetAllFavoriteListWithPaging?lastFetchDate=&pageIndex=1&keyword=&favType=3

    @POST("GetContentTypeTabByCollectionType")
    suspend fun getCollectionTypes(
        @Query("collectionType") collectionType: Int = 1,
        @Query("pageIndex") pageIndex: String,
        @Query("keyword") keyword: String,
        @Query("favType") favType: String,
    ): ContentTypeResponseContainer

    @GET
    suspend fun getAllCommentsByTargetId(
        @Url url: String,
        @Query("isAsc") isAscending: Int,
        @Query("sortBy") sortBy: Int,
        @Query("TI") targetId: String,
    ): CommentResponseContainer

    @POST("UserAppInfo")
    suspend fun getUserAppInfo(
        @Query("appVersion") appVersion: String,
        @Query("deviceType") deviceType: String
    ): ResponseBody

    @POST("GetTagsListWithTrendingTag")
    suspend fun getTags(@Query("lang") lang: Int): TagResponseContainer
}
