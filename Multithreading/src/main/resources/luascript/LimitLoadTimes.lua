local id=KEYS[1];
local count=redis.call("get",id);
if(tonumber(count)>0) then
    redis.call("DECR",tonumber(id));
    return 1;
else
    return 0;
    end;
