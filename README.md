# GAD Android SDK 0.9.0 샘플

외부 앱 개발자가 GAD SDK의 초기화와 오퍼월 화면 연결을 확인하는 Android 샘플입니다. **이 샘플은 `GadEnvironment.STAGING`을 명시적으로 사용합니다.** 앱 실행만으로 SDK를 초기화하거나 오퍼월을 열지 않습니다.

현재 연동 기준은 [Android SDK 가이드](https://developers.gpakorea.com/developers/guide/sdk/android)입니다. 이 저장소의 예제보다 해당 가이드의 최신 계약을 우선해 주세요.

## 빌드와 실행

- Android Studio 또는 JDK 17, Android SDK Platform 35가 필요합니다. Gradle Wrapper 8.10.2 / Android Gradle Plugin 8.7.3을 사용합니다.
- 지원 OS는 Android API 23 이상입니다. AndroidX, Jetifier, Data Binding, Java 8 설정은 이미 포함되어 있습니다.
- Android Studio에서 프로젝트를 열고 SDK 위치를 지정하거나, 로컬의 `ANDROID_HOME`을 설정합니다. `local.properties`는 커밋하지 않습니다.
- Media Key 없이 빌드하고 설치할 수 있습니다.

```bash
./gradlew :app:assembleDebug
./gradlew :app:installDebug
```

1. [개발자 콘솔](https://developers.gpakorea.com/developers/console)에서 **Android·SDK 테스트 매체**를 준비하고 해당 **Media Key**를 입력합니다. 서버 API용 키나 환경이 다른 키는 사용하지 않습니다.
2. 연동할 앱에서 로그인한 사용자의 안정적인 **uid(최대 48자)**를 입력합니다. 개발자 포털 계정 ID, 이메일, 광고 ID를 대신 넣지 마세요.
3. **Staging SDK 초기화**를 누릅니다. 빈 값, 공백, `{...}` 예시 값, 48자를 초과하는 uid는 초기화를 막습니다.
4. 초기화 후 **액티비티로 열기** 또는 **프래그먼트로 열기**를 선택합니다. 반복해서 열어도 같은 입력값으로 SDK를 다시 초기화하지 않습니다.
5. 입력을 바꾸면 열기 버튼을 비활성화하고 기존 내장 프래그먼트를 제거합니다. 바뀐 값으로 다시 초기화한 뒤 열어 주세요.

화면 회전이나 프로세스 재시작으로 샘플 화면이 다시 만들어지면 입력값과 SDK 화면을 복원하지 않습니다. 다시 입력하고 초기화해 주세요.

저장소에 실제 Media Key나 개인 uid를 넣지 마세요. 샘플은 별도 입력값 저장 기능을 제공하지 않으며 화면 상태 복원에도 입력값을 저장하지 않습니다. SDK 0.9.0은 초기화 정보를 앱 내부 SharedPreferences에 저장합니다. 테스트 세션을 완전히 지우려면 앱 데이터를 삭제하거나 앱을 제거하세요.

## 실제 SDK 연동 코드

공개 JitPack 아티팩트를 사용하며 로컬 AAR 파일은 필요하지 않습니다.

```groovy
// 루트 build.gradle
allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}

// app/build.gradle
implementation 'com.github.koreagpa-dev:gad:0.9.0'
```

앱 로그인 후 해당 환경의 Media Key와 앱 사용자 uid를 확보한 시점에 초기화합니다. 환경 설정과 초기화를 한 곳에 모으세요.

```java
import com.gad.sdk.Gad;
import com.gad.sdk.GadEnvironment;

// 로그인 후 한 번 초기화. 실제 값은 앱의 인증/설정 흐름에서 전달합니다.
Gad.init(this, mediaKey, appUid, GadEnvironment.STAGING);

// 초기화 후 액티비티로 열기
Gad.showAdList(this);

// 또는 화면의 컨테이너에 프래그먼트로 연결
getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment, Gad.getAdListFragment(this))
        .commit();
```

`STAGING`은 `https://gad-staging.gpakorea.com/`으로 연결됩니다. SDK 화면의 STAGING 표시와 `GPADEV` logcat으로 환경을 확인할 수 있습니다. 공개 SDK 0.9.0의 오퍼월 목록/상세에는 `FLAG_SECURE`가 적용되므로 디버그 빌드에서도 캡처가 제한될 수 있습니다.

운영 전환은 의도적인 코드 변경으로만 수행합니다. 운영 Media Key와 운영 콜백 설정을 준비한 뒤 초기화 호출을 `GadEnvironment.PRODUCTION`으로 바꾸세요. 샘플의 Staging 안내 문구도 함께 변경해야 합니다. **환경 인자를 생략하는 3인자 `Gad.init`은 Production 기본값**이며, 뒤에서 다시 호출하면 Staging 선택이 Production으로 바뀝니다. deprecated `Gad.useDebugServer`와 환경 enum을 섞지 마세요. 이 샘플의 검증에는 운영 SDK 호출이나 광고 참여가 필요하지 않습니다.

## 콜백 준비와 샘플 범위

광고 완료 테스트 전에 HTTPS 콜백 수신 서버와 Staging 호스트 허용 설정을 준비하고, 선택한 환경에서 콜백 URL을 등록한 뒤 콘솔의 `허용됨` 상태를 확인해야 합니다. 완료 당시 콜백 URL이 없으면 나중에 등록해도 해당 완료 건이 자동 재전송되지 않습니다. 같은 사용자/기기에서 완료한 광고의 재참여도 제한됩니다. [포스트백 가이드](https://developers.gpakorea.com/developers/guide/postback)와 [테스트 가이드](https://developers.gpakorea.com/developers/guide/testing)의 현재 계약을 먼저 확인하세요.

Android 앱 샘플은 SDK 화면 연결을 보여 줍니다. 콜백 통신 시험에는 별도의 [Node.js 포스트백 수신 예제](examples/postback-receiver/README.md)를 실행할 수 있습니다. 수신 예제는 실제 포인트를 지급하지 않으며, 운영 지급에는 영속 저장소의 중복 방지와 지급 처리가 필요합니다. Android 앱에는 포인트 적립, 광고주의 CPA 완료 통지, 서버 API 클라이언트가 포함되지 않습니다. [서버 API 가이드](https://developers.gpakorea.com/developers/guide/api)는 현재 준비 중입니다. SDK 연동을 서버 API 지원으로 해석하지 마세요. 과거 API/CPA/매체 설정 문서는 최신 가이드 링크로 대체했습니다.

Staging 설치·미션 fixture는 실제 설치·리뷰 완료를 보장하지 않으며, CPS도 외부 구매 확정·취소·리워드 전체 검증을 대신하지 않습니다. 연동 문의: `jyi4@gpakorea.com`.

## 키 없이 검증

새 에뮬레이터 또는 샘플 앱 데이터를 삭제한 테스트 기기에서 아래 검증을 실행할 수 있습니다. 테스트는 빈 입력, 공백, 예시 값, 누락된 필드, uid 길이 제한, 열기 버튼 차단과 화면 재생성 후 초기 상태를 확인하며 SDK를 초기화하거나 광고 요청을 보내지 않습니다. 테스트용 가짜 문자열은 실제 Media Key와 함께 사용되지 않습니다.

```bash
./gradlew :app:assembleDebug :app:assembleDebugAndroidTest
./gradlew :app:connectedDebugAndroidTest
```
