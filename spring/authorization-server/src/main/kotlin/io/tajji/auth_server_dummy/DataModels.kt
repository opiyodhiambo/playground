package io.tajji.auth_server_dummy

import io.tajji.auth_server_dummy.AccountRole.OperationalStatus
import java.util.*

data class PortfolioClaim(
    val portfolioId: UUID,
    val operationalStatus: OperationalStatus,
    val portfolioName: String,
    val portfolioImageURL: String,
    val portfolioURL: String,
    val billingStatus: PortfolioBillingStatus,
    val portfolioRoles: Set<AccountRole>,
    val propertyRoles: Set<PropertyRole>
)

enum class PortfolioBillingStatus(val value: String) {
    STABLE("STABLE"),
    IN_GRACE_PERIOD("IN GRACE PERIOD"),
    IN_DEFAULT("DEFAULTING");

    fun canTransitionTo(status: PortfolioBillingStatus): Boolean =
        when (this) {
            STABLE -> setOf(STABLE, IN_GRACE_PERIOD).contains(status)
            IN_GRACE_PERIOD -> setOf(IN_DEFAULT, STABLE).contains(status)
            IN_DEFAULT -> setOf(STABLE).contains(status)
        }
}

data class PortfolioRole(val portfolioId: UUID, val roles: Set<AccountRole>)
data class PropertyRole(val propertyId: UUID, val roles: AccountRole)

enum class AccountRole(val value: String) {
    GUEST("guest"),
    ADMIN("admin"),
    LANDLORD("landlord"),
    MANAGER("manager"),
    CARETAKER("caretaker"),
    RESIDENT("resident"),
    INVESTOR("investor"),
    SERVICE_VENDOR("service_vendor"),
    MAINTENANCE_STAFF_MEMBER("maintenance_staff_member"),
    CUSTOMER_SUPPORT_STAFF_MEMBER("customer_support_staff_member");

    companion object {
        fun fromValue(value: String): AccountRole {
            return entries.find { it.value == value }
                ?: throw IllegalArgumentException("Could not derive account role from $value")
        }
    }


    enum class OperationalStatus(val value: String) {
        OPERATIONAL("OPERATIONAL"),
        DEGRADED("DEGRADED"),
        INOPERATIVE("INOPERATIVE");

        fun canTransitionTo(status: OperationalStatus): Boolean {
            return when (status) {
                OPERATIONAL -> listOf(DEGRADED).contains(this)
                DEGRADED -> listOf(INOPERATIVE, OPERATIONAL).contains(this)
                INOPERATIVE -> listOf(OPERATIONAL, DEGRADED).contains(this)
            }
        }
    }
}