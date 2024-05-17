package com.gurutest.commands.User;

import com.gurutest.Users.Users;

import java.util.HashMap;

public class saveUserVerificationTokenCommand extends UserCommands{
    @Override
    public Object execute(HashMap<String, Object> map) throws Exception {
        Users theUser= (Users)map.get("theUser"); ;
        String verToken= (String) map.get("verToken");
        return getService().saveUserVerificationToken(theUser,verToken);
    }
}
