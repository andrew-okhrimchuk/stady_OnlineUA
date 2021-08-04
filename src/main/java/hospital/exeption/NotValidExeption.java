package hospital.exeption;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class NotValidExeption extends Exception {

    public NotValidExeption(String msg) {
        super(msg);
    }
}
