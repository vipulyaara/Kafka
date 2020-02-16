package com.kafka.user.extensions

import android.os.StrictMode
import androidx.databinding.ViewDataBinding
import com.kafka.user.R
import java.util.*

/**
 * @author Vipul Kumar; dated 22/01/19.
 */

/**
 * "let"s when the genre is non-null and non-empty
 * */
fun <T : Collection<*>> T?.letEmpty(f: (it: T) -> Unit) {
    if (this != null && this.isNotEmpty()) f(this)
}

fun getRandomCoverResource(): Int {
    val covers = arrayOf(
        R.drawable.img_cover_1,
        R.drawable.img_cover_2,
        R.drawable.img_cover_3,
        R.drawable.img_cover_4,
        R.drawable.img_cover_5,
        R.drawable.img_cover_6,
        R.drawable.img_cover_7,
        R.drawable.img_cover_8
    )

    return covers[Random().nextInt(covers.size)]
}

fun getRandomAuthorResource(): Int {
    val covers = arrayOf(
        R.drawable.img_author_camus_1,
        R.drawable.img_author_camus_3,
        R.drawable.img_author_camus_caligula,
        R.drawable.img_author_camus_latranger,
        R.drawable.img_author_camus_latranger,
        R.drawable.img_author_camus_the_fall,
        R.drawable.img_author_ghalib_1,
        R.drawable.img_author_ghalib_1,
        R.drawable.img_author_ghalib_2,
        R.drawable.img_author_ghalib_2,
        R.drawable.img_author_hemingway_1,
        R.drawable.img_author_kafka_1,
        R.drawable.img_author_kafka_2,
        R.drawable.img_author_kafka_3,
        R.drawable.img_author_kafka_the_castle,
        R.drawable.img_author_plato_1,
        R.drawable.img_author_tagore_1,
        R.drawable.img_author_tagore_2,
        R.drawable.img_author_marx_1,
        R.drawable.img_author_rustaveli_1,
        R.drawable.img_author_virginia_1,
        R.drawable.img_author_virginia_to_the_lighthouse,
        R.drawable.img_author_fitzgerald_the_great_gatsby,
        R.drawable.img_author_harper_lee_to_kill_a_mockingbird_2,
        R.drawable.img_author_harper_lee_to_kill_a_mockingbird,
        R.drawable.img_author_karl_marx,
        R.drawable.img_author_karl_marx_das_capital,
        R.drawable.img_author_nietzsche,
        R.drawable.img_author_orwell_animal_farm,
        R.drawable.img_author_paulo_eleven_minutes,
        R.drawable.img_author_tolstoy_anna_karenina,
        R.drawable.img_author_salinger_the_catcher_in_the_rye,
        R.drawable.img_author_rushdie_midnights_children
    )

    return covers[Random().nextInt(covers.size - 1)]
}

fun illegalArgumentException(msg: String): Nothing = throw RuntimeException(msg)

inline fun <T : ViewDataBinding> T.executeAfter(block: T.() -> Unit) {
    block()
    executePendingBindings()
}

fun disableStrictMode() {
    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
    StrictMode.setThreadPolicy(policy)
}