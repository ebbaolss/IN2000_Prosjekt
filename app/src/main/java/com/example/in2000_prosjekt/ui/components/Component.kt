@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.in2000_prosjekt.ui.components

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.in2000_prosjekt.R
import com.example.in2000_prosjekt.ui.*
import com.example.in2000_prosjekt.ui.database.Favorite
import com.example.in2000_prosjekt.ui.database.FavoriteViewModel
import com.example.in2000_prosjekt.ui.theme.*
import com.example.in2000_prosjekt.ui.uistate.MapUiState

@Composable
fun Sikt_BottomBar(onNavigateToMap: () -> Unit, onNavigateToFav: () -> Unit, onNavigateToRules: () -> Unit, onNavigateToSettings: () -> Unit, favoritt : Boolean, map : Boolean, info : Boolean, settings : Boolean) {

    BottomAppBar(
        modifier = Modifier.clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)),
        containerColor = Sikt_hvit,

        ) {
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Column (horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(120.dp)
            ){
                IconButton(onClick = { onNavigateToMap() }) {
                    var iconfarge = Sikt_mørkeblå
                    var iconChosen = R.drawable.outline_place_outline
                    if (map) {
                        iconChosen = R.drawable.baseline_place_filled
                    }
                    Icon(
                        painter = painterResource(iconChosen),
                        contentDescription = "Localized description",
                        tint = iconfarge,
                        modifier = Modifier
                            .clip(CircleShape)
                            .padding(5.dp))
                }
                Text(text = "Utforsk", fontSize = 13.sp)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(75.dp)
            ) {

                IconButton(onClick = { onNavigateToFav() }) {
                    var iconfarge = Sikt_mørkeblå
                    var iconChosen = R.drawable.outline_favorite
                    if (favoritt) {
                        iconChosen = R.drawable.baseline_favorite_24
                    }
                    Icon(
                        painter = painterResource(iconChosen),
                        contentDescription = "Localized description",
                        tint = iconfarge,
                        modifier = Modifier
                            .clip(CircleShape)
                            .padding(5.dp)
                    )
                }
                Text(text = "Favoritter", fontSize = 13.sp)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(98.dp)
            ) {
                IconButton(onClick = { onNavigateToRules() }) {
                    var iconfarge = Sikt_mørkeblå
                    var iconChosen = R.drawable.outline_info_20
                    if (info) {
                        iconChosen = R.drawable.baseline_info_filled
                    }
                    Icon(
                        painter = painterResource(iconChosen),
                        "",
                        tint = iconfarge,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .padding(5.dp)
                    )
                }
                Text(text = "Info", fontSize = 13.sp)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(75.dp)
            ) {
                IconButton(onClick = { onNavigateToSettings() }) {
                    var iconfarge = Sikt_mørkeblå
                    var iconChosen = R.drawable.outline_settings
                    if (settings) {
                        iconChosen = R.drawable._settings_filled
                    }
                    Icon(
                        painterResource(iconChosen),
                        "",
                        tint = iconfarge,
                        modifier = Modifier
                            .clip(CircleShape)
                            .padding(5.dp)
                    )
                }
                Text(text = "Innstillinger", fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun Sikt_BottomBar2( ) {
    //Denne brukes for for testing av design i preview
    BottomAppBar(
        modifier = Modifier.clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)),
        containerColor = Sikt_hvit,

        ) {
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Column (horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(75.dp)
            ){
                IconButton(onClick = {  }) {
                    Icon(
                        Icons.Filled.LocationOn,
                        contentDescription = "Localized description",
                        tint = Sikt_mørkeblå,
                    )
                }
                Text(text = "Utforsk")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(75.dp)
            ) {
                IconButton(onClick = {  }) {
                    Icon(
                        Icons.Outlined.Favorite,
                        contentDescription = "Localized description",
                        tint = Sikt_mørkeblå,
                    )
                }
                Text(text = "Favoritter")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(93.dp)
            ) {
                IconButton(onClick = {  }) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_info),
                        "",
                        tint = Sikt_mørkeblå,
                    )
                }
                Text(text = "Fjellvettreglene")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(75.dp)
            ) {
                IconButton(onClick = {  }) {
                    Icon(
                        Icons.Outlined.Settings,
                        "",
                        tint = Sikt_mørkeblå,
                    )
                }
                Text(text = "Innstillinger")
            }
        }
    }
}

@Composable
fun Sikt_Header(location : String , height: Int, lat: Double, lon: Double, alertinfo: MutableList<AlertInfo>, viewModel: FavoriteViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var openDialog by remember {
            mutableStateOf(false)
        }

        if (alertinfo.size != 0){
            AlertButton( alertinfo.get(0).alertTypeA, alertinfo.get(0).alertLevelA){
                openDialog = true
            }
        } else {
            // For å fikse at fjelltopp-text blir midtstilt
            IconToggleButton(
                checked = false,
                onCheckedChange = { },
            ) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "Localized description",
                    tint = Sikt_lyseblå
                )
            }

        }
        if (openDialog){
            AlertDialog(alertinfo = alertinfo){
                openDialog = false
            }
        }

        Text(text = "$location", fontWeight = FontWeight.Bold, fontSize = 30.sp)
        var checked by remember { mutableStateOf(false) }
        IconToggleButton(
            checked = checked,
            onCheckedChange = { checked = it },
        ) {
            var alreadyFav = viewModel.findFavorite(lon,lat,location,height)

            Log.d("FAVORITE", "long: $lon , lat : $lat, loc : $location, height : $height")
            if (checked) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "Localized description",
                    tint = Sikt_mørkeblå
                )
                if(!alreadyFav){
                    viewModel.addFavorite(Favorite(lon,lat,location,height))
                }
            } else {
                Icon(
                    painterResource(id = R.drawable.outline_favorite),
                    contentDescription = "Localized description",
                    tint = Sikt_mørkeblå
                )
                if(alreadyFav){
                    viewModel.deleteFavorite(lon,lat)
                }
            }
        }
    }
}

@Composable
fun Sikt_MountainHight(mountainheight : String) {
    Text(
        text = "$mountainheight m.o.h",
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun Sikt_skyillustasjon() {
    Image(
        painter = painterResource(id = R.drawable.clounds_image),
        contentDescription = "sol",
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxWidth()
        )
}

@OptIn(ExperimentalMaterial3Api::class)
fun LazyListScope.Sikt_Favorite_card(weatherinfo: MutableList<LocationInfo>, nowcastinfo: MutableList<NowCastInfo>, alertInfo: MutableList<MutableList<AlertInfo>>, favorites: List<Favorite>, viewModel: FavoriteViewModel) {
    //favorites er en mutableList med LocationInfo kan derfor kalle
    // favorite.temperatureL etc.
    /*
    Log.d("INFOSIZE", "${weatherinfo.size}")
    Log.d("FAVS", "${favorites.size}")
    Log.d("AlertSIZE", "${alertInfo.size}")

     */
    items(weatherinfo.size) {
            //Log.d("CARD", "STARTER CARD")
            val location = weatherinfo[it]
            val nowcast = nowcastinfo[it]
            val alertInfo = alertInfo[it]
            val name = favorites[it].mountainName
            val height = favorites[it].mountainHeight

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                colors = CardDefaults.cardColors(Sikt_lyseblå)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Sikt_Header(name,height, favorites[it].latitude, favorites[it].longtitude, alertInfo, viewModel)
                    Sikt_MountainHight(height.toString())
                    illustrasjon(
                        height = height,
                        temp = nowcast.temperatureNow,
                        vind = nowcast.windN,
                        weatherHigh = location.cloud_area_fraction_high,
                        weatherMid = location.cloud_area_fraction_medium,
                        weatherLow = location.cloud_area_fraction_low
                    )
                }
            }
        }
    }


fun LazyListScope.Sikt_Turer_I_Naerheten(mountains: MutableList<MapUiState.Mountain>, nowCastInfo: NowCastInfo) {

    items(1) {
        mountains.forEach { mountain ->

            val temp = nowCastInfo.temperatureNow

            Card(
                colors = CardDefaults.cardColors(Sikt_bakgrunnblå),
                modifier = Modifier.padding(end = 10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.size(100.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.turer_i_naerheten),
                            contentDescription = "",
                            modifier = Modifier.fillMaxSize()
                        )
                        Text(
                            text = "${temp}°",
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            color = Sikt_sort,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(
                        text = "${mountain.name}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Sikt_sort,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun DeleteAllButton(viewModel: FavoriteViewModel){
    //Knapp til Instillinger for å slette alle favoritter.
    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = Sikt_mørkeblå),
        onClick = {
            viewModel.deleteAll()
        }
    ){
        Text("Slett alle favoritter", color = Sikt_hvit)
    }
}

@Composable
fun illustrasjon(height : Int?, temp : Float, vind : Float, weatherHigh : Float, weatherMid : Float, weatherLow : Float){

    fun getHeightVisuals(height: Int?) : Int {
        return when (height) {
            in 0 .. 500 -> R.drawable.topp_under500
            in 501 .. 1000 -> R.drawable.topp1000to500
            in 1001 .. 1500 -> R.drawable.topp1500to1000
            in 1501 .. 2000 -> R.drawable.topp2000to1500
            else -> R.drawable.topp_over2000
        }
    }

    fun getHighClouds(highclouds: Float): Int {
        return when (highclouds.toInt()) {
            in 75..100 -> R.drawable.clouds_high_both
            in 50..74 -> R.drawable.clouds_high_big
            in 25..49 -> R.drawable.clouds_high_small
            else -> R.drawable.klart
        }
    }

    fun getMidClouds(midclouds: Float): Int {
        return when (midclouds.toInt()) {
            in 75..100 -> R.drawable.clouds_mid_both
            in 50..74 -> R.drawable.clouds_mid_big
            in 25..49 -> R.drawable.clouds_mid_small
            else -> R.drawable.klart
        }
    }

    fun getLowClouds(lowclouds: Float): Int {
        return when (lowclouds.toInt()) {
            in 75..100 -> R.drawable.clouds_low_both
            in 50..74 -> R.drawable.clouds_low_big
            in 25..49 -> R.drawable.clouds_low_small
            else -> R.drawable.klart
        }
    }

    fun getRightWeather(weather: Float): String {
        return when (weather.toInt()) {
            in 75..100 -> "Meget dårlig sikt"
            in 50..74 -> "Dårlig sikt"
            in 25..49 -> "Lettskyet"
            else -> "Klart vær"
        }
    }

    fun getRightKm(km: Float): String {
        return when (km.toInt()){
            in 75..100 -> "> 1 km sikt"
            in 50..74 -> "1-4 km sikt"
            in 25..49 -> "4-10 km sikt"
            else -> "< 10 km sikt"
        }
    }

    Box(
        modifier = Modifier.aspectRatio(1f)
    ){
        Image(
            painter = painterResource(id = R.drawable.new_background),
            contentDescription = "",
            modifier = Modifier.fillMaxSize()
        )
        Image(
            painter = painterResource(id = getHeightVisuals(height)),
            contentDescription = "",
            modifier = Modifier.fillMaxSize()
        )
        Image(
            painter = painterResource(id = getHighClouds(weatherHigh)),
            contentDescription = "",
            modifier = Modifier.fillMaxSize()
        )
        Image(
            painter = painterResource(id = getMidClouds(weatherMid)),
            contentDescription = "",
            modifier = Modifier.fillMaxSize()
        )
        Image(
            painter = painterResource(id = getLowClouds(weatherLow)),
            contentDescription = "",
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.TopStart)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.vind_icon),
                contentDescription = "",
                modifier = Modifier.size(24.dp)
            )
            Text(text = "$vind m/s", fontSize = 12.sp, color = Sikt_sort)
        }
        Text(
            text = "$temp°",
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            color = Sikt_sort,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(text = "2000 - 5000 m.o.h.", fontSize = 12.sp, color = Sikt_sort)
                Text(text = getRightKm(weatherHigh), fontSize = 16.sp, color = Sikt_sort, fontWeight = FontWeight.Bold)
                Text(text = getRightWeather(weatherHigh), fontSize = 12.sp, color = Sikt_sort)
            }
            Divider(thickness = 1.dp, color = Sikt_sort, modifier = Modifier.width(100.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(text = "1000 - 2000 m.o.h.", fontSize = 12.sp, color = Sikt_sort)
                Text(text = getRightKm(weatherMid), fontSize = 16.sp, color = Sikt_sort, fontWeight = FontWeight.Bold)
                Text(text = getRightWeather(weatherMid), fontSize = 12.sp, color = Sikt_sort)
            }
            Divider(thickness = 1.dp, color = Sikt_sort, modifier = Modifier.width(100.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(text = "0 - 1000 m.o.h.", fontSize = 12.sp, color = Sikt_sort)
                Text(text = getRightKm(weatherLow), fontSize = 16.sp, color = Sikt_sort, fontWeight = FontWeight.Bold)
                Text(text = getRightWeather(weatherLow), fontSize = 12.sp, color = Sikt_sort)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun TestComponent() {

    Card(
        colors = CardDefaults.cardColors(Sikt_bakgrunnblå),
        modifier = Modifier.padding(end = 10.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(100.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.turer_i_naerheten),
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize()
                )
                Text(
                    text = "0°",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Sikt_sort,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = "fjell",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Sikt_sort,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

}
