package controllers

import views.View;

abstract class Controller {
    abstract var view: View?

    /*
    Attaches a view to the current Controller, throws an error if there is already a view attached.

    Alternate idea: we create our own exception class and throw those to provide richer error information
     */
    fun register(newView: View) {
        if (view != null)
            throw Error(message = "View already attached to ${this::class.simpleName}")

        view = newView
    }

    fun attachedView(): View? {
        return view
    }

    /*
    Cleans up the current view state and sets the attached view to null
     */
    fun detach() {
        view = null
    }
}