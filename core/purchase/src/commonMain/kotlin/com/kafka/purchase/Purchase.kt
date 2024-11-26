package com.kafka.purchase

import androidx.compose.runtime.Composable
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.PurchasesDelegate
import com.revenuecat.purchases.kmp.models.CustomerInfo
import com.revenuecat.purchases.kmp.models.PurchasesError
import com.revenuecat.purchases.kmp.models.StoreProduct
import com.revenuecat.purchases.kmp.models.StoreTransaction
import me.tatarka.inject.annotations.Inject

@Inject
class Purchase {
    fun authenticate() {
        Purchases.sharedInstance.logIn("user_token", {}) { customerInfo, userCancelled ->

        }
    }

    fun getOfferings() {
        Purchases.sharedInstance.getOfferings({ error ->
            // An error occurred
        }) { offerings ->
        }
    }

    @Composable
    fun purchase() {

    }

    fun observeSubscriptionStatus() {
        Purchases.sharedInstance.delegate = object : PurchasesDelegate {
            override fun onCustomerInfoUpdated(customerInfo: CustomerInfo) {
                // handle any changes to customerInfo
            }

            override fun onPurchasePromoProduct(
                product: StoreProduct,
                startPurchase: (onError: (error: PurchasesError, userCancelled: Boolean) -> Unit, onSuccess: (storeTransaction: StoreTransaction, customerInfo: CustomerInfo) -> Unit) -> Unit
            ) {

            }
        }
    }
}
