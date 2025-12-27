-- Sample USSD Menus
INSERT INTO USSD_MENU VALUES
(1, 'WELCOME', 'Good {GREETING} {FIRST_NAME}, welcome to Entity Bank.Enter PIN\n(Forgot PIN reply with 1)', NULL, NULL, 'MAIN', 'N', 'N', 'authAction','OPTION',NULL);

INSERT INTO USSD_MENU VALUES
(2, 'MAIN', '\n1. Balance\n2. Send Money\n0. Exit', 'WELCOME', NULL, NULL, 'N', 'Y', NULL,'OPTION',NULL);


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

INSERT INTO USSD_MENU VALUES
(100, 'NON_CUSTOMER',
 'Welcome to Entity Bank.This number is not registered.\nPlease visit a branch to register.\n0. Exit',
 NULL, NULL, NULL, 'Y', 'N', 'exitAction', NULL, NULL);

 -- Step 1: Enter secret word
 INSERT INTO USSD_MENU VALUES
 (200, 'RESET_SECRET',
  'Enter your secret word',
  'WELCOME', '1', 'RESET_NEW_PIN',
  'N', 'N', 'resetSecretAction', 'TEXT', 'secret_word_input');

 -- Step 2: Enter new PIN
 INSERT INTO USSD_MENU VALUES
 (201, 'RESET_NEW_PIN',
  'Enter new 4-digit PIN',
  'RESET_SECRET', NULL, 'RESET_CONFIRM_PIN',
  'N', 'N', NULL, 'PIN', 'new_pin');

 -- Step 3: Confirm new PIN
 INSERT INTO USSD_MENU VALUES
 (202, 'RESET_CONFIRM_PIN',
  'Re-enter new PIN',
  'RESET_NEW_PIN', NULL, NULL,
  'N', 'N', 'resetConfirmPinAction', 'PIN', 'confirm_pin');

 -- Step 4: Success
 INSERT INTO USSD_MENU VALUES
 (203, 'RESET_SUCCESS',
  'PIN reset successful.\nPlease login with your new PIN.',
  NULL, NULL, 'WELCOME',
  'N', 'N', NULL, NULL, NULL);


-- Sample Customer
INSERT INTO USSD_CUSTOMER VALUES
('254712345678', '{bcrypt}$2a$10$kE9W5p3r1jRZrYp9oHn8eO0Z3s4zJx8FZz6p0', 0, 'N','Albert','Andrew','Baraka','1234','blue');
-- PIN: 1234
