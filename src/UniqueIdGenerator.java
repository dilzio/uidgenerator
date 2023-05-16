import java.security.MessageDigest;

public class UniqueIdGenerator {
    private static final String HASH_ALGORITHM = "SHA-256";
    private static boolean initialized = false;
    private static int region;
    private static String hashedHWAddress;

    public static boolean initialize(int region, byte[] hardwareAddressBytes) {
        if (initialized) {
            return true;
        }
        try{
            if (region <= 0 || hardwareAddressBytes == null) {
               throw new RuntimeException("Region or hardware address cannot be null values");
            }

            UniqueIdGenerator.region = region;
            UniqueIdGenerator.hashedHWAddress = generateMacHash(hardwareAddressBytes);
            initialized = true;

        } catch (Exception xn){
            throw new RuntimeException("Failed to Initialize UniqueIdGenerator with provided values.Region was: "
                                        + region + ", hardwareAddressBytes was: " + hardwareAddressBytes);
        }
        return initialized;
    }

    public static long generateUniqueId() {
        if (!initialized) {
            throw new RuntimeException("UniqueIdGenerator not initialized");
        }

        long timestamp = System.currentTimeMillis();

        // Combine the region, timestamp, and MAC address hash to form the unique ID
        String uniqueIdString = String.format("%d-%d-%s", region, timestamp, hashedHWAddress);
        long uniqueId = uniqueIdString.hashCode(); // Convert to a unique ID

        return uniqueId;
    }

    private static String generateMacHash(byte[] hwAddressBytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hashBytes = digest.digest(hwAddressBytes);

            // Convert the hash bytes to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception xn) {
            throw new RuntimeException(xn);
        }
    }
}
