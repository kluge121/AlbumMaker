package com.globe.albummaker.data.local

import java.io.Serializable

data class SelectOptionData(
        var title: String = "",
        var shape: Int = 0,
        var size: Int = 0,
        var cover: Int = 0,
        var coverCoating: Int = 0,
        var inner: Int = 0
) : Serializable