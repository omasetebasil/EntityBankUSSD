-- Sample USSD Menus
INSERT INTO USSD_MENU VALUES
(1, 'WELCOME', 'Good morning Basil, welcome to Entity Bank\nEnter PIN\n(Forgot PIN reply with 1)', NULL, NULL, 'MAIN', 'N', 'N', 'authAction');

INSERT INTO USSD_MENU VALUES
(2, 'MAIN', '1. Balance\n2. Send Money\n3. Change PIN\n0. Exit', 'WELCOME', NULL, NULL, 'N', 'Y', NULL);

INSERT INTO USSD_MENU VALUES
(3, 'BAL', 'Checking your balance...', 'MAIN', '1', NULL, 'N', 'Y', 'balanceAction');

INSERT INTO USSD_MENU VALUES
(4, 'SEND_MONEY', 'Enter recipient number', 'MAIN', '2', NULL, 'N', 'Y', 'transferAction');

INSERT INTO USSD_MENU VALUES
(5, 'PIN_CHANGE', 'Enter old PIN', 'MAIN', '3', NULL, 'N', 'Y', 'pinChangeAction');

INSERT INTO USSD_MENU VALUES
(6, 'EXIT', 'Thank you for banking with Entity', 'MAIN', '0', NULL, 'Y', 'N', 'exitAction');

-- Sample Customer
INSERT INTO USSD_CUSTOMER VALUES
('254712345678', '{bcrypt}$2a$10$kE9W5p3r1jRZrYp9oHn8eO0Z3s4zJx8FZz6p0', 0, 'N');
-- PIN: 1234
