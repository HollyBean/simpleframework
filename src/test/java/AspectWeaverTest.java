import com.hollybean.controller.HelloController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.simpleframework.aop.AspectWeaver;
import org.simpleframework.inject.DependencyInjector;
import org.simpleframework.ioc.BeanContainer;

public class AspectWeaverTest {
    @Test
    public void test_AspectWeaver() {
        String packageName = "com.hollybean";
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans(packageName);
        Assertions.assertEquals(true, beanContainer.isLoaded());
        AspectWeaver aspectWeaver = new AspectWeaver();
        aspectWeaver.doWeaver();
        DependencyInjector dependencyInjector = new DependencyInjector();
        dependencyInjector.doInject();

        HelloController helloController = (HelloController) beanContainer.getBean(HelloController.class);
        helloController.sayHello();
    }
}
