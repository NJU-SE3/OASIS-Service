container_name = csv_mysql
psw				= mysql
csv_target		= se
file_path		= framework.sql

schema2sql:
	docker exec -i $(container_name) mysqldump -uroot -p$(psw) $(csv_target) > $(file_path)

sql2schema:
	docker exec -i $(container_name) mysql -uroot -p$(psw) $(csv_target) < $(file_path)