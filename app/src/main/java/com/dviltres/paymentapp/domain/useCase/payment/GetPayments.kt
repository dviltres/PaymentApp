package com.dviltres.paymentapp.domain.useCase.payment

import android.app.Application
import com.dviltres.paymentapp.data.common.Resource
import com.dviltres.paymentapp.domain.model.Payment
import com.dviltres.paymentapp.domain.repository.PaymentRepository
import com.dviltres.paymentapp.presentation.util.uiEvent.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import com.dviltres.paymentapp.R

class GetPayments @Inject constructor(
    private val repository: PaymentRepository,
    private val application: Application,
) {
    operator fun invoke(query:String): Flow<Resource<List<Payment>>> = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(repository.getPayments(query)))
        } catch(e: HttpException) {
            emit(Resource.Error(message = UiText.StringResource(R.string.unexpectedError).asString(application)))
        } catch(e: IOException) {
            emit(Resource.Error(message = UiText.StringResource(R.string.no_internet_connection).asString(application)))
        } catch(e: Exception) {
            emit(Resource.Error(message = UiText.StringResource(R.string.unexpectedError).asString(application)))
        }
    }
}