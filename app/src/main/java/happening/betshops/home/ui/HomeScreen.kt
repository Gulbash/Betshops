package happening.betshops.home.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import happening.betshops.R
import happening.betshops.home.ui.model.Betshop
import happening.betshops.home.ui.model.BoundingBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier, homeViewModel: HomeViewModel = viewModel()) {

    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = stringResource(id = R.string.main_screen_top_bar_title),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.small_margin))
                )
            }, navigationIcon = {
                IconButton(onClick = { if (context is ComponentActivity) context.finishAffinity() }) {
                    Icon(
                        painterResource(id = R.drawable.arrow_back),
                        tint = MaterialTheme.colorScheme.secondary,
                        contentDescription = stringResource(id = R.string.main_screen_top_bar_arrow_content_description),
                        modifier = Modifier.size(dimensionResource(id = R.dimen.main_screen_back_button_size))
                    )
                }
            }, colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                titleContentColor = MaterialTheme.colorScheme.secondary
            )
            )
        }, modifier = modifier
    ) { contentPadding ->

        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            Map(
                homeViewModel,
                modifier = Modifier.fillMaxSize()
            )
            if (homeViewModel.homeState.collectAsState().value.selectedBetshop != null) {
                val location =
                    homeViewModel.homeState.collectAsState().value.selectedBetshop!!.location
                InfoPanel(
                    betshop = homeViewModel.homeState.collectAsState().value.selectedBetshop!!,
                    isOpen = homeViewModel.homeState.collectAsState().value.isSelectedOpen,
                    closeAction = { homeViewModel.selectBetshop(null) },
                    routeAction = { openNavigationApp(location, context) },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Composable
fun Map(homeViewModel: HomeViewModel, modifier: Modifier = Modifier) {

    val startingLocation = getStartingLocation()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(startingLocation, 15f)
    }

    val refreshMarkers = {
        val bounds = cameraPositionState.projection?.visibleRegion?.latLngBounds
        if (bounds != null) {
            homeViewModel.cameraMoved(
                BoundingBox(
                    LatLng(bounds.northeast.latitude, bounds.northeast.longitude),
                    LatLng(bounds.southwest.latitude, bounds.southwest.longitude)
                )
            )
        }
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            refreshMarkers()
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        onMapLoaded = refreshMarkers
    ) {
        val homeState by homeViewModel.homeState.collectAsState()
        val betshops: List<Betshop> = homeState.betshops

        DrawMarkers(betshops, homeViewModel)
    }
}

@Composable
fun DrawMarkers(betshops: List<Betshop>, homeViewModel: HomeViewModel) {
    betshops.forEach { betshop ->
        Marker(
            state = MarkerState(
                position = LatLng(betshop.location.latitude, betshop.location.longitude)
            ),
            icon = if (betshop == homeViewModel.homeState.value.selectedBetshop) {
                BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_active)
            } else {
                BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_normal)
            },
            onClick = {
                homeViewModel.selectBetshop(betshop)
                false
            }
        )
    }
}

@Composable
fun InfoPanel(
    betshop: Betshop,
    isOpen: Boolean,
    closeAction: () -> Unit,
    routeAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.primaryContainer)
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }) { }) {
        Column {
            Row(
                modifier = Modifier.padding(
                    start = dimensionResource(id = R.dimen.small_margin),
                    end = dimensionResource(id = R.dimen.small_margin),
                    top = dimensionResource(id = R.dimen.normal_margin),
                    bottom = dimensionResource(id = R.dimen.smallest_margin)
                ), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_bet_shop_location),
                    tint = MaterialTheme.colorScheme.secondaryContainer,
                    contentDescription = stringResource(id = R.string.main_screen_info_location_content_description),
                    modifier = Modifier.size(dimensionResource(id = R.dimen.main_screen_info_icons_size))
                )
                Text(
                    text = betshop.name + "\n" + betshop.address + "\n" + betshop.city + " - " + betshop.county,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(
                        start = dimensionResource(id = R.dimen.normal_margin)
                    )
                )
            }
            Row(
                modifier = Modifier.padding(
                    start = dimensionResource(id = R.dimen.small_margin),
                    end = dimensionResource(id = R.dimen.small_margin),
                    top = dimensionResource(id = R.dimen.smallest_margin),
                    bottom = dimensionResource(id = R.dimen.smallest_margin)
                ), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_bet_shop_phone),
                    tint = MaterialTheme.colorScheme.secondaryContainer,
                    contentDescription = stringResource(id = R.string.main_screen_info_phone_content_description),
                    modifier = Modifier.size(dimensionResource(id = R.dimen.main_screen_info_icons_size))
                )
                Text(
                    text = stringResource(id = R.string.main_screen_info_default_phone_number),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(
                        start = dimensionResource(id = R.dimen.normal_margin)
                    )
                )
            }
            Row(
                modifier = Modifier.padding(
                    start = dimensionResource(id = R.dimen.small_margin),
                    end = dimensionResource(id = R.dimen.small_margin),
                    top = dimensionResource(id = R.dimen.smallest_margin),
                    bottom = dimensionResource(id = R.dimen.smallest_margin)
                ), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_bet_shop_hours),
                    tint = MaterialTheme.colorScheme.secondaryContainer,
                    contentDescription = stringResource(id = R.string.main_screen_info_clock_content_description),
                    modifier = Modifier.size(dimensionResource(id = R.dimen.main_screen_info_icons_size))
                )
                Text(
                    text = if (isOpen) stringResource(id = R.string.main_screen_info_open_text) else stringResource(
                        id = R.string.main_screen_info_closed_text
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(
                        start = dimensionResource(id = R.dimen.normal_margin)
                    )
                )
            }
            Text(
                text = stringResource(id = R.string.main_screen_route_text),
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(
                        start = dimensionResource(id = R.dimen.main_screen_info_route_padding),
                        bottom = dimensionResource(id = R.dimen.small_margin),
                        top = dimensionResource(
                            id = R.dimen.smallest_margin
                        )
                    )
                    .clickable(onClick = routeAction)
            )
        }
        Icon(
            painterResource(id = R.drawable.ic_close),
            tint = MaterialTheme.colorScheme.tertiaryContainer,
            contentDescription = stringResource(id = R.string.main_screen_info_close_content_description),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = dimensionResource(id = R.dimen.main_screen_info_back_icon_padding),
                    end = dimensionResource(id = R.dimen.main_screen_info_back_icon_padding)
                )
                .size(dimensionResource(id = R.dimen.main_screen_info_icons_size))
                .clickable(onClick = closeAction)
        )
    }
}


private const val MUNICH_LATITUDE = 48.137154
private const val MUNICH_LONGITUDE = 11.576124

fun getStartingLocation(): LatLng {
    val locationPermission = false
    val currentLocation = LatLng(.0, .0)

    val centerMunich = LatLng(MUNICH_LATITUDE, MUNICH_LONGITUDE)

    return if (locationPermission) {
        currentLocation
    } else {
        centerMunich
    }
}

private const val PACKAGE = "com.google.android.apps.maps"
private const val URI_BASE = "google.navigation:q="

fun openNavigationApp(location: LatLng, context: Context) {
    val gmmIntentUri = Uri.parse("${URI_BASE}${location.latitude},${location.longitude}")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage(PACKAGE)
    mapIntent.resolveActivity(context.packageManager)?.let {
        context.startActivity(mapIntent)
    }
}