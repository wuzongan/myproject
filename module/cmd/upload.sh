#!/bin/bash

cd ../cmd/
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

m_tag2="1.1.12"

echo "准备上传文件..."
ssh -oPort=22 root@123.59.13.158

mkdir /data/ftp/update2/$m_tag2

put $DIR//hot_update/publish/$m_tag2 /data/ftp/update2/$m_tag2


