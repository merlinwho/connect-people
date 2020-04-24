package com.minar.birday.adapters

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.minar.birday.R
import com.minar.birday.persistence.EventResult
import kotlinx.android.synthetic.main.event_row.view.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class EventAdapter internal constructor(context: Context) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    private var events = emptyList<EventResult>() // Cached copy of events
    private val appContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.event_row, parent, false))
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val current = events[position]
        holder.setUpView(event = current)
    }

    inner class EventViewHolder (view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private val favoriteButton: ImageView = view.favoriteButton
        private val eventPerson: TextView = view.eventPerson
        private val eventDate: TextView = view.eventDate
        private val eventYears: TextView = view.eventYears

        init {
            view.setOnClickListener(this)
        }

        // Set every necessary text and click action in each row
        fun setUpView(event: EventResult?) {
            val personName = event?.name + " " + event?.surname + " is favorite? " + event?.favorite // TODO remove favorite data
            val formatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
            val nextDate = event?.nextDate?.format(formatter)
            val nextAge = appContext.getString(R.string.next_age_years) + ": " + (event?.nextDate?.year?.minus(event.originalDate.year)).toString()
            eventPerson.text = personName
            eventDate.text = nextDate
            eventYears.text = nextAge

            // Manage the favorite logic
            if(event?.favorite == false) favoriteButton.setImageResource(R.drawable.animated_to_favorite)
            else favoriteButton.setImageResource(R.drawable.animated_from_favorite)
            favoriteButton.setOnClickListener {
                if(event?.favorite == true) {
                    event.favorite = false
                    //setEvents(events)
                    favoriteButton.setImageResource(R.drawable.animated_from_favorite)
                }
                else {
                    event?.favorite = true
                    //setEvents(events)
                    favoriteButton.setImageResource(R.drawable.animated_to_favorite)
                }
                (favoriteButton.drawable as AnimatedVectorDrawable).start()
            }
        }

        override fun onClick(v: View?) {
            println("test")
        }
    }

    internal fun setEvents(events: List<EventResult>) {
        this.events = events
        notifyDataSetChanged()
    }

    override fun getItemCount() = events.size

}