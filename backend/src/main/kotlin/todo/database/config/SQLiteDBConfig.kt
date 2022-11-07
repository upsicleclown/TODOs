package todo.database.config

/**
 * Default configuration for `SQLiteDB`.
 *
 * Notes:
 *  - Connection string could be a secret.
 */
class SQLiteDBConfig(override val connectionString: String = "jdbc:sqlite:todo.db") : ISQLiteConfig
