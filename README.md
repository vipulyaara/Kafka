# Kafka

Currently in process of content curation in order to build our own CMS with Firebase. We have more than 5 million audio content (about 20% is popular/usable) available in more than 100 languages, all free.



</br></br>

<img src="https://user-images.githubusercontent.com/6247940/56096787-4eed5880-5f0a-11e9-850c-fa8160266a5e.png">

<img src="https://user-images.githubusercontent.com/6247940/56097011-9f65b580-5f0c-11e9-92a6-cf0498f1532a.png">




## Architecture

This app focuses on scalable, flexible and reactive app architecure. It can even be "too much code" (e.g. dependency injection) for an app this size, but it is meant to be scaled well.

It is a version of MVVM with interactors as an additional layer to enhance re-usability. The app uses following frameworks


* [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) (throughout the data layer; for thread switching)
* [Livedata](https://developer.android.com/topic/libraries/architecture/livedata) (between ViewModels and Fragments)
* [RxJava2](https://github.com/ReactiveX/RxJava) (enabling reactiveness on data layer)
* [Room](https://developer.android.com/topic/libraries/architecture/room)
* [Paging Library](https://developer.android.com/topic/libraries/architecture/paging/) (for pagination and integration with room)
* [Epoxy](https://github.com/airbnb/epoxy) - Epoxy is a very flexible framework developed by Airbnb. We use it with recyclerView for any kind of data that can be presented in a list format (e.g. venue detail fragment)
* [Kodein](https://kodein.org) (KOtlin DEpendency INjection)
* Retrofit2
* [Klint](https://github.com/shyiko/ktlint) (code formatter for Kotlin)


![final-architecture](https://user-images.githubusercontent.com/6247940/50480774-0e71d980-0a04-11e9-90fc-89c9fdfb4115.png)
