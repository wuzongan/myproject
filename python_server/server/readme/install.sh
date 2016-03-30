function checkOSType()
{
    type "yum" > /dev/null 2> /dev/null
    if [ $? -eq 0 ]; then
        echo "CENTOS"
        exit 0
    fi

    echo "UNKNOW"
    exit 1
} 

OSTYPE=$(checkOSType)
CUR_DIR=$(cd "$(dirname $0)" && pwd)
PRO_DIR=/data
BUILD_DIR=/data/soft
if [ $OSTYPE != "CENTOS" ]; then
	echo "Unsupport current OS."
    exit 1
fi

rm -rf $PRO_DIR
mkdir -p $PRO_DIR
mkdir -p $BUILD_DIR

cd $BUILD_DIR

#安装依赖包
yum clean dbcache
yum groupinstall -y "Development tools"
yum install -y zlib-devel bzip2-devel ncurses-devel openssl-devel make gcc-c++ cmake bison-devel swig patch sqlite-devel readline readline-devel mysql-devel tcl mysql-proxy  gcc swig python-devel autoconf libtool mysql sqlite httpd
yum update -y
#安装 Python 2.7和pip2.7
wget https://www.python.org/ftp/python/2.7.8/Python-2.7.8.tgz
tar xf Python-2.7.8.tgz
cd Python-2.7.8 
./configure --prefix=/usr/local 
make && make install

cd $BUILD_DIR

wget https://bitbucket.org/pypa/setuptools/raw/bootstrap/ez_setup.py
python2.7 ez_setup.py
easy_install-2.7 pip

#安装 nginx
wget http://nginx.org/packages/centos/6/noarch/RPMS/nginx-release-centos-6-0.el6.ngx.noarch.rpm
yum install -y ./nginx-release-centos-6-0.el6.ngx.noarch.rpm nginx

#安装 redis 及其相关
wget http://download.redis.io/releases/redis-2.8.13.tar.gz
tar -xzvf redis-2.8.13.tar.gz
cd redis-2.8.13
make
make test
cd src && make install

cd $BUILD_DIR

#安装python支持库
pip2.7 install pysqlite M2Crypto backports.ssl_match_hostname greenlet openpyxl==1.7.0 readline MySQL-python tornado gevent psutil supervisor redis