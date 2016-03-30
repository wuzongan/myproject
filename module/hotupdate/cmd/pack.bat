@echo off
set name=%~n1
set dir=%~dp1
if exist %dir%%name%.tps del %dir%%name%.tps
echo ^<?xml version="1.0" encoding="UTF-8"?^>                                         >>%dir%%name%.tps
echo ^<data version="1.0"^>                                                           >>%dir%%name%.tps
echo     ^<struct type="Settings"^>                                                   >>%dir%%name%.tps
echo         ^<key^>fileFormatVersion^</key^>                                           >>%dir%%name%.tps
echo         ^<int^>3^</int^>                                                           >>%dir%%name%.tps
echo         ^<key^>texturePackerVersion^</key^>                                        >>%dir%%name%.tps
echo         ^<string^>3.2.1^</string^>                                                 >>%dir%%name%.tps
echo         ^<key^>autoSDSettings^</key^>                                              >>%dir%%name%.tps
echo         ^<array^>                                                                >>%dir%%name%.tps
echo             ^<struct type="AutoSDSettings"^>                                     >>%dir%%name%.tps
echo                 ^<key^>scale^</key^>                                               >>%dir%%name%.tps
echo                 ^<double^>1^</double^>                                             >>%dir%%name%.tps
echo                 ^<key^>extension^</key^>                                           >>%dir%%name%.tps
echo                 ^<string^>^</string^>                                              >>%dir%%name%.tps
echo                 ^<key^>spriteFilter^</key^>                                        >>%dir%%name%.tps
echo                 ^<string^>^</string^>                                              >>%dir%%name%.tps
echo                 ^<key^>acceptFractionalValues^</key^>                              >>%dir%%name%.tps
echo                 ^<false/^>                                                       >>%dir%%name%.tps
echo                 ^<key^>maxTextureSize^</key^>                                      >>%dir%%name%.tps
echo                 ^<QSize^>                                                        >>%dir%%name%.tps
echo                     ^<key^>width^</key^>                                           >>%dir%%name%.tps
echo                     ^<int^>-1^</int^>                                              >>%dir%%name%.tps
echo                     ^<key^>height^</key^>                                          >>%dir%%name%.tps
echo                     ^<int^>-1^</int^>                                              >>%dir%%name%.tps
echo                 ^</QSize^>                                                       >>%dir%%name%.tps
echo             ^</struct^>                                                          >>%dir%%name%.tps
echo         ^</array^>                                                               >>%dir%%name%.tps
echo         ^<key^>allowRotation^</key^>                                               >>%dir%%name%.tps
echo         ^<false/^>                                                                >>%dir%%name%.tps
echo         ^<key^>premultiplyAlpha^</key^>                                            >>%dir%%name%.tps
echo         ^<true/^>                                                               >>%dir%%name%.tps
echo         ^<key^>shapeDebug^</key^>                                                  >>%dir%%name%.tps
echo         ^<false/^>                                                               >>%dir%%name%.tps
echo         ^<key^>dpi^</key^>                                                         >>%dir%%name%.tps
echo         ^<uint^>72^</uint^>                                                        >>%dir%%name%.tps
echo         ^<key^>dataFormat^</key^>                                                  >>%dir%%name%.tps
echo         ^<string^>cocos2d^</string^>                                               >>%dir%%name%.tps
echo         ^<key^>textureFileName^</key^>                                             >>%dir%%name%.tps
echo         ^<filename^>%name%.pvr.ccz%ppoeieieueyhryhrehreh%^</filename^>                                >>%dir%%name%.tps
echo         ^<key^>flipPVR^</key^>                                                     >>%dir%%name%.tps
echo         ^<false/^>                                                               >>%dir%%name%.tps
echo         ^<key^>ditherType^</key^>                                                  >>%dir%%name%.tps
echo         ^<enum type="SettingsBase::DitherType"^>NearestNeighbour^</enum^>          >>%dir%%name%.tps
echo         ^<key^>backgroundColor^</key^>                                             >>%dir%%name%.tps
echo         ^<uint^>0^</uint^>                                                         >>%dir%%name%.tps
echo         ^<key^>libGdx^</key^>                                                      >>%dir%%name%.tps
echo         ^<struct type="LibGDX"^>                                                 >>%dir%%name%.tps
echo             ^<key^>filtering^</key^>                                               >>%dir%%name%.tps
echo             ^<struct type="LibGDXFiltering"^>                                    >>%dir%%name%.tps
echo                 ^<key^>x^</key^>                                                   >>%dir%%name%.tps
echo                 ^<enum type="LibGDXFiltering::Filtering"^>Linear^</enum^>          >>%dir%%name%.tps
echo                 ^<key^>y^</key^>                                                   >>%dir%%name%.tps
echo                 ^<enum type="LibGDXFiltering::Filtering"^>Linear^</enum^>          >>%dir%%name%.tps
echo             ^</struct^>                                                          >>%dir%%name%.tps
echo         ^</struct^>                                                              >>%dir%%name%.tps
echo         ^<key^>shapePadding^</key^>                                                >>%dir%%name%.tps
echo         ^<uint^>0^</uint^>                                                         >>%dir%%name%.tps
echo         ^<key^>jpgQuality^</key^>                                                  >>%dir%%name%.tps
echo         ^<uint^>80^</uint^>                                                        >>%dir%%name%.tps
echo         ^<key^>pngOptimizationLevel^</key^>                                        >>%dir%%name%.tps
echo         ^<uint^>0^</uint^>                                                         >>%dir%%name%.tps
echo         ^<key^>textureSubPath^</key^>                                              >>%dir%%name%.tps
echo         ^<string^>^</string^>                                                      >>%dir%%name%.tps
echo         ^<key^>textureFormat^</key^>                                               >>%dir%%name%.tps
echo         ^<enum type="SettingsBase::TextureFormat"^>pvr2ccz^</enum^>                >>%dir%%name%.tps
echo         ^<key^>borderPadding^</key^>                                               >>%dir%%name%.tps
echo         ^<uint^>0^</uint^>                                                         >>%dir%%name%.tps
echo         ^<key^>maxTextureSize^</key^>                                              >>%dir%%name%.tps
echo         ^<QSize^>                                                                >>%dir%%name%.tps
echo             ^<key^>width^</key^>                                                   >>%dir%%name%.tps
echo             ^<int^>2048^</int^>                                                    >>%dir%%name%.tps
echo             ^<key^>height^</key^>                                                  >>%dir%%name%.tps
echo             ^<int^>2048^</int^>                                                    >>%dir%%name%.tps
echo         ^</QSize^>                                                               >>%dir%%name%.tps
echo         ^<key^>fixedTextureSize^</key^>                                            >>%dir%%name%.tps
echo         ^<QSize^>                                                                >>%dir%%name%.tps
echo             ^<key^>width^</key^>                                                   >>%dir%%name%.tps
echo             ^<int^>-1^</int^>                                                      >>%dir%%name%.tps
echo             ^<key^>height^</key^>                                                  >>%dir%%name%.tps
echo             ^<int^>-1^</int^>                                                      >>%dir%%name%.tps
echo         ^</QSize^>                                                               >>%dir%%name%.tps
echo         ^<key^>reduceBorderArtifacts^</key^>                                       >>%dir%%name%.tps
echo         ^<false/^>                                                               >>%dir%%name%.tps
echo         ^<key^>algorithmSettings^</key^>                                           >>%dir%%name%.tps
echo         ^<struct type="AlgorithmSettings"^>                                      >>%dir%%name%.tps
echo             ^<key^>algorithm^</key^>                                               >>%dir%%name%.tps
echo             ^<enum type="AlgorithmSettings::AlgorithmId"^>MaxRects^</enum^>        >>%dir%%name%.tps
echo             ^<key^>freeSizeMode^</key^>                                            >>%dir%%name%.tps
echo             ^<enum type="AlgorithmSettings::AlgorithmFreeSizeMode"^>Best^</enum^>  >>%dir%%name%.tps
echo             ^<key^>sizeConstraints^</key^>                                         >>%dir%%name%.tps
echo             ^<enum type="AlgorithmSettings::SizeConstraints"^>POT^</enum^>         >>%dir%%name%.tps
echo             ^<key^>forceSquared^</key^>                                            >>%dir%%name%.tps
echo             ^<false/^>                                                           >>%dir%%name%.tps
echo             ^<key^>forceWordAligned^</key^>                                        >>%dir%%name%.tps
echo             ^<false/^>                                                           >>%dir%%name%.tps
echo             ^<key^>maxRects^</key^>                                                >>%dir%%name%.tps
echo             ^<struct type="AlgorithmMaxRectsSettings"^>                          >>%dir%%name%.tps
echo                 ^<key^>heuristic^</key^>                                           >>%dir%%name%.tps
echo                 ^<enum type="AlgorithmMaxRectsSettings::Heuristic"^>Best^</enum^>  >>%dir%%name%.tps
echo             ^</struct^>                                                          >>%dir%%name%.tps
echo             ^<key^>basic^</key^>                                                   >>%dir%%name%.tps
echo             ^<struct type="AlgorithmBasicSettings"^>                             >>%dir%%name%.tps
echo                 ^<key^>sortBy^</key^>                                              >>%dir%%name%.tps
echo                 ^<enum type="AlgorithmBasicSettings::SortBy"^>Best^</enum^>        >>%dir%%name%.tps
echo                 ^<key^>order^</key^>                                               >>%dir%%name%.tps
echo                 ^<enum type="AlgorithmBasicSettings::Order"^>Ascending^</enum^>    >>%dir%%name%.tps
echo             ^</struct^>                                                          >>%dir%%name%.tps
echo         ^</struct^>                                                              >>%dir%%name%.tps
echo         ^<key^>andEngine^</key^>                                                   >>%dir%%name%.tps
echo         ^<struct type="AndEngine"^>                                              >>%dir%%name%.tps
echo             ^<key^>minFilter^</key^>                                               >>%dir%%name%.tps
echo             ^<enum type="AndEngine::MinFilter"^>Linear^</enum^>                    >>%dir%%name%.tps
echo             ^<key^>packageName^</key^>                                             >>%dir%%name%.tps
echo             ^<string^>Texture^</string^>                                           >>%dir%%name%.tps
echo             ^<key^>wrap^</key^>                                                    >>%dir%%name%.tps
echo             ^<struct type="AndEngineWrap"^>                                      >>%dir%%name%.tps
echo                 ^<key^>s^</key^>                                                   >>%dir%%name%.tps
echo                 ^<enum type="AndEngineWrap::Wrap"^>Clamp^</enum^>                  >>%dir%%name%.tps
echo                 ^<key^>t^</key^>                                                   >>%dir%%name%.tps
echo                 ^<enum type="AndEngineWrap::Wrap"^>Clamp^</enum^>                  >>%dir%%name%.tps
echo             ^</struct^>                                                          >>%dir%%name%.tps
echo             ^<key^>magFilter^</key^>                                               >>%dir%%name%.tps
echo             ^<enum type="AndEngine::MagFilter"^>MagLinear^</enum^>                 >>%dir%%name%.tps
echo         ^</struct^>                                                              >>%dir%%name%.tps
echo         ^<key^>dataFileNames^</key^>                                               >>%dir%%name%.tps
echo         ^<map type="GFileNameMap"^>                                              >>%dir%%name%.tps
echo             ^<key^>data^</key^>                                                    >>%dir%%name%.tps
echo             ^<struct type="DataFile"^>                                           >>%dir%%name%.tps
echo                 ^<key^>name^</key^>                                                >>%dir%%name%.tps
echo                 ^<filename^>%name%.plist^</filename^>                          >>%dir%%name%.tps
echo             ^</struct^>                                                          >>%dir%%name%.tps
echo         ^</map^>                                                                 >>%dir%%name%.tps
echo         ^<key^>multiPack^</key^>                                                   >>%dir%%name%.tps
echo         ^<false/^>                                                               >>%dir%%name%.tps
echo         ^<key^>forceIdenticalLayout^</key^>                                        >>%dir%%name%.tps
echo         ^<false/^>                                                               >>%dir%%name%.tps
echo         ^<key^>outputFormat^</key^>                                                >>%dir%%name%.tps
echo         ^<enum type="SettingsBase::OutputFormat"^>RGBA8888^</enum^>                >>%dir%%name%.tps
echo         ^<key^>contentProtection^</key^>                                           >>%dir%%name%.tps
echo         ^<struct type="ContentProtection"^>                                      >>%dir%%name%.tps
echo             ^<key^>key^</key^>                                                     >>%dir%%name%.tps
echo             ^<string^>^</string^>                                                  >>%dir%%name%.tps
echo         ^</struct^>                                                              >>%dir%%name%.tps
echo         ^<key^>autoAliasEnabled^</key^>                                            >>%dir%%name%.tps
echo         ^<true/^>                                                                >>%dir%%name%.tps
echo         ^<key^>trimSpriteNames^</key^>                                             >>%dir%%name%.tps
echo         ^<false/^>                                                               >>%dir%%name%.tps
echo         ^<key^>cleanTransparentPixels^</key^>                                      >>%dir%%name%.tps
echo         ^<true/^>                                                                >>%dir%%name%.tps
echo         ^<key^>globalSpriteSettings^</key^>                                        >>%dir%%name%.tps
echo         ^<struct type="SpriteSettings"^>                                         >>%dir%%name%.tps
echo             ^<key^>scale^</key^>                                                   >>%dir%%name%.tps
echo             ^<double^>1^</double^>                                                 >>%dir%%name%.tps
echo             ^<key^>scaleMode^</key^>                                               >>%dir%%name%.tps
echo             ^<enum type="ScaleMode"^>Smooth^</enum^>                               >>%dir%%name%.tps
echo             ^<key^>innerPadding^</key^>                                            >>%dir%%name%.tps
echo             ^<uint^>0^</uint^>                                                     >>%dir%%name%.tps
echo             ^<key^>extrude^</key^>                                                 >>%dir%%name%.tps
echo             ^<uint^>0^</uint^>                                                     >>%dir%%name%.tps
echo             ^<key^>trimThreshold^</key^>                                           >>%dir%%name%.tps
echo             ^<uint^>1^</uint^>                                                     >>%dir%%name%.tps
echo             ^<key^>trimMode^</key^>                                                >>%dir%%name%.tps
echo             ^<enum type="SpriteSettings::TrimMode"^>None^</enum^>                  >>%dir%%name%.tps
echo             ^<key^>heuristicMask^</key^>                                           >>%dir%%name%.tps
echo             ^<false/^>                                                           >>%dir%%name%.tps
echo         ^</struct^>                                                              >>%dir%%name%.tps
echo         ^<key^>fileList^</key^>                                                    >>%dir%%name%.tps
echo         ^<array^>                                                                >>%dir%%name%.tps
rem            ^<filename^>bg_portrait_data.png^</filename^>  filelist                        >>%dir%%name%.tps
echo         ^<filename^>%name%.png^</filename^>										>>%dir%%name%.tps
echo         ^</array^>                                                               >>%dir%%name%.tps
echo         ^<key^>ignoreFileList^</key^>                                              >>%dir%%name%.tps
echo         ^<array/^>                                                               >>%dir%%name%.tps
echo         ^<key^>replaceList^</key^>                                                 >>%dir%%name%.tps
echo         ^<array/^>                                                               >>%dir%%name%.tps
echo         ^<key^>ignoredWarnings^</key^>                                             >>%dir%%name%.tps
echo         ^<array/^>                                                               >>%dir%%name%.tps
echo         ^<key^>commonDivisorX^</key^>                                              >>%dir%%name%.tps
echo         ^<uint^>1^</uint^>                                                         >>%dir%%name%.tps
echo         ^<key^>commonDivisorY^</key^>                                              >>%dir%%name%.tps
echo         ^<uint^>1^</uint^>                                                         >>%dir%%name%.tps
echo     ^</struct^>                                                                  >>%dir%%name%.tps
echo ^</data^>                                                                        >>%dir%%name%.tps