package com.zurichat.app.ui.organizations.localdatabase

import androidx.room.*

@Dao
interface OrgDao {
    @Query("SELECT * FROM organizationdata")
    fun getAllOrgData(): List<OrgRoomData>

    @Query("SELECT * FROM organizationdata WHERE userID LIKE :userID LIMIT 1")
    fun getOrgDataWithID(userID: String) : OrgRoomData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg orgData: OrgRoomData)

    @Delete
    fun delete(vararg orgData: OrgRoomData)
}