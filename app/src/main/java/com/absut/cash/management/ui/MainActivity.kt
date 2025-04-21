package com.absut.cash.management.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.absut.cash.management.ui.booklist.BookListViewModel
import com.absut.cash.management.R
import com.absut.cash.management.databinding.ActivityMainBinding
import com.absut.cash.management.ui.booklist.BookListScreen
import com.absut.cash.management.ui.categoryList.CategoryScreen
import com.absut.cash.management.ui.entrydetail.AddUpdateEntryScreen
import com.absut.cash.management.ui.entrylist.EntryListScreen
import com.google.android.material.appbar.MaterialToolbar
import androidx.navigation.compose.composable
import com.absut.cash.management.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    /* private lateinit var binding:ActivityMainBinding
     private lateinit var navController: NavController
     private lateinit var appBarConfiguration: AppBarConfiguration

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         binding = ActivityMainBinding.inflate(layoutInflater)
         setContentView(binding.root)
         setSupportActionBar(binding.toolbar)

         binding.toolbar.setNavigationOnClickListener {
             navController.navigateUp()
         }

         val navHostFragment =
             supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
         navController = navHostFragment.navController

         appBarConfiguration = AppBarConfiguration(
             setOf(
                 R.id.bookListFragment
             )
         )

         setupActionBarWithNavController(navController, appBarConfiguration)
     }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberNavController()
                MainApp(navController)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(navController: NavController) {
    AppNavigation(navController as NavHostController)
}

@Preview
@Composable
fun MainAppPreview() {
    val navController = rememberNavController()
    MainApp(navController)
}