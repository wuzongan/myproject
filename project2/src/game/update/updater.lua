require "lfs"

local updater = {}
updater.STATES = {
    kDownStart = "downloadStart",
    kDownDone = "downloadDone",
    kUncompressStart = "uncompressStart",
    kUncompressDone = "uncompressDone",
    unknown = "stateUnknown",
}

updater.ERRORS = {
    kCreateFile = "errorCreateFile",
    kNetwork = "errorNetwork",
    kNoNewVersion = "errorNoNewVersion",
    kUncompress = "errorUncompress",
    unknown = "errorUnknown";
}

function updater.isState(state)
    for k,v in pairs(updater.STATES) do
        if v == state then
            return true
        end
    end
    return false
end

function updater.clone(object)
    local lookup_table = {}
    local function _copy(object)
        if type(object) ~= "table" then
            return object
        elseif lookup_table[object] then
            return lookup_table[object]
        end
        local new_table = {}
        lookup_table[object] = new_table
        for key, value in pairs(object) do
            new_table[_copy(key)] = _copy(value)
        end
        return setmetatable(new_table, getmetatable(object))
    end
    return _copy(object)
end

function updater.vardump(object, label, returnTable)
    local lookupTable = {}
    local result = {}

    local function _v(v)
        if type(v) == "string" then
            v = "\"" .. v .. "\""
        end
        return tostring(v)
    end

    local function _vardump(object, label, indent, nest)
        label = label or ""
        local postfix = ""
        if nest > 1 then postfix = "," end
        if type(object) ~= "table" then
            if type(label) == "string" then
                result[#result +1] = string.format("%s[\"%s\"] = %s%s", indent, label, _v(object), postfix)
            else
                result[#result +1] = string.format("%s%s%s", indent, _v(object), postfix)
            end
        elseif not lookupTable[object] then
            lookupTable[object] = true

            if type(label) == "string" then
                result[#result +1 ] = string.format("%s%s = {", indent, label)
            else
                result[#result +1 ] = string.format("%s{", indent)
            end
            local indent2 = indent .. "    "
            local keys = {}
            local values = {}
            for k, v in pairs(object) do
                keys[#keys + 1] = k
                values[k] = v
            end
            table.sort(keys, function(a, b)
                if type(a) == "number" and type(b) == "number" then
                    return a < b
                else
                    return tostring(a) < tostring(b)
                end
            end)
            for i, k in ipairs(keys) do
                _vardump(values[k], k, indent2, nest + 1)
            end
            result[#result +1] = string.format("%s}%s", indent, postfix)
        end
    end
    _vardump(object, label, "", 1)

    if returnTable then return result end
    return table.concat(result, "\n")
end

local u  = nil
local f = cc.FileUtils:getInstance()
-- The res index file in original package.
local lresinfo = "lua/resinfo.lua"
local uroot = f:getWritablePath()
-- The directory for save updated files.
local ures = uroot.."lua/"
-- The package zip file what download from server.
local uzip = uroot.."update.zip"
-- The directory for uncompress res.zip.
local utmp = uroot.."utmp/"
-- The res index file in zip package for update.
local zresinfo = utmp.."resinfo.lua"

-- The res index file for final game.
-- It combiled original lresinfo and zresinfo.
local uresinfo = ures .. "resinfo.lua"

local localResInfo = nil
local remoteResInfo = nil
local finalResInfo = nil

local function _initUpdater()
    print("initUpdater, ", u)
    if not u then u = Updater:new() end
    print("after initUpdater:", u)
end

function updater.writeFile(path, content, mode)
    mode = mode or "w+b"
    local file = io.open(path, mode)
    if file then
        if file:write(content) == nil then return false end
        io.close(file)
        return true
    else
        return false
    end
end

function updater.readFile(path)
    return f:getFileData(path)
end

function updater.exists(path)
    return f:isFileExist(path)
end

--[[
-- Departed, uses lfs instead.
function updater._mkdir(path)
    _initUpdater()
    return u:createDirectory(path)
end

-- Departed, get a warning in ios simulator
function updater._rmdir(path)
    _initUpdater()
    return u:removeDirectory(path)
end
--]]

function updater.mkdir(path)
    if not updater.exists(path) then
        return lfs.mkdir(path)
    end
    return true
end

function updater.rmdir(path)
    print("updater.rmdir:", path)
    if updater.exists(path) then
        local function _rmdir(path)
            local iter, dir_obj = lfs.dir(path)
            while true do
                local dir = iter(dir_obj)
                if dir == nil then break end
                if dir ~= "." and dir ~= ".." then
                    local curDir = path..dir
                    local mode = lfs.attributes(curDir, "mode") 
                    if mode == "directory" then
                        _rmdir(curDir.."/")
                    elseif mode == "file" then
                        os.remove(curDir)
                    end
                end
            end
            local succ, des = os.remove(path)
            if des then print(des) end
            return succ
        end
        _rmdir(path)
    end
    return true
end

-- Is there a update.zip package in ures directory?
-- If it is true, return its abstract path.
function updater.hasNewUpdatePackage()
    local newUpdater = ures.."lib/update.zip"
    if updater.exists(newUpdater) then
        return newUpdater
    end
    return nil
end

-- Check local resinfo and remote resinfo, compare their version value.
function updater.checkUpdate()
    localResInfo = updater.getLocalResInfo()
    local localVer = localResInfo.version
    print("localVer:", localVer,localResInfo.update_url)
    remoteResInfo = updater.getRemoteResInfo(localResInfo.update_url)
    if remoteResInfo then
        local remoteVer = remoteResInfo.version
        print("remoteVer:", remoteVer)
        return remoteVer ~= localVer
    else
        return false
    end
end

function updater.init()
    _initUpdater()
end

function updater.setRemoteInfo(remoteInfo,localInfo)
    remoteResInfo = remoteInfo
    localResInfo = localInfo
end

-- Copy resinfo.lua from original package to update directory(ures) 
-- when it is not in ures.
function updater.getLocalResInfo()
    print(string.format("updater.getLocalResInfo, lresinfo:%s, uresinfo:%s", 
        lresinfo,uresinfo))
    local resInfoTxt = nil
    if updater.exists(uresinfo) then
        resInfoTxt = updater.readFile(uresinfo)
    else
        -- assert(lfs.mkdir(ures), ures.." create error!")
        local info = updater.readFile(lresinfo)
        print("localResInfo:", info)
        assert(info, string.format("Can not get the constent from %s!", lresinfo))
        updater.writeFile(uresinfo, info)
        resInfoTxt = info
    end
    return assert(loadstring(resInfoTxt))()
end

function updater.getRemoteResInfo(path)
    _initUpdater()
    print("updater.getRemoteResInfo:", path)
    local resInfoTxt = u:getUpdateInfo(path)
    print("resInfoTxt:", resInfoTxt)
    return assert(loadstring(resInfoTxt))()
end

function updater.update(handler)
    assert(remoteResInfo and remoteResInfo.package, "Can not get remoteResInfo!")
    print("updater.update:", remoteResInfo.package)
    if handler then
        u:registerScriptHandler(handler)
    end
    updater.rmdir(utmp)
    u:update(remoteResInfo.package, uzip, utmp, false)
end

function updater._copyNewFile(resInZip)
    -- Create nonexistent directory in update res.
    local i,j = 1,1
    while true do
        j = string.find(resInZip, "/", i)
        if j == nil then break end
        local dir = string.sub(resInZip, 1,j)
        -- Save created directory flag to a table because
        -- the io operation is too slow.
        if not updater._dirList[dir] then
            updater._dirList[dir] = true
            local fullUDir = uroot..dir
            updater.mkdir(fullUDir)
        end
        i = j+1
    end
    local fullFileInURes = uroot..resInZip
    local fullFileInUTmp = utmp..resInZip
    if device.platform == "windows" then
        fullFileInUTmp = (string.gsub(fullFileInUTmp, "\\", "/"))
        fullFileInURes = (string.gsub(fullFileInURes, "\\", "/"))
        print("windows:",(string.gsub(fullFileInUTmp, "\\", "/")))
    end
    print(string.format('copy %s to %s', fullFileInUTmp, fullFileInURes))
    local zipFileContent = updater.readFile(fullFileInUTmp)
    if zipFileContent then
        updater.writeFile(fullFileInURes, zipFileContent)
        return fullFileInURes
    end
    return nil
end

function updater._copyNewFilesBatch(resType, resInfoInZip)
    local resList = resInfoInZip[resType]
    if not resList then return end
    local finalRes = finalResInfo[resType]
    for __,v in ipairs(resList) do
        local fullFileInURes = updater._copyNewFile(v)
        if fullFileInURes then
            -- Update key and file in the finalResInfo
            -- Ignores the update package because it has been in memory.
            if v ~= "res/lib/update.zip" then
                finalRes[v] = fullFileInURes
            end
        else
            print(string.format("updater ERROR, copy file %s.", v))
        end
    end
end

function updater.updateFinalResInfo()
    assert(localResInfo and remoteResInfo,
        "Perform updater.checkUpdate() first!")
    if not finalResInfo then
        finalResInfo = updater.clone(localResInfo)
    end
    --do return end
    local resInfoTxt = updater.readFile(zresinfo)
    local zipResInfo = assert(loadstring(resInfoTxt))()
    if zipResInfo["version"] then
        finalResInfo.version = zipResInfo["version"]
    end
    -- Save a dir list maked.
    updater._dirList = {}
    updater._copyNewFilesBatch("lib", zipResInfo)
    updater._copyNewFilesBatch("oth", zipResInfo)
    -- Clean dir list.
    updater._dirList = nil
    updater.rmdir(utmp)
    local dumpTable = updater.vardump(finalResInfo, "local data", true)
    dumpTable[#dumpTable+1] = "return data"
    if updater.writeFile(uresinfo, table.concat(dumpTable, "\n")) then
        return true
    end
    print(string.format("updater ERROR, write file %s.", uresinfo))
    return false
end

function updater.getResCopy()
    if finalResInfo then return updater.clone(finalResInfo) end
    return updater.clone(localResInfo)
end

function updater.clean()
    if u then
        u:unregisterScriptHandler()
        u:delete()
        u = nil
    end
    updater.rmdir(utmp)
    localResInfo = nil
    remoteResInfo = nil
    finalResInfo = nil
end

return updater