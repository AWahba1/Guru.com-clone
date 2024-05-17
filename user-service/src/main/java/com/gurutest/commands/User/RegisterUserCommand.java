package com.gurutest.commands.User;

import com.gurutest.Users.Users;
import com.gurutest.registeration.RegistrationRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class RegisterUserCommand extends UserCommands {
    @Override
    public Object execute(HashMap<String, Object> map) throws Exception {
        return getService().RegisterUser((RegistrationRequest) map.get("RegistrationRequest"));
    }
}
