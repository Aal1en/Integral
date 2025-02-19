import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IntegralCalculatorApp extends JFrame {
    private JTextField lowerBoundField;
    private JTextField upperBoundField;
    private JTextField stepField;
    private JTable table;
    private DefaultTableModel tableModel;

    public IntegralCalculatorApp() {
        setTitle("Integral Calculator");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Создание панели для ввода данных
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        inputPanel.add(new JLabel("Lower Bound:"));
        lowerBoundField = new JTextField();
        inputPanel.add(lowerBoundField);

        inputPanel.add(new JLabel("Upper Bound:"));
        upperBoundField = new JTextField();
        inputPanel.add(upperBoundField);

        inputPanel.add(new JLabel("Step:"));
        stepField = new JTextField();
        inputPanel.add(stepField);

        // Создание таблицы
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Lower Bound");
        tableModel.addColumn("Upper Bound");
        tableModel.addColumn("Step");
        tableModel.addColumn("Result");

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Разрешаем выделение одной строки
        table.setEnabled(true); // Разрешаем взаимодействие с таблицей

        JScrollPane scrollPane = new JScrollPane(table);

        // Создание кнопок
        JButton addButton = new JButton("Add");
        JButton removeButton = new JButton("Remove");
        JButton calculateButton = new JButton("Calculate");

        // Панель для кнопок
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(calculateButton);

        // Добавление компонентов на форму
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Обработчики событий для кнопок
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRow();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeRow();
            }
        });

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateIntegral();
            }
        });
    }

    private void addRow() {
        try {
            double lowerBound = Double.parseDouble(lowerBoundField.getText());
            double upperBound = Double.parseDouble(upperBoundField.getText());
            double step = Double.parseDouble(stepField.getText());

            // Проверка на дубликаты
            if (isRowExists(lowerBound, upperBound, step)) {
                JOptionPane.showMessageDialog(this, "Row with the same values already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            tableModel.addRow(new Object[]{lowerBound, upperBound, step, ""});
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isRowExists(double lowerBound, double upperBound, double step) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            double existingLowerBound = (double) tableModel.getValueAt(i, 0);
            double existingUpperBound = (double) tableModel.getValueAt(i, 1);
            double existingStep = (double) tableModel.getValueAt(i, 2);

            if (existingLowerBound == lowerBound && existingUpperBound == upperBound && existingStep == step) {
                return true;
            }
        }
        return false;
    }

    private void removeRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "No row selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculateIntegral() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            double lowerBound = (double) tableModel.getValueAt(selectedRow, 0);
            double upperBound = (double) tableModel.getValueAt(selectedRow, 1);
            double step = (double) tableModel.getValueAt(selectedRow, 2);

            double result = calculateIntegral(lowerBound, upperBound, step);
            tableModel.setValueAt(result, selectedRow, 3);
        } else {
            JOptionPane.showMessageDialog(this, "No row selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double calculateIntegral(double lowerBound, double upperBound, double step) {
        double integral = 0.0;
        for (double x = lowerBound; x < upperBound; x += step) {
            integral += Math.exp(-x) * step;
        }
        return integral;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new IntegralCalculatorApp().setVisible(true);
            }
        });
    }
}