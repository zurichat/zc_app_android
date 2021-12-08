package com.zurichat.app.utils.views.list_item

import com.zurichat.app.ui.base.BaseItem
import com.zurichat.app.utils.Z_CODE
import kotlin.reflect.KClass

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 05-Dec-21 at 12:59 AM
 *
 */
object BaseItemUtil {
    val idMap = mutableMapOf<KClass<*>, Int>()
    var increment = 0

    inline fun <reified T: BaseItem<*, *>> itemId(): Int {
        return idMap.computeIfAbsent(T::class) { Z_CODE + ++increment }
    }
}