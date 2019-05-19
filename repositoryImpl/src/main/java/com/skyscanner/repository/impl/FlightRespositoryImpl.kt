package com.skyscanner.repository.impl

import android.net.Uri
import com.skyscanner.challenge.entity.network.search.FlightResponse
import com.skyscanner.entity.request.FlightQuery
import com.skyscanner.model.SessionModel
import com.skyscanner.repository.ConfigGateway
import com.skyscanner.repository.FlightRepository
import com.skyscanner.repository.PlatformUtils
import com.skyscanner.repository.impl.retrofit.FlightApi
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import com.skyscanner.entity.response.Response as SkyResponse

class FlightRespositoryImpl @Inject constructor(
    private val configGateway: ConfigGateway,
    private val flightApi: FlightApi,
    private val platformUtils: PlatformUtils
) : FlightRepository {

    override fun createSession(query: FlightQuery): Observable<SkyResponse<SessionModel>> {

        val inboundDate = if (query.inboundDate != null) platformUtils.formatDate(
            "yyyy-MM-dd",
            query.inboundDate!!,
            query.locale
        ) else ""

        val outboundDate = platformUtils.formatDate("yyyy-MM-dd", query.outboundDate, query.locale)

        val map = HashMap<String, String>()
        map.put("country", query.country);
        map.put("currency", query.currency);
        map.put("locale", query.locale);
        map.put("originplace", query.originPlace);
        map.put("destinationplace", query.destinationPlace);
        map.put("outbounddate", outboundDate);
        map.put("inboundDate",inboundDate)
        map.put("cabinclass", query.cabinClass?.cabinClassName ?: "");
        map.put("adults", query.adults.toString());
        map.put("children", query.children.toString());
        map.put("infants", query.infants.toString());
        map.put("locationSchema", query.locationSchema);
        map.put("apiKey", configGateway.getApiKey())
        return flightApi.createSession(map).subscribeOn(Schedulers.io()).map {
            val sessionsUrl = it.headers().get("location")
            val body = it.body()
            if (it.isSuccessful && !sessionsUrl.isNullOrBlank()) {
                SkyResponse<SessionModel>(
                    success = it.isSuccessful,
                    value = SessionModel(sessionsUrl, body!!)
                )
            } else {
                SkyResponse<SessionModel>(success = it.isSuccessful, value = null)
            }
        }
    }

    override fun getFlights(sessionurl: String): Observable<SkyResponse<FlightResponse>> {
        val finalUrl =
            Uri.parse(sessionurl)
                .buildUpon()
                .appendQueryParameter("apiKey", configGateway.getApiKey())
                .build()
                .toString()
        return flightApi.getFlights(finalUrl).subscribeOn(Schedulers.io()).map {
            SkyResponse(it.body(), it.isSuccessful)
        }
    }

}