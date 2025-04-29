package com.absut.cash.management.util

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.absut.cash.management.data.model.Category
import kotlinx.serialization.json.Json

object CustomNavType {

    val CategoryNavType = object : NavType<Category>(isNullableAllowed = true) {
        override fun get(bundle: Bundle, key: String): Category? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Category {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: Category): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: Category) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }

}