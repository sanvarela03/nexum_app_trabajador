package com.example.nexum_trabajador.ui.components

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 21/08/2025
 * @version 1.0
 */
@DslMarker
annotation class RichTextDsl

@RichTextDsl
class RichTextBuilder(
    private val defaultTextStyle: SpanStyle,
    private val defaultLinkStyle: SpanStyle,
    private val tag: String = "LINK"
) {
    private val actions = mutableMapOf<String, () -> Unit>()
    private val annotated = AnnotatedString.Builder()

    /** Plain text segment */
    fun text(value: String, style: SpanStyle? = null) {
        annotated.withStyle(style ?: defaultTextStyle) { append(value) }
    }

    /** Clickable segment */
    fun link(value: String, style: SpanStyle? = null, onClick: () -> Unit) {
        val id = "id-${actions.size}-${value.hashCode()}"
        actions[id] = onClick
        annotated.pushStringAnnotation(tag = tag, annotation = id)
        annotated.withStyle(style ?: defaultLinkStyle) { append(value) }
        annotated.pop()
    }

    /** Convenience helpers */
    fun space(count: Int = 1) = text(" ".repeat(count))
    fun appendRaw(value: String) = annotated.append(value)

    fun build(): Pair<AnnotatedString, Map<String, () -> Unit>> =
        annotated.toAnnotatedString() to actions.toMap()
}

@Composable
fun RichClickableText(
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle(
        fontSize = 14.sp,
        textAlign = TextAlign.Start,
        color = Color(0xFF000000)
    ),
    defaultTextStyle: SpanStyle = SpanStyle(
        color = style.color
    ),
    defaultLinkStyle: SpanStyle = SpanStyle(
        color = Color(0xFF828282),
        textDecoration = TextDecoration.None,
        fontWeight = FontWeight.Normal
    ),
    content: RichTextBuilder.() -> Unit
) {
    val (annotated, actions) = RichTextBuilder(
        defaultTextStyle = defaultTextStyle,
        defaultLinkStyle = defaultLinkStyle
    ).apply(content).build()

    ClickableText(
        text = annotated,
        style = style,
        modifier = modifier
    ) { offset ->
        annotated.getStringAnnotations(start = offset, end = offset)
            .firstOrNull()
            ?.let { ann -> actions[ann.item]?.invoke() }
    }
}
