# Kafka

[Kafka](https://www.kafkaarchives.com) uses www.archive.org to serve millions of audiobooks and pdf books available across a multitude of genres and languages, all free and in the public domain.

- 41 million books and text; 14.5 million audio recordings - tagged and searchable with filters
- New content added continuously by the community as well as Kafka Archives
- Audio player for seamless background playback, sleep timer and playback speed controls
- Text reader for reading PDFs with features like continue-reading
- Mark content as favorite and build your own library
- Download content on your device and share it freely - or share within the app with convenient deep linking
- Dark theme with MaterialYou colors


## Download

<a href="https://play.google.com/store/apps/details?id=com.kafka.user" target="_blank">
<img src="https://play.google.com/intl/en_gb/badges/static/images/badges/en_badge_web_generic.png" width=240 />
</a>

Or [Download APK from releases](https://github.com/vipulyaara/Kafka/releases)

## Demo

### [Video](https://vimeo.com/user68598793/review/472788300/8256f4487c)

</br>


<img src="https://github.com/vipulyaara/Kafka/assets/6247940/f5a41c17-d69b-4a42-93e0-3afe71e54957">

<img src="https://user-images.githubusercontent.com/6247940/233622886-39d7c75a-363a-4c11-adde-7e0df52583d8.png">
<img src="https://user-images.githubusercontent.com/6247940/233622953-e418f2d6-d576-470b-bef3-7f193944cf3b.png">


## How to run
 - Clone the repo
 - Clone [Sarahang](https://github.com/vipulyaara/Sarahang) in the root folder (Kafka).<br/>Sarahang is the audio player being used in Kafka and not yet provided through maven.
 - Add `google_server_client_id=xxx` and `pipeless_auth_token=xxx` in your `local.properties` file. You can use arbitrary values, these tokens only provide additional functionalities.
 - Run project

`google-services.json` for debug flavour is added to the repository.

## Architecture

Kafka implements MVVM with interactors using all the latest android frameworks.<br/>
Parts of the architecture are inspired by [Tivi](https://github.com/chrisbanes/tivi). Download and audio features are copied from [DatMusic](https://github.com/alashow/datmusic-android).

The app uses [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html), [Jetpack compose](https://developer.android.com/jetpack/compose), Material3, [Room](https://developer.android.com/topic/libraries/architecture/room), [Retrofit2](https://github.com/square/retrofit), [Dagger Hilt](https://dagger.dev/hilt/), [ExoPlayer](https://github.com/google/ExoPlayer)


![final-architecture](https://user-images.githubusercontent.com/6247940/75632907-cb5f5780-5c00-11ea-974d-ff7a5e8b0a21.png)
