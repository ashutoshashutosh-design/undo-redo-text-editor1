import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

// Main class
public class UndoRedoEditor extends JFrame {

    JTextArea textArea;

    Stack<String> undoStack = new Stack<>();
    Stack<String> redoStack = new Stack<>();

    // Linked List Node
    class Node {
        String text;
        Node next;

        Node(String text) {
            this.text = text;
            this.next = null;
        }
    }

    Node head = null;
    Node tail = null;

    // Add text to linked list
    void addHistory(String text) {
        Node newNode = new Node(text);

        if (head == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
    }

    // Constructor
    UndoRedoEditor() {

        setTitle("Undo Redo Text Editor");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 18));

        JScrollPane scrollPane =
            new JScrollPane(textArea);

        JButton undoButton = new JButton("Undo");
        JButton redoButton = new JButton("Redo");
        JButton clearButton = new JButton("Clear");

        JPanel panel = new JPanel();

        panel.add(undoButton);
        panel.add(redoButton);
        panel.add(clearButton);

        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        // Initial state
        addHistory("");

        // Save changes
        textArea.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String currentText = textArea.getText();

                if (undoStack.empty() ||
                    !undoStack.peek().equals(currentText)) {

                    undoStack.push(currentText);
                    redoStack.clear();
                    addHistory(currentText);
                }
            }
        });

        // Undo operation
        undoButton.addActionListener(e -> {

            if (undoStack.size() > 1) {

                String current = undoStack.pop();
                redoStack.push(current);

                textArea.setText(undoStack.peek());

            } else {
                JOptionPane.showMessageDialog(
                    this, "Nothing to Undo");
            }
        });

        // Redo operation
        redoButton.addActionListener(e -> {

            if (!redoStack.empty()) {

                String text = redoStack.pop();
                undoStack.push(text);

                textArea.setText(text);

            } else {
                JOptionPane.showMessageDialog(
                    this, "Nothing to Redo");
            }
        });

        // Clear operation
        clearButton.addActionListener(e -> {

            if (!textArea.getText().isEmpty()) {
                undoStack.push("");
                redoStack.clear();
                addHistory("");
                textArea.setText("");
            }
        });
    }

    // Main method
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            UndoRedoEditor editor =
                new UndoRedoEditor();

            editor.setVisible(true);
        });
    }
}