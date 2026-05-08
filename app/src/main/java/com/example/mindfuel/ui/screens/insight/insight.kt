package com.example.mindfuel.ui.screens.insight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mindfuel.data.Entry
import com.example.mindfuel.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightScreen(navController: NavController) {
    var entries by remember { mutableStateOf(listOf<Entry>()) }
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val locale = Locale.getDefault()
    val sdf = remember(locale) { SimpleDateFormat("yyyyMMdd", locale) }

    LaunchedEffect(Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("entries")
        ref.orderByChild("userId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Entry>()
                    for (child in snapshot.children) {
                        val entry = child.getValue(Entry::class.java)
                        entry?.let { list.add(entry) }
                    }
                    entries = list
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    val moodCounts = listOf(
        "Happy" to MoodHappy,
        "Neutral" to MoodNeutral,
        "Sad" to MoodSad,
        "Angry" to MoodAngry,
        "Tired" to Color(0xFFA78BFA)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Insights", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Summary",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Entries", color = TextSecondary, style = MaterialTheme.typography.labelMedium)
                        Text("${entries.size}", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Primary)
                    }
                    VerticalDivider(modifier = Modifier.padding(vertical = 4.dp), color = Color(0xFFE2E8F0))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Days Logged", color = TextSecondary, style = MaterialTheme.typography.labelMedium)
                        val days = entries.map { 
                            sdf.format(Date(it.timestamp))
                        }.distinct().size
                        Text("$days", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Secondary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Mood Distribution",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    moodCounts.forEach { (mood, color) ->
                        val count = entries.count { it.mood == mood }
                        val progress = if (entries.isNotEmpty()) count.toFloat() / entries.size else 0f
                        MoodProgressRow(mood, count, progress, color)
                        if (mood != "Tired") Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            RecommendationCard(entries)
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun MoodProgressRow(mood: String, count: Int, progress: Float, color: Color) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(mood, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, fontWeight = FontWeight.Medium)
            Text("$count logs", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = color.copy(alpha = 0.1f)
        )
    }
}

@Composable
fun RecommendationCard(entries: List<Entry>) {
    val happyCount = entries.count { it.mood == "Happy" }
    val sadCount = entries.count { it.mood == "Sad" }
    
    val message = when {
        entries.isEmpty() -> "Start your journey by adding your first entry!"
        happyCount > sadCount -> "You've been feeling mostly positive. Keep up the great habits!"
        sadCount > happyCount -> "You've had some tough days. Remember to be kind to yourself."
        else -> "You're maintaining a steady balance. Keep reflecting!"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Primary.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("💡", fontSize = 32.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
