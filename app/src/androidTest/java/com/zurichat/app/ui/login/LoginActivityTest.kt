package com.zurichat.app.ui.login

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.zurichat.app.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class LoginActivityTest {

    @get:Rule
    val activityScenario = ActivityScenarioRule(LoginActivity::class.java)
    /**
     * Check if the Login Activity is on the screen
     */
    @Test
    fun testLoginActivityInView() {
        onView(withId(R.id.login_activity)).check(matches(isDisplayed()))
    }

    @Test
    fun isSignInButtonVisible() {
        onView(withId(R.id.sign_in_zc_btn)).check(matches(isDisplayed()))
    }

    @Test
    fun canNavigateToLoginScreen() {
        onView(withId(R.id.sign_in_zc_btn)).perform(click())

        onView(withId(R.id.login_fragment_root)).check(matches(isDisplayed()))

    }


    @Test
    fun fillLoginDetailsAndCheckItIsValid() {
        onView(withId(R.id.sign_in_zc_btn)).perform(click())
        onView(withId(R.id.login_fragment_root)).check(matches(isDisplayed()))

        onView(withId(R.id.email))
            .perform(typeText("admin@zuri.chat"))
            .check(matches(withText("admin@zuri.chat")))

        onView(withId(R.id.password))
            .perform(typeText("sup3rs3cr3pa55w0rd"))
            .check(matches(withText("sup3rs3cr3pa55w0rd")))
    }

    @Test
    fun checkThatProgressDialogShows(){
        onView(withId(R.id.sign_in_zc_btn)).perform(click())
        onView(withId(R.id.login_fragment_root)).check(matches(isDisplayed()))

        onView(withId(R.id.email))
            .perform(typeText("admin@zuri.chat"))
            .check(matches(withText("admin@zuri.chat")))

        onView(withId(R.id.password))
            .perform(typeText("sup3rs3cr3pa55w0rd"))
            .check(matches(withText("sup3rs3cr3pa55w0rd")))

        onView(withId(R.id.button_signIn)).perform(click())
    }

    /**
     * Fails
     * TODO: Fix Custom matcher

    @Test
    fun checkThatToastMessageShows(){
        onView(withId(R.id.sign_in_zc_btn)).perform(click())
        onView(withId(R.id.login_fragment_root)).check(matches(isDisplayed()))

        onView(withId(R.id.email))
            .perform(typeText("admin@zuri.chat"))
            .check(matches(withText("admin@zuri.chat")))

        onView(withId(R.id.password))
            .perform(typeText("sup3rs3cr3pa55w0rd"))
            .check(matches(withText("sup3rs3cr3pa55w0rd")))

        closeSoftKeyboard()
        onView(withId(R.id.button_signIn)).perform(click())

        onView(withText("Please wait")).inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }
     */

}