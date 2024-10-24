# kodak
 Uma rede social para fotógrafos

## Descrição

**kodak** é uma aplicação web que permite a fotógrafos registrar e compartilhar suas fotos em uma rede social. As fotos podem ser curtidas e comentadas por outros usuários. Os fotógrafos podem seguir e ser seguidos por outros membros da rede. Além disso, comentários e fotos podem conter hashtags para facilitar a promoção e divulgação de fotos com temas semelhantes.

## Funcionalidades para Implementar

- **Cadastro de Fotógrafos (UC01):** Usuários podem se cadastrar fornecendo nome e email. Foto, cidade e país são opcionais.
- **Publicação de Fotos (UC02):** Fotógrafos podem fazer upload de fotos com um comentário opcional e hashtags.
- **Seguir Fotógrafos (UC03):** Usuários podem seguir outros fotógrafos.
- **Curtir/Descurtir Fotos (UC04):** Usuários podem curtir ou descurtir fotos de outros fotógrafos.
- **Comentar em Fotos (UC05):** Usuários podem comentar em fotos de outros fotógrafos.
- **Listar Fotógrafos (UC20):** Administradores podem listar todos os fotógrafos cadastrados no sistema.
- **Suspender Fotógrafos (UC21):** Administradores podem suspender fotógrafos, impedindo-os de acessar o sistema.

## Tecnologias Utilizadas

- **Java 22**
- **Spring Boot 3**
- **Spring Data JPA**
- **Hibernate**
- **Thymeleaf**
- **Bootstrap**
- **Banco de Dados:** PostgreSQL
- **Lombok**

## Estrutura do Projeto

### Entidades

- **Photographer:** Representa um fotógrafo no sistema. Contém informações como id, nome, email, fotos, comentários, seguidores e seguindo.
- **Photo:** Representa uma foto publicada por um fotógrafo. Contém dados da imagem, fotógrafo que a publicou, hashtags, curtidas e comentários.
- **Comment:** Representa um comentário feito em uma foto. Contém o texto do comentário, data de criação, fotógrafo que comentou e foto associada.
- **Hashtag:** Representa uma hashtag associada a fotos. Contém o nome da tag e as fotos relacionadas.

### Repositórios

- **PhotographerRepository:** Interface que estende `JpaRepository` para operações CRUD com fotógrafos.
- **PhotoRepository:** Interface para operações com fotos.
- **CommentRepository:** Interface para operações com comentários.
- **HashtagRepository:** Interface para operações com hashtags.

## Como Executar o Projeto

1. **Pré-requisitos:**

   - **Java 22** instalado.
   - **Maven** instalado.
   - **PostgreSQL** instalado e configurado.

2. **Clonar o Repositório:**

   ```bash
   git clone https://github.com/victoralves475/kodak.git
   cd seu-repositorio
   ```

3. **Configurar o Banco de Dados:**

   - Crie um banco de dados PostgreSQL chamado `kodak`.
   - Atualize o arquivo `application.properties` com as credenciais do banco de dados.

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/kodak
   spring.datasource.username=seu-usuario
   spring.datasource.password=sua-senha
   ```

4. **Compilar e Executar o Projeto:**

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

5. **Acessar a Aplicação:**

   (PARA IMPLEMENTAR)
   - Abra o navegador e acesse `http://localhost:8080`.

## Próximos Passos

- **Implementar Autenticação e Autorização:**

  - Configurar o Spring Security para gerenciar autenticação e autorização (RNF 09 e RNF 10).

- **Desenvolver Interfaces de Usuário:**

  - Utilizar Thymeleaf e Bootstrap para criar as páginas web (RNF 02 e RNF 07).

- **Implementar Validação e Mensagens de Erro:**

  - Aplicar validações nos formulários e exibir mensagens de erro (RNF 05).

- **Implementar Padrão Post-Redirect-Get:**

  - Ajustar os controladores para seguir o padrão PRG (RNF 06).

- **Adicionar Paginação:**

  - Implementar paginação nas listagens de fotógrafos, fotos e comentários (RNF 08).

## Contribuições

Este projeto está sendo desenvolvido pela equipe **TESTEMUNHAS DO JAVA**.

 - ANTONIO VICTOR ALVES DA COSTA
 - SAMMUEL LUNA
 - RAFAEL LIMEIRA
 - ISLAN PEDRO DA SILVA FIGUEIREDO

## Licença

Este projeto está licenciado sob os termos da licença acadêmica do IFPB.
