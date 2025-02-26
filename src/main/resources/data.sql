-- INITIAL SAMPLE DATA FOR DRONE TABLE
INSERT INTO drone (serial_number, model, weight_limit, battery_capacity, state) VALUES ('A1234567890ABC', 'LIGHTWEIGHT', 250, 100, 'IDLE');
INSERT INTO drone (serial_number, model, weight_limit, battery_capacity, state) VALUES ('A1234567890DEF', 'LIGHTWEIGHT', 250, 100, 'IDLE');
INSERT INTO drone (serial_number, model, weight_limit, battery_capacity, state) VALUES ('A1234567890GHI', 'MIDDLEWEIGHT', 500, 100, 'IDLE');
INSERT INTO drone (serial_number, model, weight_limit, battery_capacity, state) VALUES ('A1234567890JKL', 'MIDDLEWEIGHT', 500, 100, 'IDLE');
INSERT INTO drone (serial_number, model, weight_limit, battery_capacity, state) VALUES ('A1234567890MNO', 'CRUISERWEIGHT', 750, 100, 'IDLE');
INSERT INTO drone (serial_number, model, weight_limit, battery_capacity, state) VALUES ('A1234567890PQR', 'CRUISERWEIGHT', 750, 100, 'IDLE');
INSERT INTO drone (serial_number, model, weight_limit, battery_capacity, state) VALUES ('A1234567890STU', 'CRUISERWEIGHT', 750, 100, 'IDLE');
INSERT INTO drone (serial_number, model, weight_limit, battery_capacity, state) VALUES ('A1234567890VWX', 'HEAVYWEIGHT', 1000, 100, 'IDLE');
INSERT INTO drone (serial_number, model, weight_limit, battery_capacity, state) VALUES ('A1234567890YZA', 'HEAVYWEIGHT', 1000, 100, 'IDLE');
INSERT INTO drone (serial_number, model, weight_limit, battery_capacity, state) VALUES ('A1234567890BCD', 'HEAVYWEIGHT', 1000, 100, 'IDLE');

-- INITIAL SAMPLE DATA FOR MEDICATION TABLE
INSERT INTO medication (name, weight, code, drone_id) VALUES ('PARACETAMOL', 100, 'N02BE01', '1');