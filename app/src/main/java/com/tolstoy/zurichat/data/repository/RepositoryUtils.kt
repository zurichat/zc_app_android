package com.tolstoy.zurichat.data.repository

import com.tolstoy.zurichat.data.localSource.Cache

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created on 25-Sep-21 at 4:04 AM
 */

val auth by lazy {
    Cache.map.getOrDefault("USER", "").toString()
}