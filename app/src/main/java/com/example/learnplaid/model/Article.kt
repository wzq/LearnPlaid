package com.example.learnplaid.model

import android.os.Parcelable
import com.example.learnplaid.model.result.PlaidItem
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue


/**
 * Created by wzq on 2019-07-12
 *
 */
@Parcelize
data class Article(
    val apkLink: String,
    val author: String,
    val chapterId: Int,
    val chapterName: String,
    var collect: Boolean,
    val courseId: Int,
    val desc: String,
    val envelopePic: String,
    val fresh: Boolean,
    val id: Int,
    val link: String,
    val niceDate: String,
    val origin: String,
    val projectLink: String,
    val publishTime: Long,
    val superChapterId: Int,
    val superChapterName: String,
    val tags: @RawValue MutableList<Tag>,
    val title: String,
    val type: Int,
    val userId: Int,
    val visible: Int,
    val zan: Int
) : PlaidItem(id, title, envelopePic, 0), Parcelable {
    var hasFadedIn: Boolean = false

    val animated = false
}

data class Tag(
    val name: String,
    val url: String
)