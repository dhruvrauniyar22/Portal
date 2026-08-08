-- SQL seed for complaints table
-- Run on MySQL server connected to your application database

CREATE TABLE IF NOT EXISTS complaints (
  id INT AUTO_INCREMENT PRIMARY KEY,
  complaint_id VARCHAR(32) NOT NULL UNIQUE,
  room VARCHAR(16) NOT NULL,
  category VARCHAR(128) NOT NULL,
  priority VARCHAR(16) NOT NULL,
  status VARCHAR(32) NOT NULL
);

INSERT INTO complaints (complaint_id, room, category, priority, status) VALUES
('HP-1024', 'A-16', 'Electrical', 'High', 'Pending'),
('HP-1019', 'A-16', 'Plumbing', 'Medium', 'In Progress'),
('HP-1008', 'A-16', 'Cleaning/Housekeeping', 'Low', 'Resolved'),
('HP-0996', 'A-16', 'WiFi', 'High', 'Rejected'),
('HP-0988', 'A-16', 'Furniture', 'Medium', 'Resolved'),
('HP-0977', 'A-16', 'Mess', 'Low', 'Pending');
