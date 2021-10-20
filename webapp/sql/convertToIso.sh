DB="marczak"
(
    echo 'ALTER DATABASE `'"$DB"'` CHARACTER SET utf8 COLLATE utf8_general_ci;' | mysql -uroot -pdupa98
    mysql -uroot -pdupa98 "$DB" -e "SHOW TABLES" --batch --skip-column-names \
    | xargs -I{} echo 'ALTER TABLE `'{}'` CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;'
) \
| mysql -uroot -pdupa98 "$DB"
