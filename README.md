# CidadãoAtivo - 2º Bimestre

Sistema de Solicitações Públicas com Spring Boot 3.2

## Arquitetura
- Controller → Service → Repository
- Banco H2
- Frontend HTML/CSS/JS

## Como Executar

```bash
mvn spring-boot:run
http://localhost:8080
```

## Endpoints
- POST /api/usuarios/registro
- POST /api/usuarios/login
- POST /api/solicitacoes
- GET /api/solicitacoes
- GET /api/solicitacoes/{id}
- GET /api/solicitacoes/protocolo/{protocolo}

## Funcionalidades
✅ Login/Registro
✅ Criar solicitações
✅ Acompanhar por protocolo
✅ Dashboard gerencial
✅ Histórico de status
✅ SLA automático