package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Pet
import com.example.model.Vehicle
import com.example.ui.theme.*
import com.example.viewmodel.AuraverseViewModel
import com.example.viewmodel.ScreenRoute

enum class InventoryTab(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    PETS("Pets", Icons.Rounded.Pets),
    VEHICLES("Vehicles", Icons.Rounded.TwoWheeler),
    WARDROBE("Wardrobe", Icons.Rounded.Checkroom)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    viewModel: AuraverseViewModel
) {
    var selectedTab by remember { mutableStateOf(InventoryTab.PETS) }
    val activePet by viewModel.activePet.collectAsState()
    val allPets by viewModel.allPets.collectAsState()
    val activeVehicle by viewModel.activeVehicle.collectAsState()
    val allVehicles by viewModel.allVehicles.collectAsState()

    Scaffold(
        containerColor = AuraDeepNavy,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "INVENTORY",
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(ScreenRoute.MAIN_MENU) }) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AuraDarkSurface)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Tab Row
            TabRow(
                selectedTabIndex = selectedTab.ordinal,
                containerColor = AuraDarkSurface,
                contentColor = AuraCyanSecondary,
                divider = {}
            ) {
                InventoryTab.entries.forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        text = { Text(tab.label, fontWeight = FontWeight.SemiBold) },
                        icon = { Icon(imageVector = tab.icon, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        selectedContentColor = AuraCyanSecondary,
                        unselectedContentColor = AuraMutedSilver
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab) {
                InventoryTab.PETS -> PetsInventory(allPets, activePet) { viewModel.selectPet(it) }
                InventoryTab.VEHICLES -> VehiclesInventory(allVehicles, activeVehicle) { viewModel.selectVehicle(it) }
                InventoryTab.WARDROBE -> WardrobeInventory(viewModel)
            }
        }
    }
}

@Composable
fun PetsInventory(
    allPets: List<Pet>,
    activePet: Pet?,
    onSelect: (Pet) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Text("COMPANION PETS", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        items(allPets) { pet ->
            val isEquipped = activePet?.id == pet.id
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AuraCardSurface),
                border = if (isEquipped) androidx.compose.foundation.BorderStroke(2.dp, AuraCyanSecondary) else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(pet) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Brush.radialGradient(listOf(Color(pet.primaryColor), Color(pet.accentColor)))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Rounded.Pets, contentDescription = null, tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(pet.customName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("Level ${pet.level} • Happiness ${pet.happiness}%", color = AuraCyanLight, fontSize = 11.sp)
                        }
                    }

                    Button(
                        onClick = { onSelect(pet) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isEquipped) AuraEmerald else AuraPurplePrimary
                        )
                    ) {
                        Text(if (isEquipped) "EQUIPPED" else "EQUIP", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun VehiclesInventory(
    allVehicles: List<Vehicle>,
    activeVehicle: Vehicle?,
    onSelect: (Vehicle) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Text("VEHICLES & MOUNTS", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        items(allVehicles) { vehicle ->
            val isEquipped = activeVehicle?.id == vehicle.id
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AuraCardSurface),
                border = if (isEquipped) androidx.compose.foundation.BorderStroke(2.dp, AuraCyanSecondary) else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(vehicle) }
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(vehicle.neonColor)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Rounded.TwoWheeler, contentDescription = null, tint = Color.White)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(vehicle.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Top Speed: ${vehicle.topSpeed}x • Accel: ${vehicle.acceleration}x", color = AuraMutedSilver, fontSize = 11.sp)
                            }
                        }

                        Button(
                            onClick = { onSelect(vehicle) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isEquipped) AuraEmerald else AuraPurplePrimary
                            )
                        ) {
                            Text(if (isEquipped) "ACTIVE" else "SELECT", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WardrobeInventory(viewModel: AuraverseViewModel) {
    val ownedIds by viewModel.ownedItemIds.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        items(ownedIds.toList()) { itemId ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AuraCardSurface),
                modifier = Modifier.fillMaxWidth().height(90.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(10.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = itemId.replace("_", " ").uppercase(),
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Button(
                        onClick = { viewModel.navigateTo(ScreenRoute.AVATAR_CREATOR) },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AuraPurplePrimary),
                        modifier = Modifier.height(28.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("CUSTOMIZE", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}
