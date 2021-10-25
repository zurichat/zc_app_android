package com.zurichat.app.ui.base

import androidx.fragment.app.Fragment

/**
 *
 * @author Jeffrey Orazulike <chukwudumebiorazulike@gmail.com>
 * Created 24-Oct-21 at 9:15 PM
 *
 * The base fragment that every fragment should extend from
 *
 * @param layout the resource id of the layout file to inflate
 */
abstract class BaseFragment(layout: Int): Fragment(layout)