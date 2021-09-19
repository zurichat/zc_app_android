package com.tolstoy.zurichat.util

import com.tolstoy.zurichat.data.localSource.entities.UserEntity
import com.tolstoy.zurichat.models.User


fun User.mapToEntity(): UserEntity {
    return UserEntity(
        id = id,
        displayName = display_name,
        firstName = first_name,
        lastName = last_name,
        email = email,
        timeZone = time_zone,
        phone = phone,
        token = token,
        createdAt = created_at,
        status = status,
        updatedAt = updated_at
    )
}

fun UserEntity.mapToApp(): User {
    return User(
        id = id,
        display_name = displayName,
        first_name = firstName,
        last_name = lastName,
        email = email,
        time_zone = timeZone,
        phone = phone,
        token = token,
        created_at = createdAt,
        status = status,
        updated_at = updatedAt
    )
}