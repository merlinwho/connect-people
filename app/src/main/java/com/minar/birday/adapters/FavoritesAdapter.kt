package com.minar.birday.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.minar.birday.FavoritesFragment
import com.minar.birday.R
import com.minar.birday.persistence.EventResult
import kotlinx.android.synthetic.main.event_row.view.eventDate
import kotlinx.android.synthetic.main.event_row.view.eventPerson
import kotlinx.android.synthetic.main.favorite_row.view.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class FavoritesAdapter internal constructor(context: Context, favoritesFragment: FavoritesFragment) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {
    private var events = emptyList<EventResult>() // Cached copy of events
    private val appContext = context
    private val fragment = favoritesFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.favorite_row, parent, false))
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val current = events[position]
        holder.setUpView(event = current)
    }

    inner class FavoriteViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        private val eventPerson: TextView = view.eventPerson
        private val eventDate: TextView = view.eventDate
        private val eventYears: TextView = view.eventYears

        // Set every necessary text and click action in each row
        fun setUpView(event: EventResult?) {
            val personName = event?.name + " " + event?.surname
            val formatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
            val nextDate = event?.nextDate?.format(formatter)
            val nextAge = appContext.getString(R.string.next_age_years) + ": " +
                    (event?.nextDate?.year?.minus(event.originalDate.year)).toString() +
                    ", " + appContext.getString(R.string.born_in) + " " + event?.originalDate?.year
            eventPerson.text = personName
            eventDate.text = nextDate
            eventYears.text = nextAge
        }
    }

    internal fun setEvents(events: List<EventResult>) {
        this.events = events
        notifyDataSetChanged()
    }

    override fun getItemCount() = events.size

}