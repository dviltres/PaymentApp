package com.dviltres.paymentapp.presentation.installment

import androidx.compose.ui.geometry.Size


sealed class InstallmentEvent {
    object OnPaymentConfirmClick: InstallmentEvent()
    data class OnShowErrorMessage(val error: String): InstallmentEvent()
    data class OnSelectInstallment(val installment: String): InstallmentEvent()
    data class OnValueChangeInstallment(val installment: String):InstallmentEvent()
    object OnInstallmentClick: InstallmentEvent()
    data class OnGloballyPositioned(val size: Size):InstallmentEvent()
    object OnDismissRequest: InstallmentEvent()
    data class OnFocusChanged(val focus: Boolean):InstallmentEvent()
    data class OnNumberValueChange(val value: String):InstallmentEvent()
    data class OnNameValueChange(val value: String):InstallmentEvent()
    data class OnExpirationValueChange(val value: String):InstallmentEvent()
    data class OnCVCValueChange(val value: String):InstallmentEvent()
    data class OnDropdownMenuFocusChanged(val focus: Boolean):InstallmentEvent()

}
