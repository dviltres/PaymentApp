package com.dviltres.paymentapp.presentation.installment


sealed class InstallmentEvent {
    object OnConfirmClick: InstallmentEvent()
    object OnRefresh: InstallmentEvent()
    data class OnShowErrorMessage(val error: String): InstallmentEvent()
    data class OnSelectInstallment(val installment: String): InstallmentEvent()
}
