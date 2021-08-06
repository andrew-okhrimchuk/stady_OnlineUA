package hospital.services.intrface;
import hospital.exeption.ServiceExeption;

import java.util.List;

public interface GenericService<T, V> {
    List<T> getAll(V v)throws ServiceExeption;
    List<T> getAll()throws ServiceExeption;
}
