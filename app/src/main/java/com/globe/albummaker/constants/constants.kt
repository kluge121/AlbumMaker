package com.globe.albummaker.constants

import com.globe.albummaker.R


const val NEW_ALBUM = -100
const val STORED_ALBUM = -200
const val TEMPLATE_SELECT = 523

const val LEFT_PAGE = 123
const val RIGHT_PAGE = 456


val album8BasePrice = arrayOf(20315, 25415, 23715, 28815)
val album10BasePrice = arrayOf(28720, 38320, 34320, 43920)
val album12BasePrice = arrayOf(31920, 44720, 38320, 51120)
val albumSquareBasePrice = arrayOf(album8BasePrice, album10BasePrice, album12BasePrice)

val pagePriceOption = arrayOf(850, 1200, 1600)

val shapeOption = arrayOf("정사각형")
val shapeOption1 = arrayOf("정사각형")

val sizeOption = arrayOf("8x8", "10x10", "12x12")
val sizeOption1 = arrayOf("8x8", "10x10", "12x12")


val coverOption = arrayOf("하드커버", "소프트커버", "레더커버")
val coverOption1 = arrayOf("하드커버", "소프트커버", "레더커버")
val coverOption2 = arrayOf("하드커버", "레더커버")

val coverCoatingOption = arrayOf("무광", "유광")
val coverCoatingOption1 = arrayOf("무광", "유광")
val coverCoatingOption2 = arrayOf("무광")

val innerOption = arrayOf("무광", "유광", "레이플랫")
val innerOption1 = arrayOf("무광", "유광", "레이플랫")
val innerOption2 = arrayOf("무광")


val albumType = arrayOf(R.layout.fragment_template0, R.layout.fragment_template1)