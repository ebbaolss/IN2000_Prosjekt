package com.example.in2000_prosjekt.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.in2000_prosjekt.R
import com.example.in2000_prosjekt.ui.AlertInfo
import com.example.in2000_prosjekt.ui.LocationInfo
import com.example.in2000_prosjekt.ui.NowCastInfo
import com.example.in2000_prosjekt.ui.database.FavoriteViewModel
import com.example.in2000_prosjekt.ui.theme.*
import com.example.in2000_prosjekt.ui.uistate.MapUiState
import java.text.SimpleDateFormat
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
fun LazyListScope.Sikt_LocationCard(mountain: MapUiState.Mountain, locationInfo: LocationInfo, nowCastInfo: NowCastInfo, alertInfoList: MutableList<AlertInfo>,favoriteViewModel: FavoriteViewModel){

    items(1) {

        val name = mountain.name
        val elevation = mountain.elevation

        val latitude = mountain.point?.latitude()
        val longitude = mountain.point?.longitude()

        val weatherHigh = locationInfo.cloud_area_fraction_high
        val weatherMid = locationInfo.cloud_area_fraction_medium
        val weatherLow = locationInfo.cloud_area_fraction_low

        val temp = nowCastInfo.temperatureNow
        val wind = nowCastInfo.windN

        // SKAL SLETTES NÅR VI FÅR LISTE OVER FJELL I NÆRHETEN:
            val testliste = mutableListOf<MapUiState.Mountain>()
        /*
            testliste.add(mountain)
            testliste.add(mountain)
            testliste.add(mountain)
            testliste.add(mountain)*/

        Card(
            colors = CardDefaults.cardColors(Sikt_lyseblå),
            modifier = Modifier.padding(20.dp),
        ){
            Column(
                modifier = Modifier.padding(20.dp),
            ) {
                Sikt_Header(location = "$name", elevation!!, latitude!!, longitude!!, alertinfo = mutableListOf(), favoriteViewModel) // Husk å endre alertinfo
                Sikt_MountainHight(mountainheight = "$elevation")
                Spacer(modifier = Modifier.size(20.dp))
                illustrasjon(elevation, temp, wind, weatherHigh, weatherMid, weatherLow)
                Spacer(modifier = Modifier.size(20.dp))
                Text(text = "Dagens siktvarsel: ", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.size(10.dp))
                Sikt_LocationCard_Hour(locationInfo) }
                Spacer(modifier = Modifier.size(20.dp))
                Text(text = "Langtidsvarsel: ", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 20.dp))
                Spacer(modifier = Modifier.size(10.dp))
                Sikt_LocationCard_NextDays(locationInfo)
                Spacer(modifier = Modifier.size(20.dp))
                Text(text = "Topper i nærheten: ", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 20.dp))
                Spacer(modifier = Modifier.size(10.dp))
                // Bytt ut testliste med fjelltopper i nærheten:
                if (testliste.size != 0) {
                    LazyRow(
                        modifier = Modifier.padding(
                            start = 20.dp,
                            end = 20.dp
                        )
                    ) { Sikt_Turer_I_Naerheten(testliste, nowCastInfo) }
                } else {
                    Text(text = "Ingen topper i nærheten...", modifier = Modifier.padding(start = 20.dp))
                }
                Spacer(modifier = Modifier.size(100.dp))
            }
        }
    }

@Composable
fun Sikt_LocationCard_Hour(locationInfo: LocationInfo) {

    val tempNext1h = locationInfo.tempNext1
    val tempNext6h = locationInfo.tempNext6
    val tempNext12h = locationInfo.tempNext6
    val cloudsNext1h = locationInfo.cloudinessNext1
    val cloudsNext6h = locationInfo.cloudinessNext6
    val cloudsNext12h = locationInfo.cloudinessNext6

    val currentTimeMillis = System.currentTimeMillis()
    val date = Date(currentTimeMillis)
    val dateFormat = SimpleDateFormat("HH")
    val tidspunkt = dateFormat.format(date)

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        for (i in 0..5) {
            val melding = if (i == 0 ) {
                "Sikt nå:"
            } else {
                "${tidspunkt.toInt()+i}:00"
            }
            if (i == 0 || i == 1) {
                item { Sikt_LocationCard_Hour_Card(melding, tempNext1h, cloudsNext1h) }
            } else {
                item { Sikt_LocationCard_Hour_Card("Om 6 timer:", tempNext6h, cloudsNext6h) }
                item { Sikt_LocationCard_Hour_Card("Om 12 timer:", tempNext12h, cloudsNext12h) }
            }

        }
    }
}

@Composable
fun Sikt_LocationCard_Hour_Card(tid : String, temp : Float, cloudiness : String) {

    val cloudinessFinal = cloudiness.split("_")[0].uppercase()
    val typeOfWeather = Sikt.valueOf(cloudinessFinal)

    Card(
        colors = CardDefaults.cardColors(Sikt_bakgrunnblå),
        modifier = Modifier.padding(end = 10.dp),
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = tid,
                color = Sikt_sort,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Box(
                modifier = Modifier.size(80.dp)
            ) {
                Image(
                    painter = painterResource(id = typeOfWeather.getIcon()),
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Text(
                text = typeOfWeather.sightInKm(),
                fontSize = 12.sp,
                color = Sikt_sort,
                fontWeight = FontWeight.Bold
            )
            Text(text = typeOfWeather.getRightWeather(), fontSize = 12.sp, color = Sikt_sort)
            Text(
                text = "$temp°",
                color = Sikt_sort,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun Sikt_LocationCard_NextDays(locationInfo: LocationInfo) {
    val tempNext1 = locationInfo.tempNext1
    val tempNext6 = locationInfo.tempNext6
    val tempNext12 = locationInfo.tempNext12
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(Sikt_hvit)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "Idag", color = Sikt_sort, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(30.dp))
                Text(text = "Lettskyet", color = Sikt_sort, fontSize = 12.sp)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "Fredag", color = Sikt_sort, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(30.dp))
                Text(text = "Lettskyet", color = Sikt_sort, fontSize = 12.sp)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "Lørdag", color = Sikt_sort, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(30.dp))
                Text(text = "Lettskyet", color = Sikt_sort, fontSize = 12.sp)
            }
        }
    }
}



