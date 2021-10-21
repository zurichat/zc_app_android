package com.zurichat.app.models

import java.util.*


sealed class SearchItem<T, U>(val room: T? = null, val channel: U? = null) {
    class Room<T, U>(room: T) : SearchItem<T, U>(room)
    class Channel<T, U>(channel: U?) : SearchItem<T, U>(channel = channel)

    override fun toString(): String {
        return "RVItem(${this.room}$channel)"
    }
}