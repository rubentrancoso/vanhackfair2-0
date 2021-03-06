spring:
  application:
    name: challenge

  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://34.212.252.237:3306/challenge
    username: root
    password: mysql
  # jpa
  jpa:
    database-platform: org.hibernate.dialect.MySQL5Dialect
    show-sql: true
    hibernate.ddl-auto: create-drop
    database: MYSQL
    
server:
  port: 8080
management:
  port: 9001
  
logging:
  config: config/log4j2-console.xml

security:
  antMatchers: 
    -
      method: 'GET'
      url: '/info'
    -
      method: 'GET'
      url: '/products/'
    -
      method: 'OPTIONS'
      url: '/products/add'
    -
      method: 'POST'
      url: '/products/add'
    -
      method: 'GET'
      url: '/hello'
    -
      method: 'POST'
      url: '/register'
    -
      method: 'POST'
      url: '/login'
      
jwt:
  secret: 'SecretKeyToGenJWTs'
  expirationTime: 1800000 # 1000 * 60 * 30 = 1800000 = 30 minutos
  tokenPrefix: ''
  headerString: 'Authorization'  
---  
spring:
  profiles: dev
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/challenge
    username: root
    password: mysql
  # jpa
  jpa:
    database-platform: org.hibernate.dialect.MySQL5Dialect
    show-sql: true
    hibernate.ddl-auto: create-drop
    database: MYSQL
logging:
  level:
    org.springframework.transaction: DEBUG
    org.springframework.orm.jpa: DEBUG     
---  
spring:
  profiles: debug
  # hsqldb  
  datasource:
    driver-class-name: org.hsqldb.jdbcDriver
    url: jdbc:hsqldb:mem:userdb
    username: sa
    password: sa
  # jpa
  jpa:
    database-platform: org.hibernate.dialect.HSQLDialect
    show-sql: true
    hibernate.ddl-auto: create-drop
    database: HSQL
logging:
  level:
    org.springframework.transaction: DEBUG
    org.springframework.orm.jpa: DEBUG     

      
      
      