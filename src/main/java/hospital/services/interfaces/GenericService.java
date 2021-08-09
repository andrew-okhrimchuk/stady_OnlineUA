package hospital.services.interfaces;
import hospital.exeption.ServiceExeption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GenericService<T, V> {
    Page<T> getAll(V v, Pageable pageable)throws ServiceExeption;
    Page<T> getAll(Pageable pageable)throws ServiceExeption;
}
