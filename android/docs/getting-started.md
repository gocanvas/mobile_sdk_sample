# Getting Started

## Installation

GoCanvas SDK requires at minimum Android API 26+.

1. Install via GitHub Packages. Add the following source to your `repositories` section and create a GitHub access token with `read:packages` scope then replace <github_username> and <github_access_token> with your credentials.

```gradle
repositories {
  //...
  maven {
     name = "GitHubPackages"
     url = uri("https://maven.pkg.github.com/gocanvas/android_sdk")
     credentials {
         username = "<github_username>"
         password = "<github_access_token>"
     }
  }
```

2. Add the library to the `dependencies` section:
```gradle
dependencies {
  //...
  implementation("com.gocanvas.sdk:android:<version>")
  //...
}

```
3. Make sure you have these repositories declared:
```gradle
repositories {
  google()
  mavenCentral()
  jcenter()
  maven { url = uri("https://jitpack.io") }
  //...
}
```

4. Make sure you have the AndroidX & Jetifier enabled inside the `gradle.properties` file:
```gradle.properties
android.useAndroidX=true
android.enableJetifier=true
```

## Usage
For information about the SDK Api Usage follow the [Usage](usage.md) guide.

## Branding
For information about customizing the SDK styling take a look into the following content:
- [Colors](branding/colors.md)
