package models

import kotlinx.serialization.Serializable

/**
 *  User.
 */

@Serializable
data class User(var username: String, var password: String) {
    /*
        Two users are said to be equal if they have the same username.
     */
    override fun equals(other: Any?): Boolean {
        return other is User && other.username == this.username
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + password.hashCode()
        return result
    }
}
