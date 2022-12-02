package theme

import kotlinx.serialization.Serializable

@Serializable
enum class Theme {
    LIGHT,
    DARK;

    companion object {
        fun stylesForTheme(theme: Theme): String {
            return when (theme) {
                LIGHT -> LIGHT_MODE_STYLES.joinToString(" ")
                DARK -> DARK_MODE_STYLES.joinToString(" ")
            }
        }

        fun primaryColorForTheme(theme: Theme): String {
            return when (theme) {
                LIGHT -> "#171717"
                DARK -> "#EEEEEE"
            }
        }
    }
}

val LIGHT_MODE_STYLES: List<String> = listOf(
    "-background--secondary: " + "#585252" + ";",
    "-text--secondary: " + "#585252" + ";",
    "-text-highlight: " + "#585252" + ";",
    "-border--secondary: " + "#585252" + ";",

    "-text--negative: " + "#FFFFFF" + ";",
    "-text-highlight--negative: " + "#FFFFFF" + ";",
    "-background--primary: " + "#FFFFFF" + ";",
    "-border--negative: " + "#FFFFFF" + ";",

    "-background--negative: " + "#171717" + ";",
    "-border--primary: " + "#171717" + ";",
    "-text--primary: " + "#171717" + ";",

    "-button--confirm: " + "#0029FF" + ";",
    "-selection--hover: " + "#0029FF" + ";",

    "-selection--static: " + "#8000FF" + ";",

    "-foreground--accent: " + "#833F00" + ";",

    "-background--accent: " + "#FFBD80" + ";"
)

val DARK_MODE_STYLES: List<String> = listOf(
    "-background--secondary: " + "#EDEAEA" + ";",
    "-text--secondary: " + "#EDEAEA" + ";",
    "-text-highlight: " + "#EDEAEA" + ";",
    "-border--secondary: " + "#EDEAEA" + ";",

    "-text--negative: " + "#242D4E" + ";",
    "-text-highlight--negative: " + "#242D4E" + ";",
    "-background--primary: " + "#242D4E" + ";",
    "-border--negative: " + "#242D4E" + ";",

    "-background--negative: " + "#EEEEEE" + ";",
    "-border--primary: " + "#EEEEEE" + ";",
    "-text--primary: " + "#EEEEEE" + ";",

    "-button--confirm: " + "#FF9900" + ";",
    "-selection--hover: " + "#FF9900" + ";",

    "-selection--static: " + "#FFD337" + ";",

    "-foreground--accent: " + "#BBA3FF" + ";",

    "-background--accent: " + "#361496" + ";"
)
