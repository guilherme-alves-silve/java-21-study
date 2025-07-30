The projects in this zip are Spring Boot projects with Maven build system. In order to use these with eclipse, do the following

- Install JDK 21 and setup in eclipse to point JDK-Loom to JDK 21
- Import the projects as Maven projects (not eclipse projects)
- Make sure JDK-Loom is associated with the build path of these projects
- Make sure all compilation error are gone. 
- Install Spring Tools 4.0 from Eclipse Marketplace
- Run using the Boot Dashbord (Windows -> Show View)

For running the applications using Maven directly

- Install Maven locally from https://maven.apache.org/download.cgi
- Add Maven bin directory to PATH environment variable
- Add Java 21 bin directory to PATH environment variable
- From the project rrot, use command 
	mvn clean spring-boot:run -Dspring-boot.run.profiles=<profile> 
  (in some cases you might have to pass the spring profile)