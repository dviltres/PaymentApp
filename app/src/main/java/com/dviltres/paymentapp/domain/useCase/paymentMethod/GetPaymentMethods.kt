package com.dviltres.paymentapp.domain.useCase.paymentMethod

import android.app.Application
import com.dviltres.paymentapp.R
import com.dviltres.paymentapp.data.common.Resource
import com.dviltres.paymentapp.domain.model.PaymentMethod
import com.dviltres.paymentapp.domain.repository.PaymentMethodRepository
import com.dviltres.paymentapp.presentation.util.uiEvent.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetPaymentMethods @Inject constructor(
    private val repository: PaymentMethodRepository,
    private val application: Application,
) {
    operator fun invoke(query:String): Flow<Resource<List<PaymentMethod>>> = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(repository.getPaymentMethods(query)))
        } catch(e: HttpException) {
            emit(Resource.Error(message = UiText.StringResource(R.string.unexpectedError).asString(application)))
        } catch(e: IOException) {
            emit(Resource.Error(message = UiText.StringResource(R.string.no_internet_connection).asString(application)))
        } catch(e: Exception) {
            emit(Resource.Error(message = UiText.StringResource(R.string.unexpectedError).asString(application)))
        }
    }
}