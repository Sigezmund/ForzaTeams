package com.forza.teams.ui.kit

import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun ImageUiCompose(
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.None,
    placeholder: Painter? = null,
    colorFilter: ColorFilter? = null,
    withCrossFade: Boolean = true,
    image: ImageUi,
) {
    val imageModel = when (image) {
        is ImageUi.ImageContent -> image.uri
        is ImageUi.ImageCustom -> image.any
        is ImageUi.ImageRemote -> image.url
        is ImageUi.ImageId -> image.image
        is ImageUi.ImageBitmap -> image.bitmap
        is ImageUi.NoImage -> ""
    }
    val context = LocalContext.current
    val request = remember(context, imageModel, withCrossFade) {
        ImageRequest.Builder(context)
            .data(imageModel)
            .crossfade(withCrossFade)
            .build()
    }

    val placeholderPainter = if (image is ImageUi.ImageId) {
        painterResource(image.image)
    } else {
        placeholder
    }

    AsyncImage(
        model = request,
        contentDescription = null,
        modifier = modifier,
        placeholder = placeholderPainter,
        error = placeholderPainter,
        contentScale = contentScale,
        colorFilter = colorFilter,
    )
}


@Stable
sealed interface ImageUi {
    @Immutable
    object NoImage : ImageUi

    @Immutable
    data class ImageRemote(val url: String) : ImageUi

    @Immutable
    data class ImageContent(val uri: Uri) : ImageUi

    @Immutable
    data class ImageId(@DrawableRes val image: Int) : ImageUi

    data class ImageBitmap(val bitmap: Bitmap) : ImageUi

    data class ImageCustom(val any: Any) : ImageUi
}
