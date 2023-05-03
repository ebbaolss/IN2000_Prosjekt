package com.example.in2000_prosjekt.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.example.in2000_prosjekt.ui.components.Sikt_BottomBar
import com.example.in2000_prosjekt.ui.components.Sikt_BottomSheet
import com.mapbox.geojson.Point
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.plugin.gestures.OnMapClickListener
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import android.annotation.SuppressLint
import android.util.Log
import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mapbox.maps.MapView
import androidx.compose.ui.text.input.ImeAction
import com.example.in2000_prosjekt.ui.MapViewModel
import java.lang.Thread.sleep

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter") //burde unngå disse så langt det lar seg gjøre, men her måtte vi for å slippe padding
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowMap(onNavigateToMap: () -> Unit, onNavigateToFav: () -> Unit, onNavigateToSettings: () -> Unit, onNavigateToRules: () -> Unit) {
    Scaffold(
        topBar = {SearchBar(MapViewModel())},
        bottomBar = { Sikt_BottomBar(onNavigateToMap, onNavigateToFav, onNavigateToRules, onNavigateToSettings,
            favoritt = false, settings = false, rules = false, map = true) })
    {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AndroidView(
                modifier = Modifier,
                factory = { createFactoryMap(it) }
            )
        }
        Sikt_BottomSheet()
    }
}
fun createFactoryMap(xt: Context) : MapView {
    return MapView(xt).apply {
        val mapboxMap = getMapboxMap()
        mapboxMap.loadStyleUri("mapbox://styles/elisabethb/clf6t1z9z00b101pen0rvc1fu/draft") {
            cameraOptions{
                zoom(19.9)
            }
        }
        mapboxMap.addOnMapClickListener(onMapClickListener = OnMapClickListener {point ->
            onMapClick(point)
        })
    }
}
fun onMapClick(point: Point): Boolean {
    Log.d("Coordinate", point.toString())
    return@onMapClick true
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(viewModel: MapViewModel){
    //MANGLER :
    // at tastaturet forsvinner når man trykker på kart

    val mapUiState = viewModel.appUiState.collectAsState()
    val mapUiStateCoordinates = viewModel.appUiState2.collectAsState()

    var input by remember { mutableStateOf("") }
    var isTextFieldFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var showRecent = true

    LazyColumn(
        modifier = Modifier
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 20.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        item {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .onFocusChanged {
                        isTextFieldFocused = it.isFocused
                    }
                    .onKeyEvent {
                        if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER){
                            if (input.length > 1) {
                                suggestionSearch(viewModel, input)
                                showRecent = false
                            }
                            input = ""
                        }
                        false //??
                    },

                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        isTextFieldFocused = false
                        focusManager.clearFocus()
                    }),
                value = input,
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
                onValueChange = { input = it },
                placeholder = { Text(text = "Søk her") },
                label = {
                    Text(
                        text = "Søk her",
                        textAlign = TextAlign.Center
                    )
                },
                trailingIcon = { //søkeknappen
                    Button(
                        onClick = {

                            if (input.length > 1) {
                                suggestionSearch(viewModel, input)
                                showRecent = false
                                focusManager.clearFocus()
                            }

                            input = ""

                        }, colors = ButtonDefaults.buttonColors(Color.White),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            tint = Color.Black,
                            contentDescription = "Search icon",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
        }
        if (isTextFieldFocused && showRecent) {

            items(mapUiState.value.recentSearch) { mountain ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .height(40.dp)
                        .padding(start = 20.dp, top = 9.dp, bottom = 7.dp)
                        .clickable( enabled = true, onClick = {

                            val searchedBefore = viewModel.updateRecentSearch(mountain, false)
                            if (searchedBefore) {
                                suggestionSearch(viewModel, mountain) //oppdaterer lista så vi kan hente mapbox_id
                                //få koden til å ikke gå videre før suggestionSearch er ferdig
                                println(mapUiState.value.optionMountains[mountain]) //null
                            }

                            println("sjekker noe")

                            retrieveSearch(viewModel, mapUiState.value.optionMountains[mountain]!!) //mapbox_id
                            //få koden til å ikke gå videre før retriveSearch er ferdig
                            println(mapUiStateCoordinates.value.latitude)
                            println(mapUiStateCoordinates.value.longitude)

                            //bruke koordinatene over til å få opp card

                            focusManager.clearFocus()
                            showRecent = true

                            println("recent search: ${mapUiState.value.recentSearch}")
                        }),

                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = mountain,
                        fontSize = 15.sp,
                        modifier = Modifier
                            .weight(1f),
                        textAlign = TextAlign.Start,
                    )
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 15.dp)
                            .clickable(
                                onClick = {
                                    viewModel.updateRecentSearch(mountain, true)
                                }
                            )

                    )
                }
            }
        }
        if (isTextFieldFocused && !showRecent) {

            items(mapUiState.value.optionMountains.keys.toList()) { mountain ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .height(40.dp)
                        .padding(start = 20.dp, top = 9.dp, bottom = 7.dp)
                        .clickable( enabled = true, onClick = {

                            viewModel.updateRecentSearch(mountain, false)

                            retrieveSearch(viewModel, mapUiState.value.optionMountains[mountain]!!) //mapbox_id
                            //få koden til å ikke gå videre før retriveSearch er ferdig
                            println(mapUiStateCoordinates.value.latitude)
                            println(mapUiStateCoordinates.value.longitude)

                            focusManager.clearFocus()
                            //bruke koordinatene over til å få opp card

                            showRecent = true
                        }),

                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = mountain,
                        fontSize = 15.sp,
                        modifier = Modifier
                            .weight(1f),
                        textAlign = TextAlign.Start,
                    )
                }
            }
        }
    }
}
fun suggestionSearch(apiViewModel: MapViewModel, searchString : String) {
    apiViewModel.getDataSearch(searchString)
}
fun retrieveSearch(apiViewModel: MapViewModel, mapboxId: String) {

    //her skal man kalle på det andre apiet med mapbox_id
    //dette må lages, men så og si bare å kopiere det man gjorde på getDataSearch
    //da får man oppdatert koordinater, lat og long
    println("retriveSearch")
    apiViewModel.getDataSearchCoordinates(mapboxId)
    println("ferdig med getDataSearchCoordinates")

}



