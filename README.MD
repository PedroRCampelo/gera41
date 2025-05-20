
# gera41

Repositório de aplicação com o objetivo de automatizar a criação da documentação MIT041



## Stack selecionada até então

**Back-end:** Java + Spring boot

**Dados:** PostgresSQL

**Lib:** - [Apache POI](https://poi.apache.org/apidocs/5.0/) - Framework para manipulação de dados em Office (Word, neste caso)

## Funcionalidades

- Geração interativa da documentação Word
- Possibilidade de manipulação
- Informações básicas já prontas

## Controllers 
Documentos (MIT041)
- http://localhost:8080/documentos/listar - listar MITs criadas
- http://localhost:8080/documentos/novo - criar nova MIT

Rotinas (Cadastro das rotinas)
- http://localhost:8080/rotinas/listar - listar rotinas
- http://localhost:8080/rotinas/novo - criar nova rotina


## Status atual (Status Report)

15/04/2025 - Frontend e criação de entidades para rotinas pré-cadastradas

[![Screenshot-2025-04-15-at-16-05-01.png](https://i.postimg.cc/htvCDN7K/Screenshot-2025-04-15-at-16-05-01.png)](https://postimg.cc/yDqhnQPG)

Impressão e ajuste de Documento (MIT041)

[![Screenshot-2025-04-15-at-16-05-46.png](https://i.postimg.cc/m2kHn4HN/Screenshot-2025-04-15-at-16-05-46.png)](https://postimg.cc/9r304v1r)

[![Screenshot-2025-04-16-at-17-37-17.png](https://i.postimg.cc/FsK4MmDv/Screenshot-2025-04-16-at-17-37-17.png)](https://postimg.cc/xqBhmwwp)

12/05/2025 - Migração para DB MySql (Persistir dados)

[![Screenshot-2025-05-12-at-13-52-27.png](https://i.postimg.cc/Nftmp9YW/Screenshot-2025-05-12-at-13-52-27.png)](https://postimg.cc/grMx00xD)

## Próximos passos
- Implementar funcionalidade de criar rotinas pelo usuário;
- Ajustar formatação das rotinas
- ...
