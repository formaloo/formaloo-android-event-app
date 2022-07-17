package com.formaloo.home.ui.gallery

import android.util.ArrayMap

val photoSource = ArrayMap<String, String>()

class GalleryItemsSource {

    fun addPhotoDetail(photoFieldSlug: String, imageUrl: String) {
        photoSource[photoFieldSlug] = imageUrl
    }

    fun getPhotoSource(): ArrayMap<String, String> {
        return photoSource
    }
}
