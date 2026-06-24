# GAD SDK 연동하기

- 이 프로젝트는 GAD SDK를 적용한 샘플앱으로 아래 가이드 한 내용을 포함하고 있습니다.
- GAD 오퍼월 사이트에 앱 등록은 [GAD 미디어 설정하기](https://github.com/koreagpa-dev/gad-sample-android/blob/master/guide_media.md#gad-%EB%AF%B8%EB%94%94%EC%96%B4-%EC%84%A4%EC%A0%95%ED%95%98%EA%B8%B0) 페이지를 참고 바랍니다.
- API 문서는 [GAD API DOCUMENT](https://github.com/koreagpa-dev/gad-sample-android/blob/main/api-doc.md#gad-api-document), [CPA 연동](https://github.com/koreagpa-dev/gad-sample-android/blob/master/guide_cpa.md#gad-cpa-%EC%97%B0%EB%8F%99-%EA%B0%80%EC%9D%B4%EB%93%9C) 페이지를 참고 바랍니다.

> 📌 이 브랜치(`kotlin`)는 **Kotlin 기반** 샘플 코드입니다. Java 버전은 `main` 브랜치를 참고하세요.

## AndroidX 마이그레이션
- 기존의 SupportLibrary는 더 이상 업데이트가 이루어지지 않으며 AndroidX 라는 이름으로 대체되었습니다.
- 주된 변경 사항중 하나는 기존에 v4, v7, v13 등으로 나눠져 있던 부분을 androidx라는 문자열로 시작하는 네임스페이스로 통일 됐습니다.
- Android 스튜디오 3.2 이상에서는 메뉴 바에서 리펙터링 > AndroidX로 이전을 선택하여 AndroidX를 사용하도록 기존 프로젝트를 빠르게 이전할 수 있습니다.
- 참고 사이트
    - AndroidX 개요 : https://developer.android.com/jetpack/androidx?hl=ko
    - AndroidX로 이전 : https://developer.android.com/jetpack/androidx/migrate#migrate

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

## 참고 사이트
- MultiDex : https://developer.android.com/studio/build/multidex.html?hl=ko
- 데이터바인딩 : https://developer.android.com/topic/libraries/data-binding/start.html?hl=ko
- Java 8 : https://developer.android.com/studio/write/java8-support?hl=ko
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
    implementation 'com.github.koreagpa-dev:gad:0.7.4'
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
        // (Optional) 고객의 성별, 나이를 알고 있다면 아래 설정을 통해 타겟팅 된 광고를 받아 볼 수 있다.
        Gad.setUserInfo("M", 22)
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

- 광고 참여를 완료하면 [미디어에 등록된 URL로 포스트백](https://github.com/koreagpa-dev/gad-sample-android/blob/master/guide_media.md#%EB%AF%B8%EB%94%94%EC%96%B4-%EC%97%B0%EB%8F%99-%EC%A0%95%EB%B3%B4-%EC%9E%85%EB%A0%A5%ED%95%98%EA%B8%B0)을 전송합니다.
- 링크 : [포스트백 API DOCUMENT](https://github.com/koreagpa-dev/gad-sample-android/blob/master/api-doc.md#%ED%8F%AC%EC%8A%A4%ED%8A%B8%EB%B0%B1)

#### SDK API 21 미만 버전 및 에러 대응
```kotlin
class MyApplication : Application() {

    companion object {
        init {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
            }
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}
```
```groovy
dependencies {
    implementation 'androidx.multidex:multidex:2.0.1'
    /* okhttp 3.13부터 API 21+ 만 지원 */
    implementation('com.squareup.okhttp3:logging-interceptor:3.12.8') {
        force = true
    }
    implementation("com.squareup.okhttp3:okhttp:3.12.8") {
        force = true
    }
}
```

#### widevine ID 값 얻기
- API 18 이상이며 구글플레이가 설치된 디바이스에서 사용가능합니다.
```kotlin
fun getWidevineId(): String {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
        return ""
    }
    val widevineUuid = UUID(0xEDEF8BA979D64ACEuL.toLong(), 0xA3C827DCD51D21EDuL.toLong())
    var mediaDrm: MediaDrm? = null
    var widevineId: String? = null
    try {
        mediaDrm = MediaDrm(widevineUuid)
        val arrUniqueId = mediaDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID)
        widevineId = Base64.encodeToString(arrUniqueId, Base64.NO_WRAP).trim()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        if (mediaDrm != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                mediaDrm.close()
            } else {
                mediaDrm.release()
            }
        }
    }
    return widevineId ?: ""
}
```

#### API 방식 사용시 (Deprecated)
- SDK를 이용한 API 방식 지원이 종료됐습니다.
- 2019.12.24 Gad.goDetail 메소드가 Deprecated 됐습니다.
- GAD 광고 참여시 내부 로직을 사용하므로 SDK 연동은 동일하게 필요합니다.
- 최종적으로 광고페이지로 이동하고자 할때 Gad.join(Context context, String adKey)를 호출하면 됩니다.
```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        ...
        Gad.init(this, mediaKey, userId)
        // 고객의 성별, 나이를 알고 있다면 아래 설정을 통해 타겟팅 된 광고를 받아 볼 수 있다.
        Gad.setUserInfo("M", 22)
        // Gad.setProgressAnimation(false)
    }

    // Gad광고를 수행하기 위해서는 광고참여신청 후 아래와 같이 SDK를 통해 광고페이지로 이동시킨다.
    private fun join(adKey: String) {
        Gad.join(this, adKey)
    }
}
```
