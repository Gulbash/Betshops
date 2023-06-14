package happening.betshops.home.ui

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    stringResource(id = R.string.main_screen_top_bar_title),
                    modifier = Modifier.padding()
                )
            }, navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(
                        painterResource(id = R.drawable.arrow_back),
                        tint = MaterialTheme.colorScheme.secondary,
                        contentDescription = stringResource(id = R.string.main_screen_top_bar_arrow_content_description)
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
                .background(MaterialTheme.colorScheme.error)
        ) {
            Map(homeViewModel, modifier = Modifier.fillMaxSize())
            val context = LocalContext.current
            if (homeViewModel.homeState.collectAsState().value.selectedBetshop != null) {
                InfoPanel(
                    betshop = homeViewModel.homeState.collectAsState().value.selectedBetshop!!,
                    checkIfOpen = homeViewModel::isOpen,
                    closeAction = { homeViewModel.selectBetshop(null) },
                    routeAction = {
                        val gmmIntentUri = Uri.parse("google.navigation:q=37.7749,-122.4194")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        mapIntent.resolveActivity(context.packageManager)?.let {
                            context.startActivity(mapIntent)
                        }
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Composable
fun Map(homeViewModel: HomeViewModel, modifier: Modifier = Modifier) {

    val centerMunich = LatLng(48.137154, 11.576124)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(centerMunich, 15f)
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
        modifier = modifier, cameraPositionState = cameraPositionState, onMapLoaded = refreshMarkers
    ) {
        val betshops: List<Betshop> by homeViewModel.betshops.collectAsState(initial = emptyList())
        betshops.forEach { betshop ->
            Marker(state = MarkerState(
                position = LatLng(
                    betshop.location.latitude, betshop.location.longitude
                )
            ),
                icon = if (betshop == homeViewModel.homeState.value.selectedBetshop) BitmapDescriptorFactory.fromResource(
                    R.drawable.ic_pin_active
                ) else BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_normal),
                onClick = {
                    homeViewModel.selectBetshop(betshop)
                    false
                })
        }
    }
}

@Composable
fun InfoPanel(
    betshop: Betshop,
    checkIfOpen: (LatLng) -> Boolean,
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
                    contentDescription = "",
                    modifier = Modifier.size(dimensionResource(id = R.dimen.main_screen_info_icons_size))
                )
                Text(
                    text = betshop.name + "\n" + betshop.address + "\n" + betshop.city + " - " + betshop.county,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(
                        start = dimensionResource(id = R.dimen.normal_margin)
                    )
                )
            }
            Row(
                modifier = Modifier.padding(
                    start = dimensionResource(id = R.dimen.small_margin),
                    top = dimensionResource(id = R.dimen.smallest_margin),
                    bottom = dimensionResource(id = R.dimen.smallest_margin)
                ), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_bet_shop_phone),
                    tint = MaterialTheme.colorScheme.secondaryContainer,
                    contentDescription = "",
                    modifier = Modifier.size(dimensionResource(id = R.dimen.main_screen_info_icons_size))
                )
                Text(
                    text = "311-56-44",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(
                        start = dimensionResource(id = R.dimen.normal_margin)
                    )
                )
            }
            Row(
                modifier = Modifier.padding(
                    start = dimensionResource(id = R.dimen.small_margin),
                    top = dimensionResource(id = R.dimen.smallest_margin),
                    bottom = dimensionResource(id = R.dimen.smallest_margin)
                ), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_bet_shop_hours),
                    tint = MaterialTheme.colorScheme.secondaryContainer,
                    contentDescription = "",
                    modifier = Modifier.size(dimensionResource(id = R.dimen.main_screen_info_icons_size))
                )
                Text(
                    text = if (checkIfOpen(betshop.location)) "Open now until [END TIME]" else "Opens tomorrow at [START TIME]",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(
                        start = dimensionResource(id = R.dimen.normal_margin)
                    )
                )
            }
            Text(
                text = "ROUTE",
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(
                        start = 84.dp,
                        bottom = dimensionResource(id = R.dimen.normal_margin),
                        top = dimensionResource(
                            id = R.dimen.small_margin
                        )
                    )
                    .clickable(onClick = routeAction)
            )
        }
        Icon(
            painterResource(id = R.drawable.ic_close),
            tint = MaterialTheme.colorScheme.tertiaryContainer,
            contentDescription = "",
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