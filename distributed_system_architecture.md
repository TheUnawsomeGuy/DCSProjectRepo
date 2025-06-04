# Distributed System Simulator - Architecture and Design Analysis

## System Overview
The Distributed System Simulator is a comprehensive platform that demonstrates distributed computing concepts including consistency models, naming services, threading, fault tolerance, and performance monitoring. The system combines multiple distributed system paradigms in a unified simulation environment.

## 1. System Architecture Diagram

```mermaid
graph TB
    subgraph "Distributed System Simulator"
        GUI["SimulatorGUI
        User Interface
        Control Panel
        Performance Monitoring"]
        
        subgraph "Core Components"
            DSS["DistributedSystemSimulator
            Main Controller
            Simulation Management
            Metrics Collection"]
            CC["CentralCoordinator
            Sequential Consistency
            Operation Ordering
            Global State Management"]
        end
        
        subgraph "Service Nodes"
            NA["NodeA
            Local Data Store
            Client Operations
            Resource Management"]
            NB["NodeB
            Local Data Store
            Client Operations
            Resource Management"]
            NC["NodeC
            Local Data Store
            Client Operations
            Resource Management"]
        end
        
        subgraph "Naming Services"
            FNS["FlatNamingService
            Simple Key-Value
            O(1) Lookup
            Hash Table Based"]
            SNS["StructuredNamingService
            Hierarchical Paths
            Tree Structure
            Path-based Resolution"]
            DNS["DNSSimulator
            Domain Name Resolution
            IP Address Mapping
            Network Simulation"]
        end
        
        subgraph "Threading & Execution"
            TP["ThreadPool
            Concurrent Execution
            Task Management
            Resource Sharing"]
            ES["ExecutorService
            Scheduled Tasks
            Background Processing
            Lifecycle Management"]
        end
    end
    
    GUI --> DSS
    DSS --> CC
    DSS --> NA
    DSS --> NB
    DSS --> NC
    DSS --> FNS
    DSS --> SNS
    DSS --> DNS
    DSS --> TP
    DSS --> ES
    
    NA -.-> CC
    NB -.-> CC
    NC -.-> CC
    
    NA --> FNS
    NA --> SNS
    NA --> DNS
    NB --> FNS
    NB --> SNS
    NB --> DNS
    NC --> FNS
    NC --> SNS
    NC --> DNS
```

## 2. Process Roles and Responsibilities

```mermaid
graph LR
    subgraph "Process Roles"
        subgraph "Client Processes"
            C1["Client-NodeA
            Issues Operations
            Read/Write Requests
            Consistency Requirements"]
            C2["Client-NodeB
            Issues Operations
            Read/Write Requests
            Consistency Requirements"]
            C3["Client-NodeC
            Issues Operations
            Read/Write Requests
            Consistency Requirements"]
        end
        
        subgraph "Server Processes"
            S1["ServiceNode-A
            Data Storage
            Operation Processing
            State Replication"]
            S2["ServiceNode-B
            Data Storage
            Operation Processing
            State Replication"]
            S3["ServiceNode-C
            Data Storage
            Operation Processing
            State Replication"]
        end
        
        subgraph "Coordination Services"
            COORD["CentralCoordinator
            Sequence Assignment
            Ordering Guarantees
            Consensus Management"]
            SYNC["Synchronization Service
            Eventual Consistency
            State Convergence
            Conflict Resolution"]
        end
        
        subgraph "Naming Services"
            NS1["Flat Naming
            Simple Resolution
            Hash-based Lookup
            Fast Access"]
            NS2["Structured Naming
            Hierarchical Resolution
            Path Navigation
            Organized Access"]
            NS3["DNS Service
            Domain Resolution
            Network Mapping
            Distributed Lookup"]
        end
        
        subgraph "Management Processes"
            PM["Performance Monitor
            Metrics Collection
            Violation Detection
            System Health"]
            GUI_PROC["GUI Process
            User Interface
            Control Operations
            Status Display"]
        end
    end
    
    C1 --> S1
    C2 --> S2
    C3 --> S3
    
    S1 --> COORD
    S2 --> COORD
    S3 --> COORD
    
    S1 --> SYNC
    S2 --> SYNC
    S3 --> SYNC
    
    S1 --> NS1
    S1 --> NS2
    S1 --> NS3
    S2 --> NS1
    S2 --> NS2
    S2 --> NS3
    S3 --> NS1
    S3 --> NS2
    S3 --> NS3
    
    PM --> S1
    PM --> S2
    PM --> S3
    PM --> COORD
    
    GUI_PROC --> S1
    GUI_PROC --> S2
    GUI_PROC --> S3
    GUI_PROC --> COORD
    GUI_PROC --> PM
```

## 3. Communication Methods and Patterns

```mermaid
sequenceDiagram
    participant GUI as GUI Interface
    participant DSS as DistributedSystemSimulator
    participant CC as CentralCoordinator
    participant NA as NodeA
    participant NB as NodeB
    participant NC as NodeC
    participant NS as NamingServices
    
    Note over GUI,NS: System Initialization
    GUI->>DSS: startSimulation()
    DSS->>CC: Initialize coordinator
    DSS->>NA: Start node operations
    DSS->>NB: Start node operations
    DSS->>NC: Start node operations
    DSS->>NS: Register services
    
    Note over GUI,NS: Operation Execution
    GUI->>DSS: performOperation(NodeA, PUT, key, value)
    DSS->>NA: performOperation(PUT, key, value)
    NA->>CC: submitOperation(PUT, key, value, clientId)
    CC->>CC: Assign sequence number
    CC-->>NA: Return operation with sequence
    
    Note over GUI,NS: Sequential Consistency
    CC->>NA: applyOperation(operation)
    CC->>NB: applyOperation(operation)
    CC->>NC: applyOperation(operation)
    
    Note over GUI,NS: Naming Service Lookup
    GUI->>DSS: lookupResource(resourceName, namingType)
    DSS->>NS: lookup(resourceName)
    NS-->>DSS: Return resource location
    DSS-->>GUI: Display result
    
    Note over GUI,NS: Eventual Consistency Sync
    loop Every 1 second
        DSS->>NA: syncWithCoordinator()
        NA->>CC: getOperationsSince(lastSequence)
        CC-->>NA: Return missed operations
        NA->>NA: Apply missed operations
    end
    
    Note over GUI,NS: Failure Recovery
    GUI->>DSS: simulateNodeFailure(NodeB)
    DSS->>NB: simulateFailure()
    NB->>NB: Set isAvailable = false
    
    GUI->>DSS: recoverNode(NodeB)
    DSS->>NB: recover()
    NB->>CC: syncWithCoordinator()
    CC-->>NB: Sync missed operations
    NB->>NB: Set isAvailable = true
```

## 4. Naming Service Architecture

```mermaid
graph TB
    subgraph "Naming Service Layer"
        subgraph "Flat Naming Service"
            FNS_HASH["Hash Table
            name to resource"]
            FNS_OPS["Operations:
            register(name, resource)
            lookup(name)
            remove(name)"]
            FNS_CHAR["Characteristics:
            O(1) lookup time
            Simple structure
            No organization
            Name collision risk"]
        end
        
        subgraph "Structured Naming Service"
            SNS_TREE["Hierarchical Tree
            path to resource"]
            SNS_OPS["Operations:
            register(path, resource)
            lookup(path)
            listChildren(path)
            validatePath(path)"]
            SNS_CHAR["Characteristics:
            Path-based organization
            Tree structure
            Namespace hierarchy
            Better management"]
        end
        
        subgraph "DNS Simulator"
            DNS_MAP["Domain Mapping
            domain to IP address"]
            DNS_OPS["Operations:
            register(domain, ip)
            lookup(domain)
            reverseLookup(ip)
            expireCache()"]
            DNS_CHAR["Characteristics:
            Industry standard
            Network-oriented
            Distributed design
            Cache support"]
        end
    end
    
    subgraph "Resource Registration Examples"
        FLAT_EX["Flat Examples:
        'nodea-service' maps to NodeA
        'database' maps to DB Server
        'cache' maps to Cache Server"]
        
        STRUCT_EX["Structured Examples:
        '/services/nodea-service' maps to NodeA
        '/databases/primary' maps to Primary DB
        '/cache/redis' maps to Redis Cache"]
        
        DNS_EX["DNS Examples:
        'service1.example.com' maps to 192.168.1.10
        'db.example.com' maps to 192.168.1.20
        'api.example.com' maps to 192.168.1.30"]
    end
    
    FNS_HASH --> FLAT_EX
    SNS_TREE --> STRUCT_EX
    DNS_MAP --> DNS_EX
    
    subgraph "Client Access Patterns"
        CLIENT["Client Application"]
        CLIENT --> FNS_HASH
        CLIENT --> SNS_TREE
        CLIENT --> DNS_MAP
    end
```

## 5. Data Replication and Consistency Strategy

```mermaid
graph TB
    subgraph "Consistency Models Implementation"
        subgraph "Sequential Consistency"
            SC_COORD["Central Coordinator
            Global Operation Ordering"]
            SC_SEQ["Sequence Numbers
            Total Order Guarantee"]
            SC_APPLY["Ordered Application
            All Nodes Same Order"]
            
            SC_COORD --> SC_SEQ
            SC_SEQ --> SC_APPLY
        end
        
        subgraph "Eventual Consistency"
            EC_ASYNC["Asynchronous Propagation
            Background Sync Process"]
            EC_CONV["State Convergence
            All Nodes Eventually Consistent"]
            EC_PART["Partition Tolerance
            Temporary Divergence Allowed"]
            
            EC_ASYNC --> EC_CONV
            EC_CONV --> EC_PART
        end
        
        subgraph "Client-Centric Consistency"
            CC_CLIENT["Client Version Tracking
            Per-Client Consistency View"]
            CC_READ["Read-Your-Writes
            Monotonic Read Consistency"]
            CC_WRITE["Write-Follows-Reads
            Monotonic Write Consistency"]
            
            CC_CLIENT --> CC_READ
            CC_READ --> CC_WRITE
        end
    end
    
    subgraph "Data Replication Flow"
        OP_SUB["Operation Submission
        Client to Node"]
        OP_COORD["Coordination
        Node to Central Coordinator"]
        OP_SEQ["Sequence Assignment
        Global Ordering"]
        OP_DIST["Distribution
        Coordinator to All Nodes"]
        OP_APPLY["Application
        Nodes Apply Operation"]
        OP_ACK["Acknowledgment
        Completion Notification"]
        
        OP_SUB --> OP_COORD
        OP_COORD --> OP_SEQ
        OP_SEQ --> OP_DIST
        OP_DIST --> OP_APPLY
        OP_APPLY --> OP_ACK
    end
    
    subgraph "Conflict Resolution"
        DETECT["Conflict Detection
        Version Vector Comparison"]
        RESOLVE["Resolution Strategy
        Last-Writer-Wins / Merge"]
        SYNC["Synchronization
        State Reconciliation"]
        
        DETECT --> RESOLVE
        RESOLVE --> SYNC
    end
    
    SC_APPLY --> OP_DIST
    EC_ASYNC --> OP_APPLY
    CC_CLIENT --> OP_SUB
```

## 6. System Interaction Flow

```mermaid
flowchart TD
    START([System Start])
    
    START --> INIT_GUI[Initialize GUI Components]
    INIT_GUI --> INIT_SIM[Initialize Simulator]
    INIT_SIM --> INIT_NODES[Create Service Nodes]
    INIT_NODES --> INIT_NAMING[Setup Naming Services]
    INIT_NAMING --> INIT_COORD[Initialize Coordinator]
    
    INIT_COORD --> READY{System Ready}
    
    READY -->|User Action| USER_OP[User Operation]
    READY -->|Auto Simulation| AUTO_OP[Automatic Operation]
    READY -->|Monitoring| MONITOR[Performance Monitoring]
    
    USER_OP --> VALIDATE{Validate Input}
    VALIDATE -->|Valid| EXECUTE[Execute Operation]
    VALIDATE -->|Invalid| ERROR[Show Error]
    ERROR --> READY
    
    EXECUTE --> COORD_SUB[Submit to Coordinator]
    COORD_SUB --> ASSIGN_SEQ[Assign Sequence Number]
    ASSIGN_SEQ --> REPLICATE[Replicate to All Nodes]
    
    AUTO_OP --> RANDOM_GEN[Generate Random Operation]
    RANDOM_GEN --> COORD_SUB
    
    REPLICATE --> CHECK_CONSISTENCY{Check Consistency}
    CHECK_CONSISTENCY -->|Consistent| UPDATE_METRICS[Update Performance Metrics]
    CHECK_CONSISTENCY -->|Violation| LOG_VIOLATION[Log Consistency Violation]
    
    LOG_VIOLATION --> UPDATE_METRICS
    UPDATE_METRICS --> UPDATE_GUI[Update GUI Display]
    UPDATE_GUI --> READY
    
    MONITOR --> COLLECT_METRICS[Collect System Metrics]
    COLLECT_METRICS --> ANALYZE[Analyze Performance]
    ANALYZE --> UPDATE_GUI
    
    READY -->|Failure Simulation| FAIL_NODE[Simulate Node Failure]
    FAIL_NODE --> MARK_UNAVAILABLE[Mark Node Unavailable]
    MARK_UNAVAILABLE --> READY
    
    READY -->|Recovery| RECOVER_NODE[Recover Node]
    RECOVER_NODE --> SYNC_STATE[Synchronize State]
    SYNC_STATE --> MARK_AVAILABLE[Mark Node Available]
    MARK_AVAILABLE --> READY
    
    READY -->|Naming Lookup| LOOKUP[Resource Lookup]
    LOOKUP --> SELECT_NAMING{Select Naming Service}
    SELECT_NAMING -->|Flat| FLAT_LOOKUP[Flat Naming Lookup]
    SELECT_NAMING -->|Structured| STRUCT_LOOKUP[Structured Naming Lookup]
    SELECT_NAMING -->|DNS| DNS_LOOKUP[DNS Lookup]
    
    FLAT_LOOKUP --> RETURN_RESULT[Return Result]
    STRUCT_LOOKUP --> RETURN_RESULT
    DNS_LOOKUP --> RETURN_RESULT
    RETURN_RESULT --> LOG_LOOKUP[Log Lookup Result]
    LOG_LOOKUP --> READY
    
    READY -->|Stop Simulation| STOP[Stop Simulation]
    STOP --> CLEANUP[Cleanup Resources]
    CLEANUP --> END([System End])
```

## Design Choices and Rationale

### Name Resolution Strategy

**1. Multiple Naming Services Approach:**
- **Flat Naming:** Chosen for simplicity and performance. O(1) lookup time makes it ideal for frequently accessed resources.
- **Structured Naming:** Implements hierarchical organization similar to file systems and DNS, providing better namespace management.
- **DNS Simulation:** Represents real-world distributed naming, demonstrating industry-standard approaches.

**Rationale:** This multi-service approach allows comparison of different naming strategies and their trade-offs in various scenarios.

### Data Replication Strategy

**1. Hybrid Consistency Model:**
- **Sequential Consistency:** Implemented through a central coordinator that assigns global sequence numbers, ensuring all nodes see operations in the same order.
- **Eventual Consistency:** Background synchronization processes allow nodes to catch up asynchronously, providing better availability.
- **Client-Centric Consistency:** Per-client version tracking ensures read-your-writes consistency for individual clients.

**2. Replication Architecture:**
- **Primary-Secondary Pattern:** Central coordinator acts as the primary for operation ordering.
- **State Machine Replication:** All nodes apply the same operations in the same order.
- **Lazy Propagation:** Operations are propagated asynchronously to improve performance.

### Key Design Benefits

1. **Fault Tolerance:** Node failure simulation and recovery mechanisms demonstrate system resilience.
2. **Performance Monitoring:** Real-time metrics collection allows analysis of consistency-performance trade-offs.
3. **Scalability Demonstration:** Threading model shows how to handle concurrent operations.
4. **Educational Value:** Multiple consistency models and naming services provide comprehensive learning opportunities.

### Trade-off Analysis

**Consistency vs. Availability:**
- Strong consistency (sequential) provides guarantees but may impact availability during partitions.
- Eventual consistency improves availability but allows temporary inconsistencies.

**Performance vs. Consistency:**
- Immediate replication ensures consistency but increases latency.
- Lazy replication reduces latency but may cause temporary divergence.

**Simplicity vs. Organization:**
- Flat naming provides fast lookups but poor organization.
- Structured naming adds overhead but improves management and navigation.

This architecture demonstrates real-world distributed system challenges and solutions, providing a comprehensive platform for understanding distributed computing concepts. 