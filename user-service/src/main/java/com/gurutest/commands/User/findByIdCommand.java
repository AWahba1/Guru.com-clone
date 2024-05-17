package com.gurutest.commands.User;

import java.util.HashMap;

public class findByIdCommand extends UserCommands{

    @Override
    public Object execute(HashMap<String, Object> map) throws Exception {
        return getService().findById((int)map.get("id"));
    }
}
