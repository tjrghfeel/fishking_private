##### 어복황제 Web Application

---
##### 로그인 관련
```
로그인/로그아웃 처리 :: PageStore.js > setAccessToken(token:토큰, service:서비스, auto:자동로그인여부)
토큰 처리 :: PageStore.js > loadAccessToken(service:서비스)
```
##### 모달 관련
```
공통 모달 :: ModalStore.js > openModal(modalType:모달유형,options:옵션객체)
개별 모달 :: /components/modal/**Modal.js (공통 제외)
```
##### 스크롤 페이징
```
스크롤 이벤트 설정 :: PageStore.js > setScrollEvent(onScroll:콜백, element:타겟엘리먼트)
스크롤 이벤트 해제 :: PageStore.js > removeScrollEvent()
```
##### 네이티브 관련
```
NativeStore.js
```
##### API 요청 관련
```
APIStore.js
* Enums, Code 데이터는 APIStore 쓰지않고, DataStore.js > getEnums(), getCodes() 활용
```
##### 데이터 처리
```
1 ) App.js > prototype functions
2 ) DataStore.js
```
