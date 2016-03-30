#!/bin/bash

rm -f ./hot_update/filelist.xml
rm -f ./hot_update/versionList
rm -fr ./hot_update/publish

cd ../qishituan_client/
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

#选择查看的版本
tag_search_name=1.0.*

build(){
	cd $DIR
    git checkout $1
	cd $DIR/../cmd/hot_update/
	python publish.py $1
}

check(){
	case $1 in
	*.*.*)
	    ;;
	*)
	    echo "===========" ‘$1’ "输入格式错误！==========="
	    exit 0
	esac
}

#修改游戏的版本号
push_version(){
	filename=$DIR/cb_lua_v001/scripts/update/resinfo.lua
	echo $filename
	sed -i "" "/CLIENT_VERSION/ s/\(.*=\).*/\1\"$m_tag2\"/" $filename
	
	# cd $DIR
	# git commit -a -m "version $m_tag2"
	# git push
	# git tag $m_tag2
}

echo "======" $tag_search_name "的历史版本号=======" 
# git tag 
git tag -l $tag_search_name 
echo -e '\n'
read -p "请输入‘前 后’两个版本号,用空格分开（如：1.1.0 1.1.1）:" m_tag1 m_tag2 \n


push_version


check $m_tag1
check $m_tag2

build $m_tag1
build $m_tag2

cd $DIR 
git checkout master

echo "======打包成功！" $m_tag2 " 生成到:publish/$m_tag2/=======" 

fab deploy:$m_tag2

