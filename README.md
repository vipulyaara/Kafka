# Kafka

Currently in process of content curation in order to build a CMS. It will host more than 5 million audio content (about 20% is popular) available in more than 100 languages, all free.



</br></br>

<img src="https://user-images.githubusercontent.com/6247940/66643361-22f22c00-ec3c-11e9-9710-c96f09c0e900.png">

<img src="https://user-images.githubusercontent.com/6247940/56097011-9f65b580-5f0c-11e9-92a6-cf0498f1532a.png">




## Architecture

This app focuses on scalable, flexible and reactive app architecure. Parts of the architecture are inspired by Chris Banes' [Tivi app](https://github.com/chrisbanes/tivi).

It is a version of MVVM with interactors as an additional layer to enhance re-usability. The app uses following frameworks


* [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) (throughout the data layer; for thread switching)
* [Livedata](https://developer.android.com/topic/libraries/architecture/livedata) (between ViewModels and Fragments)
* [Room](https://developer.android.com/topic/libraries/architecture/room)
* [Paging Library](https://developer.android.com/topic/libraries/architecture/paging/) (for pagination and integration with room)
* Retrofit2
* [Klint](https://github.com/shyiko/ktlint) (code formatter for Kotlin)
* [ExoPlayer](https://github.com/google/ExoPlayer) - For audio playback


![final-architecture](https://user-images.githubusercontent.com/6247940/50480774-0e71d980-0a04-11e9-90fc-89c9fdfb4115.png)
