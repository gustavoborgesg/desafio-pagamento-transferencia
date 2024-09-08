FROM maven:3.9.9-amazoncorretto-21 AS build

WORKDIR /pagamento-simplificado

COPY ./ /pagamento-simplificado

RUN mvn clean package -DskipTests

FROM amazoncorretto:21

WORKDIR /app

COPY --from=build /pagamento-simplificado/target/pagamento-simplificado-0.0.1-SNAPSHOT.jar ./pagamento-simplificado-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "./pagamento-simplificado-0.0.1-SNAPSHOT.jar"]
