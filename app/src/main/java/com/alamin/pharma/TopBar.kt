package com.alamin.pharma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalPharmacy
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alamin.pharma.R

@Composable
fun AlAminTopBar(
    cartCount: Int = 0,
    notifCount: Int = 1,
    onCart: () -> Unit = {},
    onNotif: () -> Unit = {},
    onSearch: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onCart) {
            BadgedBox(badge = {
                if (cartCount > 0) {
                    Badge(containerColor = MaterialTheme.colorScheme.primary) {
                        Text(cartCount.toString(), color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }) {
                Icon(
                    Icons.Outlined.ShoppingCart,
                    contentDescription = "cart",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        IconButton(onClick = onNotif) {
            BadgedBox(badge = {
                if (notifCount > 0) {
                    Badge(containerColor = MaterialTheme.colorScheme.tertiary) {
                        Text(notifCount.toString(), color = Color.White)
                    }
                }
            }) {
                Icon(
                    Icons.Outlined.NotificationsNone,
                    contentDescription = "notifications",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        IconButton(onClick = onSearch) {
            Icon(
                Icons.Outlined.Search,
                contentDescription = "search",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(Modifier.weight(1f))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = stringResource(R.string.app_name),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = stringResource(R.string.app_tagline),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.LocalPharmacy,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
