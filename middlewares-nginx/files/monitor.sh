#!/bin/bash
./inotify/bin/inotifywait -mrq --timefmt '%Y-%m-%d %H%M%S' --format '%T %w%f%e' -e close_write,modify,create,delete,attrib,move ///usr/local/openresty/nginx/html/ | while read file 
do
         rsync -avz --delete --password-file=/etc/rsync.pwd.client /usr/local/openresty/nginx/html/ chendom@192.168.0.189::ftp/
done
