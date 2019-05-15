package com.skyscanner.challenge.ui.results;

import android.os.Bundle
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import com.skyscanner.challenge.R
import com.skyscanner.challenge.app.di.MainThreadScheduler
import com.skyscanner.challenge.databinding.ActivityFlightResultsBinding
import com.skyscanner.challenge.entity.network.request.CabinClass
import com.skyscanner.entity.request.FlightQuery
import com.skyscanner.viewmodel.results.SearchResultsViewModel
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import javax.inject.Inject


class FlightSearchResultsActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewBinder: ActivityFlightResultsBinding

    @Inject
    lateinit var viewModel: SearchResultsViewModel

    val mainThreadScheduler = AndroidSchedulers.mainThread()

    lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        compositeDisposable = CompositeDisposable()
        super.onCreate(savedInstanceState)
        setContentView(viewBinder.root)
        setupToolbar()
        setupSearchResultsView();
        viewBinder.model = viewModel
        viewBinder.data = viewModel.resultsData()
    }

    private fun setupToolbar() {
        setSupportActionBar(viewBinder.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        viewBinder.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupSearchResultsView() {
        viewBinder.searchResults.apply {
            val activity = this@FlightSearchResultsActivity
            layoutManager = LinearLayoutManager(activity)
            adapter = SearchResultsAdapter(layoutInflater).apply {
                val subscribe =
                    viewModel.resultsData().observeFlightsData().observeOn(mainThreadScheduler).subscribe { value -> updateWithNewList(value) }
                compositeDisposable.add(subscribe)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onStart() {
        super.onStart()
        viewModel.searchFlight(dummyQuery())
    }


    private fun dummyQuery(): FlightQuery {
        val calendar = Calendar.getInstance(Locale.forLanguageTag("en-GB"));
        var advanceDays = 0;
        when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> advanceDays = 1
            Calendar.MONDAY -> advanceDays = 7
            Calendar.TUESDAY -> advanceDays = 6
            Calendar.WEDNESDAY -> advanceDays = 5
            Calendar.THURSDAY -> advanceDays = 4
            Calendar.FRIDAY -> advanceDays = 3
            Calendar.SATURDAY -> advanceDays = 2
        }
        calendar.add(Calendar.DATE, advanceDays)
        val dateOut = calendar.time
        calendar.add(Calendar.DATE, 1)
        val dateIn = calendar.time
        return FlightQuery(
            country = "UK",
            currency = "GBP",
            adults = 1,
            destinationPlace = "LOND-sky",
            originPlace = "EDI-sky",
            locationSchema = "sky",
            inboundDate = dateIn,
            outboundDate = dateOut,
            locale = "en-GB",
            cabinClass = CabinClass.ECONOMY
        )
    }
    override fun onDestroy() {
        viewBinder.model = null
        viewBinder.data = null
        compositeDisposable.dispose()
        super.onDestroy()
    }


}