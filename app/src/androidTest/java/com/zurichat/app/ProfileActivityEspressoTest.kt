package com.zurichat.app

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zurichat.app.models.User
import com.zurichat.app.ui.activities.ProfileActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileActivityEspressoTest {

    @get:Rule
    val activityScenarioRule: ActivityScenarioRule<ProfileActivity> =
        ActivityScenarioRule(ProfileActivity::class.java)


    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }


    @Test
    fun buttonTest() {
        onView(withId(R.id.edit_name))
            .perform(click())

        onView(withText("Edit your name"))
            .check(matches(isDisplayed()))
    }
}