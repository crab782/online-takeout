USE takeout_system;
UPDATE employee SET password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi' WHERE username = 'shop_admin1';
SELECT id, username, password, status FROM employee WHERE username='shop_admin1';
