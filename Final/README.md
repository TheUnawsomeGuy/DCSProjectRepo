# 🚀 Integrated Distributed System Simulator

## Overview

This comprehensive simulation combines **multithreading**, **consistency models**, and **naming services** into a unified distributed system simulator with a graphical user interface for analysis and performance evaluation.

## 🏗️ Architecture

### Core Components

#### 1. **DistributedSystemSimulator** - Main Orchestrator
- Manages 3 service nodes (NodeA, NodeB, NodeC)
- Coordinates different consistency models
- Integrates all naming services
- Provides performance monitoring

#### 2. **ServiceNode** - Distributed System Participants
- **Multithreading**: Each node runs in separate threads
- **Shared Resource Management**: Thread-safe operations (deposit/withdraw)
- **Data Storage**: Key-value store with consistency tracking
- **Client-Centric Consistency**: Per-client version tracking
- **Failure Simulation**: Node can fail and recover

#### 3. **Consistency Models**
- **Sequential Consistency**: Central coordinator ensures same operation order
- **Eventual Consistency**: Nodes sync periodically with delays
- **Client-Centric Consistency**: Read-your-writes guarantees

#### 4. **Naming Services**
- **Flat Naming**: Hash table (dictionary) approach - O(1) lookup
- **Structured Naming**: Hierarchical paths like `/services/nodea-service`
- **DNS Simulation**: Domain name to IP address resolution

#### 5. **Performance Analysis GUI**
- Real-time monitoring dashboard
- Performance metrics collection
- Trade-off analysis tools
- Interactive controls for operations

## 🎯 Features Implemented

### ✅ Core Requirements

1. **2-3 Processes/Services**: NodeA, NodeB, NodeC running concurrently
2. **Multithreading**: 
   - Each node runs in separate thread
   - Thread-safe shared resource operations
   - Concurrent data access with proper synchronization
3. **Flat Naming**: Dictionary-based resource lookup
4. **Structured Naming**: Hierarchical DNS-like paths
5. **Sequential Consistency**: Central log with ordered operations
6. **Eventual Consistency**: Delayed synchronization with network simulation
7. **Client-Centric Consistency**: Version tracking per client

### ✅ GUI Features

1. **Interactive Controls**:
   - Start/Stop simulation
   - Execute operations on specific nodes
   - Simulate node failures and recovery
   - Resource lookups using different naming services

2. **Real-time Monitoring**:
   - Live performance metrics
   - Operation count tracking
   - Consistency violation monitoring
   - System latency measurement

3. **Analysis Tools**:
   - Performance trend analysis
   - Naming service comparison
   - Consistency model trade-offs
   - System status reports

## 🚀 How to Run

### Prerequisites
- Java 8 or higher
- Swing GUI support

### Compilation
```bash
javac *.java
```

### Execution
```bash
java Final.DistributedSystemSimulator
```

## 🎮 Usage Guide

### Starting the Simulation
1. Click **"Start Simulation"** to begin the distributed system
2. Watch real-time metrics in the monitoring panel
3. Observe system logs in the bottom panel

### Testing Operations
1. **Select Node**: Choose NodeA, NodeB, or NodeC
2. **Choose Operation**: PUT, GET, DELETE, DEPOSIT, WITHDRAW
3. **Enter Key/Value**: For data operations
4. **Execute**: Click "Execute Operation"

### Testing Naming Services
1. **Select Naming Type**: flat, structured, or dns
2. **Enter Resource Name**: 
   - Flat: `nodea-service`
   - Structured: `/services/nodea-service`
   - DNS: `nodea-service.example.com`
3. **Lookup**: Click "Lookup Resource"

### Failure Testing
1. **Select Node**: Choose target node
2. **Simulate Failure**: Click "Simulate Failure"
3. **Observe**: Watch consistency mechanisms handle failure
4. **Recover**: Click "Recover Node" to restore

### Performance Analysis
1. **Analyze Performance**: View current system metrics
2. **Compare Naming**: Understand trade-offs between naming services
3. **Analyze Consistency**: Learn about consistency model implications

## 📊 Performance Metrics

### Monitored Metrics
- **Total Operations**: Count of all system operations
- **Consistency Violations**: Percentage of consistency issues
- **Average Latency**: System response time
- **System Load**: Current processing load

### Analysis Features
- **Trend Analysis**: Performance over time
- **Violation Tracking**: Consistency guarantee monitoring
- **Latency Monitoring**: Response time analysis
- **Load Balancing**: Distribution across nodes

## 🔄 Threading Architecture

### Thread Distribution
- **Main GUI Thread**: User interface management
- **Node Threads**: 3 threads for service nodes
- **Coordination Thread**: Sequential consistency management
- **Sync Thread**: Eventual consistency synchronization
- **Monitor Thread**: Performance metric collection
- **GUI Update Thread**: Real-time display updates

### Synchronization Mechanisms
- **Synchronized Blocks**: Thread-safe shared resource access
- **ConcurrentHashMap**: Thread-safe data storage
- **AtomicInteger**: Atomic sequence number generation
- **ExecutorService**: Managed thread pool

## 🏛️ Consistency Models Deep Dive

### Sequential Consistency
```
Operation Flow:
Client -> Central Coordinator -> Assign Sequence -> Broadcast to All Nodes
```
- **Guarantee**: All nodes see operations in same order
- **Trade-off**: Higher latency for stronger consistency

### Eventual Consistency
```
Update Flow:
Node Updates Locally -> Periodic Sync -> Eventually Consistent
```
- **Guarantee**: Convergence given no new updates
- **Trade-off**: Temporary inconsistency for better performance

### Client-Centric Consistency
```
Client Flow:
Write Operation -> Version Tracking -> Read from Suitable Node
```
- **Guarantee**: Read-your-writes, monotonic reads
- **Trade-off**: Balanced consistency and performance

## 🗂️ Naming Services Comparison

| Feature | Flat Naming | Structured Naming | DNS Simulation |
|---------|-------------|-------------------|----------------|
| **Lookup Time** | O(1) | O(1) | O(1) + Network |
| **Organization** | None | Hierarchical | Domain-based |
| **Scalability** | Limited | Good | Excellent |
| **Complexity** | Low | Medium | High |
| **Use Case** | Local services | Organized systems | Internet-scale |

## 🎯 Trade-off Analysis

### Performance vs Consistency
- **Strong Consistency**: Higher latency, guaranteed correctness
- **Eventual Consistency**: Lower latency, temporary inconsistency
- **Client-Centric**: Balanced approach with per-client guarantees

### Naming Service Trade-offs
- **Flat**: Fast but unorganized
- **Structured**: Organized but requires path management
- **DNS**: Scalable but network-dependent

### Threading vs Process Design
- **Threading**: Shared memory, faster communication
- **Processes**: Isolation, better fault tolerance
- **Current Choice**: Threading for simulation simplicity

## 🔍 Testing Scenarios

### High Concurrency Testing
1. Start simulation
2. Execute rapid operations on multiple nodes
3. Observe consistency violation metrics
4. Analyze performance under load

### Node Failure Testing
1. Simulate node failure during operations
2. Observe system recovery mechanisms
3. Test eventual consistency synchronization
4. Verify data integrity after recovery

### Naming Service Comparison
1. Test same resource lookup across all naming types
2. Compare response times
3. Analyze success rates
4. Evaluate organizational benefits

## 📈 Expected Results

### Performance Characteristics
- **Low Consistency Violations**: < 5% under normal load
- **Acceptable Latency**: < 200ms average response time
- **High Availability**: > 95% uptime with failure recovery

### Trade-off Observations
- Sequential consistency shows higher latency but zero violations
- Eventual consistency provides better performance with temporary inconsistencies
- Flat naming offers fastest lookups but poor organization
- Structured naming provides good organization with minimal overhead

## 🎓 Educational Value

This simulator demonstrates:
1. **Real-world distributed system challenges**
2. **Consistency model implications**
3. **Naming service design decisions**
4. **Performance monitoring techniques**
5. **System failure handling**
6. **Multi-threading coordination**

## 🚧 Future Enhancements

- **Network Partition Simulation**: Simulate split-brain scenarios
- **Dynamic Load Balancing**: Automatic request distribution
- **Persistence Layer**: Data durability simulation
- **Byzantine Fault Tolerance**: Handle malicious node behavior
- **Geographic Distribution**: Multi-datacenter simulation

## 📝 Implementation Notes

- **Thread Safety**: All shared data structures use concurrent collections
- **Resource Management**: Proper thread pool and executor management
- **Error Handling**: Graceful degradation during failures
- **GUI Responsiveness**: Background processing with UI updates
- **Performance Optimization**: Efficient data structures and algorithms

---

## 🏆 Key Learning Outcomes

After using this simulator, you will understand:
- How distributed systems maintain consistency
- Trade-offs between different naming approaches
- Impact of threading vs process-based architectures
- Real-time system monitoring and analysis
- Failure recovery mechanisms in distributed systems
- Performance characteristics under various loads

**Ready to explore the complex world of distributed systems!** 🌟 
