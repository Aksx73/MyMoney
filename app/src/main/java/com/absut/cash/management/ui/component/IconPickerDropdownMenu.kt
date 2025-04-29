package com.absut.cash.management.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Attractions
import androidx.compose.material.icons.outlined.Autorenew
import androidx.compose.material.icons.outlined.BakeryDining
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Chair
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.CurrencyBitcoin
import androidx.compose.material.icons.outlined.Diamond
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.Handyman
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LocalBar
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.LocalTaxi
import androidx.compose.material.icons.outlined.Memory
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Percent
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.Phishing
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.SimCard
import androidx.compose.material.icons.outlined.SmokingRooms
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.absut.cash.management.ui.icon.MyIcons
import com.absut.cash.management.ui.icon.outlined.Apparel
import com.absut.cash.management.ui.icon.outlined.Dentistry
import com.absut.cash.management.ui.icon.outlined.Exercise
import com.absut.cash.management.ui.icon.outlined.Monitoring
import com.absut.cash.management.ui.icon.outlined.Pill
import com.absut.cash.management.ui.icon.outlined.Scooter
import com.absut.cash.management.ui.icon.outlined.SelfCare
import com.absut.cash.management.ui.icon.outlined.Travel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IconPickerDropdownMenu(
    currentIcon: ImageVector,
    onSelectedIconClick: (Int) -> Unit,
    onClick: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
        IconButton(
            onClick = {
                onClick()
                expanded = true
            }
        ) {
            Icon(
                imageVector = currentIcon,
                contentDescription = null,
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            FlowRow(maxItemsInEachRow = 5) {
                StoredIcon.entries.forEach { icon ->
                    IconButton(
                        onClick = {
                            onSelectedIconClick(icon.storedId)
                            expanded = false
                        }
                    ) {
                        Icon(
                            imageVector = icon.imageVector,
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}

enum class StoredIcon(
    val imageVector: ImageVector,
    val storedId: Int,
) {
    CATEGORY(Icons.Outlined.Category, 0),
    ACCOUNT_BALANCE(Icons.Outlined.AccountBalance, 1),
    APPAREL(MyIcons.Outlined.Apparel, 2),
    CHAIR(Icons.Outlined.Chair, 3),
    EXERCISE(MyIcons.Outlined.Exercise, 4),
    FASTFOOD(Icons.Outlined.Fastfood, 5),
    RESTAURANT(Icons.Outlined.Restaurant, 17),
    BAKERY_DINING(Icons.Outlined.BakeryDining, 37),
    DIRECTIONS_BUS(Icons.Outlined.DirectionsBus, 6),
    LOCAL_TAXI(Icons.Outlined.LocalTaxi, 35),
    SCOOTER(MyIcons.Outlined.Scooter, 36),
    HANDYMAN(Icons.Outlined.Handyman, 7),
    LANGUAGE(Icons.Outlined.Language, 8),
    LOCAL_BAR(Icons.Outlined.LocalBar, 9),
    LOCAL_GAS_STATION(Icons.Outlined.LocalGasStation, 10),
    MEMORY(Icons.Outlined.Memory, 11),
    PAYMENTS(Icons.Outlined.Payments, 12),
    PETS(Icons.Outlined.Pets, 13),
    PHISHING(Icons.Outlined.Phishing, 14),
    PILL(MyIcons.Outlined.Pill, 15),
    TRANSACTION(Icons.Outlined.ReceiptLong, 16),
    SCHOOL(Icons.Outlined.School, 18),
    SELF_CARE(MyIcons.Outlined.SelfCare, 19),
    SHOPPING_CART(Icons.Outlined.ShoppingCart, 20),
    SIM_CARD(Icons.Outlined.SimCard, 21),
    SMOKING_ROOMS(Icons.Outlined.SmokingRooms, 22),
    SPORTS_ESPORTS(Icons.Outlined.SportsEsports, 23),
    TRAVEL(MyIcons.Outlined.Travel, 24),
    ATTRACTIONS(Icons.Outlined.Attractions, 25),
    CREDIT_CARD(Icons.Outlined.CreditCard, 26),
    MONITORING(MyIcons.Outlined.Monitoring, 27),
    MUSIC_NOTE(Icons.Outlined.MusicNote, 28),
    WORK(Icons.Outlined.Work, 29),
    BITCOIN(Icons.Outlined.CurrencyBitcoin, 30),
    BOOK(Icons.Outlined.Book, 31),
    HOSPITAL(Icons.Outlined.LocalHospital, 32),
    DENTISTRY(MyIcons.Outlined.Dentistry, 33),
    DIAMOND(Icons.Outlined.Diamond, 34),
    MOVIE(Icons.Outlined.Movie, 35),
    PERCENT(Icons.Outlined.Percent, 36),
    AUTORENEW(Icons.Outlined.Autorenew, 37);

    companion object {

        fun asImageVector(storedId: Int?): ImageVector =
            entries.find { it.storedId == storedId }?.imageVector ?: CATEGORY.imageVector
    }
}