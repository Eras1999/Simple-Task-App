package com.example.taskapp

data class Task(val description: String, val date: String) {
    override fun toString(): String {
        return "$description|$date"
    }

    companion object {
        fun fromString(taskString: String): Task {
            val parts = taskString.split("|")
            return Task(parts[0], parts[1])
        }
    }
}