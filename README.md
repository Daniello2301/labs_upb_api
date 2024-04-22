Application.properties file configuration


server.port=8082
server.servlet.context-path=your/path

spring.datasource.url=jdbc:mysql://databasehost/databsename?serverTimezone=America/Bogota
spring.datasource.username=user
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

#Optional
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.database=MYSQL
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

security.jwt.secret-key=yoursecretkeyfortoekn
security.jwt.expiration=yourtimeexpirationtoken
security.jwt.refresh-token.expiration=yourtimerefreshtokenb


spring.application.name=appName
