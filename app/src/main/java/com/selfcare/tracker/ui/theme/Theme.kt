package com.selfcare.tracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColors(
    val primary: Color = Color(0xFF6366F1),
    val primaryDark: Color = Color(0xFF4F46E5),
    val secondary: Color = Color(0xFF8B5CF6),
    val danger: Color = Color(0xFFEF4444),
    val success: Color = Color(0xFF10B981),
    val background: Color = Color(0xFFF8FAFC),
    val surface: Color = Color(0xFFFFFFFF),
    val textPrimary: Color = Color(0xFF1E293B),
    val textSecondary: Color = Color(0xFF64748B),
    val border: Color = Color(0xFFE2E8F0)
)

val LocalAppColors = staticCompositionLocalOf { AppColors() }

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6366F1),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0E7FF),
    secondary = Color(0xFF8B5CF6),
    onSecondary = Color.White,
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF1E293B),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1E293B),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF64748B),
    outline = Color(0xFFE2E8F0),
    error = Color(0xFFEF4444),
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF818CF8),
    onPrimary = Color(0xFF1E1B4B),
    primaryContainer = Color(0xFF3730A3),
    secondary = Color(0xFFA78BFA),
    onSecondary = Color(0xFF2E1065),
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF1F5F9),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = Color(0xFF475569),
    error = Color(0xFFF87171),
    onError = Color(0xFF7F1D1D)
)

@Composable
fun SelfCareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(LocalAppColors provides AppColors(
        primary = colorScheme.primary,
        primaryDark = colorScheme.primaryContainer,
        secondary = colorScheme.secondary,
        danger = colorScheme.error,
        success = Color(0xFF10B981),
        background = colorScheme.background,
        surface = colorScheme.surface,
        textPrimary = colorScheme.onBackground,
        textSecondary = colorScheme.onSurfaceVariant,
        border = colorScheme.outline
    )) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}
