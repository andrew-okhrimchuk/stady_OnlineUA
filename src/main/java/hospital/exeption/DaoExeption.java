package hospital.exeption;

import lombok.EqualsAndHashCode;
import org.springframework.dao.DataAccessException;

@EqualsAndHashCode(callSuper = true)
public class DaoExeption extends DataAccessException {
    private  Exception e;

    public DaoExeption(String msg, Exception e) {
        super(msg);
        this.e = e;
    }
}
