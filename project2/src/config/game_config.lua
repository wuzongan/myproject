
---------------- define -----------------

SERVER_LOCAL_DEBUG = "192.168.1.188"
SERVER_APPSTORE = "120.132.54.184"
SERVER_S1 = "s1.xbcq.49app.com"
SERVER_S2 = "s2.xbcq.49app.com"
PLATFORM = ""


---------------- config -----------------

require("config.common_config")
require("config.data_config")
game_url = require("config.game_url_config")
require("config.game_version_config")
require("config.platform_config")
require("config.string_config")

---------------- common -----------------
require("common.common_func")
game_pop = require("common.game_pop").new()
