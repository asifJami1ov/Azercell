package com.task.azercell.util

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


fun AppCompatActivity.addFragment(containerId: Int, fragment: Fragment, bundle: Bundle? = null) {
    fragment.arguments = bundle
    supportFragmentManager.inTransaction {
        add(containerId, fragment)
    }
}

fun AppCompatActivity.replaceFragment(
    containerId: Int,
    fragment: Fragment,
    bundle: Bundle? = null
) {
    fragment.arguments = bundle
    supportFragmentManager.inTransaction {
        replace(containerId, fragment)
    }
}

fun AppCompatActivity.removeFragment(fragment: Fragment) {
    supportFragmentManager.inTransaction {
        remove(fragment)
    }
}


fun FragmentManager.replaceFragment(
    containerId: Int,
    fragment: Fragment,
    bundle: Bundle? = null
) {
    fragment.arguments = bundle
    this.inTransaction {
        replace(containerId, fragment)
    }
}

fun FragmentManager.removeFragment(fragment: Fragment) {
    this.inTransaction {
        remove(fragment)
    }
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}