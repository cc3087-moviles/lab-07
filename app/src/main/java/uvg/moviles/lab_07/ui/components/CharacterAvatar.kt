package uvg.moviles.lab_07.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage

@Composable
fun CharacterAvatar(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    placeholderIconSize: androidx.compose.ui.unit.Dp = 32.dp
) {
    val fallback: @Composable () -> Unit = {
        Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer), Alignment.Center) {
            Icon(
                Icons.Default.Person,
                contentDescription,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(placeholderIconSize)
            )
        }
    }
    if (imageUrl.isBlank()) Box(modifier.clip(CircleShape)) { fallback() }
    else SubcomposeAsyncImage(imageUrl, contentDescription, modifier = modifier.clip(CircleShape),
        contentScale = ContentScale.Crop, loading = { fallback() }, error = { fallback() })
}
