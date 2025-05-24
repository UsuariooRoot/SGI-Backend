-- User Table
CREATE TABLE Users (
    c_user BIGSERIAL PRIMARY KEY,
    x_username VARCHAR(100) NOT NULL,
    x_password VARCHAR NOT NULL,
    c_employee BIGINT NOT NULL,
    b_enabled BOOLEAN DEFAULT TRUE
);

-- Employee Table
CREATE TABLE Employees (
    c_employee BIGSERIAL PRIMARY KEY,
    x_name VARCHAR(100) NOT NULL,
    x_paternal_surname VARCHAR(100) NOT NULL,
    x_maternal_surname VARCHAR(100),
    x_email VARCHAR(150) UNIQUE NOT NULL,
    c_role INTEGER NOT NULL,
    c_it_team INTEGER
);

-- Employee Roles Table
CREATE TABLE EmployeeRoles (
    c_role SERIAL PRIMARY KEY,
    x_name VARCHAR(50) NOT NULL
);

-- IT Teams Table
CREATE TABLE ITTeams (
    c_it_team SERIAL PRIMARY KEY,
    x_name VARCHAR(100) NOT NULL
);

-- Incident Categories Table
CREATE TABLE IncidentCategories (
    c_category SERIAL PRIMARY KEY,
    x_name VARCHAR(100) NOT NULL,
    c_it_team INTEGER NOT NULL
);

-- Incidents Table
CREATE TABLE Incidents (
    c_incident SERIAL PRIMARY KEY,
    x_description VARCHAR(255) NOT NULL,
    c_category INTEGER NOT NULL,
    c_priority INTEGER NOT NULL
);

-- Tickets Table
CREATE TABLE Tickets (
    c_ticket BIGSERIAL PRIMARY KEY,
    c_employee_creator BIGINT NOT NULL,
    c_employee_owner BIGINT NOT NULL,
    c_incident INTEGER NOT NULL,
    x_description VARCHAR(255),
    n_current_history INTEGER DEFAULT NULL,
    f_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Ticket Histories Table
CREATE TABLE TicketHistories (
    c_history BIGSERIAL PRIMARY KEY,
    c_ticket INTEGER NOT NULL,
    c_employee BIGINT NOT NULL,
    c_employee_assigned BIGINT,
    c_action INTEGER NOT NULL,
    c_current_priority INTEGER NOT NULL,
    c_current_status INTEGER NOT NULL,
    c_current_team INTEGER NOT NULL,
    x_comment TEXT,
    f_logged TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Ticket Priorities Table
CREATE TABLE TicketPriorities (
    c_priority SERIAL PRIMARY KEY,
    x_name VARCHAR(50) NOT NULL
);

-- Ticket Actions Table
CREATE TABLE TicketActions (
    c_action SERIAL PRIMARY KEY,
    x_name VARCHAR(50) NOT NULL
);

-- Ticket Status Table
CREATE TABLE TicketStatuses (
    c_status SERIAL PRIMARY KEY,
    x_name VARCHAR(50) NOT NULL
);


-- FOREIGN KEYS
-- Users Table
ALTER TABLE Users
ADD CONSTRAINT fk_users_employee FOREIGN KEY (c_employee) REFERENCES Employees (c_employee);

-- Employees Table
ALTER TABLE Employees
ADD CONSTRAINT fk_employees_role FOREIGN KEY (c_role) REFERENCES EmployeeRoles (c_role);

ALTER TABLE Employees
ADD CONSTRAINT fk_employees_team FOREIGN KEY (c_it_team) REFERENCES ITTeams (c_it_team);

-- Incident Categories Table
ALTER TABLE IncidentCategories
ADD CONSTRAINT fk_categories_team FOREIGN KEY (c_it_team) REFERENCES ITTeams (c_it_team);

-- Incidents Table
ALTER TABLE Incidents
ADD CONSTRAINT fk_incidents_category FOREIGN KEY (c_category) REFERENCES IncidentCategories (c_category);

ALTER TABLE Incidents
ADD CONSTRAINT fk_incidents_priority FOREIGN KEY (c_priority) REFERENCES TicketPriorities (c_priority);

-- Tickets Table
ALTER TABLE Tickets
ADD CONSTRAINT fk_tickets_creator FOREIGN KEY (c_employee_creator) REFERENCES Employees (c_employee);

ALTER TABLE Tickets
ADD CONSTRAINT fk_tickets_owner FOREIGN KEY (c_employee_owner) REFERENCES Employees (c_employee);

ALTER TABLE Tickets
ADD CONSTRAINT fk_tickets_incident FOREIGN KEY (c_incident) REFERENCES Incidents (c_incident);

-- TicketHistories Table
ALTER TABLE TicketHistories
ADD CONSTRAINT fk_histories_ticket FOREIGN KEY (c_ticket) REFERENCES Tickets (c_ticket);

ALTER TABLE TicketHistories
ADD CONSTRAINT fk_histories_employee FOREIGN KEY (c_employee) REFERENCES Employees (c_employee);

ALTER TABLE TicketHistories
ADD CONSTRAINT fk_histories_employee_assigned FOREIGN KEY (c_employee_assigned) REFERENCES Employees (c_employee);

ALTER TABLE TicketHistories
ADD CONSTRAINT fk_histories_action FOREIGN KEY (c_action) REFERENCES TicketActions (c_action);

ALTER TABLE TicketHistories
ADD CONSTRAINT fk_histories_priority FOREIGN KEY (c_current_priority) REFERENCES TicketPriorities (c_priority);

ALTER TABLE TicketHistories
ADD CONSTRAINT fk_histories_status FOREIGN KEY (c_current_status) REFERENCES TicketStatuses (c_status);

ALTER TABLE TicketHistories
ADD CONSTRAINT fk_histories_team FOREIGN KEY (c_current_team) REFERENCES ITTeams (c_it_team);


-- TRIGGER TO UPDATE CURRENT HISTORY
CREATE OR REPLACE FUNCTION fn_update_current_history() RETURNS TRIGGER AS $$
BEGIN
    UPDATE Tickets
    SET n_current_history = NEW.c_history
    WHERE c_ticket = NEW.c_ticket;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_current_history
AFTER INSERT ON TicketHistories
FOR EACH ROW EXECUTE FUNCTION fn_update_current_history();


-- INSERT INITIAL DATA
INSERT INTO EmployeeRoles (x_name) VALUES
    ('EMPLEADO_NO_TI'),
    ('EMPLEADO_TI'),
    ('LIDER_EQUIPO_TI');

INSERT INTO ITTeams (x_name) VALUES
    ('Soporte Tecnico'),
    ('Sistemas'),
    ('Desarrollo'),
    ('Infraestructura');

INSERT INTO TicketStatuses (x_name) VALUES
    ('Pendiente'),
    ('Asignado'),
    ('Atendido'),
    ('Cancelado');

INSERT INTO TicketActions (x_name) VALUES
    ('Creación'),
    ('Cambio de prioridad'),
    ('Cambio de estado'),
    ('Asignar'),
    ('Desasignar'),
    ('Reasignar'),
    ('Cambio de equipo'),
    ('Cola de atención');

INSERT INTO TicketPriorities (x_name) VALUES
    ('Baja'),
    ('Media'),
    ('Alta'),
    ('Crítica');

INSERT INTO IncidentCategories (x_name, c_it_team) VALUES
    ('Errores de Software', 3),
    ('Problemas con Servidores y Sistemas', 2),
    ('Soporte a Usuarios', 1),
    ('Redes y Conectividad', 4);

INSERT INTO Incidents (x_description, c_category, c_priority) VALUES
    ('Error en aplicación interna', 1, 2),
    ('Bug en nueva funcionalidad', 1, 2),
    ('Fallo en integración de APIs', 1, 2),
    ('Problemas con base de datos', 1, 2),
    ('Tiempo de respuesta lento en aplicación', 1, 2),
    ('Problemas en despliegue de software', 1, 2),
    ('Fallo en servidor', 2, 2),
    ('Espacio insuficiente en disco', 2, 2),
    ('Problemas con máquinas virtuales', 2, 2),
    ('Errores en sistema operativo', 2, 2),
    ('Falla en autenticación de usuario', 2, 2),
    ('Cuentas de usuario bloqueadas', 2, 2),
    ('Actualización de software requerida', 2, 2),
    ('Problema con impresoras', 3, 2),
    ('Fallo en acceso a correo electrónico', 3, 2),
    ('Error en software de ofimática', 3, 2),
    ('Reinicio inesperado de equipo', 3, 2),
    ('No funciona acceso remoto', 3, 2),
    ('Solicitud de instalación de software', 3, 2),
    ('Fallo en conexión a internet', 4, 2),
    ('Pérdida de paquetes en red', 4, 2),
    ('Problemas con VPN', 4, 2),
    ('Desempeño bajo en red interna', 4, 2),
    ('Acceso denegado a recursos compartidos', 4, 2),
    ('Caída de switch o router', 4, 2),
    ('Solicitud de nueva conexión de red', 4, 2);


INSERT INTO Employees (x_name, x_paternal_surname, x_maternal_surname, x_email, c_role, c_it_team) VALUES
    ('Rogelio', 'Flores', 'García', 'rogelio.flores@example.com', 2, 1),
    ('Angel', 'Martines', 'López', 'angel.martines@example.com', 3, 1),
    ('Carlos', 'Bustamante', 'Rodríguez', 'carlos.bustamante@example.com', 2, 2),
    ('Miguel', 'Pierola', 'Sánchez', 'miguel.pierola@example.com', 3, 2),
    ('Facundo', 'Gonzales', 'Martínez', 'facundo.gonzales@example.com', 2, 3),
    ('Juan', 'Pérez', 'Gómez', 'juan.perez@example.com', 3, 3),
    ('Ricardo', 'Gómez', 'Hernández', 'ricardo.gomez@example.com', 2, 4),
    ('Angela', 'Rosario', 'Díaz', 'angela.rosario@example.com', 3, 4),
    ('Richard', 'Stalmant', 'Smith', 'richard.stalmant@example.com', 1, NULL),
    ('Camila', 'Sánchez', 'Vargas', 'camila.sanchez@example.com', 1, NULL),
    ('Esteban', 'Pacheco', 'Ruiz', 'angelpacheco@example.com', 1, NULL),
    ('Pepe', 'Cácerez', 'Mendoza', 'carloscacerez@example.com', 1, NULL),
    ('Mickey', 'Dier', 'Johnson', 'mickeydier@example.com', 1, NULL);

INSERT INTO Users (x_username, x_password, c_employee) VALUES
	('rogelio', '$2a$10$EM85LRx9dw8ai/M6PSsqIOiCNfsnlq13Hr4NWPQ24wg1/dhFMYbAW', 1),
	('angel', '$2a$10$EM85LRx9dw8ai/M6PSsqIOiCNfsnlq13Hr4NWPQ24wg1/dhFMYbAW', 2),
	('carlos', '$2a$10$EM85LRx9dw8ai/M6PSsqIOiCNfsnlq13Hr4NWPQ24wg1/dhFMYbAW', 3),
	('miguel', '$2a$10$EM85LRx9dw8ai/M6PSsqIOiCNfsnlq13Hr4NWPQ24wg1/dhFMYbAW', 4),
	('facundo', '$2a$10$EM85LRx9dw8ai/M6PSsqIOiCNfsnlq13Hr4NWPQ24wg1/dhFMYbAW', 5),
	('juan', '$2a$10$EM85LRx9dw8ai/M6PSsqIOiCNfsnlq13Hr4NWPQ24wg1/dhFMYbAW', 6),
	('ricardo', '$2a$10$EM85LRx9dw8ai/M6PSsqIOiCNfsnlq13Hr4NWPQ24wg1/dhFMYbAW', 7),
	('angela', '$2a$10$EM85LRx9dw8ai/M6PSsqIOiCNfsnlq13Hr4NWPQ24wg1/dhFMYbAW', 8),
	('richard', '$2a$10$EM85LRx9dw8ai/M6PSsqIOiCNfsnlq13Hr4NWPQ24wg1/dhFMYbAW', 9),
	('camila', '$2a$10$EM85LRx9dw8ai/M6PSsqIOiCNfsnlq13Hr4NWPQ24wg1/dhFMYbAW', 10),
	('esteban', '$2a$10$EM85LRx9dw8ai/M6PSsqIOiCNfsnlq13Hr4NWPQ24wg1/dhFMYbAW', 11),
	('pepe', '$2a$10$EM85LRx9dw8ai/M6PSsqIOiCNfsnlq13Hr4NWPQ24wg1/dhFMYbAW', 12),
	('mickey', '$2a$10$EM85LRx9dw8ai/M6PSsqIOiCNfsnlq13Hr4NWPQ24wg1/dhFMYbAW', 13);



CREATE TYPE employee_detail AS (
    -- Personal info
    c_employee BIGINT,
    x_name VARCHAR,
    x_paternal_surname VARCHAR,
    x_maternal_surname VARCHAR,
    x_email VARCHAR,
    -- Role
    c_role INTEGER,
    role_name VARCHAR,
    -- IT Team
    c_it_team INTEGER,
    team_name VARCHAR
);

-- Filtrar empleados
CREATE OR REPLACE FUNCTION ufn_filter_employees(
    p_it_team_id INTEGER DEFAULT NULL,
    p_role_id INTEGER DEFAULT NULL
)
RETURNS SETOF employee_detail
LANGUAGE SQL AS $$
    SELECT 
        e.c_employee,
        e.x_name,
        e.x_paternal_surname,
        e.x_maternal_surname,
        e.x_email,
        r.c_role,
        r.x_name AS role_name,
        t.c_it_team,
        t.x_name AS team_name
    FROM 
        Employees e
    JOIN 
        EmployeeRoles r ON e.c_role = r.c_role
    LEFT JOIN 
        ITTeams t ON e.c_it_team = t.c_it_team
    WHERE 
        (p_it_team_id IS NULL OR e.c_it_team = p_it_team_id) AND
        (p_role_id IS NULL OR e.c_role = p_role_id);
$$;

-- Obtener empleado por ID
CREATE OR REPLACE FUNCTION ufn_get_employee_by_id(
    p_employee_id BIGINT DEFAULT NULL
)
RETURNS SETOF employee_detail
LANGUAGE SQL AS $$
    SELECT 
        e.c_employee,
        e.x_name,
        e.x_paternal_surname,
        e.x_maternal_surname,
        e.x_email,
        r.c_role,
        r.x_name AS role_name,
        t.c_it_team,
        t.x_name AS team_name
    FROM 
        Employees e
    JOIN 
        EmployeeRoles r ON e.c_role = r.c_role
    LEFT JOIN 
        ITTeams t ON e.c_it_team = t.c_it_team
    WHERE 
        e.c_employee = p_employee_id;
$$;







-- Recuperar historial por su id
CREATE OR REPLACE FUNCTION ufn_find_history_by_id(
    p_history_id BIGINT
) RETURNS TABLE (
    -- History fields
    c_history BIGINT,
    c_ticket INTEGER,
    c_employee BIGINT,
    c_employee_assigned BIGINT,
    c_action INTEGER, 
    c_current_priority INTEGER,
    c_current_status INTEGER,
    c_current_team INTEGER,
    x_comment TEXT,
    f_logged TIMESTAMP,
    
    -- Additional fields with names
    action_id INTEGER,
    action_name VARCHAR(50),
    status_id INTEGER,
    status_name VARCHAR(50),
    priority_id INTEGER,
    priority_name VARCHAR(50),
    team_id INTEGER,
    team_name VARCHAR(100)
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        h.c_history,
        h.c_ticket,
        h.c_employee,
        h.c_employee_assigned,
        h.c_action,
        h.c_current_priority,
        h.c_current_status,
        h.c_current_team,
        h.x_comment,
        h.f_logged,
        
        ta.c_action AS action_id, 
        ta.x_name AS action_name,
        ts.c_status AS status_id, 
        ts.x_name AS status_name,
        tp.c_priority AS priority_id, 
        tp.x_name AS priority_name,
        it.c_it_team AS team_id, 
        it.x_name AS team_name
    FROM 
        TicketHistories h
        JOIN TicketActions ta ON ta.c_action = h.c_action
        JOIN TicketStatuses ts ON ts.c_status = h.c_current_status
        JOIN TicketPriorities tp ON tp.c_priority = h.c_current_priority
        JOIN ITTeams it ON it.c_it_team = h.c_current_team
    WHERE 
        h.c_history = p_history_id;
END;
$$ LANGUAGE plpgsql;


-- SELECT * FROM ufn_find_history_by_id(1);


-- Recuperar todo el historial de un ticket
CREATE OR REPLACE FUNCTION ufn_find_all_history_by_ticket_id(
	p_ticket_id BIGINT
)
RETURNS TABLE (
    history_id BIGINT,
    -- Employee data (who made the action)
    employee_id BIGINT,
    employee_name VARCHAR,
    employee_paternal_surname VARCHAR,
    employee_maternal_surname VARCHAR,
    employee_email VARCHAR,
    
    -- Assigned employee data
    assigned_employee_id BIGINT,
    assigned_employee_name VARCHAR,
    assigned_employee_paternal_surname VARCHAR,
    assigned_employee_maternal_surname VARCHAR,
    assigned_employee_email VARCHAR,
    
    -- action
    action_id INTEGER,
    action_name VARCHAR,
    
    -- current status
    status_id INTEGER,
    status_name VARCHAR,
    
    -- current priority
    priority_id INTEGER,
    priority_name VARCHAR,
    
    -- current team
    team_id INTEGER,
    team_name VARCHAR,
    
    -- Comment and timestamp
    comment TEXT,
    logged TIMESTAMP
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        th.c_history AS history_id,
        
        -- Employee data
        e.c_employee AS employee_id,
        e.x_name AS employee_name,
        e.x_paternal_surname AS employee_paternal_surname,
        e.x_maternal_surname AS employee_maternal_surname,
        e.x_email AS employee_email,
        
        -- Assigned employee data
        ae.c_employee AS assigned_employee_id,
        ae.x_name AS assigned_employee_name,
        ae.x_paternal_surname AS assigned_employee_paternal_surname,
        ae.x_maternal_surname AS assigned_employee_maternal_surname,
        ae.x_email AS assigned_employee_email,
        
        -- Action data
        th.c_action AS action_id,
        a.x_name AS action_name,
        
        -- Status data
        th.c_current_status AS status_id,
        s.x_name AS status_name,
        
        -- Priority data
        th.c_current_priority AS priority_id,
        p.x_name AS priority_name,
        
        -- Team data
        th.c_current_team AS team_id,
        t.x_name AS team_name,
        
        -- Comment and timestamp
        th.x_comment AS comment,
        th.f_logged AS logged
    FROM 
        TicketHistories th
    INNER JOIN 
        Employees e ON th.c_employee = e.c_employee
    LEFT JOIN 
        Employees ae ON th.c_employee_assigned = ae.c_employee
    LEFT JOIN 
        (SELECT c_action, x_name FROM TicketActions) a ON th.c_action = a.c_action
    LEFT JOIN 
        (SELECT c_status, x_name FROM TicketStatuses) s ON th.c_current_status = s.c_status
    LEFT JOIN 
        (SELECT c_priority, x_name FROM TicketPriorities) p ON th.c_current_priority = p.c_priority
    LEFT JOIN 
        ITTeams t ON th.c_current_team = t.c_it_team
    WHERE 
        th.c_ticket = p_ticket_id
    ORDER BY 
        th.f_logged ASC;
END;
$$ LANGUAGE plpgsql;

-- SELECT * FROM ufn_find_all_history_by_ticket_id(1);

-- Recuperar historial de un ticket
-- SELECT
--     h.c_history,
--     h.f_logged,
--     h.c_ticket,
--     emp.x_name || ' ' || emp.x_paternal_surname AS employee,
--     COALESCE(
--         emp_asg.x_name || ' ' || emp_asg.x_paternal_surname,
--         'N/A'
--     ) AS assigned_employee,
--     act.x_name AS accion,
--     prio.x_name AS prioridad,
--     stat.x_name AS estado,
--     team.x_name AS equipo,
--     h.x_comment AS comentario
-- FROM
--     TicketHistories h
--     JOIN TicketActions act ON h.c_action = act.c_action
--     JOIN TicketStatuses stat ON h.c_current_status = stat.c_status
--     JOIN TicketPriorities prio ON h.c_current_priority = prio.c_priority
--     JOIN ITTeams team ON h.c_current_team = team.c_it_team
--     JOIN Employees emp ON h.c_employee = emp.c_employee
--     LEFT JOIN Employees emp_asg ON h.c_employee_assigned = emp_asg.c_employee
-- WHERE
--     h.c_ticket = 1
-- ORDER BY
--     h.c_history;


-- CONSULTAS TICKETS
CREATE TYPE ticket_detail AS (
    ticket_id BIGINT,
    creator_id BIGINT,
    owner_id BIGINT,
    incident_id INTEGER,
    ticket_description TEXT,
    current_history_id BIGINT,
    f_created TIMESTAMP,
    
    -- Creator employee fields
    creator_name VARCHAR,
    creator_paternal_surname VARCHAR,
    creator_maternal_surname VARCHAR,
    creator_email VARCHAR,
    creator_role_id INTEGER,
    creator_it_team_id INTEGER,
    
    -- Owner employee fields
    owner_name VARCHAR,
    owner_paternal_surname VARCHAR,
    owner_maternal_surname VARCHAR,
    owner_email VARCHAR,
    owner_role_id INTEGER,
    owner_it_team_id INTEGER,
    
    -- Incident fields
    incident_description VARCHAR,
    incident_category_id INTEGER,
    incident_priority_id INTEGER
);

CREATE OR REPLACE FUNCTION ufn_filter_tickets (
    p_show_new_tickets BOOLEAN DEFAULT FALSE,
    p_status_ids INT[] DEFAULT NULL,
    p_assigned_employee_id BIGINT DEFAULT NULL,
    p_owner_employee_id BIGINT DEFAULT NULL,
    p_date_from TIMESTAMP DEFAULT NULL,
    p_date_to TIMESTAMP DEFAULT NULL,
    p_it_team_id BIGINT DEFAULT NULL
) RETURNS SETOF ticket_detail AS $$
SELECT
    t.c_ticket AS ticket_id,
    t.c_employee_creator AS creator_id,
    t.c_employee_owner AS owner_id,
    t.c_incident AS incident_id,
    t.x_description AS x_ticket_description,
    t.n_current_history AS current_history_id,
    t.f_created,
    
    -- Creator employee data
    e_creator.x_name AS creator_name,
    e_creator.x_paternal_surname AS creator_paternal_surname,
    e_creator.x_maternal_surname AS creator_maternal_surname,
    e_creator.x_email AS creator_email,
    e_creator.c_role AS creator_role_id,
    e_creator.c_it_team AS creator_it_team_id,
    
    -- Owner employee data
    e_owner.x_name AS owner_name,
    e_owner.x_paternal_surname AS owner_paternal_surname,
    e_owner.x_maternal_surname AS owner_maternal_surname,
    e_owner.x_email AS owner_email,
    e_owner.c_role AS owner_role_id,
    e_owner.c_it_team AS owner_it_team_id,
    
    -- Incident data
    i.x_description AS incident_description,
    i.c_category AS incident_category_id,
    i.c_priority AS incident_priority_id
FROM
    Tickets t
    JOIN Incidents i ON i.c_incident = t.c_incident
    JOIN TicketHistories h ON h.c_history = t.n_current_history
    JOIN Employees e_creator ON e_creator.c_employee = t.c_employee_creator
    JOIN Employees e_owner ON e_owner.c_employee = t.c_employee_owner
WHERE
    (
        p_status_ids IS NULL
        OR h.c_current_status = ANY(p_status_ids)
    )
    AND (
        p_assigned_employee_id IS NULL
        OR h.c_employee_assigned = p_assigned_employee_id
    )
    AND (
        p_owner_employee_id IS NULL
        OR t.c_employee_owner = p_owner_employee_id
    )
    AND (
        (
            p_show_new_tickets
            AND (CURRENT_TIMESTAMP - t.f_created <= INTERVAL '24 hours')
        )
        OR (
            NOT p_show_new_tickets
            AND t.f_created >= COALESCE(p_date_from, '1900-01-01'::TIMESTAMP)
            AND t.f_created <= COALESCE(p_date_to, CURRENT_TIMESTAMP)
        )
    )
    AND (
        p_it_team_id IS NULL
        OR h.c_current_team = p_it_team_id
    )
$$ LANGUAGE sql;



-- Example usage for IT Employee
-- SELECT * FROM ufn_filter_tickets();

-- Example usage for non-IT Employee
-- SELECT * FROM ufn_filter_tickets(
--     FALSE,
--     ARRAY[1, 2, 3, 4],
--     NULL,
--     NULL
-- );

-- Función para obtener información adicional de los tickets por su current history id
CREATE OR REPLACE FUNCTION ufn_find_ticket_info_by_history_ids(
    p_history_ids BIGINT[]
) RETURNS TABLE (
    history_id BIGINT,
    
    -- Assigned employee data
    assigned_employee_id BIGINT,
    assigned_employee_name VARCHAR,
    assigned_employee_paternal_surname VARCHAR,
    assigned_employee_maternal_surname VARCHAR,
    assigned_employee_email VARCHAR,
    
    -- Status data
    status_id INTEGER,
    status_name VARCHAR,
    
    -- Priority data
    priority_id INTEGER,
    priority_name VARCHAR,
    
    -- Team data
    team_id INTEGER,
    team_name VARCHAR
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        th.c_history AS history_id,
        
        -- Assigned employee data
        ae.c_employee AS assigned_employee_id,
        ae.x_name AS assigned_employee_name,
        ae.x_paternal_surname AS assigned_employee_paternal_surname,
        ae.x_maternal_surname AS assigned_employee_maternal_surname,
        ae.x_email AS assigned_employee_email,
        
        -- Status data
        th.c_current_status AS status_id,
        s.x_name AS status_name,
        
        -- Priority data
        th.c_current_priority AS priority_id,
        p.x_name AS priority_name,
        
        -- Team data
        th.c_current_team AS team_id,
        t.x_name AS team_name
    FROM 
        TicketHistories th
    LEFT JOIN 
        Employees ae ON th.c_employee_assigned = ae.c_employee
    LEFT JOIN 
        TicketStatuses s ON th.c_current_status = s.c_status
    LEFT JOIN 
        TicketPriorities p ON th.c_current_priority = p.c_priority
    LEFT JOIN 
        ITTeams t ON th.c_current_team = t.c_it_team
    WHERE 
        th.c_history = ANY(p_history_ids);
END;
$$ LANGUAGE plpgsql;

-- SELECT * FROM ufn_find_ticket_info_by_history_ids(ARRAY[1]);

CREATE OR REPLACE FUNCTION ufn_find_ticket_by_id (
    p_ticket_id BIGINT
) RETURNS SETOF ticket_detail AS $$
SELECT
    t.c_ticket AS ticket_id,
    t.c_employee_creator AS creator_id,
    t.c_employee_owner AS owner_id,
    t.c_incident AS incident_id,
    t.x_description AS x_ticket_description,
    t.n_current_history AS current_history_id,
    t.f_created,
    
    -- Creator employee data
    e_creator.x_name AS creator_name,
    e_creator.x_paternal_surname AS creator_paternal_surname,
    e_creator.x_maternal_surname AS creator_maternal_surname,
    e_creator.x_email AS creator_email,
    e_creator.c_role AS creator_role_id,
    e_creator.c_it_team AS creator_it_team_id,
    
    -- Owner employee data
    e_owner.x_name AS owner_name,
    e_owner.x_paternal_surname AS owner_paternal_surname,
    e_owner.x_maternal_surname AS owner_maternal_surname,
    e_owner.x_email AS owner_email,
    e_owner.c_role AS owner_role_id,
    e_owner.c_it_team AS owner_it_team_id,
    
    -- Incident data
    i.x_description AS incident_description,
    i.c_category AS incident_category_id,
    i.c_priority AS incident_priority_id
FROM
    Tickets t
    JOIN Incidents i ON i.c_incident = t.c_incident
    JOIN TicketHistories h ON h.c_history = t.n_current_history
    JOIN Employees e_creator ON e_creator.c_employee = t.c_employee_creator
    JOIN Employees e_owner ON e_owner.c_employee = t.c_employee_owner
WHERE
    t.c_ticket = p_ticket_id
$$ LANGUAGE sql;


-- SELECT * FROM ufn_find_ticket_by_id(1);

-- CREAR TICKET
-- CREATE OR REPLACE PROCEDURE usp_create_ticket(
--     IN p_creator_employee_id BIGINT,
--     IN p_owner_employee_id BIGINT,
--     IN p_incident_id INT,
--     IN p_priority_id INT,
--     IN p_team_id INT,
--     IN p_description TEXT DEFAULT NULL
-- ) LANGUAGE plpgsql AS $$
-- DECLARE
--     v_ticket_id BIGINT;
-- BEGIN
--     -- Insert the new ticket
--     INSERT INTO Tickets (
--         c_employee_creator,
--         c_employee_owner,
--         c_incident,
--         x_description
--     )
--     VALUES (
--         p_creator_employee_id,
--         p_owner_employee_id,
--         p_incident_id,
--         p_description
--     ) RETURNING c_ticket INTO v_ticket_id;

--     -- Insert into ticket history
--     INSERT INTO TicketHistories (
--         c_ticket,
--         c_employee,
--         c_employee_assigned,
--         c_action,
--         c_current_priority,
--         c_current_status,
--         c_current_team
--     )
--     VALUES (
--         v_ticket_id,
--         p_creator_employee_id,
--         NULL,
--         1, -- Create action
--         p_priority_id,
--         1, -- Initial status
--         p_team_id
--     );

--     EXCEPTION
--         WHEN OTHERS THEN
--             RAISE EXCEPTION 'Error creating ticket: %', SQLERRM;
-- END;
-- $$;

-- Example usage
-- CALL usp_create_ticket(
--     1,  -- creator employee id
--     1,  -- owner employee id
--     2,  -- incident id
--     2,  -- priority id
--     1,  -- team id
--     'Need a phone installation'
-- );


-- Función para crear ticket y retornar ID
CREATE OR REPLACE FUNCTION ufn_create_ticket(
    IN p_creator_employee_id BIGINT,
    IN p_owner_employee_id BIGINT,
    IN p_incident_id INT,
    IN p_priority_id INT,
    IN p_team_id INT,
    IN p_description TEXT DEFAULT NULL
) RETURNS BIGINT LANGUAGE plpgsql AS $$
DECLARE
    v_ticket_id BIGINT;
BEGIN
    -- Insert the new ticket
    INSERT INTO Tickets (
        c_employee_creator,
        c_employee_owner,
        c_incident,
        x_description
    )
    VALUES (
        p_creator_employee_id,
        p_owner_employee_id,
        p_incident_id,
        p_description
    ) RETURNING c_ticket INTO v_ticket_id;

    -- Insert into ticket history
    INSERT INTO TicketHistories (
        c_ticket,
        c_employee,
        c_employee_assigned,
        c_action,
        c_current_priority,
        c_current_status,
        c_current_team
    )
    VALUES (
        v_ticket_id,
        p_creator_employee_id,
        NULL,
        1, -- Create action
        p_priority_id,
        1, -- Initial status
        p_team_id
    );

    RETURN v_ticket_id;
EXCEPTION
    WHEN OTHERS THEN
        RAISE EXCEPTION 'Error creating ticket: %', SQLERRM;
END;
$$;


-- SELECT * FROM ufn_create_ticket(
--     1,  -- creator employee id
--     1,  -- owner employee id
--     2,  -- incident id
--     2,  -- priority id
--     1,  -- team id
--     'Need a phone installation'
-- );

CREATE OR REPLACE FUNCTION ufn_execute_ticket_action(
    p_employee_id    BIGINT,           -- quién ejecuta la acción
    p_ticket_id      BIGINT,
    p_action_id      INT,
    p_update_value   INT,
    p_comment        TEXT DEFAULT NULL
)
RETURNS void
LANGUAGE plpgsql
AS $$
DECLARE
    v_history TicketHistories%ROWTYPE;
BEGIN
    -- Obtener los datos del histórico actual del ticket
    SELECT 
        h.*
    INTO 
        v_history
    FROM 
        TicketHistories h
        JOIN Tickets t ON h.c_history = t.n_current_history
    WHERE 
        t.c_ticket = p_ticket_id;

    -- Insertar un nuevo registro con los datos actualizados
    INSERT INTO TicketHistories (
        c_ticket,
        c_employee,
        c_employee_assigned,
        c_action,
        c_current_priority,
        c_current_status,
        c_current_team,
        x_comment
    ) VALUES (
        v_history.c_ticket,
        p_employee_id,
        CASE
            WHEN p_action_id IN (4, 6, 8) THEN p_update_value  -- Asignar, reasignar
            WHEN p_action_id = 5             THEN NULL         -- Desasignar
            ELSE v_history.c_employee_assigned
        END,
        p_action_id,
        CASE
            WHEN p_action_id = 2 THEN p_update_value  -- Cambiar prioridad
            ELSE v_history.c_current_priority
        END,
        CASE
            WHEN p_action_id IN (4, 6, 8) THEN 2     -- "Asignado"
            WHEN p_action_id = 5         THEN 1      -- "Pendiente"
            WHEN p_action_id = 3         THEN p_update_value  -- Cambiar estado
            ELSE v_history.c_current_status
        END,
        CASE
            WHEN p_action_id = 7 THEN p_update_value  -- Cambiar equipo
            ELSE v_history.c_current_team
        END,
        p_comment
    );

    RETURN;
EXCEPTION
    WHEN OTHERS THEN
        RAISE EXCEPTION 'Error al cambiar el estado del ticket: %', SQLERRM;
END;
$$;



-- funcion postgres para obtener objeto IncidentCategory.java
CREATE OR REPLACE FUNCTION ufn_get_category_team_and_incidents()
RETURNS TABLE (
    category_id         INTEGER,
    category_name       VARCHAR(100),
    it_team_id          INTEGER,
    it_team_name        VARCHAR(100),
    incident_id         INTEGER,
    incident_description VARCHAR(255)
)
LANGUAGE sql
STABLE
AS $$
  SELECT
      ic.c_category AS category_id,
      ic.x_name AS category_name,
      ic.c_it_team AS it_team_id,
      itt.x_name AS it_team_name,
      i.c_incident AS incident_id,
      i.x_description AS incident_description
  FROM
      IncidentCategories ic
  JOIN
      ITTeams itt ON ic.c_it_team = itt.c_it_team
  LEFT JOIN
      Incidents i ON ic.c_category = i.c_category
  ORDER BY
      ic.c_category, i.c_incident;
$$;

-- SELECT * FROM ufn_get_category_team_and_incidents();



CREATE OR REPLACE FUNCTION ufn_get_user_by_name(
    p_username VARCHAR(50)
) RETURNS TABLE (
    user_id BIGINT,
    username VARCHAR,
    password VARCHAR,
    enabled BOOLEAN,

    -- Employee
    employee_id BIGINT,
    name VARCHAR,
    paternal_surname VARCHAR,
    maternal_surname VARCHAR,
    email VARCHAR,

    -- Role
    role_id INTEGER,
    role_name VARCHAR,

    -- IT Team
    it_team_id INTEGER,
    it_team_name VARCHAR
)
AS $$
BEGIN
    RETURN QUERY
    SELECT
        u.c_user AS user_id,
        u.x_username AS username,
        u.x_password AS password,
        u.b_enabled AS enabled,

        e.c_employee AS employee_id,
        e.x_name AS name,
        e.x_paternal_surname AS paternal_surname,
        e.x_maternal_surname AS maternal_surname,
        e.x_email AS email,

        r.c_role AS role_id,
        r.x_name AS role_name,

        t.c_it_team AS it_team_id,
        t.x_name AS it_team_name
    FROM Users u
    INNER JOIN Employees e ON u.c_employee = e.c_employee
    INNER JOIN EmployeeRoles r ON e.c_role = r.c_role
    LEFT JOIN ITTeams t ON e.c_it_team = t.c_it_team
    WHERE u.x_username = p_username;
END;
$$ LANGUAGE plpgsql;