package com.absut.cash.management.ui

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AccountApplication: Application()


//TODO
// 0. Prepopulate default category => DONE
// 0. Add test cases for CRUD operation on entry, book and category
// 1. Add Empty list illustration for Book list and transaction list
// 2. Add Smooth dismiss/insertion animation on book/entry deleted/added in db
// 3. Add haptic feedback on major action button clicked
