```
src/main/java/
└── com
    └── tuempresa  // Cambia esto por el nombre de tu organización o dominio
        └── incidentmanagement
            ├── IncidentManagementApplication.java  // Clase principal de Spring Boot
            │
            ├── config                     // Configuración general de Spring, Beans, Seguridad, etc.
            │   ├── SecurityConfig.java    // Configuración de Spring Security
            │   ├── JdbcConfig.java        // Configuración específica de Spring Data JDBC si es necesaria
            │   ├── WebConfig.java         // Configuración de CORS, interceptores, etc.
            │   └── ...
            │
            ├── shared                     // (Opcional) Código compartido entre módulos/slices
            │   ├── domain
            │   │   ├── model              // Clases base, Value Objects comunes (e.g., IdBase)
            │   │   └── event              // Interfaces o clases base para eventos de dominio
            │   └── infrastructure
            │       └── // Componentes de infraestructura compartidos (e.g., helpers, excepciones base)
            │
            ├── auth                       // --- Slice: Autenticación y Autorización ---
            │   ├── application            // Casos de uso de autenticación
            │   │   ├── command            // Comandos (e.g., Login)
            │   │   │   └── LoginCommand.java
            │   │   │   └── LoginCommandHandler.java
            │   │   ├── query              // Consultas (e.g., Obtener usuario actual)
            │   │   │   └── GetCurrentUserQuery.java
            │   │   │   └── GetCurrentUserQueryHandler.java
            │   │   ├── service            // Servicios de aplicación (orquestación)
            │   │   │   └── AuthService.java
            │   │   └── dto                // Data Transfer Objects para la capa de aplicación
            │   │       ├── LoginRequest.java
            │   │       └── AuthResponse.java
            │   ├── domain                 // Lógica y estado de dominio de autenticación (puede ser ligero)
            │   │   ├── model              // Modelos específicos de auth si los hay (e.g., TokenInfo)
            │   │   └── service            // Servicios de dominio (lógica pura de negocio)
            │   └── infrastructure         // Implementaciones e interacciones externas
            │       ├── security           // Implementaciones de Spring Security
            │       │   ├── JwtTokenProvider.java
            │       │   ├── UserDetailsServiceImpl.java // Implementa UserDetailsService de Spring Security
            │       │   └── ...
            │       └── web                // Adaptadores de entrada (Controladores REST)
            │           └── AuthController.java
            │
            ├── users                      // --- Slice: Gestión de Usuarios (Empleados) ---
            │   ├── application
            │   │   ├── command            // CreateUser, UpdateUserRole, etc.
            │   │   ├── query              // GetUserById, GetUsersByTeam, etc.
            │   │   ├── service
            │   │   │   └── UserService.java
            │   │   └── dto
            │   │       ├── UserDto.java
            │   │       └── CreateUserRequest.java
            │   ├── domain
            │   │   ├── model              // El núcleo del usuario
            │   │   │   ├── User.java      // Aggregate Root
            │   │   │   ├── UserId.java    // Value Object
            │   │   │   ├── Role.java      // Enum: NON_IT_EMPLOYEE, IT_EMPLOYEE, IT_TEAM_LEAD
            │   │   │   └── TeamMembership.java // Podría modelar la pertenencia a un equipo
            │   │   └── repository         // Interfaces de Repositorio (Contratos)
            │   │       └── UserRepository.java
            │   └── infrastructure
            │       ├── persistence        // Implementación de persistencia con Spring Data JDBC
            │       │   └── JdbcUserRepository.java // Implementa UserRepository
            │       └── web
            │           └── UserController.java
            │
            ├── teams                      // --- Slice: Gestión de Equipos de TI ---
            │   ├── application            // Casos de uso simples (CRUD o solo lectura)
            │   │   ├── query
            │   │   │   └── GetAllTeamsQuery.java
            │   │   │   └── GetAllTeamsQueryHandler.java
            │   │   └── dto
            │   │       └── TeamDto.java
            │   ├── domain
            │   │   ├── model
            │   │   │   ├── Team.java      // Aggregate Root (Sistemas, Soporte, Infra, Desarrollo)
            │   │   │   └── TeamId.java    // Value Object
            │   │   └── repository
            │   │       └── TeamRepository.java // Interface
            │   └── infrastructure
            │       ├── persistence
            │       │   └── JdbcTeamRepository.java
            │       └── web
            │           └── TeamController.java
            │
            ├── incidents                  // --- Slice: Gestión de Incidentes (Tickets) ---
            │   ├── application
            │   │   ├── command            // CreateTicket, AssignTicket, ChangeStatus, AddHistory, TransferTicket
            │   │   │   └── CreateTicketCommand.java
            │   │   │   └── CreateTicketCommandHandler.java
            │   │   │   └── AssignTicketCommand.java // Podría ser usado por empleado o líder
            │   │   │   └── AssignTicketCommandHandler.java
            │   │   │   └── PrioritizeAndAssignTicketCommand.java // Específico para líder
            │   │   │   └── PrioritizeAndAssignTicketCommandHandler.java
            │   │   │   └── ChangeTicketStatusCommand.java
            │   │   │   └── TransferTicketCommand.java // Para el líder
            │   │   ├── query              // GetTicketById, GetTicketsByStatus, GetTeamQueue, etc.
            │   │   │   └── GetTicketDetailsQuery.java
            │   │   │   └── GetTicketDetailsQueryHandler.java
            │   │   │   └── GetTeamTicketQueueQuery.java
            │   │   │   └── GetTeamTicketQueueQueryHandler.java
            │   │   ├── service            // Orquesta la lógica, aplica reglas de negocio (cola, permisos)
            │   │   │   ├── TicketManagementService.java
            │   │   │   └── TicketQueueService.java // Gestiona la lógica de la cola FIFO
            │   │   ├── event              // Listeners para eventos (e.g., notificar al crear ticket)
            │   │   │   └── TicketEventListener.java
            │   │   └── dto
            │   │       ├── TicketDto.java
            │   │       ├── TicketDetailsDto.java // Incluye historial, chat?, etc.
            │   │       ├── CreateTicketRequest.java
            │   │       └── TicketHistoryEntryDto.java
            │   ├── domain
            │   │   ├── model
            │   │   │   ├── Ticket.java      // Aggregate Root
            │   │   │   ├── TicketId.java    // Value Object
            │   │   │   ├── Status.java      // Enum: PENDIENTE, ASIGNADO, CANCELADO, ATENDIDO, EMITIDO_SD
            │   │   │   ├── Priority.java    // Value Object o Enum
            │   │   │   ├── IncidentCategory.java // Podría ser otra Aggregate o Value Object
            │   │   │   ├── TicketHistory.java // Entidad o Value Object parte del Ticket Aggregate
            │   │   │   └── AssignedTeam.java // Value Object o referencia a Team Aggregate
            │   │   ├── event              // Eventos de dominio puros
            │   │   │   ├── TicketCreated.java
            │   │   │   ├── TicketAssigned.java
            │   │   │   ├── TicketStatusChanged.java
            │   │   │   └── TicketTransferred.java
            │   │   ├── repository
            │   │   │   └── TicketRepository.java // Interface
            │   │   ├── service            // Lógica de dominio pura (si es compleja y no cabe en el Aggregate)
            │   │   │   └── TicketRoutingService.java // Determina el equipo inicial basado en categoría
            │   └── infrastructure
            │       ├── persistence
            │       │   └── JdbcTicketRepository.java // Implementa TicketRepository
            │       ├── routing            // Podría haber config o lógica específica de ruteo aquí
            │       │   └── CategoryToTeamMapper.java // O cargado desde BBDD/config
            │       └── web
            │           └── IncidentController.java // O TicketController.java
            │
            ├── chat                       // --- Slice: Chat de Tickets ---
            │   ├── application
            │   │   ├── command
            │   │   │   └── SendChatMessageCommand.java
            │   │   ├── query
            │   │   │   └── GetChatMessagesForTicketQuery.java
            │   │   ├── service
            │   │   │   └── ChatService.java
            │   │   └── dto
            │   │       ├── ChatMessageDto.java
            │   │       └── SendMessageRequest.java
            │   ├── domain
            │   │   ├── model
            │   │   │   ├── ChatMessage.java // Entidad o Value Object
            │   │   │   └── ChatSession.java // Podría ser un Aggregate si tiene lógica compleja
            │   │   └── repository
            │   │       └── ChatRepository.java // Interface
            │   └── infrastructure
            │       ├── persistence
            │       │   └── JdbcChatRepository.java
            │       ├── websocket          // Si usas WebSockets para el chat en tiempo real
            │       │   └── ChatWebSocketHandler.java
            │       │   └── WebSocketConfig.java
            │       └── web
            │           └── ChatController.java  // Para obtener historial, etc.
            │
            ├── notifications              // --- Slice: Notificaciones (Email, In-App) ---
            │   ├── application
            │   │   ├── command            // Comandos para enviar notificaciones (probablemente internos)
            │   │   │   └── SendNotificationCommand.java
            │   │   ├── service            // Interfaz para el servicio de notificación
            │   │   │   └── NotificationService.java
            │   │   └── event              // Listeners que reaccionan a eventos de dominio/app
            │   │       └── IncidentNotificationListener.java // Escucha TicketCreated, Assigned, etc.
            │   ├── domain                 // Modelo de notificación si es necesario
            │   │   └── model
            │   │       └── Notification.java
            │   └── infrastructure
            │       ├── email              // Implementación para envío de emails
            │       │   └── SmtpEmailNotificationService.java // Implementa NotificationService
            │       └── inapp              // Implementación para notificaciones en la app (e.g., guardar en BBDD, enviar por WebSocket)
            │           └── // InAppNotificationService.java, JdbcNotificationRepository.java, etc.
            │
            ├── reporting                  // --- Slice: Generación de Reportes (PDF, Excel) ---
            │   ├── application
            │   │   ├── query              // Consultas para obtener datos para reportes
            │   │   │   └── GenerateTicketReportQuery.java
            │   │   ├── service            // Orquesta la obtención de datos y la generación
            │   │   │   └── ReportGenerationService.java
            │   │   └── dto
            │   │       └── ReportRequest.java // Parámetros para el reporte (fechas, equipo, etc.)
            │   ├── domain                 // Probablemente ligero, más enfocado en datos agregados
            │   └── infrastructure
            │       ├── generation         // Lógica para generar los archivos
            │       │   ├── PdfReportGenerator.java  // Usando iText, Apache PDFBox, etc.
            │       │   └── ExcelReportGenerator.java // Usando Apache POI, etc.
            │       ├── persistence        // Repositorio para obtener datos agregados para reportes
            │       │   └── JdbcReportingRepository.java
            │       └── web
            │           └── ReportController.java    // Endpoint para solicitar/descargar reportes
            │
            └── dashboard                  // --- Slice: Datos para el Dashboard/Estadísticas ---
                ├── application
                │   ├── query              // Consultas específicas para las métricas del dashboard
                │   │   ├── GetGeneralStatsQuery.java
                │   │   ├── GetTeamPerformanceQuery.java
                │   │   └── ... // Query Handlers correspondientes
                │   └── dto
                │       ├── DashboardStatsDto.java
                │       └── TeamStatsDto.java
                ├── domain                 // Probablemente no tenga dominio propio, es solo lectura/agregación
                └── infrastructure
                    ├── persistence        // Repositorios optimizados para lectura de estadísticas
                    │   └── JdbcDashboardRepository.java // Consultas SQL complejas para agregar datos
                    └── web
                        └── DashboardController.java

src/main/resources/
├── application.properties / application.yml // Configuración principal
├── db                     // Scripts SQL (si usas Flyway/Liquibase)
│   └── migration
│       └── V1__Initial_schema.sql
├── templates              // Si generas emails desde plantillas (e.g., Thymeleaf)
│   └── email
│       └── notification-template.html
└── static                 // Contenido estático (si sirves algo desde el backend)
```
