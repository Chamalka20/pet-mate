package uk.ac.wlv.petmate.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ErrorRow(
    message  : String  = "Something went wrong",
    onRetry  : () -> Unit
) {
    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.error.copy(alpha = 0.08f))
            .clickable { onRetry() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Icon(
            imageVector        = Icons.Default.WifiOff,
            contentDescription = null,
            tint               = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
            modifier           = Modifier.size(18.dp)
        )
        Text(
            text       = message,
            fontSize   = 12.sp,
            color      = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
            modifier   = Modifier.weight(1f)
        )
        Icon(
            imageVector        = Icons.Default.Refresh,
            contentDescription = "Retry",
            tint               = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
            modifier           = Modifier.size(18.dp)
        )
    }
}