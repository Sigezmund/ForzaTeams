package com.forza.teams.ui.kit

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.forza.teams.ui.theme.LocalColorScheme

@Composable
fun TextUiCompose(
    text: TextUi,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    shadow: Shadow? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
    autoSize: TextAutoSize? = null,
    vararg formatArgs: Any,
) {
    val textColor =
        color.takeOrElse {
            style.color.takeOrElse {
                LocalColorScheme.current.textColor
            }
        }

    var textStyle = style
    if (textAlign != null) {
        textStyle = textStyle.copy(textAlign = textAlign)
    }
    if (shadow != null) {
        textStyle = textStyle.copy(shadow = shadow)
    }

    when (text) {
        is TextUi.Text -> {
            text.text
        }

        is TextUi.TextId -> {
            stringResource(text.resId, *formatArgs)
        }

        is TextUi.TextPluralWithValueId -> {
            val value = text.value
            LocalResources.current.getQuantityString(text.text, value, value)
        }

        is TextUi.TextResFormatted -> {
            val args = text.args.map { if (it is TextUi) it.toCharSequence() else it }.toTypedArray()
            stringResource(text.text, *args)
        }

        is TextUi.TextAnnotated -> {
            text.text
        }
    }.also { resultText ->
        if (resultText is AnnotatedString) {
            BasicText(
                text = resultText,
                modifier = modifier,
                color = { textColor },
                overflow = overflow,
                softWrap = softWrap,
                maxLines = maxLines,
                minLines = minLines,
                onTextLayout = onTextLayout,
                style = textStyle,
                autoSize = autoSize,
            )
        } else {
            BasicText(
                text = resultText.toString(),
                modifier = modifier,
                color = { textColor },
                overflow = overflow,
                softWrap = softWrap,
                maxLines = maxLines,
                minLines = minLines,
                onTextLayout = onTextLayout,
                style = textStyle,
                autoSize = autoSize,
            )
        }
    }
}

@Stable
sealed interface TextUi {
    @Immutable
    data class Text(
        val text: String,
    ) : TextUi

    @Immutable
    data class TextId(
        @StringRes val resId: Int,
    ) : TextUi

    @Immutable
    data class TextPluralWithValueId(
        @PluralsRes val text: Int,
        val value: Int,
    ) : TextUi

    data class TextResFormatted(
        @StringRes val text: Int,
        val args: List<Any>,
    ) : TextUi

    @Immutable
    data class TextAnnotated(
        val text: AnnotatedString,
    ) : TextUi
}

fun TextUi.toText(
    context: Context,
    vararg formatArgs: Any,
): String =
    when (this) {
        is TextUi.Text -> {
            this.text
        }

        is TextUi.TextId -> {
            context.getString(this.resId, *formatArgs)
        }

        is TextUi.TextPluralWithValueId -> {
            context.resources.getQuantityString(this.text, value, value)
        }

        else -> {
            ""
        }
    }

@Composable
fun TextUi.toText(vararg formatArgs: Any): String =
    when (this) {
        is TextUi.Text -> {
            this.text
        }

        is TextUi.TextId -> {
            stringResource(this.resId, *formatArgs)
        }

        is TextUi.TextPluralWithValueId -> {
            LocalResources.current.getQuantityString(this.text, value, value)
        }

        is TextUi.TextResFormatted -> {
            LocalResources.current.getString(text, *args.toTypedArray())
        }

        else -> {
            ""
        }
    }

@Composable
fun TextUi.toCharSequence(vararg formatArgs: Any): CharSequence =
    when (this) {
        is TextUi.Text -> {
            this.text
        }

        is TextUi.TextId -> {
            stringResource(this.resId, *formatArgs)
        }

        is TextUi.TextPluralWithValueId -> {
            LocalResources.current.getQuantityString(this.text, value, value)
        }

        is TextUi.TextAnnotated -> {
            text
        }

        is TextUi.TextResFormatted -> {
            LocalResources.current.getString(text, *args.toTypedArray())
        }
    }

fun String?.toTextUi(): TextUi = if (isNullOrEmpty()) TextUi.Text("") else TextUi.Text(this)

fun Int.toTextUi(): TextUi = TextUi.TextId(this)

fun AnnotatedString.toTextUi(): TextUi = TextUi.TextAnnotated(this)