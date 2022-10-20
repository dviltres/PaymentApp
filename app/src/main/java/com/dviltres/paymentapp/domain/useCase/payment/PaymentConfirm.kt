package com.dviltres.paymentapp.domain.useCase.payment

import android.app.Application
import com.dviltres.paymentapp.R
import com.dviltres.paymentapp.data.common.Resource
import com.dviltres.paymentapp.domain.model.CreditCard
import com.dviltres.paymentapp.domain.model.Payment
import com.dviltres.paymentapp.domain.repository.PaymentRepository
import com.dviltres.paymentapp.presentation.util.uiEvent.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PaymentConfirm @Inject constructor(
    private val repository: PaymentRepository,
    private val application: Application,
) {
    operator fun invoke(card: CreditCard, payment: Payment): Flow<Resource<Payment>> = flow {
        try {
            emit(Resource.Loading())
            if(payment.paymentMethod != null && payment.cardIssuer != null && !payment.installment.isNullOrBlank() &&
                payment.amount != null && card.number.isNotBlank() && card.holderName.isNotBlank() && card.holderName.isNotBlank()
            )
            {
                val paymentSuccess = repository.paymentConfirm(
                    card = card,
                    payment = payment
                )
                if(paymentSuccess != null)
                    emit(Resource.Success(payment))
                else
                    emit(Resource.Error(message = UiText.StringResource(R.string.unexpectedError).asString(application)))
            }
            else {
                emit(Resource.Error(message = UiText.StringResource(R.string.error_input_data).asString(application)))
            }
        } catch(e: HttpException) {
            emit(Resource.Error(message = UiText.StringResource(R.string.unexpectedError).asString(application)))
        } catch(e: IOException) {
            emit(Resource.Error(message = UiText.StringResource(R.string.no_internet_connection).asString(application)))
        } catch(e: Exception) {
            emit(Resource.Error(message = UiText.StringResource(R.string.unexpectedError).asString(application)))
        }
    }
}