insert into gosterbilgini.topic (id,created_time,deleted,user_id_1,user_id_2)
SELECT 'id',SYSDATE(),false,1,2
WHERE NOT EXISTS (SELECT * FROM gosterbilgini.topic WHERE user_id_1 = 1 and user_id_2 = 2 LIMIT 1);
