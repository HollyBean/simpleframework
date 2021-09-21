import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.simpleframework.util.ClassUtil;

import java.util.Set;

public class ClassUtilTest {
    @Test
    public void test_ClassUtil() {
        String packageName = "com.hollybean";
        Set<Class<?>> classSet = ClassUtil.extractPackageClassSet(packageName);
        Assertions.assertEquals(3, classSet.size());
    }
}
