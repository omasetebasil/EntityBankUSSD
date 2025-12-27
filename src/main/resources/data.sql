INSERT INTO USSD_SCREEN VALUES
('WELCOME',
 'Good {GREETING} {FIRST_NAME}, welcome to Entity Bank\nEnter PIN\n(Forgot PIN reply with 1)',
 'OPTION', NULL, 'authAction', 'N', 'N');

INSERT INTO USSD_SCREEN VALUES
('MAIN',
 '1. Balance\n2. Send Money\n0. Exit',
 'OPTION', NULL, NULL, 'Y', 'N');

INSERT INTO USSD_TRANSITION VALUES
(1, 'WELCOME', '1', 'RESET_SECRET', 1),
(2, 'WELCOME', '*', 'WELCOME', 0),

(3, 'MAIN', '1', 'BAL', 1),
(4, 'MAIN', '2', 'SEND_RECIPIENT', 1),
(5, 'MAIN', '0', 'EXIT', 1);

INSERT INTO USSD_SCREEN VALUES
('BAL',
 NULL,
 'NONE', NULL, 'balanceAction', 'Y', 'N');

INSERT INTO USSD_TRANSITION VALUES
(10, 'BAL', '0', 'EXIT', 1);

INSERT INTO USSD_SCREEN VALUES
('SEND_RECIPIENT', 'Enter recipient number',
 'NUMBER', 'recipient', NULL, 'Y', 'N');

INSERT INTO USSD_SCREEN VALUES
('SEND_AMOUNT', 'Enter amount to send',
 'NUMBER', 'amount', NULL, 'Y', 'N');

INSERT INTO USSD_SCREEN VALUES
('SEND_CONFIRM', NULL,
 'NONE', NULL, 'sendMoneyConfirmAction', 'Y', 'N');

INSERT INTO USSD_SCREEN VALUES
('SEND_POST', NULL,
 'CONFIRM', NULL, 'sendMoneyPostAction', 'Y', 'N');

INSERT INTO USSD_SCREEN VALUES
('SEND_SUCCESS', 'Transaction successful\n0. Exit',
 'OPTION', NULL, NULL, 'Y', 'N');

INSERT INTO USSD_TRANSITION VALUES
(20, 'SEND_RECIPIENT', '*', 'SEND_AMOUNT', 0),
(21, 'SEND_AMOUNT', '*', 'SEND_CONFIRM', 0),
(22, 'SEND_CONFIRM', '*', 'SEND_POST', 0),
(23, 'SEND_POST', '1', 'SEND_SUCCESS', 1),
(24, 'SEND_POST', '0', 'EXIT', 1),
(25, 'SEND_SUCCESS', '0', 'EXIT', 1);

INSERT INTO USSD_TRANSITION VALUES
(100, 'RESET_NEW_PIN', '*', 'RESET_CONFIRM_PIN', 0);


INSERT INTO USSD_SCREEN VALUES
('RESET_SECRET', 'Enter your secret word',
 'TEXT', 'secret_word_input', 'resetSecretAction', 'N', 'N');

INSERT INTO USSD_SCREEN VALUES
('RESET_NEW_PIN', 'Enter new 4-digit PIN',
 'PIN', 'new_pin', NULL, 'N', 'N');

INSERT INTO USSD_SCREEN VALUES
('RESET_CONFIRM_PIN', 'Re-enter new PIN',
 'PIN', 'confirm_pin', 'resetConfirmPinAction', 'N', 'N');


INSERT INTO USSD_SCREEN VALUES
('EXIT', 'Thank you for banking with Entity',
 'NONE', NULL, 'exitAction', 'N', 'Y');

INSERT INTO USSD_SCREEN VALUES
('NON_CUSTOMER',
 'This number is not registered.\nVisit a branch.\n0. Exit',
 'OPTION', NULL, 'exitAction', 'N', 'Y');



-- Sample Customer
INSERT INTO USSD_CUSTOMER VALUES
('254712345678', '{bcrypt}$2a$10$kE9W5p3r1jRZrYp9oHn8eO0Z3s4zJx8FZz6p0', 0, 'N','Albert','Andrew','Baraka','1234','blue');
-- PIN: 1234
