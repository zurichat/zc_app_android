package com.tolstoy.zurichat.data.repository

import com.tolstoy.zurichat.data.localSource.Cache
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.models.network_response.OrganizationCreatorIdData

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created on 25-Sep-21 at 4:04 AM
 */

val auth by lazy {
    (Cache.map.get("user") as User).token
}

val orgId by lazy {
    (Cache.map.get("orgId") as OrganizationCreatorIdData).InsertedID
}