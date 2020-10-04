package com.minar.birday.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.minar.birday.R
import com.minar.birday.model.EventResult
import com.minar.birday.utilities.getAge
import com.minar.birday.utilities.getRemainingDays
import kotlinx.android.synthetic.main.event_row.view.eventDate
import kotlinx.android.synthetic.main.event_row.view.eventPerson
import kotlinx.android.synthetic.main.favorite_row.view.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class FavoritesAdapter internal constructor(context: Context) : ListAdapter<EventResult, FavoritesAdapter.FavoriteViewHolder>(FavoritesDiffCallback()) {
    private val appContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.favorite_row, parent, false))
    }

    override fun onBindViewHolder(holder: FavoritesAdapter.FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // Can't use elsewhere without overriding as a public function
    public override fun getItem(position: Int): EventResult {
        return super.getItem(position)
    }

    inner class FavoriteViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        private val eventPerson = view.eventPerson
        private val eventDate = view.eventDate
        private val eventYears = view.eventYears
        private val eventCountdown = view.eventCountdown

        // Set every necessary text and click action in each row
        fun bind(event: EventResult) {
            val personName = event.name + " " + event.surname
            val formatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
            val age = getAge(event)
            val daysCountdown = "-" + getRemainingDays(event.nextDate!!)
            var nextDate = event.nextDate.format(formatter)

            if (event.yearMatter == false) nextDate = event.nextDate.format(formatter)
            val actualAge = appContext.getString(R.string.next_age_years) + ": " + age.toString() +
                    ", " + appContext.getString(R.string.born_in) + " " + event.originalDate.year
            eventPerson.text = personName
            eventDate.text = nextDate
            eventCountdown.text = daysCountdown
            // Age -2 means that the year is not considered and the age is meaningless
            // TODO write something else in the third line instead of removing it
            if (age != -2) {
                eventYears.visibility = View.VISIBLE
                eventYears.text = actualAge
            }
            else eventYears.visibility = View.GONE
        }
    }
}

class FavoritesDiffCallback : DiffUtil.ItemCallback<EventResult>() {
    override fun areItemsTheSame(oldItem: EventResult, newItem: EventResult): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: EventResult, newItem: EventResult): Boolean {
        return oldItem == newItem
    }
}