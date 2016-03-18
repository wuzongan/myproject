local scheduler = require("framework.scheduler")
local Platform_Appstore = class("Platform_Appstore",function()
    return display.newLayer()
end)

function Platform_Appstore:ctor()
    self:initIAP()
end


--初始化iap
function Platform_Appstore:initIAP()
    if not Store then
        Store = require("framework.api.Store")
        local function func(event)
            local productIdentifier = event.transaction.transactionIdentifier
            local receipt = event.transaction.receipt
            
            if event.transaction.state == "purchased" or event.transaction.state == "cancelled"
            or event.transaction.state == "failed" then
                if self.perIAP then
                    --loading界面关闭
                    -- require("game_ui.game_loading").close()
                    scheduler.unscheduleGlobal(self.perIAP)
                    self.perIAP = nil
                    if event.transaction.state == "failed" then
                        --失败提示框
                        -- game_util:addMoveTips({text = event.transaction.errorString})
                    end
                end
                
            end
            --处理购买中的事件回调，如购买成功
            if event.transaction.state == "purchased" then
                
                Store.finishTransaction(event.transaction)
                if productIdentifier and receipt then
                    --请求服务端验证receipt
                    local params = {}
                    receipt = crypto.encodeBase64(receipt)
                    params["receipt-data"] = receipt

                    local appstore_iap = cc.UserDefault:getInstance():getStringForKey("appstore_iap")
                    if appstore_iap and appstore_iap ~= "" then
                        appstore_iap = json.decode(appstore_iap)
                    else
                        appstore_iap = {}
                    end
                    table.insert(appstore_iap,receipt)
                    cc.UserDefault:getInstance():setStringForKey("appstore_iap", json.encode(appstore_iap))
                    
                    print("=========服务端验证===========")
                end
            end
            
        end
        Store.init(func)
    end
end

--支付
--如：pay("com.lein.PPPoker.pay1")
function Platform_Appstore:pay(productId)
    --loading界面显示
    -- local loadView = require("game_ui.game_loading")
    -- loadView.show()
    local function func(msg)
        Store.purchase(productId)
        self.perIAP = scheduler.performWithDelayGlobal(function()
            --loading关闭
            -- loadView.close()
        end, 20)
    end
    local products = {productId}
    Store.loadProducts(products, func)
end


function Platform_Appstore.iap_send(table1)
    local data = table1
    if data and #data > 0 then
        local params = {}
        params["receipt-data"] = data[1]
        local function responseMethod(tag,gameData)
            table.remove(data,1)
            if data and #data > 0 then
                cc.UserDefault:getInstance():setStringForKey("appstore_iap", json.encode(data))
                game_data_download.iap_send(data)
            else
                cc.UserDefault:getInstance():setStringForKey("appstore_iap", "")
            end
        end
        network.sendHttpRequest(responseMethod, game_url.getUrlForKey("pay_kubi"), http_request_method.POST, params,"pay_kubi", true, true);
    end
end

function Platform_Appstore.reSendIap(self)
    local appstore_iap = cc.UserDefault:getInstance():getStringForKey("appstore_iap")
    if appstore_iap and appstore_iap ~= "" then
        appstore_iap = json.decode(appstore_iap)
        Platform_Appstore.iap_send(appstore_iap)
    end
end

return Platform_Appstore
