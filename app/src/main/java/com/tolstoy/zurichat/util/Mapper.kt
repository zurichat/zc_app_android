package com.tolstoy.zurichat.util

import com.tolstoy.zurichat.data.localSource.entities.UserEntity
import com.tolstoy.zurichat.models.User

fun UserEntity.mapToApp(): User {
    return User(
        created_at,
        display_name,
        email,
        first_name,
        id,
        last_name,
        phone,
        status,
        time_zone,
        updated_at,
        token
    )
}

fun User.mapToEntity(): UserEntity {
    return UserEntity(
        created_at,
        display_name,
        email,
        first_name,
        id,
        last_name,
        phone,
        status,
        time_zone,
        updated_at,
        token
    )
}
