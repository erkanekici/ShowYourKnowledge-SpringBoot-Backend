INSERT INTO gosterbilgini.user_info (created_time,deleted,activity,email,user_password,user_name,wrong_entry)
SELECT '2011-12-18 13:17:17',false,1,'email','123','Erkan',1
WHERE NOT EXISTS (SELECT * FROM gosterbilgini.user_info WHERE email = 'email' and user_password = '123' LIMIT 1);