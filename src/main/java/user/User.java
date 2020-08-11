package user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class User implements Serializable{

    private String email;
    private String name;
    private String password;
    private Boolean isAHead;
}
