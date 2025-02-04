# 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리
* 구현 단계에서는 각 요구사항을 구현하는데 집중한다. 
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다. 

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답
* HttpGetRequestUtils 클래스의 getRequestUrl 메서드를 정적(static) 메서드로 작성한 이유
  1. getRequestUrl 메서드는 어떠한 클래스의 인스턴수 변수와 상태를 사용하지않고, 전달된 입력값만 처리하고 있기때문에, 굳이 객체를 생성해서 호출할 필요가 없다.
  2. 정적 메서드는 객체의 상태와 상관없이 동작하며, 특정 입력을 처리하기 위한 도구(유틸리티)임을 명확히 나타낸다.
  3. URL을 파싱하는 역할을 하기 때문에, 굳이 객체 지향적 접근(객체 상태 저장 등)을 사용할 필요가 없다.
  4. 정적 메서드를 사용하면 메서드를 호출하기 위해 객체를 생성할 필요가 없으므로, 메모리와 자원을 절약할 수 있다.
  5. Utils라는 이름을 가진 클래스는 일반적으로 정적 메서드로 설계되며, 이것은 Java의 관례이다.
* assertEquals와 assertThat의 차이
  1. assertEquals는 두 값이 같으면 통과하지만, assertThat은 다양한 조건을 명확하게 표현할 수 있다.
  2. assertThat은 동등성 확인(is), null 확인(notNullValue()), 포함 여부(containsString()), 여러 조건 등 다양한 조건 검사가 가능하다.

### 요구사항 2 - get 방식으로 회원가입
* ArrayList로 Header의 method, url, params, httpVersion을 다루는 중에 각 값이 몇번째에 있는지 헷갈리기 시작하여 VO 또는 DTO로 관리하기로 함.
* DTO 
   * 데이터를 전달하기 위해 사용, 주로 계층 간 데이터 전달
   * 가볍고 간단한 객체로, 비즈니스 로직을 포함하지 않음.
   * 값 변경이 필요할 때 사용.
* VO
   * 데이터를 전달하기 위해 사용되는 불변 객체, 값 자체가 중요할 때 사용.
   * 생성 시 값을 설정한 후, 값을 변경할 수 없도록 설계.
   * 생성자나 Builder 패턴을 통해 필드 초기화.

### 요구사항 3 - post 방식으로 회원가입
* 브라우저 개발자 도구 네트워크 탭에서 요청 헤더에 바디가 안보이는 이유
  * 바디는 따로 요청 페이로드탭에 표시
  * 하지만, 브라우저에 요청을 보내는 HTTP 메시지 구조는 Http_Post_Join.txt에 나온 것과 같이 header -> 공백 -> Body가 전달 
  * 네트워크 탭에서는 요청헤더와 바디를 따로 표시
* String과 StringBuilder의 차이
  * String은 불변객체, StringBuilder는 가변 객체
  * String은 값이 변경될 때마다 새로운 객체가 생성되고, 기존 객체는 변경되지 않음
  * StringBuilder는 값이 변경될 때, 같은 메모리 공간에서 값을 수정

### 요구사항 4 - redirect 방식으로 이동
* 지금까지 상태코드를 이용한 리다이렉트를 사용하지 않고, 직접 redirect를 했었는데 구현 후 상태코드를 더 알게됨
  * 302 Found : 요청된 리소스가 다른 URL로 임시 이동
  * Location : 해당 URL로 이동

### 요구사항 5 - cookie
* HTTP 302 상태코드로 Location 리다이렉트 시 쿠키가 사라지는 이슈
  * login을 하여 쿠키가 생성되는 것 까지는 되지만, 리다이렉트시 쿠키가 사라졌다. 
  * 이는 쿠키의 SameSite 속성이 None 또는 Strict로 설정되어 있으면 전송되지 않을 수 있다.
  * SameSite 속성의 값을 Lax로 설정하여 해결

### 요구사항 6 - stylesheet 적용
* 

### heroku 서버에 배포 후
* 