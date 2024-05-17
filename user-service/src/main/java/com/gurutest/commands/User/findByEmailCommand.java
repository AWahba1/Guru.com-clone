package com.gurutest.commands.User;

import java.util.HashMap;

public class findByEmailCommand extends UserCommands{
    @Override
    public Object execute(HashMap<String, Object> map) throws Exception {
        return getService().findByEmail((String) map.get("email"));
    }
}
