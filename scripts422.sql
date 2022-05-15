CREATE TABLE persons (
    id REAL,
    name TEXT UNIQUE NOT NULL PRIMARY KEY,
    age SMALLSERIAL CHECK (age > 0),
    hasDriverLicense BOOLEAN,
    carInUse car_id
)

CREATE TABLE cars (
    id INTEGER PRIMARY KEY,
    owner name NOT NULL,
    brand TEXT,
    color TEXT,
    price INTEGER CHECK (price > 0),
    licensePlate TEXT UNIQUE NOT NULL
)