## Como gerar a  imagem docker da aplicação

1. Usar o comando do maven `mvn clean package -DskipTests` para gerar o arquivo .jar.
2. Comando `docker compose build` para gerar a imagem da aplicação que será utilizada no docker compose.
3. Para subir os containers basta usar o comando `docker compose up -d`.