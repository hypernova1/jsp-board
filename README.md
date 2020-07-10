# Sam's pring
jsp 프로젝트로 간단한 스프링 프레임워크를 구현한다.

## 목적
* 스프링 내부 구조 파악
* 객체 지향 학습
* Reflection 학습

## 구현(*리팩토링, 보완해야할..*)된 기능들
### 기본 기능
#### BeanContainer
@Component 가 붙은 클래스를 싱글턴 빈으로 만들어 저장하고 @Autowired 가 붙은 필드에 주입(Injection)해준다.
#### DispatcherServlet
최초로 요청을 받고 다른 컴포넌트에게 작업을 위임 후 결과(View, Json)를 리턴한다.
#### RequestHandlerMapping
요청을 분석 후 조건에 맞는 핸들러를 찾는다.
#### RequestHandlerAdaptor
RequestHandlerMapping 이 찾은 핸들러를 실행한 후 결과를 받아 DispatcherServlet 에게 반환한다.
#### Converter
요청 파라미터를 인스턴스로 변환한다.
#### ViewResolver
핸들러에서 View 이름을 반환시 경로를 만들어 준다.
#### Model
View 에서 사용할 값을 담는 객체
#### Annotation
* @Component
    * @Controller
    * @RestController
    * @Service
    * @Configuration
    * @Bean
* @RequestMapping
    * @GetMapping
    * @PostMapping
    * @PutMapping
* @RequestBody
* @ResponseBody
* @Autowired
