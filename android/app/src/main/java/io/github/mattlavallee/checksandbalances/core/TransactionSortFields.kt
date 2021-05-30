package io.github.mattlavallee.checksandbalances.core

enum class TransactionSortFields(val id: Int) {
    Location(0),
    Amount(1),
    Description(2),
    Date(3);

    companion object {
        fun fromInt(id: Int): TransactionSortFields = values().find { value -> value.id == id } ?: Date
    }
}