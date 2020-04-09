# Mini SpringFramework

## 결론
* 여기까지 학습된 내용을 바탕으로 다시 만들어야할 것 같다..
## 문제점
### 같은 타입의 빈을 여러개 등록했을 때 생겨야할 에러들을 구현할 수 없음
* 빈을 타입으로 등록한 게 아니라 이름으로 등록했기 때문..
* 하나의 빈을 만들고 그 이상 테스트하지 않고 넘어간 것이 원인.
### Root Context를 사용자가 지정할 수 없음..
* 서블릿에서 요청을 받을 때 와일드카드(`*`)로 받으면 뷰를 호출할 때 무한 루프가 발생함..
    * 서블릿이 모든 요청을 계속 받으려고 하기 때문
    * 원인을 찾으면 새로 만들 프로젝트에 적용해봐야겠다.
## 목적
* jsp 프로젝트를 스프링처럼 구현
* 스프링 내부 구조 파악
* 객체 지향 학습
* 리플렉션 학습

## 구현 완료된(*리팩토링, 보완해야할..*) 기능들
* 기본 기능
    * Converter
    * ViewResolver
    * DispatcherServlet
    * RequestHandlerMapping
    * RequestHandlerAdaptor
    * Model
    * BeanContainer

* Annotation
    * @Controller
    * @RequestMapping (@GetMapping, @PostMapping 등..)
    * @RequestBody, @ResponseBody
    

## 구현 예정인 기능들
* @PathVariable
* ResponseEntity
* 예외처리

## 구현 가능할지 의문인 기능들
* **JPA**
* 자동 보안 설정(like Spring Security..)
* properties를 이용한 자동 설정