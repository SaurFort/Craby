package fr.saurfort.database.init

import org.jetbrains.exposed.sql.Database
import java.sql.Connection

class MySQLDatabase(private val databaseName: String) {
    private val databaseAddress = "jdbc:mysql:"

    init {
        Database.connect("jdbc:mysql:")
    }

    companion object {
        var conn: Connection? = null
        private val REQUIRED_TABLES = arrayOf("users_messages", "registered")
    }
}
