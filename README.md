# Refurbhyb

Update Database Username and Password in Main.java

### How to Run
1. [Install Maven](https://maven.apache.org/install.html)
2. `mvn package`
3. `java -jar target/Refurb-1.0-SNAPSHOT.jar`
-----
1. save [mysql-connector-j-8.0.33.jar](https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar) inside `target/dependency` directory
2. `javac -cp target/dependency/ -d target/classes $(find src/main/java -name "*.java")`
3. `java -cp "target/classes:target/dependency/*" org.example.Main`