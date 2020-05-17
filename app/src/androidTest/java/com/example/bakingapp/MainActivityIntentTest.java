package com.example.bakingapp;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

//import static android.support.test.espresso.Espresso.onView;
//import static android.support.test.espresso.action.ViewActions.click;
//import static android.support.test.espresso.assertion.ViewAssertions.matches;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
//import static android.support.test.espresso.matcher.ViewMatchers.withText;
//
//import androidx.*;
//
//import android.support.test.runner.AndroidJUnit4;
//import androidx.test.espresso.*;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.bakingapp.Activities.MainActivity;
import com.example.bakingapp.Testing.SimpleIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityIntentTest {
    // We will focus on the main Activity
    //
    // once the recipe data is downloaded properly and the recycler view is populated
    // our test will click on the recipes list recycler viiew
    @Rule public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    SimpleIdlingResource sIdlingResource = null;;
    @Before
    public void registerIdlingResource() {
        // we have idling resources since we have data that will be downloaded to populate our recipes recycler view
        if (sIdlingResource == null) {
            sIdlingResource = new SimpleIdlingResource();
        }
        mIdlingResource = sIdlingResource;
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void clickRecipe_OpenRecipeInfoActivity(){
        // when the data is downloaded, click on the first element of the recycler view
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
