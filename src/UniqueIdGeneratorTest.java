import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UniqueIdGeneratorTest {

    @Order(1)
    @Test
    public void uninitializedClassThrowsException() {
        assertThrows(RuntimeException.class, UniqueIdGenerator::generateUniqueId);
    }

    @Order(2)
    @Test
    public void badInitializationVarsThrowException() {
        assertThrows(RuntimeException.class, ()->UniqueIdGenerator.initialize(0, null));
    }

    @Test
    public void testInitialize_SuccessfulInitialization() {
        int region = 5;
        byte[] hwAddress = new byte[]{25, 127, 55, 40};
        boolean initialized = UniqueIdGenerator.initialize(region, hwAddress);
        Assertions.assertTrue(initialized);
    }

    @Test
    public void testInitialize_AlreadyInitialized() {
        int region = 123;
        byte[] hardwareAddress = {0x00, 0x11, 0x22, 0x33, 0x44, 0x55};

        UniqueIdGenerator.initialize(region, hardwareAddress);
        boolean initializedAgain = UniqueIdGenerator.initialize(region, hardwareAddress);

        Assertions.assertTrue(initializedAgain);
    }

    @Test
    public void testInitialize_NullRegion() {
        byte[] hardwareAddress = {0x00, 0x11, 0x22, 0x33, 0x44, 0x55};

        Assertions.assertThrows(RuntimeException.class, () -> {
            UniqueIdGenerator.initialize(0, hardwareAddress);
        });
    }

    @Test
    public void testInitialize_NullHardwareAddress() {
        int region = 123;

        Assertions.assertThrows(RuntimeException.class, () -> {
            UniqueIdGenerator.initialize(region, null);
        });
    }

    @Test
    public void testGenerateUniqueId_NotInitialized() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            UniqueIdGenerator.generateUniqueId();
        });
    }

    @Test
    public void testGenerateUniqueId_InitializationSuccessful() {
        int region = 123;
        byte[] hardwareAddress = {0x00, 0x11, 0x22, 0x33, 0x44, 0x55};

        UniqueIdGenerator.initialize(region, hardwareAddress);
        long uniqueId = UniqueIdGenerator.generateUniqueId();

        Assertions.assertNotEquals(0, uniqueId);
    }


}
