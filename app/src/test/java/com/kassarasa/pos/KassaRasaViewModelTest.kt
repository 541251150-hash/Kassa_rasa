package com.kassarasa.pos

import com.kassarasa.pos.model.MenuCategory
import com.kassarasa.pos.model.PaymentMethod
import com.kassarasa.pos.state.KassaRasaViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class KassaRasaViewModelTest {

    @Test
    fun initialCartMatchesDesignTotal() {
        val vm = KassaRasaViewModel()
        assertEquals(2, vm.cartTotalItemCount)
        assertEquals(44_000, vm.cartTotalAmount)
    }

    @Test
    fun addingAndCompletingOrderUpdatesDashboardAndHistory() {
        val vm = KassaRasaViewModel()
        val initialSales = vm.totalSalesToday
        val initialOrders = vm.totalOrdersToday
        val initialHistorySize = vm.transactionHistory.size

        vm.selectedPaymentMethod = PaymentMethod.CASH
        vm.cashReceivedAmount = 50_000
        val record = vm.completePayment()

        assertNotNull(record)
        assertEquals(6_000, record!!.changeAmount)
        assertEquals(initialSales + 44_000, vm.totalSalesToday)
        assertEquals(initialOrders + 1, vm.totalOrdersToday)
        assertEquals(initialHistorySize + 1, vm.transactionHistory.size)
        assertEquals(0, vm.cartTotalAmount)
    }

    @Test
    fun addAndDeleteMenuUpdatesManagedList() {
        val vm = KassaRasaViewModel()
        val beforeCount = vm.managedMenus.size

        vm.addMenuItem("Es Kopi Caramel", MenuCategory.DRINK, 23_000, 15, "cup")
        assertEquals(beforeCount + 1, vm.managedMenus.size)

        val addedId = vm.managedMenus.first().id
        vm.deleteMenuItem(addedId)
        assertEquals(beforeCount, vm.managedMenus.size)
    }

    @Test
    fun registerAndLoginValidationWorks() {
        val vm = KassaRasaViewModel()
        assertTrue(vm.register("invalid", "123456", "123456").isFailure)
        assertTrue(vm.register("kasir@kassarasa.id", "123", "123").isFailure)
        assertTrue(vm.register("kasir@kassarasa.id", "123456", "654321").isFailure)
        assertTrue(vm.register("kasir@kassarasa.id", "123456", "123456").isSuccess)
        assertTrue(vm.login("kasir@kassarasa.id", "123456").isSuccess)
    }
}
