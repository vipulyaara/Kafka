# Kafka

[Kafka](https://www.kafkaarchives.com) uses archive.org to serve millions of audiobooks and pdf books available across a multitude of genres and languages, all free and in the public domain.


## Download

<a href="https://play.google.com/store/apps/details?id=com.kafka.user" target="_blank">
<img src="https://play.google.com/intl/en_gb/badges/static/images/badges/en_badge_web_generic.png" width=240 />
</a>

## Demo

### [Video](https://vimeo.com/user68598793/review/472788300/8256f4487c)

</br>


<img src="https://github.com/vipulyaara/Kafka/assets/6247940/f5a41c17-d69b-4a42-93e0-3afe71e54957">

<img src="https://user-images.githubusercontent.com/6247940/233622886-39d7c75a-363a-4c11-adde-7e0df52583d8.png">
<img src="https://user-images.githubusercontent.com/6247940/233622953-e418f2d6-d576-470b-bef3-7f193944cf3b.png">


## How to run
 - Clone the repo
 - Clone [Sarahang](https://github.com/vipulyaara/Sarahang) in the same parent folder as Kafka.<br/>Sarahang is the audio player being used in Kafka and not yet provided through maven.
 - Run project

`google-services.json` for debug flavour is added to the repository.

## Architecture

Kafka implements MVVM with interactors using all the latest android frameworks.<br/>
Parts of the architecture are inspired by [Tivi](https://github.com/chrisbanes/tivi). Download and audio features are copied from [DatMusic](https://github.com/alashow/datmusic-android).

The app uses [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html), [Jetpack compose](https://developer.android.com/jetpack/compose), Material3, [Room](https://developer.android.com/topic/libraries/architecture/room), [Retrofit2](https://github.com/square/retrofit), [Dagger Hilt](https://dagger.dev/hilt/), [ExoPlayer](https://github.com/google/ExoPlayer)


![final-architecture](https://user-images.githubusercontent.com/6247940/75632907-cb5f5780-5c00-11ea-974d-ff7a5e8b0a21.png)
