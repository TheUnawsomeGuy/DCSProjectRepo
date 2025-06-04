package Consistency;

/**
 * Represents an operation in our distributed system
 * Used for maintaining operation logs and ensuring consistency
 */
public class Operation {
    private final int sequenceNumber;
    private final String type; // "PUT", "DELETE"
    private final String key;
    private final String value;
    private final long timestamp;
    private final String clientId;

    public Operation(int sequenceNumber, String type, String key, String value, String clientId) {
        this.sequenceNumber = sequenceNumber;
        this.type = type;
        this.key = key;
        this.value = value;
        this.clientId = clientId;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters
    public int getSequenceNumber() { return sequenceNumber; }
    public String getType() { return type; }
    public String getKey() { return key; }
    public String getValue() { return value; }
    public long getTimestamp() { return timestamp; }
    public String getClientId() { return clientId; }

    @Override
    public String toString() {
        return String.format("Op[seq=%d, type=%s, key=%s, value=%s, client=%s]", 
                           sequenceNumber, type, key, value, clientId);
    }
} 