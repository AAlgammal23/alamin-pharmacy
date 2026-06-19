package com.alamin.pharma.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.alamin.pharma.data.Article
import com.alamin.pharma.ui.PharmacyViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ArticlesScreen(vm: PharmacyViewModel, onBack: () -> Unit) {
    val articles by vm.articles.collectAsState()
    var selected by remember { mutableStateOf<Article?>(null) }

    if (selected != null) {
        ArticleDetailScreen(article = selected!!, onBack = { selected = null })
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Outlined.ArrowBack, contentDescription = "back", tint = MaterialTheme.colorScheme.onBackground)
            }
            Text("المقالات الصحية", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        }

        if (articles.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(40.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Outlined.Article, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(64.dp))
                    Spacer(Modifier.height(8.dp))
                    Text("لا توجد مقالات بعد", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(articles) { a ->
                    ArticleCard(a) { selected = a }
                }
            }
        }
    }
}

@Composable
private fun ArticleCard(a: Article, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Column {
            AsyncImage(
                model = a.imageUrl,
                contentDescription = a.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color(0xFFE6F7F4)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(a.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground, maxLines = 2)
                Spacer(Modifier.height(4.dp))
                Text(a.summary, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
                Spacer(Modifier.height(8.dp))
                Text(
                    "${a.author} • ${formatDate(a.publishedAt)}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ArticleDetailScreen(article: Article, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Outlined.ArrowBack, contentDescription = "back", tint = MaterialTheme.colorScheme.onBackground)
            }
            Text("مقال", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        }
        LazyColumn {
            item {
                AsyncImage(
                    model = article.imageUrl,
                    contentDescription = article.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(Color(0xFFE6F7F4)),
                    contentScale = ContentScale.Crop
                )
            }
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(article.title, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = MaterialTheme.colorScheme.onBackground)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "${article.author} • ${formatDate(article.publishedAt)}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(article.content, fontSize = 15.sp, color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("ar"))
    return sdf.format(Date(timestamp))
}
