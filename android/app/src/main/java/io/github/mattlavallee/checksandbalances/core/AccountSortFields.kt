package io.github.mattlavallee.checksandbalances.core

enum class AccountSortFields(val id: Int) {
    Name(0),
    Balance(1);

    companion object {
        fun fromInt(id: Int): AccountSortFields = values().find { value -> value.id == id } ?: Name
    }
}