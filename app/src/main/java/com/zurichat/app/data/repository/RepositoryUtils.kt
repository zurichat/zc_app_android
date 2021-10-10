package com.zurichat.app.data.repository

import com.zurichat.app.data.localSource.Cache
import com.zurichat.app.models.User
import com.zurichat.app.models.organization_model.OrganizationCreatorIdData

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created on 25-Sep-21 at 4:04 AM
 */

val auth by lazy {
    (Cache.map.get("user") as User).token
}

val orgId by lazy {
    (Cache.map.get("orgId") as OrganizationCreatorIdData).organization_id
}