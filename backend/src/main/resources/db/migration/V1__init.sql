CREATE TABLE vendors (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    vendor_name VARCHAR(255) NOT NULL,
    vendor_email VARCHAR(255) UNIQUE NOT NULL,
    vendor_phone VARCHAR(20),
    company_name VARCHAR(255),
    contract_start_date DATE,
    contract_end_date DATE,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    offboarding_reason TEXT,
    offboarding_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_status ON vendors(status);
CREATE INDEX idx_contract_end ON vendors(contract_end_date);