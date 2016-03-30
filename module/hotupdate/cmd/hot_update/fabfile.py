#!/usr/bin/env python

from fabric.api import *

env.hosts = '123.59.13.158'
env.user = 'root'
env.password = '49App@klwjelt'

def deploy(version):
    file_zip = 'update.zip'
    file_config = 'config.lua'
    local_path = 'publish/'+version+'/'+file_zip
    remote_path = '/data/ftp/update2/'
    remote_version_path = remote_path+version
    
    run('rm -rf %s' % remote_version_path)
    run('mkdir %s' % remote_version_path)
    #upload
    with cd(remote_path):  
        with settings(warn_only=True):  
            result = put(file_config,remote_path+file_config)
            result1 = put(local_path,remote_version_path+'/'+file_zip)
        if result.failed or result1.failed:  
            print "===========upload failed!======="
        else:
            print "===========upload success!======="


if __name__ == '__main__':
    execute(deploy,"1.0.3")