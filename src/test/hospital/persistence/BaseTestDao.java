package hospital.persistence;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/initDB2_psq.sql"})
@ActiveProfiles(profiles = "qa")
public class BaseTestDao {
}
