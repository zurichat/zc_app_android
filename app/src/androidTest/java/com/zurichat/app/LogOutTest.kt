package com.zurichat.app

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zurichat.app.data.localSource.AccountsDatabase
import com.zurichat.app.data.localSource.dao.AccountsDao
import com.zurichat.app.models.User
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LogOutTest {
    private lateinit var db: AccountsDatabase
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private lateinit var dao: AccountsDao

   //User-data for testing purpose
    private val user = User("","kolinton","ak@gmail.com","Kola",
                      "1x02","Akin","",1, "","",
    "", true)

    @Before
    fun setUp() = runBlocking{
        db = Room.inMemoryDatabaseBuilder(context,AccountsDatabase::class.java).build()
        /**
         * An object of a user would be added to the AccountsdDatabase when he logs in
         */
        dao = db.AccountsDao()
        dao.addUser(user)
    }

    @Test
    fun logOut() =runBlocking {
        /**
         * When a user logs out, his currentUser status would be updated to false
         */
        dao.updateUser(user.copy(currentUser = false))

        val userUpdate = dao.getUser(user.email)
        /**
         * Here, we are verifying if the user's currentUser status is actually false
         */
        assertEquals(userUpdate.currentUser, false)

    }
    @After
    fun cleanDataBase(){
        db.close()
    }
}