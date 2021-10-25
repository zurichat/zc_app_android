package com.zurichat.app.util

import com.zurichat.app.data.localSource.entity.OrganizationMemberEntity
import com.zurichat.app.models.OrganizationMember

fun OrganizationMember.mapToEntity() = OrganizationMemberEntity(
    id,
    bio,
    deleted,
    deletedAt,
    displayName,
    email,
    firstName,
    imageUrl,
    joinedAt,
    lastName,
    orgId,
    phone,
    presence,
    pronouns,
    role,
    //status,
    timeZone,
    userName
)
fun OrganizationMemberEntity.mapToMember() = OrganizationMember(
    id = id,
    bio = bio,
    deleted = deleted,
    deletedAt = deletedAt,
    displayName = displayName,
    email = email,
    files = null,
    firstName = firstName,
    imageUrl = imageUrl,
    joinedAt = joinedAt,
    lastName = lastName,
    orgId = orgId,
    phone = phone,
    presence = presence,
    pronouns = pronouns,
    role = role,
    settings = null,
    socials = null,
   // status = status,
    timeZone =timeZone,
    userName = userName
)
fun List<OrganizationMember>.mapToEntityList(): List<OrganizationMemberEntity> {
    val list = mutableListOf<OrganizationMemberEntity>()
    for (i in this) {
        list.add(i::mapToEntity.invoke())
    }
    return list
}
fun List<OrganizationMemberEntity>.mapToMemberList(): List<OrganizationMember> {
    val list = mutableListOf<OrganizationMember>()
    for (i in this) {
        list.add(i::mapToMember.invoke())
    }
    return list
}