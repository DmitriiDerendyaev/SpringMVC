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
