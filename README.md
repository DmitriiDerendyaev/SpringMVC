# Spring MVC
## lesson 15 - first application

- Создать приложение по архитектуре Maven и внедрить следующие зависимости:
  - `spring-core`
  - `spring-context`
  - `spring-web`
  - `spring-webmvc`
- В файле web.xml прописать для dispatcher:
```xml
<!DOCTYPE web-app PUBLIC
 "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
 "http://java.sun.com/dtd/web-app_2_3.dtd">

<web-app>
  <display-name>Archetype Created Web Application</display-name>

  <absolute-ordering/>

  <servlet>
    <servlet-name>dispatcher</servlet-name> //Имя сервлета
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class> // Импорт сервлета
    <init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>/WEB-INF/applicationContextMVC.xml</param-value> // Импорт конфигурации
    </init-param>
    <load-on-startup>1</load-on-startup> // Указание на то, чтобы загружался первым
  </servlet>

  <servlet-mapping>
    <servlet-name>dispatcher</servlet-name>
    <url-pattern>/</url-pattern> // Открытие по url "/"
  </servlet-mapping>
</web-app>
```
### ВАЖНО, ЧТОБЫ ВЕРСИИ БЫЛИ СОПОСТАВИМЫ
```xml
<dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-core</artifactId>
      <version>5.2.1.RELEASE</version>
    </dependency>
```
- Первичная настойка контроллера:
```java
@Controller
public class HelloController {
  @GetMapping("/hello")
  public String sayHello() {
    return "hello_world";
  }
}
```

## lesson 16 - конфигурация приложения
- Создадим класс SpringConfig.java, в котором будет расположена конфигурация приложения, заменяющая xml
- Это делается для того, чтобы настроить вместо стандартного шаблонизатора шаблонизатор Thymeleaf
- С помощью `@Autowired` внедрена зависимость applicationContext
```java
@Configuration
@ComponentScan("ru.derendyaev.SpringMVC")
@EnableWebMvc
public class SpringConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;

    @Autowired
    public SpringConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        registry.viewResolver(resolver);
    }
}
```
- Вместо `web.xml` теперь у нас `MySpringMvcDispatcherInitializer.java`, который в методах будет указывать на необходимые внедрения
- Теперь конфигурация Сервлета находится getServletConfigClasses:
```java
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {SpringConfig.class};
    }
```
### ВАЖНО: 
проследить, чтоб не было лишних зависимостей в `target/SpringMVC/WEB-INF/lib`

## lesson 17 - controller
![img.png](mdResources/1.png)

- Настройка контроллеров через различные аннотации:
```java
@Controller
@RequestMapping("/first")
public class FirstController {
    @GetMapping("/hello")
    public String helloPage(){
        return "first/hello";
    }

    @GetMapping("/goodbye")
    public String gooByePage(){
        return "first/goodbye";

    }
}
```

- `RequestMapping` накладывает при применении общий шаблон на запрос .../first/... 


## lesson 18 - HTTP
- GET
![img.png](mdResources/get.png)
- POST
![img.png](mdResources/post.png)
- Сравнение GET/POST
![img.png](mdResources/get-post.png)

## lesson - 19 - GET - запрос
- Данные можно получить с помощью `HttpServletRequest`, который содержит в себе много информации, включая параметры запроса.
- Этот вариант не совсем верный, т.к. содержит в себе много лишних параметров:
```java
@GetMapping("/hello")
    public String helloPage(HttpServletRequest request){
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");

        System.out.println("Name: " + name + ", surname: " + surname);
        return "first/hello";
    }
```
- Работа с аннотацией `@RequestParam`, где можно указать сами переменные, куда необходимо положить значения запроса по ключам:
```java
    public String helloPage(@RequestParam("name") String name,
                            @RequestParam("surname") String surname){
        System.out.println("Name: " + name + ", surname: " + surname);
        return "first/hello";
    }
```
ВАЖНО: `@RequestParam` требует наличие параметров, иначе 404. Это можно изменить, если сделать так: `@RequestParam(value = "name", required = false) String name,...`
- А передать ссылку можно следующим образом:
```html
<a href="/first/hello?name=Dima&surname=Derendyaev">Request With Param</a>
```

## lesson - 20 Модель, передача данных от контроллера к представлению
![img.png](mdResources/modelToView.png)
- Реализовать калькулятор, в который будет передаваться запрос `http://localhost:8080/first/calculator?a=5&b=5&action=multiplication`
```java
@GetMapping("/calculator")
    public String calculator(@RequestParam(value = "a", required = false) String a,
                             @RequestParam(value = "b", required = false) String b,
                             @RequestParam(value = "action", required = false) String action,
                             Model calcModel){
        switch (action){
            case "multiplication":
                calcModel.addAttribute("answer", "Answer of multiplication: " + (Integer.parseInt(a) * Integer.parseInt(b)));
                break;
            case "addition":
                calcModel.addAttribute("answer", "Answer of addition: " + (Integer.parseInt(a) + Integer.parseInt(b)));
                break;
            case "subtraction":
                calcModel.addAttribute("answer", "Answer of subtraction: " + (Integer.parseInt(a) - Integer.parseInt(b)));
                break;
            case "division":
                calcModel.addAttribute("answer", "Answer of division: " + (Integer.parseInt(a) / Integer.parseInt(b)));
                break;
            default:
                calcModel.addAttribute("answer", "Incorrect action");
        }

        return "calc/answer";
    }
```