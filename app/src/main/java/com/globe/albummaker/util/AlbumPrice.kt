package com.globe.albummaker.util

import com.globe.albummaker.data.local.SelectOptionData
import com.globe.albummaker.constants.albumSquareBasePrice
import com.globe.albummaker.constants.pagePriceOption


class AlbumPrice {
    companion object {
        fun getPrice(option: SelectOptionData, pageCount: Int, isDefaultPrice: Boolean): Int {

            return if (option.shape == 0) {
                squareAlbum(option, pageCount, isDefaultPrice)
            } else {
                0
            }
        }

        private fun squareAlbum(option: SelectOptionData, pageCount: Int, isDefaultPrice: Boolean): Int {

            var baseAlbumPrice = 0
            var pageAddPrice = 0
            var addPageCount = 0
            var pagePrice: Int = pagePriceOption[option.size]

            //내지가 레이플랫이면 페이지 추가가격이 무려 2배
            if (option.inner == 2)
                pagePrice *= 2

            //추가 페이지 가격 계
            if (pageCount > 13) {
                addPageCount = pageCount - 13
                pageAddPrice = addPageCount * pagePrice
            }

            //TODO 8*8 소프트 커버만 미리 처리, 추후 변경 가능성
            if (option.size == 0 && option.cover == 1)
                baseAlbumPrice = 15215
            //정사각형 소프트커버를 제외한 가격 계산
            else {
                var tmpIndex = 0
                if (option.cover == 2)
                    tmpIndex = 2
                if (option.inner == 2)
                    tmpIndex += 1
                baseAlbumPrice = albumSquareBasePrice[option.size][tmpIndex]
            }
            return if (isDefaultPrice)
                baseAlbumPrice
            else
                baseAlbumPrice + pageAddPrice
        }
    }

}