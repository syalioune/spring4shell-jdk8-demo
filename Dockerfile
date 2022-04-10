FROM tomcat:8.5.42-jdk8

#  Build spring app

RUN apt update && apt install maven -y

ADD src/ /customer/src
ADD pom.xml /customer
WORKDIR /customer/
RUN mvn clean package

#  Deploy to tomcat
RUN mv target/spring4shell-jdk8-demo-0.0.1.war /usr/local/tomcat/webapps/customer.war


EXPOSE 8080
CMD ["catalina.sh", "run"]

