package com.gurutest.commands.User;

import com.gurutest.Users.UserService;
import com.gurutest.commands.Command;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class UserCommands extends Command {
    private UserService userService;

    public UserService getService() {
        return userService;
    }

    @Autowired
    public final void setService(UserService userService) {
        this.userService = userService;
    }
}
