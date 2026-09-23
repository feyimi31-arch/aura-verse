package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.CatalogData
import com.example.model.ChatChannel
import com.example.ui.theme.*
import com.example.viewmodel.AuraverseViewModel
import com.example.viewmodel.ScreenRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialScreen(
    viewModel: AuraverseViewModel
) {
    val profile by viewModel.profile.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()
    var selectedChannel by remember { mutableStateOf(ChatChannel.DISTRICT) }
    var chatInput by remember { mutableStateOf("") }

    Scaffold(
        containerColor = AuraDeepNavy,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "SOCIAL HUB",
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
            Spacer(modifier = Modifier.height(10.dp))

            // Profile Card
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AuraCardSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, AuraBorderGlow),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(AuraPurplePrimary, AuraCyanSecondary))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(profile.username.take(1), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(profile.username, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = AuraPurpleDark
                            ) {
                                Text("Lv.${profile.level}", color = AuraPurpleLight, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                        Text("Style Score: ${profile.styleScore} ⭐", color = AuraGold, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Text(profile.bio, color = AuraMutedSilver, fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Online Friends
            Text("ONLINE FRIENDS", color = AuraMutedSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(
                    Triple("Celeste", "Aura Academy", 0xFF8B5CF6),
                    Triple("Kaito", "Aura City", 0xFF06B6D4),
                    Triple("Maya", "Aura Beach", 0xFFEC4899),
                    Triple("Nyx", "Mystic Forest", 0xFF10B981)
                ).forEach { (friendName, districtName, colorHex) ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = AuraCardSurface),
                        modifier = Modifier.weight(1f).clickable { viewModel.navigateTo(ScreenRoute.WORLD) }
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier.size(32.dp).background(Color(colorHex), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(friendName.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(friendName, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(districtName.split(" ").last(), color = AuraCyanLight, fontSize = 9.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Channel Selector (District vs Global)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(ChatChannel.DISTRICT, ChatChannel.GLOBAL).forEach { ch ->
                    val isSelected = ch == selectedChannel
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) AuraPurplePrimary else AuraCardSurface,
                        modifier = Modifier.clickable { selectedChannel = ch }
                    ) {
                        Text(
                            text = if (ch == ChatChannel.DISTRICT) "DISTRICT CHAT" else "GLOBAL CHAT",
                            color = if (isSelected) Color.White else AuraSilver,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Chat Messages Feed
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(AuraDarkSurface, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(chatMessages) { msg ->
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(msg.sender, color = Color(msg.senderColor), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(msg.timestamp, color = AuraMutedSilver, fontSize = 10.sp)
                        }
                        Text(msg.text, color = Color.White, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Message Input
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = chatInput,
                    onValueChange = { chatInput = it },
                    placeholder = { Text("Type friendly message...", fontSize = 12.sp, color = AuraMutedSilver) },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    textStyle = LocalTextStyle.current.copy(fontSize = 12.sp, color = Color.White)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        viewModel.sendChatMessage(chatInput, selectedChannel)
                        chatInput = ""
                    },
                    modifier = Modifier.size(46.dp).background(AuraPurplePrimary, CircleShape)
                ) {
                    Icon(imageVector = Icons.Rounded.Send, contentDescription = "Send", tint = Color.White)
                }
            }
        }
    }
}
