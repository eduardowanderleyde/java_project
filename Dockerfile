# Use a imagem oficial do OpenJDK 17
FROM openjdk:17-jdk-slim

# Crie diretório de trabalho
WORKDIR /app

# Copie o fat jar e dependências
COPY target/java_project-1.0-SNAPSHOT-jar-with-dependencies.jar /app/
COPY chat_history.json /app/

# Exponha a porta do servidor
EXPOSE 8887

# Comando para rodar o servidor
CMD ["java", "-cp", "java_project-1.0-SNAPSHOT-jar-with-dependencies.jar", "com.eduardowanderley.server.ChatServer"] 