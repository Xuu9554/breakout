public class TestServiceImpl implements TestService {
    @Override
    public String get(Integer id) {
        return String.valueOf(id);
    }
}
