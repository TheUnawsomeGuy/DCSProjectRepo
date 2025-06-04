# DNS Simulation Project

This project simulates a distributed naming system that demonstrates how different naming services work in distributed computing environments. The simulation includes flat naming, structured naming, and DNS resolution mechanisms.

## Overview

The DNS simulation creates a network environment where resources can be located using different naming strategies. Resources represent network services or files that exist at specific locations, and various naming services help clients find these resources efficiently.

## Core Components

### Resource.java

The Resource class serves as the fundamental data structure for the entire simulation. Each resource represents a network entity such as a web server, database, or file that has both an identifier and a physical location. The class stores the resource ID (like a hostname) and its network location (like an IP address or node identifier). This abstraction allows the naming services to maintain mappings between human-readable names and actual network addresses.

### FlatNamingService.java

The flat naming service implements a simple dictionary-based approach to name resolution. It maintains a direct mapping between resource names and their corresponding Resource objects using a HashMap. Names in this system are unstructured, meaning there is no hierarchy or organization beyond the basic key-value relationship. The service supports registration of new resources, removal of existing ones, and lookup operations that return the location of a requested resource. This approach is similar to how local host files work or simple DNS caching mechanisms.

### StructuredNamingService.java

The structured naming service provides hierarchical name organization similar to file system paths or DNS domain structures. Resource names must follow a path-like format starting with a forward slash, creating a tree-like namespace. This allows for better organization and potentially more efficient lookups in large systems. The service validates path formats and maintains the hierarchical structure while providing the same basic operations as the flat naming service. This approach mirrors how modern DNS systems organize domains into hierarchical structures.

### DNSSimulator.java

The DNS simulator provides a realistic domain name resolution service that maps domain names to IP addresses. It operates independently of the Resource class and directly maintains string-to-string mappings between domain names and IP addresses. The simulator converts all domain names to lowercase for case-insensitive lookups and provides the core DNS functionality that real-world internet infrastructure relies upon. This component demonstrates how traditional DNS servers resolve human-readable domain names into machine-readable IP addresses.

### Node.java

The Node class represents individual computers or servers in the distributed system. Each node can operate in two modes: either using a centralized DNS service for name resolution or maintaining its own local flat naming service. Nodes that use DNS can resolve domain names to find resources anywhere in the network, while nodes with local naming services can only find resources they have registered locally. This design choice simulates real-world scenarios where some systems rely on global DNS infrastructure while others maintain local service registries.

### LogicSim.java

The main simulation class orchestrates the entire system and demonstrates how all components work together. It creates instances of all naming services, registers sample resources across different naming systems, and simulates various lookup scenarios. The simulation shows how a client node can use DNS to find resources, how different naming services can be used for different purposes, and how nodes can maintain both local and global naming capabilities. The scenarios demonstrate practical use cases such as web service discovery, database location, and local resource management.

## How the Simulation Works

The simulation begins by initializing three separate naming services and creating sample resources that represent different network services. Resources are registered in multiple naming systems simultaneously, demonstrating how the same physical resource might be accessible through different naming schemes.

The simulation creates different types of nodes to show various network configurations. A client node configured with DNS access can resolve domain names globally, while compute nodes might maintain local resource registries for services they host directly.

Multiple scenarios demonstrate different aspects of distributed naming. DNS resolution shows how clients find services using domain names, flat naming demonstrates simple administrative lookups, structured naming shows hierarchical organization benefits, and local lookups simulate how nodes access their own resources efficiently.

## Running the Simulation

Compile all Java files in the DNSSim package and run the LogicSim class to see the complete demonstration. The simulation will show each naming service in action and demonstrate how different approaches to naming affect resource discovery in distributed systems.

The output shows the progression from service initialization through resource registration to actual lookup operations, providing insight into how each naming strategy handles the same basic problem of mapping names to locations in distributed computing environments. 