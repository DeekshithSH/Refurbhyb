CREATE TABLE User (
    uid VARCHAR(36) PRIMARY KEY, -- UUID v4
    email VARCHAR(50),
    name VARCHAR(50),
    phoneNo VARCHAR(15),
    password VARCHAR(255),
    address VARCHAR(255)
);

CREATE TABLE Category (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE
);

CREATE TABLE Conditions (
    `condition` CHAR(1) PRIMARY KEY  -- [A, B, C, D ...]
);

CREATE TABLE Item (
    id VARCHAR(36) PRIMARY KEY,  -- UUID v4
    name VARCHAR(50),
    model VARCHAR(50),
    category_id INTEGER,
    description VARCHAR(255),
    price INT,
    warranty DATETIME,
    `condition` CHAR,
    seller VARCHAR(36),
    quantity INT,
    FOREIGN KEY (category_id) REFERENCES Category(id) ON DELETE CASCADE,
    FOREIGN KEY (`condition`) REFERENCES Conditions(`condition`) ON DELETE CASCADE,
    FOREIGN KEY (seller) REFERENCES User(uid) ON DELETE CASCADE
);

CREATE TABLE Cart (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    uid VARCHAR(36),
    item_id VARCHAR(36),
    quantity INTEGER DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (uid) REFERENCES User(uid) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES Item(id) ON DELETE CASCADE
);

-- Seller will Update status like if payment successfull, shipped
-- Shipping Partner, Tracking Id,
CREATE TABLE `Order` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    uid VARCHAR(36), -- ID of user who bought the item
    item_id VARCHAR(36), -- id from Item table
    quantity INT DEFAULT 1, -- No of items
    transaction_id VARCHAR(255),
    payment_success TINYINT DEFAULT 0, -- if customer done the payment
    shipped TINYINT DEFAULT 0, -- if item is shipped(1) or not (0)
    tracking_id VARCHAR(255) DEFAULT NULL, -- id to track shipment
    shipping_partner VARCHAR(255) DEFAULT NULL, -- courier company
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (uid) REFERENCES User(uid) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES Item(id) ON DELETE CASCADE
);

CREATE TABLE Session (
    sid VARCHAR(36) PRIMARY KEY,
    uid VARCHAR(36),
    FOREIGN KEY (uid) REFERENCES User(uid) ON DELETE CASCADE
);

CREATE TABLE Files (
    id VARCHAR(36) PRIMARY KEY,
    item_id VARCHAR(36),
    file LONGBLOB NOT NULL,
    FOREIGN KEY (item_id) REFERENCES Item(id) ON DELETE CASCADE
);

CREATE TABLE Metadata(
    `key` VARCHAR(50) PRIMARY KEY,
    `value` VARCHAR(50)
); -- Configuration KV

INSERT INTO Metadata VALUES("Version", "0.0.1");
