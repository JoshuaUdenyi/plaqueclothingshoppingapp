package com.codesultan.plaqueclothing.data

import com.google.gson.annotations.SerializedName

data class ResponseItems(
    val page: Int,
    val size: Int,
    val total: Int,
    val debug: Any?,
    @SerializedName("previous_page") val previousPage: Any?,
    @SerializedName("next_page") val nextPage: Any?,
    val items: List<ResponseItem>
)

data class ResponseItem(
    val name: String,
    val description: String,
    @SerializedName("unique_id") val uniqueId: String,
    @SerializedName("url_slug") val urlSlug: String,
    @SerializedName("is_available") val isAvailable: Boolean,
    @SerializedName("is_service") val isService: Boolean,
    @SerializedName("previous_url_slugs") val previousUrlSlugs: Any?,
    val unavailable: Boolean,
    @SerializedName("unavailable_start") val unavailableStart: Any?,
    @SerializedName("unavailable_end") val unavailableEnd: Any?,
    val id: String,
    @SerializedName("parent_product_id") val parentProductId: Any?,
    val parent: Any?,
    @SerializedName("organization_id") val organizationId: String,
    @SerializedName("stock_id") val stockId: Any?,
    @SerializedName("product_image") val productImage: List<Any>,
    val categories: List<Any>,
    @SerializedName("date_created") val dateCreated: String,
    @SerializedName("last_updated") val lastUpdated: String,
    @SerializedName("user_id") val userId: String,
    val photos: List<Photo>,
    val prices: Any?,
    val stocks: Any?,
    @SerializedName("current_price") val currentPrice: List<CurrentPrice>,
    @SerializedName("is_deleted") val isDeleted: Boolean,
    @SerializedName("available_quantity") val availableQuantity: Any?,
    @SerializedName("selling_price") val sellingPrice: Any?,
    @SerializedName("discounted_price") val discountedPrice: Any?,
    @SerializedName("buying_price") val buyingPrice: Any?,
    @SerializedName("extra_infos") val extraInfos: Any?,
    @SerializedName("featured_reviews") val featuredReviews: Any?,
    val unavailability: List<Any>
)

data class Photo(
    @SerializedName("model_name") val modelName: String,
    @SerializedName("model_id") val modelId: String,
    @SerializedName("organization_id") val organizationId: String,
    val filename: String,
    val url: String,
    @SerializedName("is_featured") val isFeatured: Boolean,
    @SerializedName("save_as_jpg") val saveAsJpg: Boolean,
    @SerializedName("is_public") val isPublic: Boolean,
    @SerializedName("file_rename") val fileRename: Boolean,
    val position: Int
)

data class CurrentPrice(
    val NGN: List<Any?>
)

sealed class ProductState {
    object Loading : ProductState()
    data class Success(val data: ResponseItem) : ProductState()
    data class Error(val errorMessage: String) : ProductState()
}
sealed class ProductsState {
    object Loading : ProductsState()
    data class Success(val data: List<ResponseItem>) : ProductsState()
    data class Error(val errorMessage: String) : ProductsState()
}
