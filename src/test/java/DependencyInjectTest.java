import com.hollybean.controller.HelloController;
import org.junit.jupiter.api.*;
import org.simpleframework.inject.DependencyInjector;
import org.simpleframework.ioc.BeanContainer;

public class DependencyInjectTest {
    @Test
    public void test_DependencyInjector() {
        String packageName = "com.hollybean";
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans(packageName);
        Assertions.assertEquals(true, beanContainer.isLoaded());
        DependencyInjector dependencyInjector = new DependencyInjector();
        dependencyInjector.doInject();
        HelloController helloController = (HelloController) beanContainer.getBean(HelloController.class);
        helloController.sayHello();
    }
}
