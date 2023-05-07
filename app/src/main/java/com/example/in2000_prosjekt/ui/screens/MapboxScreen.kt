package com.example.in2000_prosjekt.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.in2000_prosjekt.ui.APIViewModel
import com.example.in2000_prosjekt.ui.AppUiState
import com.example.in2000_prosjekt.ui.components.FavoriteScreenError
import com.example.in2000_prosjekt.ui.components.Sikt_BottomBar
import com.example.in2000_prosjekt.ui.components.*
import com.example.in2000_prosjekt.ui.components.Sikt_BottomSheet
import com.example.in2000_prosjekt.ui.components.Sikt_LocationCard
import com.example.in2000_prosjekt.ui.database.MapViewModel
import com.example.in2000_prosjekt.ui.theme.Sikt_hvit
import com.example.in2000_prosjekt.ui.theme.Sikt_mellomblå
import com.example.in2000_prosjekt.ui.uistate.MapUiState
import com.mapbox.bindgen.Expected
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.style.expressions.dsl.generated.eq
import com.mapbox.maps.extension.style.layers.generated.circleLayer
import com.mapbox.maps.extension.style.sources.generated.vectorSource
import com.mapbox.maps.extension.style.style
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.scalebar.scalebar
import androidx.compose.runtime.Composable
import android.view.KeyEvent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import com.mapbox.maps.MapView
import androidx.compose.ui.text.input.ImeAction
import com.example.in2000_prosjekt.R
import com.example.in2000_prosjekt.ui.SearchbarMapViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowMap(
    onNavigateToMap: () -> Unit,
    onNavigateToFav: () -> Unit,
    onNavigateToInfo: () -> Unit,
    onNavigateToSettings: () -> Unit,
    mapViewModel: MapViewModel,
    apiViewModel: APIViewModel
) {

    val cameraOptionsUiState by mapViewModel.cameraOptionsUiState.collectAsState()
    val mountainUiState by mapViewModel.mountainUiState.collectAsState()
    val appUiState by apiViewModel.appUiState.collectAsState()

    var locationCardState by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            SearchBar(SearchbarMapViewModel(), {focusManager.clearFocus()
                println("clear")
            }) { /* locationCardState = true */ }
        },
        bottomBar = {
            Sikt_BottomBar(
                onNavigateToMap,
                onNavigateToFav,
                onNavigateToInfo,
                onNavigateToSettings,
                favorite = false,
                info = false,
                map = true,
                settings = false
            )
        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AndroidView(
                modifier = Modifier,
                factory = {
                    val map = createFactoryMap(it, cameraOptionsUiState)
                    val mapboxMap = map.getMapboxMap()

                    mapboxMap.addOnCameraChangeListener(onCameraChangeListener = {
                        focusManager.clearFocus()
                        locationCardState = false
                    })

                    mapboxMap.addOnMapClickListener(onMapClickListener = { point ->
                        Log.d("LocationCardState", "$locationCardState")
                        focusManager.clearFocus()
                        locationCardState = false

                        onMapClick(point, mapboxMap, mapViewModel, apiViewModel) {
                            locationCardState = true
                            Log.d("Location Card State", "$locationCardState")
                        }

                    })
                    mapboxMap.addOnCameraChangeListener(onCameraChangeListener = {
                        Log.d("CameraChangeListener", "invoked")
                        onCameraChange(mapboxMap, mapViewModel)
                    })

                    map
                },
                update = {
                    // pull cameraSettings from the UiState
                    // Camera settings
                    it.getMapboxMap().setCamera(
                        cameraOptions {
                            // Henter kamerakoordinater fra UiState
                            val lng = cameraOptionsUiState.currentScreenLongitude
                            val lat = cameraOptionsUiState.currentScreenLatitude
                            val zoom = cameraOptionsUiState.currentScreenZoom

                            Log.d("Update Camera Coordinates", "Lng: $lng, Lat: $lat")

                            center(Point.fromLngLat(lng, lat))
                        }
                    )
                }
            )

            if (!locationCardState) {
                // Skal ta inn liste over topper i nærheten og nowcast:
                Sikt_BottomSheet()
            }

            if (locationCardState){
                when (appUiState) {
                    is AppUiState.Loading -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Sikt_mellomblå),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_pending),
                                contentDescription = "",
                                tint = Sikt_hvit,
                                modifier = Modifier.size(50.dp)
                            )
                            Text(
                                text = "Loading",
                                color = Sikt_hvit,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    is AppUiState.Error -> {
                        FavoriteScreenError( onNavigateToMap,
                            onNavigateToFav,onNavigateToSettings,
                            onNavigateToInfo)
                    }
                    is AppUiState.Success -> {
                        Log.d("Location Card", "Initialising")

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 65.dp, bottom = 70.dp)
                        ) {
                            // Må legge inn listen over fjelltopper i nærheten:
                            Sikt_LocationCard(
                                mountainUiState,
                                (appUiState as AppUiState.Success).locationF,
                                (appUiState as AppUiState.Success).nowCastF,
                                (appUiState as AppUiState.Success).alertListF
                            )
                        }
                    }
                }
            }
        }
    }
}

fun onCameraChange(mapboxMap: MapboxMap, viewModel: MapViewModel) {
    val screenCenter = mapboxMap.cameraState.center
    val zoom = mapboxMap.cameraState.zoom
    viewModel.updateCameraPosition(screenCenter)
    viewModel.updateCameraZoomState(zoom)
    Log.d("onCameraChange", "Coordinates $screenCenter, Zoom level: $zoom")
}

fun createFactoryMap(xt: Context, cameraOptionsUiState: MapUiState.MapboxCameraOptions) : MapView {

    val mapView = MapView(xt).apply {
        val mapboxMap = getMapboxMap()
        val cameraOptionsUiState = cameraOptionsUiState

        mapboxMap.loadStyle(
            // Declares map style
            style(styleUri = Style.OUTDOORS) {

                // Adding data layer source to rendered map
                +vectorSource(id = "STREETS_V8") {
                    url("mapbox://mapbox.mapbox-streets-v8")
                }

                // Creates an interactable point layer on top of style layer
                +circleLayer(layerId = "MOUNTAINS_DATALAYER", sourceId = "STREETS_V8") {
                    // natural label er et lag i STREETS_V8 datasettet til Mapbox og inneholder naturobjekter som fjell, innsjøer etc.
                    sourceLayer("natural_label")

                    // Filtering out all natural labels points that are not marked with the mountains icon
                    filter(
                        eq {
                            get { literal("maki") }
                            literal("mountain")
                        }
                    )

                    circleOpacity(0.0)
                }
            }
        )

        // Camera settings
        mapboxMap.setCamera(
            cameraOptions {
                zoom(cameraOptionsUiState.currentScreenZoom)
                // Koordinatene til Glittertind
                center(Point.fromLngLat(cameraOptionsUiState.currentScreenLongitude,cameraOptionsUiState.currentScreenLatitude))
            }
        )
    }


    // Editing compass settings, so that searchbar does not block compass
    mapView.compass.updateSettings {
        marginTop = 250F
    }

    // Editing scalebar, so that searchbar does not block scalebar
    mapView.scalebar.updateSettings {
        marginTop = 250F
    }

    return mapView
}

// Definerer hva som skal skje når brukeren trykker på kartet
fun onMapClick(point: Point, mapboxMap: MapboxMap, mapViewModel: MapViewModel, apiViewModel: APIViewModel, onClick : () -> Unit) : Boolean {
    Log.d("Coordinate", point.toString())

    mapboxMap.queryRenderedFeatures(
        RenderedQueryGeometry(ScreenBox(
            ScreenCoordinate(
                mapboxMap.pixelForCoordinate(point).x - 10.0,
                mapboxMap.pixelForCoordinate(point).y - 10.0
            ),
            ScreenCoordinate(
                mapboxMap.pixelForCoordinate(point).x + 10.0,
                mapboxMap.pixelForCoordinate(point).y + 10.0
            )
        )),
        RenderedQueryOptions(listOf("MOUNTAINS_DATALAYER"), null)
    ) { it ->
        onFeatureClicked(it) { feature ->
            if (feature.id() != null) {
                val name = feature.getStringProperty("name")
                val elevation = feature.getStringProperty("elevation_m").toInt()
                val point = feature.geometry() as Point

                // Saving a clicked mountain to the UiState through the view model
                mapViewModel.updateMountain(MapUiState.Mountain(name, point, elevation))

                val latitude =  point.latitude()
                val longitude = point.longitude()

                apiViewModel.getAll("$latitude", "$longitude", "$elevation")

                onClick()

                // DEBUGGING
                Log.d("Map Feature Clicked", feature.toString())
                Log.d("Feature Contents:", " \nMountain Name:" + feature.getStringProperty("name").toString() +
                        "\nElevation: " + feature.getStringProperty("elevation_m").toString() + " m.o.h.\n" +
                        "Point: " + feature.geometry().toString()
                )
            }
        }
    }
    return true
}

// When panning and zooming the map view the center screen coordinates and camera settings are updated to the cameraUiState.
// funOnScreenGesture(cameraOptions: MapboxCameraOptions)

fun onFeatureClicked(
    expected: Expected<String, List<QueriedFeature>>,
    onFeatureClicked: (Feature) -> Unit
) {
    if (expected.isValue && expected.value?.size!! > 0) {
        expected.value?.get(0)?.feature?.let { feature ->
            onFeatureClicked.invoke(feature)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(viewModel: SearchbarMapViewModel, onSearch : () -> Unit, clearFocus : () -> Unit){
    //MANGLER :
    // at tastaturet forsvinner når man trykker på kart

    val mapUiState = viewModel.appUiState.collectAsState()

    var input by remember { mutableStateOf("") }
    var isTextFieldFocused by remember { mutableStateOf(false) }
    val recentSearchHashmap : HashMap<String, String> = hashMapOf()
    //val focusManager = LocalFocusManager.current
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
                            input = input.replace("ø", "oe")
                            input = input.replace("æ", "ae")
                            input = input.replace("å", "aa")
                            if (input.length > 1) {
                                viewModel.getDataSearch(input)
                                showRecent = false
                            }
                            input = ""
                        }
                        false
                    },

                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        input = input.replace("ø", "oe")
                        input = input.replace("æ", "ae")
                        input = input.replace("å", "aa")
                        if (input.length > 1) {
                            viewModel.getDataSearch(input)
                            showRecent = false
                        }
                        input = ""
                        //keyboardController?.hide()
                        //isTextFieldFocused = false
                        //focusManager.clearFocus()
                    }),
                value = input,
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
                onValueChange = {
                    //if (it == "ø")
                    println(it)
                    input = it
                                },
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
                                showRecent = false
                                input = input.replace("ø", "oe")
                                input = input.replace("æ", "ae")
                                input = input.replace("å", "aa")
                                viewModel.getDataSearch(input)

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

                            viewModel.updateRecentSearch(mountain, false)

                            viewModel.showSelectedMountain(recentSearchHashmap[mountain]!!)

                            //focusManager.clearFocus()
                            clearFocus()
                            showRecent = true

                            onSearch()
                            //bruke koordinatene over til å få opp card

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

                            recentSearchHashmap[mountain] = mapUiState.value.optionMountains[mountain]!!

                            viewModel.showSelectedMountain(recentSearchHashmap[mountain]!!)

                            //focusManager.clearFocus()
                            clearFocus()
                            showRecent = true

                            //skal gjøre den true så carded vises i ShowMap():
                            onSearch()

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
