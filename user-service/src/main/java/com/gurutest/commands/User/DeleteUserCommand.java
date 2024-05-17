package com.gurutest.commands.User;

import java.util.HashMap;

public class DeleteUserCommand  extends UserCommands{
    @Override
    public Object execute(HashMap<String, Object> map) throws Exception {
        int user_id=(int)(map.get("id"));
        return getService().deleteUser(user_id);
    }
}
