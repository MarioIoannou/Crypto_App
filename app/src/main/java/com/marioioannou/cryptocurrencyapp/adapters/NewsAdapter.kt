package com.marioioannou.cryptocurrencyapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.marioioannou.cryptocurrencyapp.R
import com.marioioannou.cryptocurrencyapp.coin_data.model.coin_news.New
import com.marioioannou.cryptocurrencyapp.databinding.NewsRecyclerviewRowBinding

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(var binding: NewsRecyclerviewRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<New>() {
        override fun areItemsTheSame(oldItem: New, newItem: New): Boolean {
            return oldItem.link == newItem.link
        }

        override fun areContentsTheSame(oldItem: New, newItem: New): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            NewsRecyclerviewRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article =  differ.currentList[position]
        holder.binding.apply {
            if (article.imgURL == null){
                imgImage.load(R.drawable.no_image_available)
            }else{
                imgImage.load(article.imgURL)
            }
            tvTitle.text = article.title
            //tvDescription.text = article.description
            tvSource.text = article.source
            cvReadMore.setOnClickListener {
                    onItemClickListener?.let{
                        it(article)
                    }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((New) -> Unit)? = null

    fun setOnItemClickListener(listener: (New) -> Unit){
        onItemClickListener = listener
    }
}