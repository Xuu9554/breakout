package model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 5462023615103520939L;

    /**
     *
     */
    private String userId;

    /**
     *
     */
    private String password;

}
