cd $WORKDIR

function stopServer(){
	LOKER=.lock
	if [ -f $LOKER ]; then
		. $LOKER
		
		kill $PID
		
		echo "正在关闭服务器...如果关闭之前有客户端连接，可能时间会稍长"
		for i in {1..100} 
		do
			OCCUPIED=`netstat -an|grep $PORT`
			if [ -z "$OCCUPIED" ]; then
				break
			fi
			sleep 1	
		done
		echo "关闭成功"
	fi
}

function startServer(){
    nohup java -server -jar ${SERVER_NAME}-0.0.1-SNAPSHOT.jar > /dev/null &
}

function restartServer(){
    stopServer
    startServer
}

case $1 in
	"start") startServer;;
	"stop") stopServer;;
	"restart") restartServer;;
	*) echo "无效的输入";;
esac
