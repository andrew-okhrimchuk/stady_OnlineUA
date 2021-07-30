package hospital.exeption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

import java.sql.SQLException;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class CustomSQLErrorCodeTranslator extends SQLErrorCodeSQLExceptionTranslator {
    private final static String EXCEPTION_23505 = "23505";
    private final static String EXCEPTION_23000 = "23000";
    private final static String EXCEPTION_23502 = "23502";
    private final static String EXCEPTION_23503 = "23503";
    private final static String EXCEPTION_23514 = "23514";

    @Override
    protected DataAccessException customTranslate(String task, String sql, SQLException e) {
        if (e.getSQLState().equalsIgnoreCase(EXCEPTION_23502)) {
            log.error(e.getMessage());
            return new DaoExeption (e.getMessage(), e);
        }
        if (e.getSQLState().equalsIgnoreCase(EXCEPTION_23000) ||
                e.getSQLState(). equalsIgnoreCase(EXCEPTION_23502) ||
                e.getSQLState(). equalsIgnoreCase( EXCEPTION_23503) ||
                e.getSQLState(). equalsIgnoreCase( EXCEPTION_23514)
        ) {
            log.error(e.getMessage());
            return new DaoExeption(
                    e.getMessage(), e);
        }
        return null;
    }
}
