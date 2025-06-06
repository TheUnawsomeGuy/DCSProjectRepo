package Final;

/**
 * Console demonstration of the integrated distributed system
 * Tests all major functionality without GUI
 */
public class ConsoleDemo {
    
    public static void main(String[] args) {
        System.out.println("🚀 Distributed System Console Demo");
        System.out.println("===================================\n");
        
        // Create simulator
        DistributedSystemSimulator simulator = new DistributedSystemSimulator();
        
        try {
            // Test naming services
            System.out.println("🔍 Testing Naming Services:");
            testNamingServices(simulator);
            
            // Start simulation
            System.out.println("\n🎯 Starting Simulation...");
            simulator.startSimulation();
            
            // Let it run for a bit
            Thread.sleep(3000);
            
            // Test operations
            System.out.println("\n💻 Testing Operations:");
            testOperations(simulator);
            
            // Test failure scenarios
            System.out.println("\n💥 Testing Failure Scenarios:");
            testFailureScenarios(simulator);
            
            // Show status
            System.out.println("\n📊 System Status:");
            simulator.printSystemStatus();
            
            // Let it run more
            Thread.sleep(2000);
            
            // Show metrics
            System.out.println("\n📈 Performance Metrics:");
            showMetrics(simulator);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // Stop simulation
            System.out.println("\n🛑 Stopping Simulation...");
            simulator.stopSimulation();
        }
        
        System.out.println("\n✅ Demo completed successfully!");
    }
    
    private static void testNamingServices(DistributedSystemSimulator simulator) {
        // Test flat naming
        String result = simulator.lookupResource("nodea-service", "flat");
        System.out.println("Flat lookup result: " + result);
        
        // Test structured naming
        result = simulator.lookupResource("/services/nodeb-service", "structured");
        System.out.println("Structured lookup result: " + result);
        
        // Test DNS
        result = simulator.lookupResource("nodec-service.example.com", "dns");
        System.out.println("DNS lookup result: " + result);
        
        // Test non-existent resource
        result = simulator.lookupResource("nonexistent", "flat");
        System.out.println("Non-existent lookup result: " + result);
    }
    
    private static void testOperations(DistributedSystemSimulator simulator) {
        // Test PUT operations
        simulator.performOperation("NodeA", "PUT", "testKey1", "testValue1");
        simulator.performOperation("NodeB", "PUT", "testKey2", "testValue2");
        
        // Test GET operations
        simulator.performOperation("NodeA", "GET", "testKey1", "");
        simulator.performOperation("NodeC", "GET", "testKey2", "");
        
        // Test threading operations
        simulator.performOperation("NodeA", "DEPOSIT", "", "");
        simulator.performOperation("NodeB", "WITHDRAW", "", "");
        simulator.performOperation("NodeC", "DEPOSIT", "", "");
        
        // Test DELETE operations
        simulator.performOperation("NodeA", "DELETE", "testKey1", "");
    }
    
    private static void testFailureScenarios(DistributedSystemSimulator simulator) {
        // Simulate node failure
        simulator.simulateNodeFailure("NodeB");
        
        // Try operations on failed node
        simulator.performOperation("NodeB", "PUT", "failTest", "value");
        
        // Wait a bit
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Recover node
        simulator.recoverNode("NodeB");
        
        // Test operation after recovery
        simulator.performOperation("NodeB", "PUT", "recoveryTest", "value");
    }
    
    private static void showMetrics(DistributedSystemSimulator simulator) {
        java.util.List<PerformanceMetric> metrics = simulator.getMetrics();
        
        if (metrics.isEmpty()) {
            System.out.println("No metrics collected yet.");
            return;
        }
        
        PerformanceMetric latest = metrics.get(metrics.size() - 1);
        System.out.println("Latest metrics:");
        System.out.println("- Total Operations: " + latest.getTotalOperations());
        System.out.println("- Consistency Violations: " + 
                         String.format("%.2f%%", latest.getConsistencyViolations() * 100));
        System.out.println("- Average Latency: " + 
                         String.format("%.2f ms", latest.getAverageLatency()));
        
        if (metrics.size() > 1) {
            System.out.println("\nMetrics history (" + metrics.size() + " entries):");
            for (int i = Math.max(0, metrics.size() - 3); i < metrics.size(); i++) {
                PerformanceMetric metric = metrics.get(i);
                System.out.println("  " + metric);
            }
        }
    }
} 