# Android 업그레이드 계획 — gad-sample-android

기준일: 2026-08-16. 현재 sample은 AGP 8.7.3, Gradle 8.10.2, compileSdk 35, targetSdk 34, minSdk 23이며 AndroidX와 legacy support multidex 의존성이 섞여 있다.

## 교체 대상

| 중요도 | 현재 | 대체 및 방법 |
|---|---|---|
| P0 | targetSdk 34, compileSdk 35 | API 36으로 함께 올리고 Android 13~16 에뮬레이터/실기기에서 SDK 초기화·광고 호출·딥링크를 검증한다. |
| P1 | AGP 8.7.3/Gradle 8.10.2 | API 36 대응에는 최소 AGP 8.9.1 이상으로 올린다. AGP 9.3/Gradle 9.5/JDK 17은 gad SDK와 Kotlin 호환성을 확인한 뒤 별도 현대화 단계로 진행한다. |
| P1 | com.android.support:multidex | minSdk 23에서는 platform multidex가 내장되어 있으므로 legacy support multidex 의존성을 제거한다. 모든 import를 AndroidX로 통일한다. |
| P1 | OkHttp 3.12.8 | API·TLS·interceptor 회귀 테스트 후 유지보수되는 OkHttp 4.x의 특정 검증 버전으로 교체한다. |
| P1 | gad 0.7.7 고정 | gad SDK와 sample을 함께 수정하지 않는다. API 36 호환 SDK release를 먼저 publish하고, 해당 release의 정확한 버전으로 sample을 pin한 뒤 소비자 테스트를 한다. |
| P0 | cleartextTraffic 허용 | release에서는 false로 바꾸며, 개발 HTTP가 불가피하면 debug 전용 Network Security Config로만 제한한다. |
| P1 | release minify 비활성 | SDK consumer rule을 확인한 후 R8/minify 활성 release를 별도 내부 트랙에서 검증한다. |

## 실행 순서

1. gad SDK의 API 36 호환 release와 migration note를 확정한다.
2. sample의 AndroidX/Multidex 및 API 36 전환을 적용한다.
3. 네트워크 라이브러리와 R8는 작은 단위로 올리고, 광고 로드 실패·offline·TLS 실패를 포함한 smoke test를 만든다.
4. debug/release network policy를 분리하고 internal test 후 배포한다.

## 출처

- [Google Play target API requirements](https://developer.android.com/google/play/requirements/target-sdk?hl=ko)
- [AGP release compatibility](https://developer.android.com/build/releases/about-agp)
- [AGP 9.3 release notes](https://developer.android.com/build/releases/agp-9-3-0-release-notes)
- [AGP Upgrade Assistant](https://developer.android.com/build/agp-upgrade-assistant)
- [Migrate to AndroidX](https://developer.android.com/jetpack/androidx/migrate)
- [Multidex](https://developer.android.com/build/multidex?hl=en)
- [Network Security Configuration](https://developer.android.com/privacy-and-security/security-config)
