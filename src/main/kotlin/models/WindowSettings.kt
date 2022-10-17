package models

import kotlinx.serialization.Serializable

/*
    Window settings.

    Height and width default to 500.0 when nothing is saved in the backend.

    TODO: For some unknown reason, the default values in the constructor cannot be the same as the values in
        `resources/server/database/window_settings.json` or else the get endpoint does not return the fields.
 */
@Serializable
data class WindowSettings(var height: Double = 500.0, var width: Double = 500.0)
