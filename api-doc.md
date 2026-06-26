# GAD API DOCUMENT

#### 문서에 정의되지 않은 필드들은 언제든지 변경 또는 제거될 수 있으니 정의된 필드만을 사용 바랍니다.
#### 기존 URL 도 지원합니다.
#### (기존) https://gad.api.gpakorea.com/advertisement
#### (변경) https://gad.api.gpakorea.com/campaign


## 광고 목록
#### URL : https://gad.api.gpakorea.com/campaign/list
#### REQUEST (GET)
| 파라미터 | 내용 |
| ------------------- | ------------------- |
| media | 미디어키 |
#### RESPONSE
| 파라미터 | 내용 |
| ------------------- | ------------------- |
| code | 결과 CODE |
| message | 결과 메시지 |
| **items** | 광고 목록 배열 |
#### items
| 파라미터       | 내용                                               |
|------------|--------------------------------------------------|
| key        | 광고키                                              |
| type       | 광고타입 (0: 참여형, 1: 설치형, 2: 실행형, 3: 미션형, 4: 액션형)    |
| subtype    | 광고유형 (하단 subtype 참조) |
| title      | 광고명                                              |
| url        | 광고 URL                                           |
| point      | 적립 포인트                                           |
| icon       | 광고 아이콘                                           |
| mission    | 참여방법                                             |
| platform   | 플랫폼(비트연산) (1: AOS, 2: iOS, 3: AOS+iOS)           |
| **app**    | 앱정보                                              |
| **detail** | 광고 상세 정보                                         |
| **target** | 광고 타겟 정보                                         |

#### subtype
| type | subtype                   | name              |
|------|---------------------------|-------------------|
| 0    | NSJ                       | 스토어알림받기           |
| 0    | NCJ                       | 상품찜               |
| 0    | NCS                       | 카페회원가입            |
| 0    | DCS                       | 다음카페회원가입          |
| 0    | YTL                       | 유튜브좋아요            |
| 0    | GMS                       | 구글맵저장             |
| 0    | KPF                       | 카카오채널추가           |
| 0    | ISL                       | 인스타좋아요            |
| 0    | IPF                       | 인스타팔로워            |
| 0    | NBN                       | 블로그이웃추가           |
| 0    | NBBN                      | 블로그서로이웃           |
| 0    | NBP                       | 블로그공감             |
| 0    | NML                       | 플레이스즐겨찾기          |
| 0    | BAND                      | 밴드회원가입            |
| 0    | NPA                       | 플레이스알림받기          |
| 0    | NPB                       | 플레이스저장하기(+리워드트래픽) |
| 0    | TSL                       | 티스토리공감            |
| 0    | MPL                       | 무신사상품찜            |
| 0    | YDA                       | 와디즈알림신청           |
| 0    | VWI                       | 방문형               |
| 0    | DGF                       | 당근마켓단골맺기          |
| 0    | TGJ                       | 텔레그램한국인사용자        |
| 0    | CPQ                       | 퀴즈형               |
| 0    | DCJ                       | 디스코드채널가입          |
| 1    | CPI                       | 설치형               |
| 3    | MISSION_INSTAGRAM_COMMENT | 인스타댓글             |
| 3    | MISSION_YOUTUBE_COMMENT   | 유튜브댓글             |
| 3    | MISSION_ETC               | 미션형               |
| 4    | CPA                       | 액션형               |

#### app (optional)
| 파라미터 | 내용 |
| ------------------- | ------------------- |
| market | 마켓 종류 (1: 구글플레이, 2: 원스토어, 3: 웹, 4: 앱스토어, 9:etc) |
| package | 패키지명 |
| scheme | custom url scheme |
#### detail
| 파라미터        | 내용 |
|-------------| ------------------- |
| require     | 광고에 필요한 정보 (ex. 카페닉네임, 사전예약전화번호, 댓글아이디) |
| summary     | 개요 |
| description | 상세설명 |
| image1      | (optional) 광고 도움 이미지 (참여방법) |
| image2      | (optional) 썸네일 (720X360 이미지 소재) |
| image3      | (iOS, optional) 상세 배너 (720X780 이미지 소재) |
#### target (optional)
| 파라미터 | 내용 |
| ------------------- | ------------------- |
| sex | 0: 공통, 1:남성만, 2:여성만 |
| age_from | 최소연령 (0: 무제한) |
| age_to | 최대연령 (0: 무제한) |

## 광고 참여 요청
#### URL : https://gad.api.gpakorea.com/campaign/join
#### REQUEST (POST)
| 파라미터 | 내용 |
| --- | --- |
| media | 미디어키 |
| adKey | 광고키 |
| uid | 유저 ID (MAX:36) |
| adid | 유저 ADID / 유저 IDFA |
| data | 매체사 데이터 (포스트백에서 전송받을 데이터) |
| ip | 클라이언트 IP |
| udid <sup>(권장) | widevine ID |
| android_id <sup>(선택) | android id |
| imei <sup>(선택) | IMEI |
- 권장 또는 선택 값이 존재하지 않을 경우 광고 참여에 제약이 있을 수 있습니다.
- [[가이드] widevine ID 값 얻기](https://github.com/koreagpa-dev/gad-sample-android#widevine-ID-값-얻기)
#### RESPONSE
| 파라미터 | 내용 |
| ------------------- | ------------------- |
| code | 결과 CODE |
| message | 결과 메시지 |
| **join** | 참여 요청 결과 (optional) |
#### join
| 파라미터 | 내용 |
| ------------------- | ------------------- |
| ... | SDK 에약 필드 |
| url | 이 값이 존재하는 경우 광고 URL로 사용해야함 |

## 참여상태조회
- 현재 참여 상태를 조회
#### URL : https://gad.api.gpakorea.com/campaign/status
#### REQUEST (GET)
| 파라미터 | 내용 |
| --- | --- |
| media | 미디어키 |
| adKey | 광고키 |
| uid | 유저 ID (MAX:36) |
| adid | 유저 ADID / 유저 IDFA |

#### RESPONSE
| 파라미터 | 내용 |
| ------------------- | ------------------- |
| code | 결과 CODE |
| message | 결과 메시지 |
- 예) 미션을 제출한 상태인 경우 code: 24 로 전달됩니다.
- 예) 기참여 또는 적립된 경우 code: 30 으로 전달됩니다.


## 설치완료(advertisement type=1, 설치형만 해당)
- 앱 설치 확인 후 호출
#### URL : https://gad.api.gpakorea.com/campaign/complete
#### REQUEST (POST)
| 파라미터 | 내용 |
| --- | --- |
| media | 미디어키 |
| adKey | 광고키 |
| uid | 유저 ID (MAX:36) |
| adid | 유저 ADID / 유저 IDFA |
| ip | 클라이언트 IP |
#### RESPONSE
| 파라미터 | 내용 |
| ------------------- | ------------------- |
| code | 결과 CODE |
| message | 결과 메시지 |

## 포스트백
- Content-Type: application/json

| 파라미터 | 내용 |
| ------------------- | ------------------- |
| seq | 적립 고유 ID |
| media | 미디어키 |
| adKey | 광고키 |
| adTitle | 광고명 |
| uid | 유저 ID |
| publisher_data | API 방식으로 광고 참여시 전송했던 publisher_data |
| point | 적립 금액 |
- seq 또는 adKey uid 를 통해 중복 적립을 방지해야 합니다.
- HTTP 200 응답을 받지 않으면 1, 5, 10, 60분 후 포스트백을 재전송 합니다.

## 결과 코드
| CODE | 내용 |
| --- | --- |
| 0 | 정상 |
| 20 | 미디어키 값 오류 |
| 21 | 광고키 값 오류 |
| 22 | 이미 참여한 광고 |
| 23 | 존재하지 않는 미션 |
| 24 | 제출한 미션이 검증중인 상태 |
| 25 | 참여신청이 존재하지 않음 |
| 26 | 광고 수량 소진 |
| 30 | 이미 적립됨 |
| 100 | 파라미터 누락 |
| 200 | 권한 오류 |
| 300 | 기타 오류 |
