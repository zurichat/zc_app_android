package com.zurichat.app.ui.organizations.localdatabase

import androidx.room.*
import com.zurichat.app.models.organization_model.OrgData

@Dao
interface OrgDao {
    @Query("SELECT * FROM orgdata")
    fun getAllOrgData(): List<OrgData>

    @Query("SELECT * FROM orgdata WHERE id LIKE :id LIMIT 1")
    fun getOrgDataWithID(id: String) : OrgData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg orgData: OrgData)

    @Delete
    fun delete(vararg orgData: OrgData)
}