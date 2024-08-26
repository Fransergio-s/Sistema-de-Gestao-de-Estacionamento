# Sistema-de-Gestão-de-Estacionamento
Este projeto trata-se de uma aplicação desenvolvida na linguagem Java, com um banco de dados Mysql que visa criar um sistema que realiza conexão com o banco de dados graças a bibilioteca JDBC do Java.

## Requisitos
Certifique-se de ter as seguintes ferramentas instaladas:
- Java Development Kit (JDK) 11 ou superior
- Um banco de dados Mysql instalado na maquina (Dê preferência a versão 8.0 em diante)
- A IDEA Intellij (No minimo versão 2023.01)

## Instalação
Siga os passos abaixo para configurar o projeto no seu ambiente
1. **Clone o repositório**
 ```bash
 git clone https://github.com/Fransergio-Filho/Sistema-de-Gest-o-de-Estacionamento.git
 ```
2. **Abra o projeto na IDEA**

 ```INTELLIJ
 Open local-do-diretório-salvo
 ``

3. **Execute o projeto**
 Para executar o projeto:

 ```Intellij
 Abra a classe program, no canto superior e clique e Run 'Program.java'
 ```
### Estrutura do Projeto
* `Sistema de Gestão de Estacionamento/src/Program.java`: Código-fonte principal do projeto
* `Sistema de Gestão de Estacionamento/src/DB.java`: Possui todas as funções que conectam ao banco de dados
* `Sistema de Gestão de Estacionamento/src/DbException.java`: Cria exceções personalizadas
* `Sistema de Gestão de Estacionamento/db.properties`: Local onde você deve colocar os dados do seu banco de dados (Se estiver incorreto não sera possivel conectar ao banco
