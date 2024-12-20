use park;

CREATE TABLE Users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  role ENUM('admin', 'employee', 'customer') DEFAULT 'customer',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Vehicles (
  vehicle_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  license_plate VARCHAR(20) NOT NULL UNIQUE,
  vehicle_type ENUM('car', 'motorcycle', 'truck'),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);


CREATE TABLE ParkingSpots (
  spot_id INT AUTO_INCREMENT PRIMARY KEY,
  spot_number VARCHAR(10) NOT NULL UNIQUE,
  spot_type ENUM('car', 'motorcycle', 'truck'),
  is_available TINYINT(1) DEFAULT 1
);


-- Örnek Kullanıcılar
INSERT INTO Users (username, password_hash, email, role) 
VALUES ('merve', '123', 'merve@ornek.com', 'customer');
INSERT INTO Users (username, password_hash, email, role) 
VALUES ('admin', 'admin', 'admin@ornek.com', 'admin'),
       ('melek', 'melek', 'melek@ornek.com', 'customer');


-- Örnek Araçlar
INSERT INTO Vehicles (user_id, license_plate, vehicle_type) 
VALUES (2, '34ABC123', 'car'),
       (2, '35XYZ789', 'motorcycle');

- A1 ile A50 arasında park yerleri eklemek için

INSERT INTO ParkingSpots (spot_number, spot_type,is_available)
VALUES 
('A3', 'car',1),
('A4', 'car',1),
('A5', 'car',1),
('A6', 'car',1),
('A7', 'car',1),
('A8', 'car',1),
('A9', 'car',1),
('A10', 'car',1),
('A11', 'car',1),
('A12', 'car',1),
('A13', 'car',1),
('A14', 'car',1),
('A15', 'car',1),
('A16', 'car',1),
('A17', 'car',1),
('A18', 'car',1),
('A19', 'car',1),
('A20', 'car',1),
('B2', 'motorcycle',1),
('B3', 'motorcycle',1),
('B4', 'motorcycle',1),
('B5', 'motorcycle',1),
('B6', 'motorcycle',1),
('B7', 'motorcycle',1),
('B8', 'motorcycle',1),
('B9', 'motorcycle',1),
('B10', 'motorcycle',1),
('C1', 'truck',1),
('C2', 'truck',1),
('C3', 'truck',1),
('C4', 'truck',1),
('C5', 'truck',1);

