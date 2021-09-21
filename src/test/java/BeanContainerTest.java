import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.simpleframework.ioc.BeanContainer;

public class BeanContainerTest {
    @Test
    public void test_BeanContainer() {
        String packageName = "com.hollybean";
        BeanContainer beanContainer = BeanContainer.getInstance();
        Assertions.assertEquals(false, beanContainer.isLoaded());
        beanContainer.loadBeans(packageName);
        Assertions.assertEquals(true, beanContainer.isLoaded());
        Assertions.assertEquals(3, beanContainer.size());
    }
}
