package com.example.watcht.ui.view.List

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.watcht.R
import com.example.watcht.data.model.PopularMovieListResponse
import com.example.watcht.databinding.MovieItemBinding
import com.example.watcht.utils.utils.POSTER_BASE_URL
import javax.inject.Inject


class MoviesListAdapter @Inject constructor(

) : RecyclerView.Adapter<MoviesListAdapter.ViewHolder>() {

    private lateinit var binding: MovieItemBinding
    private lateinit var context: Context
    private lateinit var onClickItem: ((PopularMovieListResponse.Result) -> Unit)

    fun setOnClickItemListener(listener: (PopularMovieListResponse.Result) -> Unit) {
        onClickItem = listener
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        binding = MovieItemBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder()

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.holder(differ.currentList[position])
        holder.setIsRecyclable(false)

    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ViewHolder() : RecyclerView.ViewHolder(binding.root) {

        fun holder(
            item: PopularMovieListResponse.Result,
        ) {
            binding.apply {


                tvMovieName.text = item.originalTitle
                tvLang.text = item.originalLanguage
                tvRate.text = item.voteAverage.toString()
                tvMovieDateRelease.text = item.releaseDate
                val moviePoster = POSTER_BASE_URL + item.posterPath
                imgMovie.load(moviePoster) {
                    crossfade(true)
                    placeholder(R.drawable.poster_placeholder)
                    scale(scale = Scale.FILL)
                }

            }
            itemView.setOnClickListener { onClickItem(item) }
        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<PopularMovieListResponse.Result>() {
        override fun areItemsTheSame(
            oldItem: PopularMovieListResponse.Result,
            newItem: PopularMovieListResponse.Result
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: PopularMovieListResponse.Result,
            newItem: PopularMovieListResponse.Result
        ): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)


}