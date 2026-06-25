# GAD SDK 연동하기

- 이 프로젝트는 GAD SDK를 적용한 샘플앱으로 아래 가이드 한 내용을 포함하고 있습니다.
- GAD 오퍼월 사이트에 앱 등록은 [GAD 미디어 설정하기](https://github.com/GPA-KOREA/gad-sample-android/blob/main/guide_media.md#gad-%EB%AF%B8%EB%94%94%EC%96%B4-%EC%84%A4%EC%A0%95%ED%95%98%EA%B8%B0) 페이지를 참고 바랍니다.
- API 문서는 [GAD API DOCUMENT](https://github.com/GPA-KOREA/gad-sample-android/blob/main/api-doc.md#gad-api-document), [CPA 연동](https://github.com/GPA-KOREA/gad-sample-android/blob/main/guide_cpa.md#gad-cpa-%EC%97%B0%EB%8F%99-%EA%B0%80%EC%9D%B4%EB%93%9C) 페이지를 참고 바랍니다.

> 📌 이 브랜치(`kotlin`)는 **Kotlin 기반** 샘플 코드입니다. Java 버전은 `main` 브랜치를 참고하세요.

## Gradle 설정
- 모듈 수준의 build.gradle 파일에서 아래 코드를 추가합니다.

```groovy
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'kotlin-kapt'   // dataBinding 사용 시 필요
}

android {
    defaultConfig {
        multiDexEnabled true
    }
    buildFeatures {
        dataBinding = true
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = '1.8'
    }
}
```

## TargetSdkVersion 33 (Android 13) 이상인 경우
- 매니페스트 파일에서 Google Play 서비스 일반 권한을 다음과 같이 선언해야 합니다.
```xml
<uses-permission android:name="com.google.android.gms.permission.AD_ID"/>
```

- AD_ID : https://support.google.com/googleplay/android-developer/answer/6048248?hl=ko

## GAD SDK 라이브러리 사용
#### 프로젝트 설정
- GAD SDK는 [JitPack](https://jitpack.io/) 원격 저장소를 가지고 있습니다.
- 프로젝트 수준의 build.gradle 파일에 저장소르 추가합니다.
```groovy
buildscript {
    dependencies {
        // Kotlin Gradle 플러그인
        classpath 'org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.0'
    }
}

allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
- 모듈 수준의 build.gradle 파일에 GAD 종속성을 추가합니다.
    
```groovy
dependencies {
    implementation 'com.github.koreagpa-dev:gad:0.7.5'
}
```
#### GAD 초기화 및 실행
- GAD SDK를 사용하기 위해서는 가장 먼저 초기화 함수를 호출해야 합니다.
- GAD는 새창(액티비티) 형태와 인레이아웃(프레그먼트) 형식을 모두 지원합니다.
- 샘플앱은 MEDIA KEY와 USER ID 변경 테스트를 위한 코드를 포함하고 있으며 GAD SDK 연동을 위한 코드는 아래와 같습니다.
```kotlin
class MainActivity : AppCompatActivity() {

    // GAD 초기화 함수. 가장 먼저 호출 필요
    // (로그인 후 1회만) 앱 로그인 완료 후 미디어키와 유저ID(유저를 식별할 수 있는 유니크한 값)로 초기화 한다.
    private fun initializeGad(mediaKey: String, userId: String) {
        Gad.init(this, mediaKey, userId)
    }

    // 새창(액티비티)으로 GAD를 보여준다.
    private fun startGadActivity() {
        Gad.showAdList(this)
    }

    // GAD Fragment를 보여준다.
    private fun showGadFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, Gad.getAdListFragment(this))
            .commitAllowingStateLoss()
    }
}
```

- 광고 참여를 완료하면 [미디어에 등록된 URL로 포스트백](https://github.com/GPA-KOREA/gad-sample-android/blob/main/guide_media.md#%EB%AF%B8%EB%94%94%EC%96%B4-%EC%97%B0%EB%8F%99-%EC%A0%95%EB%B3%B4-%EC%9E%85%EB%A0%A5%ED%95%98%EA%B8%B0)을 전송합니다.
- 링크 : [포스트백 API DOCUMENT](https://github.com/GPA-KOREA/gad-sample-android/blob/main/api-doc.md#%ED%8F%AC%EC%8A%A4%ED%8A%B8%EB%B0%B1)
