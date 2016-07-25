angularjs-springmvc-sample
==========================

An example application using AnguarJS/Bootstrap as frontend and Spring MVC as REST API producer.

**More details about the codes, please read the online GitBook: [Building REST APIs with Spring MVC](https://www.gitbook.com/book/hantsy/build-a-restful-app-with-spring-mvc-and-angularjs/details/).**

[![Build Status](https://drone.io/github.com/hantsy/angularjs-springmvc-sample/status.png)](https://drone.io/github.com/hantsy/angularjs-springmvc-sample/latest)

##Contribution

_I appreciate any contribution for this project, including suggestions, documentation improvements, reporting issues, forks and bugfixs,  etc. I have found there are some unrelated issues added, before you file an issue, please **READ THE STEPS IN THIS README.md**  carefully_.

**在你提交 ISSUE 前，请务必确认已经严格完成了本文中描述的操作步骤**。

Thank the DevFactory team member [@misgersameer](https://github.com/misgersameer) for sending several PRs to improve the code quaulity according to the sonar rules.


##Requirements

   * JDK 8

     Oracle Java 8 is required, go to [Oracle Java website](http://java.oracle.com) to download it and install into your system. 
     
     Optionally, you can set **JAVA\_HOME** environment variable and add *&lt;JDK installation dir>/bin* in your **PATH** environment variable.

   * Apache Maven
   
     Download the latest Apache Maven from [http://maven.apache.org](http://maven.apache.org), and uncompress it into your local system. 
    
     Optionally, you can set **M2\_HOME** environment varible, and also do not forget to append *&lt;Maven Installation dir>/bin* your **PATH** environment variable.  

## Run this project

   1. Clone the codes.

    <pre>
    git clone https://github.com/hantsy/angularjs-springmvc-sample
    </pre>
  
   2. And enter the root folder, run `mvn tomcat7:run` to start up an embedded tomcat7 to serve this application.
  
    <pre>
    mvn tomcat7:run
    </pre>

   3. Go to [http://localhost:8080/angularjs-springmvc-sample/](http://localhost:8080/angularjs-springmvc-sample/) to test it. If you want to explore the REST API docs online, there is a *Swagger UI* configured for visualizing the REST APIs, just go to [http://localhost:8080/angularjs-springmvc-sample/swagger-ui.html](http://localhost:8080/angularjs-springmvc-sample/swagger-ui.html).

## Spring Boot

If you are interested in Spring Boot, I have moved the `boot` branch into a new project [angularjs-springmvc-sample-boot](https://github.com/hantsy/angularjs-springmvc-sample-boot).
