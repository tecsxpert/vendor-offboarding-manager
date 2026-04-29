CREATE TABLE audit_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    action VARCHAR(100) NOT NULL,
    entity_name VARCHAR(100) NOT NULL,
    entity_id BIGINT,
    details TEXT,
    performed_by VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_entity_name ON audit_log(entity_name);
CREATE INDEX idx_audit_entity_id ON audit_log(entity_id);
CREATE INDEX idx_audit_created_at ON audit_log(created_at);