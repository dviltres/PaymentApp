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

class GetCardIssuers @Inject constructor(
    private val repository: CardIssuerRepository,
    private val application: Application,
) {
    operator fun invoke(query:String, paymentMethodId:String): Flow<Resource<List<CardIssuer>>> = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(repository.getCardIssuers(query = query, paymentMethodId = paymentMethodId)))
        } catch(e: HttpException) {
            emit(Resource.Error(message = UiText.StringResource(R.string.unexpectedError).asString(application)))
        } catch(e: IOException) {
            emit(Resource.Error(message = UiText.StringResource(R.string.no_internet_connection).asString(application)))
        } catch(e: Exception) {
            emit(Resource.Error(message = UiText.StringResource(R.string.unexpectedError).asString(application)))
        }
    }
}