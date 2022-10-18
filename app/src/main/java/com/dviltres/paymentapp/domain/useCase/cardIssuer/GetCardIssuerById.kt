package com.dviltres.paymentapp.domain.useCase.cardIssuer

import android.app.Application
import com.dviltres.paymentapp.R
import com.dviltres.paymentapp.data.common.Resource
import com.dviltres.paymentapp.domain.model.CardIssuer
import com.dviltres.paymentapp.domain.repository.CardIssuerRepository
import com.dviltres.paymentapp.presentation.util.uiEvent.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCardIssuerById @Inject constructor(
    private val repository: CardIssuerRepository,
    private val application: Application,
) {
    operator fun invoke(cardIssuerId:String): Flow<Resource<CardIssuer>> = flow {
        try {
            emit(Resource.Loading())
            val cardIssuer = repository.getCardIssuerId(cardIssuerId)
            if(cardIssuer != null)
                emit(Resource.Success(cardIssuer))
            else
                emit(Resource.Error(message = UiText.StringResource(R.string.no_payment_method_found).asString(application)))

        } catch(e: HttpException) {
            emit(Resource.Error(message = UiText.StringResource(R.string.unexpectedError).asString(application)))
        } catch(e: IOException) {
            emit(Resource.Error(message = UiText.StringResource(R.string.no_internet_connection).asString(application)))
        } catch(e: Exception) {
            emit(Resource.Error(message = UiText.StringResource(R.string.unexpectedError).asString(application)))
        }
    }
}