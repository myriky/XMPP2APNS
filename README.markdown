# XMPP2APNS
### Openfire(WildFire) 에서 발생하는 Message 태그를 APNS으로 포워딩하는 플러그인입니다.
해당 타겟(targetJID)으로 향하는 모든 메시지 스탠자를 검사하여 OpenFire DB안에 별도로 생성된 ofAPNS 테이블을 검사하여, 해당 사용자의 UDID를 통해 APNS로 푸쉬 메시지 발송을 요청합니다.

## 설치방법
Openfire Admin Console ( http://serverip:9090 ) 에 접속한 다음, Plugins 페이지에서 위에 있는 파일을 업로드 하면 됩니다.
플러그인이 설치되자마자, 데이터베이스에 ofAPNS 라는 테이블이 자동으로 생성됩니다.

그리고, APNS 인증서를 다운로드 받은 후 /usr/local/openfire/ 에 올려두시면 됩니다.

## 요청방법
### 디바이스 토큰 등록하기
<iq type="set" to="sds.co.kr" id="apns68057d6a">
       <query xmlns="urn:xmpp:apns">
       <token>8A67BCCA710233A31406E9399A343A251F79EAC756C83D3A98AC0FA224FBC597</token>
       </query>
</iq>

### 리턴 값
<iq type="result" id="apns68057d6a" from="210.205.58.23" to="user@210.205.58.23/68057d6a">
       <query xmlns="urn:xmpp:apns">
       <token>8A67BCCA710233A31406E9399A343A251F79EAC756C83D3A98AC0FA224FBC597</token>
       </query>
</iq>
### 디바이스 토큰 가져오기
<iq type="get" to="210.205.58.23" id="apns68057d6a">
       <query xmlns="urn:xmpp:apns"/>
</iq>

### 리턴 값
<iq type="result" id="apns68057d6a" from="sds.co.kr" to="user@sds.co.kr/68057d6a">
       <query xmlns="urn:xmpp:apns">
       <token>8A67BCCA710233A31406E9399A343A251F79EAC756C83D3A98AC0FA224FBC597</token>
       </query>
</iq>

## 참고사항
Openfire에서 APNS로 요청하는 모듈은 ( Javapns 'http://code.google.com/p/javapns/' ) 를 이용하였습니다.
Openfire 플러그인을 컴파일 해서 jar 패키지로 만드는 방법은 ( http://community.igniterealtime.org/docs/DOC-1020 ) 을 참고하세요.