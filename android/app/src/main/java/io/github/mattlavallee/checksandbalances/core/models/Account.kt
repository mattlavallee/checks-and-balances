package io.github.mattlavallee.checksandbalances.core.models

import java.io.Serializable

class Account: Serializable {
    var id: Int
    var name: String
    var description: String
    var startingBalance: Double
    var isActive: Boolean

    constructor() {
        this.id = -1
        this.name = ""
        this.description = ""
        this.startingBalance = 0.0
        this.isActive = false
    }

    constructor(id: Int, name: String, description: String, balance: Double, isActive: Boolean) {
        this.id = id
        this.name = name
        this.description = description
        this.startingBalance = balance
        this.isActive = isActive
    }

    fun hasSameContents(item: Account): Boolean {
        return this.name == item.name &&
               this.description == item.description &&
               this.startingBalance == item.startingBalance
    }
}
