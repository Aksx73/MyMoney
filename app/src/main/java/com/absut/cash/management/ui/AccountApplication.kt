package com.absut.cash.management.ui

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AccountApplication: Application()


//TODO
// 0. Prepopulate default category => DONE
// 1. Add test cases for CRUD operation on entry, book and category
// 2. Add Empty list illustration for Book list and transaction list => DONE
// 3. Add Smooth dismiss/insertion animation on book/entry deleted/added in db => DONE
// 4. Add haptic feedback on major action button clicked
// 5. Add countdown timer for confirm button of alert dialog before deleting a book => DONE
