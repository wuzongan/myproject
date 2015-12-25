#!/bin/bash
# 参数说明 1 包名字 2 目录（相对脚本） 3 包里的图片名列表 没有拓展名 图片是png格式
function Pack {
local FORMATE=RGBA4444
local DitherType=FloydSteinbergAlpha
local NAME=$1 
local DIR=$2/
if [ -f $DIR$NAME.tps ]; then 
rm  $DIR$NAME.tps
fi   
local fileList=($*)  
fileList=(${fileList[@]:2})
if [ ${#fileList[@]} -le 0 ];then
  return
fi                                                                                                                                    
echo    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>                                        
        <data version=\"1.0\">                                                         
        <struct type=\"Settings\">                                                 
            <key>fileFormatVersion</key>                                         
            <int>1</int>                                                         
            <key>variation</key>                                                 
            <string>main</string>                                                
            <key>verbose</key>                                                   
            <false/>                                                             
            <key>autoSDSettings</key>                                            
            <array/>                                                             
            <key>allowRotation</key>                                             
            <false/>                                                             
            <key>quiet</key>                                                     
            <false/>                                                             
            <key>premultiplyAlpha</key>                                          
            <false/>                                                              
            <key>shapeDebug</key>                                                
            <false/>                                                             
            <key>dpi</key>                                                       
            <uint>72</uint>                                                      
            <key>dataFormat</key>                                                
            <string>cocos2d</string>                                             
            <key>textureFileName</key>                                           
            <filename>$NAME.pvr.ccz</filename>                                   
            <key>flipPVR</key>                                                   
            <false/>                                                             
            <key>ditherType</key>                                                
            <enum type=\"SettingsBase::DitherType\">${DitherType}</enum>        
            <key>backgroundColor</key>                                           
            <uint>0</uint>                                                       
            <key>libGdx</key>                                                    
            <struct type=\"LibGDX\">                                               
                <key>filtering</key>                                             
                <struct type=\"LibGDXFiltering\">                                  
                    <key>x</key>                                                 
                    <enum type=\"LibGDXFiltering::Filtering\">Linear</enum>        
                    <key>y</key>                                                 
                    <enum type=\"LibGDXFiltering::Filtering\">Linear</enum>        
                </struct>                                                        
            </struct>                                                            
            <key>shapePadding</key>                                              
            <uint>2</uint>                                                       
            <key>jpgQuality</key>                                                
            <uint>80</uint>                                                      
            <key>pngOptimizationLevel</key>                                      
            <uint>0</uint>                                                       
            <key>textureSubPath</key>                                            
            <string></string>                                                    
            <key>textureFormat</key>                                             
            <enum type=\"SettingsBase::TextureFormat\">pvr2ccz</enum>              
            <key>borderPadding</key>                                             
            <uint>2</uint>                                                       
            <key>maxTextureSize</key>                                            
            <QSize>                                                              
                <key>width</key>                                                 
                <int>1024</int>                                                  
                <key>height</key>                                                
                <int>1024</int>                                                  
            </QSize>                                                             
            <key>fixedTextureSize</key>                                          
            <QSize>                                                              
                <key>width</key>                                                 
                <int>-1</int>                                                    
                <key>height</key>                                                
                <int>-1</int>                                                    
            </QSize>                                                             
            <key>reduceBorderArtifacts</key>                                     
            <false/>                                                             
            <key>algorithmSettings</key>                                         
            <struct type=\"AlgorithmSettings\">                                    
                <key>algorithm</key>                                             
                <enum type=\"AlgorithmSettings::AlgorithmId\">MaxRects</enum>      
                <key>freeSizeMode</key>                                          
                <enum type=\"AlgorithmSettings::AlgorithmFreeSizeMode\">Best</enum>
                <key>sizeConstraints</key>                                       
                <enum type=\"AlgorithmSettings::SizeConstraints\">NPOT</enum>       
                <key>forceSquared</key>                                          
                <false/>                                                         
                <key>forceWordAligned</key>                                      
                <false/>                                                         
                <key>maxRects</key>                                              
                <struct type=\"AlgorithmMaxRectsSettings\">                        
                    <key>heuristic</key>                                         
                    <enum type=\"AlgorithmMaxRectsSettings::Heuristic\">Best</enum>
                </struct>                                                        
                <key>basic</key>                                                 
                <struct type=\"AlgorithmBasicSettings\">                           
                    <key>sortBy</key>                                            
                    <enum type=\"AlgorithmBasicSettings::SortBy\">Best</enum>      
                    <key>order</key>                                             
                    <enum type=\"AlgorithmBasicSettings::Order\">Ascending</enum>  
                </struct>                                                        
            </struct>                                                            
            <key>andEngine</key>                                                 
            <struct type=\"AndEngine\">                                            
                <key>minFilter</key>                                             
                <enum type=\"AndEngine::MinFilter\">Linear</enum>                  
                <key>packageName</key>                                           
                <string>Texture</string>                                         
                <key>javaFileName</key>                                          
                <filename>$NAME.java</filename>                                  
                <key>wrap</key>                                                  
                <struct type=\"AndEngineWrap\">                                    
                    <key>s</key>                                                 
                    <enum type=\"AndEngineWrap::Wrap\">Clamp</enum>                
                    <key>t</key>                                                 
                    <enum type=\"AndEngineWrap::Wrap\">Clamp</enum>                
                </struct>                                                        
                <key>magFilter</key>                                             
                <enum type=\"AndEngine::MagFilter\">MagLinear</enum>               
            </struct>                                                            
            <key>dataFileName</key>                                              
            <filename>$NAME.plist</filename>                                     
            <key>multiPack</key>                                                 
            <true/>                                                             
            <key>mainExtension</key>                                             
            <string></string>                                                    
            <key>forceIdenticalLayout</key>                                      
            <false/>                                                             
            <key>outputFormat</key>                                              
            <enum type=\"SettingsBase::OutputFormat\">$FORMATE</enum>              
            <key>contentProtection</key>                                         
            <struct type=\"ContentProtection\">                                    
                <key>key</key>                                                   
                <string></string>                                                
            </struct>                                                            
            <key>autoAliasEnabled</key>                                          
            <true/>                                                              
            <key>trimSpriteNames</key>                                           
            <false/>                                                             
            <key>globalSpriteSettings</key>                                      
            <struct type=\"SpriteSettings\">                                       
                <key>scale</key>                                                 
                <double>1</double>                                               
                <key>scaleMode</key>                                             
                <enum type=\"ScaleMode\">Smooth</enum>                             
                <key>innerPadding</key>                                          
                <uint>0</uint>                                                   
                <key>extrude</key>                                               
                <uint>2</uint>                                                   
                <key>trimThreshold</key>                                         
                <uint>1</uint>                                                   
                <key>trimMode</key>                                              
                <enum type=\"SpriteSettings::TrimMode\">None</enum>                
                <key>heuristicMask</key>                                         
                <false/>                                                         
            </struct>                                                            
            <key>fileList</key>                                                  
            <array>  "      >> $DIR$NAME.tps  
            for file in ${fileList[@]};do
echo            "<filename>$file.png</filename>  " >> $DIR$NAME.tps
            done
echo           " </array>                                                             
            <key>ignoreFileList</key>                                            
            <array/>                                                             
            <key>replaceList</key>                                               
            <array/>                                                             
            <key>ignoredWarnings</key>                                           
            <array/>                                                             
            <key>commonDivisorX</key>                                            
            <uint>1</uint>                                                       
            <key>commonDivisorY</key>                                            
            <uint>1</uint>                                                       
        </struct>                                                                
    </data>            " >> $DIR$NAME.tps                                                          
}                   

function PackDir {
    local DIR=$1
    declare -a filelist
    # declare -a filesize
    for item in `ls $DIR` ;do
        if [ -f $DIR/$item ]  ;then
            if [ "${item:0,-4}" == ".png" ] ;then
                # Pack ${item%.png} $DIR
                # texturepacker $DIR/${item%.png}.tps
                # rm $DIR/${item%.png}.tps
                filelist[${#filelist[@]}]=${item%.png}
                # filesize[${#filesize[@]}]=`ls -l $DIR/$item|awk '{print $5}'`
            fi
        elif [ -d $DIR/$item ]; then
            local NEXTDIR=$DIR/$item
            PackDir $NEXTDIR
        fi
    done 
    # 文件大于500 000B 单独打包
   # for i in `seq 0 1 $((${#filelist[@]} -1 ))`;do
   #     if [ ${filesize[$i]} -ge 500000 ] ;then
   #          Pack ${filelist[$i]} $DIR ${filelist[$i]}
   #          unset filelist[$i]
   #          unset filesize[$i]
   #     fi
   # done
   # 
   local NAME=${DIR##*/}
   Pack  $NAME\{n\} $DIR ${filelist[*]}
   if [ -f $DIR/$NAME\{n\}.tps ]; then 
       texturepacker  $DIR/$NAME\{n\}.tps
       rm $DIR/$NAME\{n\}.tps
    fi  
  
   # rm $DIR/$NAME\{n\}.tps
}  

echo off  
echo 正在打包 ...
PackDir  `pwd`
echo 打包完毕

echo  on                                                               