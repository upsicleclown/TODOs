package models

import kotlinx.serialization.Serializable

/*
    Window settings.

    Default values are stored in `resources/server/database/window_settings.json` and loaded by the FileDB.
 */
@Serializable
data class WindowSettings(var x: Double, var y: Double, var height: Double, var width: Double)
