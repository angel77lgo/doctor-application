CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE doctor (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100),
    last_name VARCHAR(100),
    second_last_name VARCHAR(100),
    speciality VARCHAR(100)
);

CREATE TABLE office (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    floor VARCHAR(10),
    floor_number VARCHAR(10)
);

CREATE TABLE appointment (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    doctor_id UUID,
    office_id UUID,
    patient_name VARCHAR(150),
    schedule TIMESTAMP,

    CONSTRAINT fk_doctor
        FOREIGN KEY (doctor_id)
        REFERENCES doctor(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,


    CONSTRAINT fk_office
        FOREIGN KEY (office_id)
        REFERENCES office(id)
        ON DELETE SET NULL
            ON UPDATE CASCADE
);

INSERT INTO doctor(name, last_name, second_last_name, speciality) VALUES
                  ('Alfredo', 'Lopez', 'Perez', 'Pediatria'),
                  ('Juan', 'Gallardo', 'Solis', 'Cardiologo'),
                  ('Jazmin', 'Torres', 'Cardenas', 'Neurologa'),
                  ('Alberto', 'Lugo', 'Martinez', 'Radiologo');

INSERT INTO office (floor, floor_number) VALUES
                    ('1','A'),
                    ('3', '1'),
                    ('3', '2'),
                    ('2', 'C');