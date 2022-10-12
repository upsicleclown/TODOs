package models

import tornadofx.EventBus.RunOn.*;
import tornadofx.FXEvent

/**
    Fired by the Sidepane when a Group is focused on the nav bar
 */
class InvalidateGroupViewRequest(val group: Group) : FXEvent(BackgroundThread)

/**
    Fired by the GroupViewController after it has finished completing an
    InvalidateGroupViewRequest
 */
class InvalidateGroupViewEvent(val items: List<Item>) : FXEvent()