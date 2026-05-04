package uk.ac.wlv.petmate.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshLayout(
    isRefreshing : Boolean,
    onRefresh    : () -> Unit,
    modifier     : Modifier = Modifier,
    content      : @Composable () -> Unit
) {
    val state = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh    = onRefresh,
        state        = state,
        indicator    = {
            PullToRefreshDefaults.Indicator(
                state          = state,
                isRefreshing   = isRefreshing,
                color          = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.surface,
                modifier       = Modifier.align(Alignment.TopCenter)
            )
        },
        modifier = modifier
    ) {
        content()
    }
}