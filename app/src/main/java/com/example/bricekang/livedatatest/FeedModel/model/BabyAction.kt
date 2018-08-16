package com.example.bricekang.livedatatest.FeedModel.model

import java.util.*

abstract class BabyAction (val actionType: Type) {
    enum class Type(val type: Int) {
        FEED(1), POOP(2)
    }

    var actionDate: Date

    init {
        this.actionDate = Date()
    }
}