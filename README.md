# Comics Box Application #

Comics Box application is Comics title browser and reader application. It allows user to browse and read comics titles.
It also allows user to download comics titles for offline reading.

## Comics Box application dependencies and used libraries##

- [Jetpack Compose](https://developer.android.com/jetpack/compose) 
  - okhttp3 - HTTP client for Android
  - coil - Image loading library for Android backed by Kotlin Coroutines
  - moshi - A modern JSON library for Android and Java

## Features ##
- Comics title browser
- Comics title reader
- Comics title download for offline reading
- Comics title search
- Comics personal library

## Adding developers API key ##
- Marvel API keys are injected into project during build time from `secret.properties` file, placed in the root of the project
- Add the following lines to the `secret.properties` file:
  api.key.marvel.private=111_your_private_key
  api.key.marvel.public=115_your_public_key

