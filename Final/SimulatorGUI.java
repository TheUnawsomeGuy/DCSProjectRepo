package Final;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.util.Properties;

/**
 * Comprehensive GUI for the Distributed System Simulator
 * Provides controls for simulation and performance analysis
 */
public class SimulatorGUI extends JFrame {
    private DistributedSystemSimulator simulator;
    private ScheduledExecutorService guiUpdateExecutor;
    
    // GUI Components
    private JTextArea logArea;
    private JTable performanceTable;
    private DefaultTableModel performanceTableModel;
    private JLabel statusLabel;
    private JComboBox<String> nodeSelector;
    private JComboBox<String> operationSelector;
    private JTextField keyField;
    private JTextField valueField;
    private JComboBox<String> namingTypeSelector;
    private JTextField lookupField;
    private JButton startButton;
    private JButton stopButton;
    private JProgressBar systemLoadBar;
    
    // Performance tracking components
    private JLabel totalOpsLabel;
    private JLabel consistencyLabel;
    private JLabel latencyLabel;
    private JTextArea analysisArea;

    // New components for enhanced functionality
    private JTextField domainField;
    private JTextField ipField;
    private JSpinner threadPoolSizeSpinner;
    private JSpinner simulationIntervalSpinner;
    private JTable dnsTable;
    private DefaultTableModel dnsTableModel;
    private JTextField configNameField;

    public SimulatorGUI() {
        simulator = new DistributedSystemSimulator();
        initializeGUI();
        setupEventHandlers();
        startGUIUpdates();
    }

    private void initializeGUI() {
        setTitle("Distributed System Simulator - Analysis Tool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Create main panels
        createControlPanel();
        createConfigurationPanel();
        createMonitoringPanel();
        createAnalysisPanel();
        createLogPanel();
        
        // Set up the frame
        pack();
        setLocationRelativeTo(null);
        setSize(1600, 1000);
    }

    private void createControlPanel() {
        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBorder(new TitledBorder("Simulation Controls"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Simulation control buttons
        gbc.gridx = 0; gbc.gridy = 0;
        startButton = new JButton("Start Simulation");
        startButton.setBackground(Color.GREEN);
        controlPanel.add(startButton, gbc);

        gbc.gridx = 1;
        stopButton = new JButton("Stop Simulation");
        stopButton.setBackground(Color.RED);
        stopButton.setEnabled(false);
        controlPanel.add(stopButton, gbc);

        gbc.gridx = 2;
        statusLabel = new JLabel("Status: Stopped");
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        controlPanel.add(statusLabel, gbc);

        // Operation controls
        gbc.gridx = 0; gbc.gridy = 1;
        controlPanel.add(new JLabel("Node:"), gbc);
        gbc.gridx = 1;
        nodeSelector = new JComboBox<>(new String[]{"NodeA", "NodeB", "NodeC"});
        controlPanel.add(nodeSelector, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        controlPanel.add(new JLabel("Operation:"), gbc);
        gbc.gridx = 1;
        operationSelector = new JComboBox<>(new String[]{"PUT", "GET", "DELETE", "DEPOSIT", "WITHDRAW"});
        controlPanel.add(operationSelector, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        controlPanel.add(new JLabel("Key:"), gbc);
        gbc.gridx = 1;
        keyField = new JTextField(10);
        controlPanel.add(keyField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        controlPanel.add(new JLabel("Value:"), gbc);
        gbc.gridx = 1;
        valueField = new JTextField(10);
        controlPanel.add(valueField, gbc);

        gbc.gridx = 2; gbc.gridy = 3;
        JButton executeButton = new JButton("Execute Operation");
        executeButton.addActionListener(e -> executeOperation());
        controlPanel.add(executeButton, gbc);

        // Naming service controls
        gbc.gridx = 0; gbc.gridy = 5;
        controlPanel.add(new JLabel("Naming Type:"), gbc);
        gbc.gridx = 1;
        namingTypeSelector = new JComboBox<>(new String[]{"flat", "structured", "dns"});
        controlPanel.add(namingTypeSelector, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        controlPanel.add(new JLabel("Lookup:"), gbc);
        gbc.gridx = 1;
        lookupField = new JTextField(10);
        controlPanel.add(lookupField, gbc);

        gbc.gridx = 2; gbc.gridy = 6;
        JButton lookupButton = new JButton("Lookup Resource");
        lookupButton.addActionListener(e -> performLookup());
        controlPanel.add(lookupButton, gbc);

        // Failure simulation controls
        gbc.gridx = 0; gbc.gridy = 7;
        JButton failButton = new JButton("Simulate Failure");
        failButton.addActionListener(e -> simulateFailure());
        controlPanel.add(failButton, gbc);

        gbc.gridx = 1; gbc.gridy = 7;
        JButton recoverButton = new JButton("Recover Node");
        recoverButton.addActionListener(e -> recoverNode());
        controlPanel.add(recoverButton, gbc);

        add(controlPanel, BorderLayout.NORTH);
    }

    private void createConfigurationPanel() {
        JTabbedPane configTabs = new JTabbedPane();
        
        // DNS Configuration Tab
        JPanel dnsPanel = createDNSConfigPanel();
        configTabs.addTab("DNS Management", dnsPanel);
        
        // Threading Configuration Tab
        JPanel threadPanel = createThreadConfigPanel();
        configTabs.addTab("Thread Config", threadPanel);
        
        // Save/Load Configuration Tab
        JPanel saveLoadPanel = createSaveLoadPanel();
        configTabs.addTab("Save/Load Config", saveLoadPanel);
        
        configTabs.setBorder(new TitledBorder("Configuration"));
        add(configTabs, BorderLayout.CENTER);
    }

    private JPanel createDNSConfigPanel() {
        JPanel dnsPanel = new JPanel(new BorderLayout());
        
        // DNS Entry Controls
        JPanel dnsControlPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        dnsControlPanel.add(new JLabel("Domain:"), gbc);
        gbc.gridx = 1;
        domainField = new JTextField(15);
        dnsControlPanel.add(domainField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dnsControlPanel.add(new JLabel("IP Address:"), gbc);
        gbc.gridx = 1;
        ipField = new JTextField(15);
        dnsControlPanel.add(ipField, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        JButton addDnsButton = new JButton("Add DNS Entry");
        addDnsButton.addActionListener(e -> addDNSEntry());
        dnsControlPanel.add(addDnsButton, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        JButton removeDnsButton = new JButton("Remove Selected");
        removeDnsButton.addActionListener(e -> removeDNSEntry());
        dnsControlPanel.add(removeDnsButton, gbc);

        gbc.gridx = 3; gbc.gridy = 0;
        JButton clearDnsButton = new JButton("Clear All");
        clearDnsButton.addActionListener(e -> clearDNSEntries());
        dnsControlPanel.add(clearDnsButton, gbc);

        gbc.gridx = 3; gbc.gridy = 1;
        JButton refreshDnsButton = new JButton("Refresh Table");
        refreshDnsButton.addActionListener(e -> refreshDNSTable());
        dnsControlPanel.add(refreshDnsButton, gbc);

        // DNS Table
        String[] dnsColumns = {"Domain", "IP Address"};
        dnsTableModel = new DefaultTableModel(dnsColumns, 0);
        dnsTable = new JTable(dnsTableModel);
        JScrollPane dnsScrollPane = new JScrollPane(dnsTable);
        dnsScrollPane.setPreferredSize(new Dimension(500, 200));

        dnsPanel.add(dnsControlPanel, BorderLayout.NORTH);
        dnsPanel.add(dnsScrollPane, BorderLayout.CENTER);
        
        // Load initial DNS entries
        refreshDNSTable();
        
        return dnsPanel;
    }

    private JPanel createThreadConfigPanel() {
        JPanel threadPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0;
        threadPanel.add(new JLabel("Thread Pool Size:"), gbc);
        gbc.gridx = 1;
        threadPoolSizeSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 50, 1));
        threadPanel.add(threadPoolSizeSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        threadPanel.add(new JLabel("Simulation Interval (ms):"), gbc);
        gbc.gridx = 1;
        simulationIntervalSpinner = new JSpinner(new SpinnerNumberModel(1000, 100, 10000, 100));
        threadPanel.add(simulationIntervalSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JButton applyThreadConfigButton = new JButton("Apply Thread Configuration");
        applyThreadConfigButton.addActionListener(e -> applyThreadConfiguration());
        threadPanel.add(applyThreadConfigButton, gbc);

        gbc.gridy = 3;
        JTextArea threadInfoArea = new JTextArea(8, 40);
        threadInfoArea.setEditable(false);
        threadInfoArea.setText("Thread Configuration Info:\n\n" +
                "• Thread Pool Size: Controls the number of concurrent threads\n" +
                "• Simulation Interval: Time between automated operations\n" +
                "• Lower intervals = higher system load\n" +
                "• Higher thread count = better parallelism but more overhead\n" +
                "• Recommended: 3-10 threads, 500-2000ms interval");
        JScrollPane threadInfoScrollPane = new JScrollPane(threadInfoArea);
        threadPanel.add(threadInfoScrollPane, gbc);

        return threadPanel;
    }

    private JPanel createSaveLoadPanel() {
        JPanel saveLoadPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0;
        saveLoadPanel.add(new JLabel("Config Name:"), gbc);
        gbc.gridx = 1;
        configNameField = new JTextField(20);
        saveLoadPanel.add(configNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JButton saveConfigButton = new JButton("Save Configuration");
        saveConfigButton.addActionListener(e -> saveConfiguration());
        saveLoadPanel.add(saveConfigButton, gbc);

        gbc.gridx = 1;
        JButton loadConfigButton = new JButton("Load Configuration");
        loadConfigButton.addActionListener(e -> loadConfiguration());
        saveLoadPanel.add(loadConfigButton, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JButton loadDefaultButton = new JButton("Load Default Configuration");
        loadDefaultButton.addActionListener(e -> loadDefaultConfiguration());
        saveLoadPanel.add(loadDefaultButton, gbc);

        gbc.gridy = 3;
        JTextArea configInfoArea = new JTextArea(10, 40);
        configInfoArea.setEditable(false);
        configInfoArea.setText("Configuration Management:\n\n" +
                "• Save: Saves current DNS entries, thread settings, and node configurations\n" +
                "• Load: Restores a previously saved configuration\n" +
                "• Default: Loads pre-configured setup with sample data\n\n" +
                "Configurations are saved as .properties files in the configs/ directory\n" +
                "Include DNS entries, thread pool settings, and simulation parameters");
        JScrollPane configInfoScrollPane = new JScrollPane(configInfoArea);
        saveLoadPanel.add(configInfoScrollPane, gbc);

        return saveLoadPanel;
    }

    private void createMonitoringPanel() {
        JPanel monitoringPanel = new JPanel(new BorderLayout());
        monitoringPanel.setBorder(new TitledBorder("Real-time Monitoring"));

        // Performance metrics
        JPanel metricsPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        metricsPanel.setBorder(new TitledBorder("Performance Metrics"));

        totalOpsLabel = new JLabel("Total Operations: 0");
        consistencyLabel = new JLabel("Consistency Violations: 0%");
        latencyLabel = new JLabel("Avg Latency: 0ms");
        systemLoadBar = new JProgressBar(0, 100);
        systemLoadBar.setStringPainted(true);
        systemLoadBar.setString("System Load");

        metricsPanel.add(totalOpsLabel);
        metricsPanel.add(consistencyLabel);
        metricsPanel.add(latencyLabel);
        metricsPanel.add(systemLoadBar);
        metricsPanel.add(new JLabel(""));
        metricsPanel.add(new JLabel(""));

        // Performance table
        String[] columnNames = {"Timestamp", "Operations", "Violations %", "Latency (ms)"};
        performanceTableModel = new DefaultTableModel(columnNames, 0);
        performanceTable = new JTable(performanceTableModel);
        performanceTable.setPreferredScrollableViewportSize(new Dimension(500, 150));
        JScrollPane tableScrollPane = new JScrollPane(performanceTable);
        tableScrollPane.setBorder(new TitledBorder("Performance History"));

        monitoringPanel.add(metricsPanel, BorderLayout.NORTH);
        monitoringPanel.add(tableScrollPane, BorderLayout.CENTER);

        add(monitoringPanel, BorderLayout.WEST);
    }

    private void createAnalysisPanel() {
        JPanel analysisPanel = new JPanel(new BorderLayout());
        analysisPanel.setBorder(new TitledBorder("Trade-off Analysis"));

        analysisArea = new JTextArea(15, 40);
        analysisArea.setEditable(false);
        analysisArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane analysisScrollPane = new JScrollPane(analysisArea);

        JPanel analysisButtonPanel = new JPanel(new FlowLayout());
        JButton analyzeButton = new JButton("Analyze Performance");
        analyzeButton.addActionListener(e -> performAnalysis());
        
        JButton compareButton = new JButton("Compare Naming");
        compareButton.addActionListener(e -> compareNamingServices());
        
        JButton consistencyButton = new JButton("Analyze Consistency");
        consistencyButton.addActionListener(e -> analyzeConsistencyModels());

        analysisButtonPanel.add(analyzeButton);
        analysisButtonPanel.add(compareButton);
        analysisButtonPanel.add(consistencyButton);

        analysisPanel.add(analysisButtonPanel, BorderLayout.NORTH);
        analysisPanel.add(analysisScrollPane, BorderLayout.CENTER);

        add(analysisPanel, BorderLayout.EAST);
    }

    private void createLogPanel() {
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(new TitledBorder("System Logs"));

        logArea = new JTextArea(10, 80);
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        JScrollPane logScrollPane = new JScrollPane(logArea);

        JPanel logButtonPanel = new JPanel(new FlowLayout());
        JButton clearLogsButton = new JButton("Clear Logs");
        clearLogsButton.addActionListener(e -> logArea.setText(""));
        
        JButton statusButton = new JButton("Show System Status");
        statusButton.addActionListener(e -> showSystemStatus());

        logButtonPanel.add(clearLogsButton);
        logButtonPanel.add(statusButton);

        logPanel.add(logButtonPanel, BorderLayout.NORTH);
        logPanel.add(logScrollPane, BorderLayout.CENTER);

        add(logPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        startButton.addActionListener(e -> startSimulation());
        stopButton.addActionListener(e -> stopSimulation());
    }

    private void startGUIUpdates() {
        guiUpdateExecutor = Executors.newScheduledThreadPool(1);
        guiUpdateExecutor.scheduleAtFixedRate(this::updateGUI, 0, 1, TimeUnit.SECONDS);
    }

    private void updateGUI() {
        SwingUtilities.invokeLater(() -> {
            List<PerformanceMetric> metrics = simulator.getMetrics();
            if (!metrics.isEmpty()) {
                PerformanceMetric latest = metrics.get(metrics.size() - 1);
                totalOpsLabel.setText("Total Operations: " + latest.getTotalOperations());
                consistencyLabel.setText(String.format("Consistency Violations: %.1f%%", 
                                                     latest.getConsistencyViolations() * 100));
                latencyLabel.setText(String.format("Avg Latency: %.1fms", latest.getAverageLatency()));
                
                int load = Math.min(100, latest.getTotalOperations() % 100);
                systemLoadBar.setValue(load);
                
                if (performanceTableModel.getRowCount() > 20) {
                    performanceTableModel.removeRow(0);
                }
                
                Object[] row = {
                    new java.util.Date(latest.getTimestamp()),
                    latest.getTotalOperations(),
                    String.format("%.2f", latest.getConsistencyViolations() * 100),
                    String.format("%.1f", latest.getAverageLatency())
                };
                performanceTableModel.addRow(row);
            }
            
            statusLabel.setText("Status: " + (simulator.isRunning() ? "Running" : "Stopped"));
        });
    }

    private void startSimulation() {
        simulator.startSimulation();
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        logArea.append("Simulation started...\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void stopSimulation() {
        simulator.stopSimulation();
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        logArea.append("Simulation stopped.\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void executeOperation() {
        String node = (String) nodeSelector.getSelectedItem();
        String operation = (String) operationSelector.getSelectedItem();
        String key = keyField.getText().trim();
        String value = valueField.getText().trim();
        
        if (key.isEmpty() && !operation.equals("DEPOSIT") && !operation.equals("WITHDRAW")) {
            key = "defaultKey";
        }
        if (value.isEmpty()) {
            value = "defaultValue";
        }
        
        simulator.performOperation(node, operation, key, value);
        logArea.append(String.format("Executed %s on %s (key: %s, value: %s)\n", 
                                   operation, node, key, value));
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void performLookup() {
        String namingType = (String) namingTypeSelector.getSelectedItem();
        String resourceName = lookupField.getText().trim();
        
        if (resourceName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a resource name to lookup.");
            return;
        }
        
        String result = simulator.lookupResource(resourceName, namingType);
        logArea.append(String.format("Lookup (%s): %s -> %s\n", namingType, resourceName, result));
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void simulateFailure() {
        String node = (String) nodeSelector.getSelectedItem();
        simulator.simulateNodeFailure(node);
        logArea.append("Simulated failure for " + node + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void recoverNode() {
        String node = (String) nodeSelector.getSelectedItem();
        simulator.recoverNode(node);
        logArea.append("Recovered " + node + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void showSystemStatus() {
        logArea.append("\n=== SYSTEM STATUS ===\n");
        simulator.printSystemStatus();
        logArea.append("Status printed to console.\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void performAnalysis() {
        StringBuilder analysis = new StringBuilder();
        analysis.append("=== SYSTEM PERFORMANCE ANALYSIS ===\n\n");
        
        List<PerformanceMetric> metrics = simulator.getMetrics();
        if (metrics.isEmpty()) {
            analysis.append("No performance data available yet.\nStart the simulation to collect metrics.\n");
        } else {
            PerformanceMetric latest = metrics.get(metrics.size() - 1);
            
            analysis.append("Current Performance:\n");
            analysis.append(String.format("- Total Operations: %d\n", latest.getTotalOperations()));
            analysis.append(String.format("- Consistency Violations: %.2f%%\n", 
                                         latest.getConsistencyViolations() * 100));
            analysis.append(String.format("- Average Latency: %.2f ms\n\n", latest.getAverageLatency()));
            
            if (metrics.size() > 5) {
                analysis.append("Performance Trends:\n");
                double avgViolations = metrics.stream()
                    .mapToDouble(PerformanceMetric::getConsistencyViolations)
                    .average().orElse(0.0);
                double avgLatency = metrics.stream()
                    .mapToDouble(PerformanceMetric::getAverageLatency)
                    .average().orElse(0.0);
                
                analysis.append(String.format("- Avg Consistency Violations: %.2f%%\n", avgViolations * 100));
                analysis.append(String.format("- Avg System Latency: %.2f ms\n", avgLatency));
                
                if (avgViolations > 0.05) {
                    analysis.append("Warning: High consistency violations!\n");
                }
                if (avgLatency > 200) {
                    analysis.append("Warning: High latency detected!\n");
                }
            }
        }
        
        analysisArea.setText(analysis.toString());
    }

    private void compareNamingServices() {
        StringBuilder comparison = new StringBuilder();
        comparison.append("=== NAMING SERVICES COMPARISON ===\n\n");
        
        comparison.append("1. FLAT NAMING:\n");
        comparison.append("   + Simple O(1) lookup time\n");
        comparison.append("   + Fast registration\n");
        comparison.append("   - No organization\n");
        comparison.append("   - Name collision issues\n\n");
        
        comparison.append("2. STRUCTURED NAMING:\n");
        comparison.append("   + Hierarchical organization\n");
        comparison.append("   + Better name management\n");
        comparison.append("   - Slightly higher overhead\n");
        comparison.append("   - Path format required\n\n");
        
        comparison.append("3. DNS SIMULATION:\n");
        comparison.append("   + Industry standard\n");
        comparison.append("   + Distributed capability\n");
        comparison.append("   - Network dependency\n");
        comparison.append("   - Potential cache issues\n\n");
        
        comparison.append("RECOMMENDATIONS:\n");
        comparison.append("- Flat: Simple local services\n");
        comparison.append("- Structured: Organized systems\n");
        comparison.append("- DNS: Internet-scale apps\n");
        
        analysisArea.setText(comparison.toString());
    }

    private void analyzeConsistencyModels() {
        StringBuilder analysis = new StringBuilder();
        analysis.append("=== CONSISTENCY MODELS ANALYSIS ===\n\n");
        
        analysis.append("1. SEQUENTIAL CONSISTENCY:\n");
        analysis.append("   - All nodes see same order\n");
        analysis.append("   - Strong guarantees\n");
        analysis.append("   - Higher latency\n");
        analysis.append("   - Best for: Financial systems\n\n");
        
        analysis.append("2. EVENTUAL CONSISTENCY:\n");
        analysis.append("   - Temporary divergence allowed\n");
        analysis.append("   - Lower latency\n");
        analysis.append("   - Higher availability\n");
        analysis.append("   - Best for: Social media\n\n");
        
        analysis.append("3. CLIENT-CENTRIC:\n");
        analysis.append("   - Per-client consistency\n");
        analysis.append("   - Read-your-writes guarantee\n");
        analysis.append("   - Good balance\n");
        analysis.append("   - Best for: User sessions\n\n");
        
        analysis.append("TRADE-OFFS:\n");
        analysis.append("Strong Consistency <-> Availability\n");
        analysis.append("Low Latency <-> Consistency\n");
        analysis.append("Partition Tolerance <-> Consistency\n\n");
        
        List<PerformanceMetric> metrics = simulator.getMetrics();
        if (!metrics.isEmpty()) {
            PerformanceMetric latest = metrics.get(metrics.size() - 1);
            analysis.append("CURRENT STATUS:\n");
            if (latest.getConsistencyViolations() < 0.01) {
                analysis.append("Strong consistency maintained\n");
            } else if (latest.getConsistencyViolations() < 0.05) {
                analysis.append("Moderate consistency violations\n");
            } else {
                analysis.append("High consistency violations\n");
            }
        }
        
        analysisArea.setText(analysis.toString());
    }

    // DNS Management Methods
    private void addDNSEntry() {
        String domain = domainField.getText().trim();
        String ip = ipField.getText().trim();
        
        if (domain.isEmpty() || ip.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both domain and IP address.");
            return;
        }
        
        // Validate IP format (basic validation)
        if (!ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid IP address format (e.g., 192.168.1.1).");
            return;
        }
        
        simulator.addDNSEntry(domain, ip);
        refreshDNSTable();
        domainField.setText("");
        ipField.setText("");
        logArea.append("Added DNS entry: " + domain + " -> " + ip + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void removeDNSEntry() {
        int selectedRow = dnsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a DNS entry to remove.");
            return;
        }
        
        String domain = (String) dnsTableModel.getValueAt(selectedRow, 0);
        simulator.removeDNSEntry(domain);
        refreshDNSTable();
        logArea.append("Removed DNS entry: " + domain + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void clearDNSEntries() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to clear all DNS entries? This will reset to defaults.",
            "Confirm Clear", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            simulator.clearDNSEntries();
            refreshDNSTable();
            logArea.append("Cleared all DNS entries and reset to defaults.\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        }
    }

    private void refreshDNSTable() {
        // Clear existing rows
        dnsTableModel.setRowCount(0);
        
        // Get current DNS entries from simulator
        java.util.Set<String> domains = simulator.getAllDNSDomains();
        for (String domain : domains) {
            String ip = simulator.lookupDNSEntry(domain);
            if (ip != null) {
                dnsTableModel.addRow(new Object[]{domain, ip});
            }
        }
    }

    // Thread Configuration Methods
    private void applyThreadConfiguration() {
        int threadPoolSize = (Integer) threadPoolSizeSpinner.getValue();
        int simulationInterval = (Integer) simulationIntervalSpinner.getValue();
        
        simulator.configureThreads(threadPoolSize, simulationInterval);
        logArea.append(String.format("Applied thread configuration: Pool size=%d, Interval=%dms\n", 
                                    threadPoolSize, simulationInterval));
        logArea.setCaretPosition(logArea.getDocument().getLength());
        
        JOptionPane.showMessageDialog(this, 
            "Thread configuration applied successfully!\nRestart simulation for changes to take effect.",
            "Configuration Applied", JOptionPane.INFORMATION_MESSAGE);
    }

    // Configuration Save/Load Methods
    private void saveConfiguration() {
        String configName = configNameField.getText().trim();
        if (configName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a configuration name.");
            return;
        }
        
        try {
            // Create configs directory if it doesn't exist
            File configDir = new File("configs");
            if (!configDir.exists()) {
                configDir.mkdirs();
            }
            
            Properties config = new Properties();
            
            // Save thread configuration
            config.setProperty("thread.pool.size", threadPoolSizeSpinner.getValue().toString());
            config.setProperty("simulation.interval", simulationIntervalSpinner.getValue().toString());
            
            // Save DNS entries
            java.util.Set<String> domains = simulator.getAllDNSDomains();
            config.setProperty("dns.count", String.valueOf(domains.size()));
            int i = 0;
            for (String domain : domains) {
                String ip = simulator.lookupDNSEntry(domain);
                if (ip != null) {
                    config.setProperty("dns." + i + ".domain", domain);
                    config.setProperty("dns." + i + ".ip", ip);
                    i++;
                }
            }
            
            // Save node configuration
            config.setProperty("nodes", String.join(",", simulator.getNodeIds()));
            
            // Save current simulation state
            config.setProperty("simulation.running", String.valueOf(simulator.isRunning()));
            
            File configFile = new File(configDir, configName + ".properties");
            try (FileOutputStream fos = new FileOutputStream(configFile)) {
                config.store(fos, "Distributed System Simulator Configuration - " + configName);
            }
            
            logArea.append("Configuration saved: " + configFile.getAbsolutePath() + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
            
            JOptionPane.showMessageDialog(this, "Configuration saved successfully!", 
                                        "Save Successful", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving configuration: " + e.getMessage(), 
                                        "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadConfiguration() {
        String configName = configNameField.getText().trim();
        if (configName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a configuration name.");
            return;
        }
        
        try {
            File configFile = new File("configs", configName + ".properties");
            if (!configFile.exists()) {
                JOptionPane.showMessageDialog(this, "Configuration file not found: " + configName, 
                                            "File Not Found", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Properties config = new Properties();
            try (FileInputStream fis = new FileInputStream(configFile)) {
                config.load(fis);
            }
            
            // Load thread configuration
            if (config.containsKey("thread.pool.size")) {
                threadPoolSizeSpinner.setValue(Integer.parseInt(config.getProperty("thread.pool.size")));
            }
            if (config.containsKey("simulation.interval")) {
                simulationIntervalSpinner.setValue(Integer.parseInt(config.getProperty("simulation.interval")));
            }
            
            // Load DNS entries
            simulator.clearDNSEntries();
            if (config.containsKey("dns.count")) {
                int dnsCount = Integer.parseInt(config.getProperty("dns.count"));
                for (int i = 0; i < dnsCount; i++) {
                    String domain = config.getProperty("dns." + i + ".domain");
                    String ip = config.getProperty("dns." + i + ".ip");
                    if (domain != null && ip != null) {
                        simulator.addDNSEntry(domain, ip);
                    }
                }
            }
            
            refreshDNSTable();
            
            logArea.append("Configuration loaded: " + configFile.getAbsolutePath() + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
            
            JOptionPane.showMessageDialog(this, "Configuration loaded successfully!", 
                                        "Load Successful", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error loading configuration: " + e.getMessage(), 
                                        "Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDefaultConfiguration() {
        // Set default thread configuration
        threadPoolSizeSpinner.setValue(5);
        simulationIntervalSpinner.setValue(1000);
        
        // Reset DNS to defaults
        simulator.clearDNSEntries();
        
        // Add some sample DNS entries
        simulator.addDNSEntry("service1.example.com", "192.168.1.10");
        simulator.addDNSEntry("service2.example.com", "192.168.1.11");
        simulator.addDNSEntry("database.example.com", "192.168.1.20");
        simulator.addDNSEntry("api.example.com", "192.168.1.30");
        simulator.addDNSEntry("cache.example.com", "192.168.1.40");
        
        refreshDNSTable();
        
        logArea.append("Default configuration loaded with sample DNS entries.\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
        
        JOptionPane.showMessageDialog(this, "Default configuration loaded successfully!", 
                                    "Default Loaded", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void dispose() {
        if (guiUpdateExecutor != null) {
            guiUpdateExecutor.shutdown();
        }
        if (simulator != null) {
            simulator.stopSimulation();
        }
        super.dispose();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SimulatorGUI().setVisible(true);
        });
    }
} 