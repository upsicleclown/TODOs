package models

import kotlinx.serialization.Serializable
import theme.Theme

/*
    Window settings.
 */
@Serializable
data class WindowSettings(var x: Double, var y: Double, var height: Double, var width: Double, var theme: Theme)
