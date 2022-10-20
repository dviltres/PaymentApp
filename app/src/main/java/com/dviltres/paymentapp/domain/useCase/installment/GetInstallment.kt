package com.dviltres.paymentapp.domain.useCase.installment

import android.app.Application
import com.dviltres.paymentapp.R
import com.dviltres.paymentapp.data.common.Resource
import com.dviltres.paymentapp.domain.model.Installment
import com.dviltres.paymentapp.domain.repository.InstallmentRepository
import com.dviltres.paymentapp.presentation.util.uiEvent.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetInstallment @Inject constructor(
    private val repository: InstallmentRepository,
    private val application: Application,
) {
    operator fun invoke(amount:String, paymentMethodId:String, issuerId:String): Flow<Resource<Installment>> = flow {
        try {
            emit(Resource.Loading())
            val installment = repository.getInstallment(
                amount = amount.toDouble(),
                paymentMethodId = paymentMethodId,
                issuerId = issuerId
            )
            if(installment != null){
                if(installment.recommended_message.isEmpty())
                    emit(Resource.Error(message = UiText.StringResource(R.string.no_exist_installment).asString(application)))
               else
                   emit(Resource.Success(installment))
            }

            else
                emit(Resource.Error(message = UiText.StringResource(R.string.unexpectedError).asString(application)))

        } catch(e: HttpException) {
            emit(Resource.Error(message = UiText.StringResource(R.string.unexpectedError).asString(application)))
        } catch(e: IOException) {
            emit(Resource.Error(message = UiText.StringResource(R.string.no_internet_connection).asString(application)))
        } catch(e: Exception) {
            emit(Resource.Error(message = UiText.StringResource(R.string.unexpectedError).asString(application)))
        }
    }
}