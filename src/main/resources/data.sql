-- Sample USSD Menus
INSERT INTO USSD_MENU VALUES
(1, 'WELCOME', 'Good morning Basil, welcome to Entity Bank\nEnter PIN\n(Forgot PIN reply with 1)', NULL, NULL, 'MAIN', 'N', 'N', 'authAction','PIN',NULL);

INSERT INTO USSD_MENU VALUES
(2, 'MAIN', '1. Balance\n2. Send Money\n0. Exit', 'WELCOME', NULL, NULL, 'N', 'Y', NULL,'OPTION',NULL);


-- Balance menu (with action)
INSERT INTO USSD_MENU VALUES
(3, 'BAL', NULL, 'MAIN', '1', NULL, 'N', 'Y', 'balanceAction','OPTION',NULL);



INSERT INTO USSD_MENU VALUES
(6, 'EXIT', 'Thank you for banking with Entity', 'MAIN', '0', NULL, 'Y', 'N', 'exitAction',NULL,NULL);


-- Step 1: Enter recipient
INSERT INTO USSD_MENU VALUES
(10, 'SEND_MONEY', 'Enter recipient number', 'MAIN', '2', 'SEND_AMOUNT', 'N', 'Y', NULL,'NUMBER','recipient');

-- Step 2: Enter amount
INSERT INTO USSD_MENU VALUES
(11, 'SEND_AMOUNT', 'Enter amount to send', 'SEND_MONEY', NULL, 'SEND_CONFIRM', 'N', 'Y', NULL,'NUMBER','amount');

-- Step 3: Confirm details (action)
INSERT INTO USSD_MENU VALUES
(12, 'SEND_CONFIRM', NULL, 'SEND_AMOUNT', NULL, 'SEND_POST', 'N', 'Y', 'sendMoneyConfirmAction','CONFIRM',NULL);

-- Step 4: Post transaction
INSERT INTO USSD_MENU VALUES
(13, 'SEND_POST', NULL, 'SEND_CONFIRM', '1', 'SEND_SUCCESS', 'N', 'Y', 'sendMoneyPostAction','CONFIRM',NULL);

-- Step 5: Success
INSERT INTO USSD_MENU VALUES
(15, 'SEND_SUCCESS', 'Transaction successful\n0. Exit', 'SEND_POST', NULL, NULL, 'N', 'Y', NULL,NULL,NULL);

INSERT INTO USSD_MENU VALUES
(16, 'SEND_EXIT', 'Thank you for banking with Entity', 'SEND_SUCCESS', '0', NULL, 'Y', 'N', 'exitAction',NULL,NULL);


-- Sample Customer
INSERT INTO USSD_CUSTOMER VALUES
('254712345678', '{bcrypt}$2a$10$kE9W5p3r1jRZrYp9oHn8eO0Z3s4zJx8FZz6p0', 0, 'N','Albert','Andrew','Baraka','1234');
-- PIN: 1234
