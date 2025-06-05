package gui;

import factory.Factory;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FactoryGUI {
    private final Factory factory;
    private final JFrame frame;
    private final JLabel bodyLabel;
    private final JLabel engineLabel;
    private final JLabel accessoryLabel;
    private final JLabel carLabel;
    private final JLabel taskLabel;
    private final JLabel productionLabel;
    private final JSlider bodySpeedSlider;
    private final JSlider engineSpeedSlider;
    private final JSlider accessorySpeedSlider;
    private final JSlider dealerSpeedSlider;
    private Timer timer;

    public FactoryGUI(Factory factory) {
        this.factory = factory;
        frame = new JFrame("Car Factory");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(9, 2, 10, 10));

        bodyLabel = new JLabel("Bodies: 0");
        engineLabel = new JLabel("Engines: 0");
        accessoryLabel = new JLabel("Accessories: 0");
        carLabel = new JLabel("Cars in Storage: 0 / Produced: 0");
        taskLabel = new JLabel("Task Queue: 0 / Completed: 0");
        productionLabel = new JLabel("Produced - Bodies: 0, Engines: 0, Accessories: 0");

        bodySpeedSlider = new JSlider(JSlider.HORIZONTAL, 1000, 30000, 10000);
        engineSpeedSlider = new JSlider(JSlider.HORIZONTAL, 1000, 30000, 10000);
        accessorySpeedSlider = new JSlider(JSlider.HORIZONTAL, 1000, 30000, 10000);
        dealerSpeedSlider = new JSlider(JSlider.HORIZONTAL, 1000, 30000, 10000);

        bodySpeedSlider.setMajorTickSpacing(1000);
        bodySpeedSlider.setPaintTicks(true);
        engineSpeedSlider.setMajorTickSpacing(1000);
        engineSpeedSlider.setPaintTicks(true);
        accessorySpeedSlider.setMajorTickSpacing(1000);
        accessorySpeedSlider.setPaintTicks(true);
        dealerSpeedSlider.setMajorTickSpacing(1000);
        dealerSpeedSlider.setPaintTicks(true);

        bodySpeedSlider.addChangeListener(e -> factory.setBodySupplierSpeed(bodySpeedSlider.getValue()));
        engineSpeedSlider.addChangeListener(e -> factory.setEngineSupplierSpeed(engineSpeedSlider.getValue()));
        accessorySpeedSlider.addChangeListener(e -> factory.setAccessorySupplierSpeed(accessorySpeedSlider.getValue()));
        dealerSpeedSlider.addChangeListener(e -> factory.setDealerSpeed(dealerSpeedSlider.getValue()));

        JButton shutdownButton = new JButton("Shutdown Factory");
        shutdownButton.addActionListener(e -> {
            frame.dispose();
            factory.shutdown();
        });

        JPanel bodySliderPanel = new JPanel(new BorderLayout());
        bodySliderPanel.add(new JLabel("Body supply time interval (ms):"), BorderLayout.NORTH);
        bodySliderPanel.add(bodySpeedSlider, BorderLayout.CENTER);

        JPanel engineSliderPanel = new JPanel(new BorderLayout());
        engineSliderPanel.add(new JLabel("Engine supply time interval (ms):"), BorderLayout.NORTH);
        engineSliderPanel.add(engineSpeedSlider, BorderLayout.CENTER);

        JPanel accessorySliderPanel = new JPanel(new BorderLayout());
        accessorySliderPanel.add(new JLabel("Accessory supply time interval (ms):"), BorderLayout.NORTH);
        accessorySliderPanel.add(accessorySpeedSlider, BorderLayout.CENTER);

        JPanel dealerSliderPanel = new JPanel(new BorderLayout());
        dealerSliderPanel.add(new JLabel("Dealer acquire time interval (ms):"), BorderLayout.NORTH);
        dealerSliderPanel.add(dealerSpeedSlider, BorderLayout.CENTER);



        frame.add(bodyLabel);
        frame.add(engineLabel);
        frame.add(accessoryLabel);
        frame.add(carLabel);
        frame.add(taskLabel);
        frame.add(productionLabel);

        frame.add(new JLabel(""));
        frame.add(bodySliderPanel);
        frame.add(new JLabel(""));
        frame.add(engineSliderPanel);
        frame.add(new JLabel(""));
        frame.add(accessorySliderPanel);
        frame.add(new JLabel(""));
        frame.add(dealerSliderPanel);
        frame.add(new JLabel(""));
        frame.add(shutdownButton);


        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    public void start() {
        Timer timer = new Timer(100, e -> SwingUtilities.invokeLater(this::updateLabels));
        timer.start();
        frame.setVisible(true);
    }


    private void updateLabels() {
        bodyLabel.setText("Bodies in storage: " + factory.getBodyStorageSize());
        engineLabel.setText("Engines in storage: " + factory.getEngineStorageSize());
        accessoryLabel.setText("Accessories in storage: " + factory.getAccessoryStorageSize());
        carLabel.setText("Cars in Storage: " + factory.getCarStorageSize() + " / Produced: " + factory.getCarsProduced());


        taskLabel.setText("Task Queue: " + factory.getTaskQueueSize() + " / In process:" + factory.getTasksInProcess() +
                " / Completed: " + factory.getTasksCompletedByWorkers());
        productionLabel.setText(String.format("Produced details - Bodies: %d, Engines: %d, Accessories: %d",
                factory.getBodiesProduced(), factory.getEnginesProduced(), factory.getAccessoriesProduced()));
    }
}