# Distributed System Simulator GUI - User Guide

This comprehensive GUI provides a powerful interface for simulating and analyzing distributed systems with support for various naming services, consistency models, and performance analysis.

## Table of Contents
1. [Getting Started](#getting-started)
2. [Main Interface Overview](#main-interface-overview)
3. [Simulation Controls](#simulation-controls)
4. [DNS Management](#dns-management)
5. [Thread Configuration](#thread-configuration)
6. [Configuration Save/Load](#configuration-saveload)
7. [Performance Monitoring](#performance-monitoring)
8. [Analysis Tools](#analysis-tools)
9. [Advanced Features](#advanced-features)
10. [Troubleshooting](#troubleshooting)

## Getting Started

### Prerequisites
- Java 8 or higher
- All source files in the `Final` package

### Running the Application
```bash
# Compile all Java files
javac Final/*.java

# Run the GUI application
java Final.SimulatorGUI
```

### First Launch
1. The application starts with a stopped simulation
2. Default configuration includes 3 nodes (NodeA, NodeB, NodeC)
3. Basic DNS entries are pre-loaded
4. Default thread pool size is 5 with 1000ms intervals

## Main Interface Overview

The GUI is organized into several main sections:

### Top Panel - Simulation Controls
- **Start/Stop Simulation**: Controls the main simulation
- **Node Operations**: Execute operations on specific nodes
- **Naming Service Lookups**: Test different naming services
- **Failure Simulation**: Simulate node failures and recovery

### Center Panel - Configuration Tabs
- **DNS Management**: Add, remove, and manage DNS entries
- **Thread Config**: Configure thread pool and simulation timing
- **Save/Load Config**: Manage configuration files

### Left Panel - Real-time Monitoring
- **Performance Metrics**: Live performance statistics
- **Performance History**: Historical data table

### Right Panel - Analysis Tools
- **Trade-off Analysis**: Compare different approaches
- **Performance Analysis**: Detailed performance insights
- **Consistency Analysis**: Consistency model comparisons

### Bottom Panel - System Logs
- **Activity Logs**: Real-time system activity
- **System Status**: Current state information

## Simulation Controls

### Starting/Stopping Simulation
1. **Start Simulation**: Begins automated operations across all nodes
2. **Stop Simulation**: Halts all automated operations
3. **Status Indicator**: Shows current simulation state

### Manual Operations
1. **Select Node**: Choose from NodeA, NodeB, or NodeC
2. **Select Operation**: 
   - `PUT`: Store a key-value pair
   - `GET`: Retrieve a value by key
   - `DELETE`: Remove a key-value pair
   - `DEPOSIT`: Add to account balance
   - `WITHDRAW`: Subtract from account balance
3. **Enter Key/Value**: Specify operation parameters
4. **Execute**: Perform the operation

### Naming Service Testing
1. **Select Naming Type**:
   - `flat`: Simple flat namespace
   - `structured`: Hierarchical namespace
   - `dns`: Domain Name System simulation
2. **Enter Resource Name**: Name to lookup
3. **Lookup**: Perform the naming resolution

### Failure Simulation
- **Simulate Failure**: Make selected node unavailable
- **Recover Node**: Restore failed node to operation

## DNS Management

The DNS Management tab provides comprehensive DNS configuration:

### Adding DNS Entries
1. Enter **Domain Name** (e.g., `api.example.com`)
2. Enter **IP Address** (e.g., `192.168.1.100`)
3. Click **Add DNS Entry**
4. Entry appears in the DNS table

### Managing Existing Entries
- **View All Entries**: DNS table shows all current mappings
- **Remove Entry**: Select row and click "Remove Selected"
- **Clear All**: Reset to default DNS entries
- **Refresh Table**: Update display with current entries

### DNS Entry Validation
- Domain names are case-insensitive
- IP addresses must follow standard IPv4 format (x.x.x.x)
- Duplicate domains will overwrite existing entries

### Default DNS Entries
- `localhost` → `127.0.0.1`
- `example.com` → `192.0.2.1`
- `www.example.com` → `192.0.2.1`

## Thread Configuration

### Thread Pool Settings
- **Thread Pool Size**: Number of concurrent threads (1-50)
  - Recommended: 3-10 for most scenarios
  - Higher values increase parallelism but add overhead
- **Simulation Interval**: Time between operations (100-10000ms)
  - Lower values create higher system load
  - Higher values provide more stable simulation

### Applying Configuration
1. Adjust spinners to desired values
2. Click **Apply Thread Configuration**
3. **Important**: Restart simulation for changes to take effect

### Performance Impact
- **More Threads**: Better parallelism, higher resource usage
- **Lower Intervals**: More realistic high-load scenarios
- **Higher Intervals**: Better for observing individual operations

## Configuration Save/Load

### Saving Configurations
1. Enter a **Config Name** (no file extension needed)
2. Click **Save Configuration**
3. File saved to `configs/[name].properties`
4. Includes:
   - DNS entries
   - Thread settings
   - Node configurations
   - Current simulation state

### Loading Configurations
1. Enter existing **Config Name**
2. Click **Load Configuration**
3. All settings restored from file
4. DNS table automatically updated

### Default Configuration
- Click **Load Default Configuration** for pre-configured setup
- Includes sample DNS entries and optimal settings
- Useful for quick demos or reset scenarios

### Configuration Files
- Stored in `configs/` directory (auto-created)
- Standard Java Properties format
- Can be manually edited if needed
- Include timestamps and descriptions

## Performance Monitoring

### Real-time Metrics
- **Total Operations**: Cumulative operation count
- **Consistency Violations**: Percentage of consistency issues
- **Average Latency**: Mean operation response time
- **System Load**: Visual load indicator

### Performance History Table
- **Timestamp**: When measurement was taken
- **Operations**: Total operations at that time
- **Violations %**: Consistency violation percentage
- **Latency (ms)**: Average latency measurement
- **Auto-scrolling**: Latest entries always visible
- **Limited History**: Keeps last 20 measurements

### Monitoring Frequency
- Metrics updated every 1 second
- Performance data collected every 2 seconds
- GUI remains responsive during heavy simulation

## Analysis Tools

### Performance Analysis
Click **Analyze Performance** for detailed insights:
- Current performance snapshot
- Trend analysis (when sufficient data available)
- Warning alerts for concerning metrics
- Recommendations for optimization

### Naming Services Comparison
Click **Compare Naming** for feature comparison:
- **Flat Naming**: Pros, cons, and use cases
- **Structured Naming**: Hierarchical benefits
- **DNS Simulation**: Internet-scale considerations
- **Recommendations**: Best choice for different scenarios

### Consistency Models Analysis
Click **Analyze Consistency** for deep dive:
- **Sequential Consistency**: Strong guarantees
- **Eventual Consistency**: High availability approach
- **Client-centric**: Balanced approach
- **Trade-offs**: CAP theorem implications
- **Current Status**: Assessment of system consistency

## Advanced Features

### Log Management
- **Clear Logs**: Remove all log entries
- **Auto-scroll**: Logs automatically scroll to latest
- **System Status**: Print detailed status to console
- **Operation Logging**: All operations logged with timestamps

### Node Management
- **Multi-node Operations**: Operations distributed across nodes
- **Failure Recovery**: Automatic sync after recovery
- **Load Balancing**: Operations distributed for realism

### Network Simulation
- **Partition Recovery**: Simulated network healing
- **Latency Simulation**: Realistic timing delays
- **Failure Modes**: Various failure scenarios

## Troubleshooting

### Common Issues

#### Simulation Won't Start
- **Check**: All nodes initialized correctly
- **Solution**: Restart application if needed
- **Logs**: Check system logs for error messages

#### DNS Entries Not Saving
- **Check**: Valid domain and IP format
- **Check**: Write permissions for `configs/` directory
- **Solution**: Try default configuration first

#### High Consistency Violations
- **Cause**: High system load or network issues
- **Solution**: Increase simulation intervals
- **Solution**: Reduce thread pool size

#### Performance Lag
- **Cause**: Too many concurrent operations
- **Solution**: Stop simulation temporarily
- **Solution**: Clear performance history
- **Solution**: Reduce thread count

### Configuration File Issues
- **Location**: Files in `configs/` directory
- **Format**: Standard Java Properties
- **Backup**: Keep copies of working configurations
- **Reset**: Use "Load Default" to restore working state

### Memory Usage
- **Long Running**: Clear logs periodically
- **Performance Data**: Limited to last 20 entries
- **Large Configurations**: Monitor system resources

## Tips for Effective Use

### Performance Testing
1. Start with default configuration
2. Gradually increase load (reduce intervals)
3. Monitor consistency violations
4. Save successful configurations

### Demonstration Scenarios
1. Load default configuration for quick demo
2. Show different naming services in action
3. Simulate failures to show recovery
4. Compare consistency models with analysis tools

### Educational Use
1. Use analysis tools to explain concepts
2. Show trade-offs between different approaches
3. Experiment with configuration changes
4. Save different scenarios as named configurations

### Development Testing
1. Create configurations for different test scenarios
2. Use performance monitoring to validate changes
3. Export configurations for team sharing
4. Document performance baselines

## Support

For additional help:
- Check the console output for detailed error messages
- Verify all Java files are compiled correctly
- Ensure proper file permissions for configuration directory
- Review system requirements and dependencies

The simulator provides comprehensive logging to help diagnose issues and understand system behavior. 