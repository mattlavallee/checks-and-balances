package io.github.mattlavallee.checksandbalances.database.entities


class AccountWithSum(
    val id: Int,
    val name: String,
    val description: String?,
    val starting_balance: Double,
    val is_active: Boolean,
    val sum: Double
)
