package ru.vsu.cs.mobiledevelopmentproject.ui.schedule

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.vsu.cs.mobiledevelopmentproject.databinding.ItemLessonBinding
import ru.vsu.cs.mobiledevelopmentproject.domain.model.Lesson
import ru.vsu.cs.mobiledevelopmentproject.domain.model.WeekType

class LessonAdapter : RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {

    private var items: List<Lesson> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<Lesson>) {
        if (newItems == items) return
        items = newItems
        notifyDataSetChanged()
    }

    class LessonViewHolder(val binding: ItemLessonBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val binding = ItemLessonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LessonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        val lesson = items[position]
        with(holder.binding) {
            lessonTime.text = "${lesson.day} · ${lesson.time}"
            lessonWeek.text = when (lesson.week) {
                WeekType.EVERY -> "каждую неделю"
                WeekType.NUMERATOR -> "числитель"
                WeekType.DENOMINATOR -> "знаменатель"
            }
            lessonTitle.text = lesson.title
            lessonDetails.text = lesson.details
            lessonDetails.isVisible = lesson.details != null
        }
    }

    override fun getItemCount(): Int = items.size
}