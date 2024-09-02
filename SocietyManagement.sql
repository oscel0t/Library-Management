create database dbmsproject;
use dbmsproject;



-- Create Block Table
CREATE TABLE Block (
    block_id INT PRIMARY KEY AUTO_INCREMENT,
    block_name VARCHAR(50)
);

-- Create Residents Table
CREATE TABLE Residents (
    resident_id INT PRIMARY KEY AUTO_INCREMENT,
    residentname VARCHAR(50),
    flatnumber VARCHAR(20),
    mobile VARCHAR(20),
    block_id INT,
    FOREIGN KEY (block_id) REFERENCES Block(block_id)
);

-- Create Visitors Table
CREATE TABLE Visitors (
    visitor_id INT PRIMARY KEY AUTO_INCREMENT,
    visitor_name VARCHAR(50),
    v_address VARCHAR(100),
    v_email VARCHAR(50),
    phone_number VARCHAR(20),
    resident_id INT,
    FOREIGN KEY (resident_id) REFERENCES Residents(resident_id)
);

-- Create Complaints Table
CREATE TABLE Complaints (
    complaint_id INT PRIMARY KEY AUTO_INCREMENT,
    resident_id INT,
    complaint_text TEXT,
    complaint_type VARCHAR(50),
    FOREIGN KEY (resident_id) REFERENCES Residents(resident_id)
);

-- Create Clubs Table
CREATE TABLE Clubs (
    club_id INT PRIMARY KEY AUTO_INCREMENT,
    club_name VARCHAR(50),
    club_activities VARCHAR(255),
    block_id INT,
    FOREIGN KEY (block_id) REFERENCES Block(block_id)
);

-- Create Househelp Table
CREATE TABLE Househelp (
    househelp_id INT PRIMARY KEY AUTO_INCREMENT,
    helper_name VARCHAR(50),
    availability VARCHAR(50),
    aadhar_number VARCHAR(20),
    househelper_number VARCHAR(20),
    block_id INT,
    FOREIGN KEY (block_id) REFERENCES Block(block_id)
);

-- Insert Sample Data into Block Table
INSERT INTO Block (block_name) VALUES 
('Block A'),
('Block B'),
('Block C');

-- Insert Sample Data into Residents Table
INSERT INTO Residents (residentname, flatnumber, mobile, block_id) VALUES 
('John Doe', '101', '1234567890', 1),
('Jane Smith', '202', '9876543210', 2),
('Bob Johnson', '303', '9998887776', 3);

SELECT * FROM Residents;

-- Insert Sample Data into Visitors Table
INSERT INTO Visitors (visitor_name, v_address, v_email, phone_number, resident_id) VALUES 
('Alice', 'Address 1', 'alice@example.com', '1112223334', 1),
('Bob', 'Address 2', 'bob@example.com', '2223334445', 2),
('Charlie', 'Address 3', 'charlie@example.com', '3334445556', 3);

-- Insert Sample Data into Complaints Table
INSERT INTO Complaints (resident_id, complaint_text, complaint_type) VALUES 
(1, 'No water supply', 'Utility'),
(2, 'Loud noise at night', 'Noise'),
(3, 'Garbage not collected', 'Sanitation');

-- Insert Sample Data into Clubs Table
INSERT INTO Clubs (club_name, club_activities, block_id) VALUES 
('Fitness Club', 'Yoga, Zumba', 1),
('Gardening Club', 'Gardening workshops', 2),
('Book Club', 'Book discussions', 3);

-- Insert Sample Data into Househelp Table
INSERT INTO Househelp (helper_name, availability, aadhar_number, househelper_number, block_id) VALUES 
('Mary', 'Part-time', '1234-5678-9012', '111-222-3333', 1),
('David', 'Full-time', '9876-5432-1098', '444-555-6666', 2),
('Sarah', 'Part-time', '5678-9012-3456', '777-888-9999', 3);



-- Create Trigger to Update Househelp Availability
DELIMITER //

CREATE TRIGGER UpdateHousehelpAvailability
AFTER INSERT ON Complaints
FOR EACH ROW
BEGIN
    UPDATE Househelp
    SET availability = 'Unavailable'
    WHERE block_id IN (
        SELECT block_id
        FROM Residents
        WHERE resident_id = NEW.resident_id
    );
END;
//


DELIMITER ;