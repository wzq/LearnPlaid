package com.example.learnplaid.pages

import android.app.Activity
import android.app.ActivityOptions
import android.app.SharedElementCallback
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.learnplaid.GlideApp
import com.example.learnplaid.R
import com.example.learnplaid.model.Article
import com.example.learnplaid.model.result.PlaidItem
import android.util.Pair
import androidx.core.os.bundleOf

class PlaidAdapter(
    private val host: Activity,
    private val isDarkTheme: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(host)
    private val shotLoadingPlaceholders: Array<ColorDrawable?>
//    private val shotPreloadSizeProvider = ViewPreloadSizeProvider<Shot>()

    @ColorInt
    private val initialGifBadgeColor: Int

    var items: List<PlaidItem> = emptyList()
        /**
         * Main entry point for setting items to this adapter.
         */
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    init {
        setHasStableIds(true)

        // get the dribbble shot placeholder colors & badge color from the theme
        val a = host.obtainStyledAttributes(R.styleable.DribbbleFeed)
        val loadingColorArrayId =
            a.getResourceId(R.styleable.DribbbleFeed_shotLoadingPlaceholderColors, 0)
        if (loadingColorArrayId != 0) {
            val placeholderColors = host.resources.getIntArray(loadingColorArrayId)
            shotLoadingPlaceholders = arrayOfNulls(placeholderColors.size)
            placeholderColors.indices.forEach {
                shotLoadingPlaceholders[it] = ColorDrawable(placeholderColors[it])
            }
        } else {
            shotLoadingPlaceholders = arrayOf(ColorDrawable(Color.DKGRAY))
        }
        val initialGifBadgeColorId = a.getResourceId(R.styleable.DribbbleFeed_initialBadgeColor, 0)
        initialGifBadgeColor = if (initialGifBadgeColorId != 0) {
            ContextCompat.getColor(host, initialGifBadgeColorId)
        } else {
            0x40ffffff
        }
        a.recycle()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_GANK -> createDribbbleShotHolder(parent)
            else -> throw IllegalStateException("Unsupported View type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DribbbleShotHolder -> {
                bindDribbbleShotHolder(items[position] as Article, holder, position)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    private fun bindDribbbleShotHolder(
        shot: Article,
        holder: DribbbleShotHolder,
        position: Int
    ) {
        GlideApp.with(host)
            .load(shot.envelopePic)
            .listener(object : RequestListener<Drawable> {
                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    if (!shot.hasFadedIn) {
                        holder.fade()
                        shot.hasFadedIn = true
                    }
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ) = false
            })
            .placeholder(shotLoadingPlaceholders[position % shotLoadingPlaceholders.size])
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .fitCenter()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.image)
        holder.image.tag = shot.envelopePic
        // need both placeholder & background to prevent seeing through shot as it fades in
        shotLoadingPlaceholders[position % shotLoadingPlaceholders.size]?.apply {
            holder.prepareForFade(
                this,
                shot.animated,
                // need a unique transition name per shot, let's use its url
                shot.htmlUrl ?: ""
            )
        }
//        shotPreloadSizeProvider.setView(holder.image)
    }

    private fun createDribbbleShotHolder(parent: ViewGroup): DribbbleShotHolder {
        return DribbbleShotHolder(
            layoutInflater.inflate(R.layout.dribbble_shot_item, parent, false),
            initialGifBadgeColor,
            isDarkTheme
        ) { view, position ->
            val intent = Intent(parent.context, DetailActivity::class.java).apply {
                putExtra("url", view.tag.toString())
            }
            val options = ActivityOptions.makeSceneTransitionAnimation(
                host,
                Pair.create(view, host.getString(R.string.transition_shot)),
                Pair.create(view, host.getString(R.string.transition_shot_background))
            )
            host.startActivityForResult(intent, 1, options.toBundle())
            Unit
        }
    }

    companion object {

        private const val TYPE_GANK = 0

        fun createSharedElementReenterCallback(
            context: Context
        ): SharedElementCallback {
            val shotTransitionName = context.getString(R.string.transition_shot)
            val shotBackgroundTransitionName =
                context.getString(R.string.transition_shot_background)
            return object : SharedElementCallback() {

                /**
                 * We're performing a slightly unusual shared element transition i.e. from one view
                 * (image in the grid) to two views (the image & also the background of the details
                 * view, to produce the expand effect). After changing orientation, the transition
                 * system seems unable to map both shared elements (only seems to map the shot, not
                 * the background) so in this situation we manually map the background to the
                 * same view.
                 */
                override fun onMapSharedElements(
                    names: List<String>,
                    sharedElements: MutableMap<String, View>
                ) {
                    if (sharedElements.size != names.size) {
                        // couldn't map all shared elements
                        sharedElements[shotTransitionName]?.let {
                            // has shot so add shot background, mapped to same view
                            sharedElements[shotBackgroundTransitionName] = it
                        }
                    }
                }
            }
        }
    }

}