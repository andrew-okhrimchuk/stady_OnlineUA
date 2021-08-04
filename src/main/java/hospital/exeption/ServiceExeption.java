package hospital.exeption;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class ServiceExeption extends Exception {
    private  Exception e;

    public ServiceExeption(String msg, Exception e) {
        super(msg);
        this.e = e;
    }
}
