package com.gurutest.Event;

import com.gurutest.Users.Users;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent  extends ApplicationEvent {

    private Users user;
    private String applicationURL;

    public RegistrationCompleteEvent( Users user, String applicationURL) {
        super(user);
        this.user = user;
        this.applicationURL = applicationURL;
    }


}
