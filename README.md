# Kafka

[Kafka](https://www.kafkaarchives.com) uses www.archive.org to serve millions of audiobooks and pdf books available in more than 100 languages, all free.


## Download

<a href="https://play.google.com/store/apps/details?id=com.kafka.user" target="_blank">
<img src="https://play.google.com/intl/en_gb/badges/static/images/badges/en_badge_web_generic.png" width=240 />
</a>

## Demo

### [Video](https://vimeo.com/user68598793/review/472788300/8256f4487c)

</br>


<img src="https://user-images.githubusercontent.com/6247940/232330667-838af627-54ff-4987-b0e8-0763c2e09752.png">
<img src="https://user-images.githubusercontent.com/6247940/232330669-c0bd7376-3d0f-4d52-b968-8f7a3b55fb2a.png">

## How to run
 - Clone the repo
 - Clone [Sarahang](https://github.com/vipulyaara/Sarahang) in the same parent folder as Kafka.<br/>Sarahang is the audio player being used in Kafka and not yet provided through maven.
 - Run project

## Architecture

Kafka implements MVVM with interactors using all the latest android frameworks.<br/>
Parts of the architecture are inspired by [Tivi](https://github.com/chrisbanes/tivi). Download and audio features are copied from [DatMusic](https://github.com/alashow/datmusic-android).

The app uses

* [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html)
* [Jetpack compose](https://developer.android.com/jetpack/compose) with Material3 theming
* [Room](https://developer.android.com/topic/libraries/architecture/room)
* [Retrofit2](https://github.com/square/retrofit)
* [Dagger Hilt](https://dagger.dev/hilt/)
* [ExoPlayer](https://github.com/google/ExoPlayer)


![final-architecture](https://user-images.githubusercontent.com/6247940/75632907-cb5f5780-5c00-11ea-974d-ff7a5e8b0a21.png)
