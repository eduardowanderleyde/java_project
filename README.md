# Chat em Tempo Real

Um aplicativo de chat em tempo real desenvolvido em Java com JavaFX e WebSocket.

## Funcionalidades

- Chat em tempo real usando WebSocket
- Suporte a múltiplas salas de chat
- Compartilhamento de arquivos
- Interface gráfica moderna com JavaFX
- Histórico de mensagens
- Notificações de eventos (conexão, desconexão, etc.)

## Requisitos

- Java 17 ou superior
- Maven 3.6 ou superior

## Como executar

1. Clone o repositório:

```bash
git clone git@github.com:eduardowanderleyde/java_project.git
```

2. Entre no diretório do projeto:

```bash
cd java_project
```

3. Compile o projeto:

```bash
mvn clean package
```

4. Inicie o servidor:

```bash
java -cp target/java_project-1.0-SNAPSHOT.jar com.eduardowanderley.server.ChatServer
```

5. Em outro terminal, inicie o cliente:

```bash
java -cp target/java_project-1.0-SNAPSHOT.jar com.eduardowanderley.ChatApp
```

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/eduardowanderley/
│   │   ├── controller/
│   │   │   └── ChatController.java
│   │   ├── model/
│   │   │   └── Message.java
│   │   ├── server/
│   │   │   └── ChatServer.java
│   │   └── ChatApp.java
│   └── resources/
│       ├── fxml/
│       │   └── chat.fxml
│       └── styles/
│           └── chat.css
└── test/
    └── java/com/eduardowanderley/
        └── (testes)
```

## Tecnologias Utilizadas

- JavaFX para a interface gráfica
- Java-WebSocket para comunicação em tempo real
- Gson para serialização JSON
- Maven para gerenciamento de dependências

## Como Usar

1. Inicie o servidor
2. Inicie o cliente
3. Digite seu nome e clique em "Conectar"
4. Digite o nome da sala e clique em "Entrar na Sala"
5. Comece a conversar!
6. Use o botão "Arquivo" para compartilhar arquivos

## Contribuindo

Sinta-se à vontade para contribuir com o projeto através de pull requests.
