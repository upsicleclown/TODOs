package models

import tornadofx.EventBus.RunOn.*;
import tornadofx.FXEvent

/**
    Creates a request to the GroupViewController to refresh the rendered information
 */
class InvalidateGroupViewRequest(val group: Group) : FXEvent(BackgroundThread)

/**
    Fired by the GroupViewController after it has finished completing an
    InvalidateGroupViewRequest. Contains all info required to render a group view
 */
class InvalidateGroupViewEvent(val items: List<Item>) : FXEvent()