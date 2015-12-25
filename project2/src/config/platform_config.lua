-- if device.platform == "ios" then
--     PLATFORM_SDK = require("Platform_iOS")
--     PLATFORM_SDK.init()
-- end

function isPlatform()
    if PLATFORM == "360" or PLATFORM == "xiaomi" or PLATFORM == "lenovo" 
        or PLATFORM == "htc" or PLATFORM == "uc" or PLATFORM == "youku" 
        or PLATFORM == "sj49you" or PLATFORM == "baidu" or PLATFORM == "sogou"
        or PLATFORM == "downjoy" or PLATFORM == "anzhi" or PLATFORM == "coolpad"
        or PLATFORM == "huawei" or PLATFORM == "oppo" or PLATFORM == "vivo" 
        or PLATFORM == "cn91" or PLATFORM == "meizu" or PLATFORM == "wdj"
        or PLATFORM == "baofeng" or PLATFORM == "mogoo" or PLATFORM == "am"
        or PLATFORM == "pptv" or PLATFORM == "mopo" or PLATFORM == "gfan"
        or PLATFORM == "mumayi" or PLATFORM == "pps" or PLATFORM == "sy07073"
        or PLATFORM == "tencent" or PLATFORM == "letv" or PLATFORM == "sj49you_tyh"
        or PLATFORM == "sj49you_fx" or PLATFORM == "sj49ios_fx" or PLATFORM == "jiudu" 
        or PLATFORM == "shandou" or PLATFORM == "tfgame" or PLATFORM == "koudai"
        or PLATFORM == "xy" or PLATFORM == "i4" or PLATFORM == "pp" or PLATFORM == "itools" 
        or PLATFORM == "kuaiyong" or PLATFORM == "haima" or PLATFORM == "tongbu"
        or PLATFORM == "iiapple" or PLATFORM == "appstore" or PLATFORM == "sj49ios"
        then
        return true
    else
        return false
    end
end

