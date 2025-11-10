import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.*;

public class ArithmeticGame extends JFrame implements ActionListener {

    // --- Theme ---
    private static final Color BG = new Color(245, 245, 255);
    private static final Color CARD = new Color(250, 250, 250);
    private static final Color ACCENT = new Color(100, 149, 237);
    private static final Color GREEN = new Color(144, 238, 144);
    private static final Color RED = new Color(255, 182, 193);

    // --- Game Logic ---
    private final QuestionGenerator generator = new QuestionGenerator();
    private String selectedOperation = "+";
    private GameLevel selectedLevel = GameLevel.LEVEL1;

    // --- Components ---
    private final RoundedButton submitButton = new RoundedButton("SUBMIT", 20, ACCENT);
    private final RoundedButton continueButton = new RoundedButton("CONTINUE", 20, new Color(34, 139, 34));
    private final RoundedButton exitButton = new RoundedButton("EXIT", 20, new Color(220, 20, 60));

    private final JTextField answerField = new JTextField(10);
    private final JLabel num1Label = new JLabel("0", SwingConstants.CENTER);
    private final JLabel operatorLabel = new JLabel("+", SwingConstants.CENTER);
    private final JLabel num2Label = new JLabel("0", SwingConstants.CENTER);
    private final JLabel correctScoreLabel = new JLabel("0", SwingConstants.CENTER);
    private final JLabel incorrectScoreLabel = new JLabel("0", SwingConstants.CENTER);

    private final ButtonGroup operationGroup = new ButtonGroup();
    private final ButtonGroup levelGroup = new ButtonGroup();

    // --- State ---
    private int correctCount = 0;
    private int incorrectCount = 0;
    private boolean questionAnswered = false; // Has user answered current question
    private boolean readyForNext = false;     // Ready to proceed to next
    private boolean processingEnter = false; // Prevent concurrent Enter handling
    private enum InputState { ANSWERING, REVEALED }
    private InputState inputState = InputState.ANSWERING;

    public ArithmeticGame() {
        installNimbusLaf();
        setTitle("Arithmetic Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createTopArea(), BorderLayout.CENTER);
        add(createBottomArea(), BorderLayout.SOUTH);

        getContentPane().setBackground(BG);
        configureButtons();
        generateNewQuestion();

        setMinimumSize(new Dimension(900, 560));
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(true);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                scaleUI();
            }
        });
    }

    // --- Gradient background ---
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        GradientPaint gp = new GradientPaint(0, 0, BG, 0, getHeight(), new Color(230, 240, 255));
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
        super.paint(g);
    }

    // --- Top Area ---
    private JPanel createTopArea() {
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setOpaque(false);
        topContainer.setBorder(BorderFactory.createEmptyBorder(18, 24, 12, 24));

        JLabel header = new JLabel("Practice Your Arithmetic!", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 36));
        header.setForeground(new Color(50, 50, 50));
        topContainer.add(header, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.X_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));

        RoundedPanel equationPanel = new RoundedPanel(20, CARD);
        equationPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 8));

        styleNumberLabel(num1Label);
        styleNumberLabel(num2Label);

        operatorLabel.setFont(new Font("Montserrat", Font.BOLD, 44));
        operatorLabel.setOpaque(false);

        JLabel equals = new JLabel("=", SwingConstants.CENTER);
        equals.setFont(new Font("Montserrat", Font.BOLD, 44));

        styleAnswerField();

        equationPanel.add(num1Label);
        equationPanel.add(operatorLabel);
        equationPanel.add(num2Label);
        equationPanel.add(equals);
        equationPanel.add(answerField);

        // Right column buttons
        JPanel rightCol = new JPanel();
        rightCol.setOpaque(false);
        rightCol.setLayout(new BoxLayout(rightCol, BoxLayout.Y_AXIS));
        rightCol.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));

        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightCol.add(Box.createVerticalStrut(8));
        rightCol.add(submitButton);
        rightCol.add(Box.createVerticalStrut(12));
        rightCol.add(continueButton);
        rightCol.add(Box.createVerticalStrut(12));
        rightCol.add(exitButton);

        // Initially hide Continue and Exit
        continueButton.setVisible(false);
        exitButton.setVisible(false);

        center.add(Box.createHorizontalGlue());
        center.add(equationPanel);
        center.add(Box.createHorizontalStrut(30));
        center.add(rightCol);
        center.add(Box.createHorizontalGlue());

        topContainer.add(center, BorderLayout.CENTER);
        return topContainer;
    }

    // --- Bottom Area ---
    private JPanel createBottomArea() {
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(12, 24, 18, 24));

        JPanel left = new JPanel(new GridLayout(1, 2, 28, 0));
        left.setOpaque(false);
        left.add(createOperationPanel());
        left.add(createLevelPanel());
        bottom.add(left, BorderLayout.WEST);

        JPanel scoreContainer = new JPanel(new BorderLayout());
        scoreContainer.setOpaque(false);

        RoundedPanel scoreBox = new RoundedPanel(20, CARD);
        scoreBox.setLayout(new GridLayout(2, 2, 10, 8));
        scoreBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), "SCORE:"),
                BorderFactory.createEmptyBorder(12, 18, 12, 18)));

        JLabel correctTitle = new JLabel("CORRECT", SwingConstants.CENTER);
        JLabel incorrectTitle = new JLabel("INCORRECT", SwingConstants.CENTER);
        styleScoreLabel(correctScoreLabel, GREEN, new Color(180, 240, 180));
        styleScoreLabel(incorrectScoreLabel, RED, new Color(255, 200, 210));

        scoreBox.add(correctTitle);
        scoreBox.add(incorrectTitle);
        scoreBox.add(correctScoreLabel);
        scoreBox.add(incorrectScoreLabel);

        JPanel rightWrap = new JPanel(new BorderLayout());
        rightWrap.setOpaque(false);
        rightWrap.add(scoreBox, BorderLayout.SOUTH);

        bottom.add(rightWrap, BorderLayout.EAST);

        return bottom;
    }

    private JPanel createOperationPanel() {
        RoundedPanel opPanel = new RoundedPanel(20, CARD);
        opPanel.setLayout(new GridLayout(5, 1, 12, 12));
        opPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), "OPERATIONS"));

        String[] ops = { "+", "-", "*", "/", "%" };
        String[] names = {
            "➕ Addition (+)",
            "➖ Subtraction (-)",
            "✖ Multiplication (*)",
            "➗ Division (/)",
            "% Modulo (%)"
        };

        for (int i = 0; i < ops.length; i++) {
            JRadioButton rb = new JRadioButton(names[i]);
            rb.setActionCommand(ops[i]);
            styleRadioButton(rb);
            rb.setBackground(CARD);
            rb.addActionListener(this::handleControlSelection);
            operationGroup.add(rb);
            opPanel.add(rb);
            if (ops[i].equals(selectedOperation))
                rb.setSelected(true);
        }
        return opPanel;
    }

    private JPanel createLevelPanel() {
        RoundedPanel lvlPanel = new RoundedPanel(20, CARD);
        lvlPanel.setLayout(new GridLayout(GameLevel.values().length, 1, 12, 12));
        lvlPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), "LEVELS"));

        String[] levelNames = { "Beginner", "Easy", "Intermediate", "Advanced", "Expert", "Legend" };
        int idx = 0;
        for (GameLevel gl : GameLevel.values()) {
            String label = "LEVEL " + gl.name().substring(5) + " - " + levelNames[idx] + " (" + gl.getDisplay() + ")";
            JRadioButton rb = new JRadioButton(label);
            rb.setActionCommand(gl.name());
            styleRadioButton(rb);
            rb.setBackground(CARD);
            rb.addActionListener(this::handleControlSelection);
            levelGroup.add(rb);
            lvlPanel.add(rb);
            if (gl == selectedLevel)
                rb.setSelected(true);
            idx++;
        }
        return lvlPanel;
    }

    // --- Styling ---
    private void styleNumberLabel(JLabel l) {
        l.setFont(new Font("Montserrat", Font.BOLD, 42));
        l.setOpaque(true);
        l.setBackground(CARD);
        l.setBorder(BorderFactory.createCompoundBorder(
                createShadowBorder(),
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1)
        ));
        l.setPreferredSize(new Dimension(140, 90));
        l.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void styleAnswerField() {
        answerField.setFont(new Font("Montserrat", Font.BOLD, 40));
        answerField.setHorizontalAlignment(SwingConstants.CENTER);
        answerField.setBorder(BorderFactory.createLineBorder(ACCENT, 1, true));
        answerField.setPreferredSize(new Dimension(220, 90));

        // Allow numbers, dot, negative, and arithmetic symbols
        ((AbstractDocument) answerField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (string.matches("[0-9.+\\-*/%]*")) super.insertString(fb, offset, string, attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (text.matches("[0-9.+\\-*/%]*")) super.replace(fb, offset, length, text, attrs);
            }
        });

        answerField.addActionListener(e -> {
            // Serialize Enter handling so rapid double presses behave as: 1) check/reveal, 2) proceed
            if (processingEnter) return;
            processingEnter = true;
            try {
                if (inputState == InputState.ANSWERING) {
                    // First Enter: submit answer
                    checkAnswer();
                } else if (inputState == InputState.REVEALED) {
                    // Second Enter: proceed to next
                    proceedNextQuestion();
                }
            } finally {
                processingEnter = false;
            }
        });

        // Add key binding for Enter to trigger the continue button
        continueButton.addActionListener(e -> proceedNextQuestion());
    }

    private void styleScoreLabel(JLabel label, Color textColor, Color borderColor) {
        label.setFont(new Font("Montserrat", Font.BOLD, 24));
        label.setForeground(textColor);
        label.setOpaque(true);
        label.setBackground(CARD);
        label.setBorder(BorderFactory.createLineBorder(borderColor, 1));
        label.setPreferredSize(new Dimension(80, 50));
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void styleRadioButton(JRadioButton rb) {
        rb.setFont(new Font("Montserrat", Font.BOLD, 20));
        rb.setForeground(new Color(60, 60, 100));
        rb.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        rb.setFocusPainted(false);
    }

    private static Border createShadowBorder() {
        return BorderFactory.createMatteBorder(0, 0, 3, 3, new Color(0, 0, 0, 15));
    }

    private void configureButtons() {
        submitButton.addActionListener(this);
        continueButton.addActionListener(e -> proceedNextQuestion());
        exitButton.addActionListener(e -> System.exit(0));

        // Global Enter binding: when the UI has revealed the answer (Continue visible),
        // pressing Enter anywhere in the window will proceed to the next question.
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "globalEnter");
        am.put("globalEnter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (processingEnter) return;
                processingEnter = true;
                try {
                    if (inputState == InputState.REVEALED) {
                        proceedNextQuestion();
                    }
                } finally {
                    processingEnter = false;
                }
            }
        });
    }

    // --- Shared logic for proceeding to next question ---
    private void proceedNextQuestion() {
        // Only allow proceeding if we've revealed an answer
        if (!readyForNext && inputState != InputState.REVEALED) return;

        // Reset state for next question
        questionAnswered = false;
        readyForNext = false;
        inputState = InputState.ANSWERING;
        answerField.setEditable(true);
        answerField.setText(""); // Clear field before new question
        generateNewQuestion();
        // Reset UI buttons and field styling
        submitButton.setVisible(true);
        continueButton.setVisible(false);
        exitButton.setVisible(false);
        answerField.setBackground(CARD);
        answerField.requestFocusInWindow();
    }

    // --- Game Logic ---
    private void handleControlSelection(ActionEvent e) {
        String cmd = e.getActionCommand();
        if ("+-*/%".contains(cmd))
            selectedOperation = cmd;
        else
            selectedLevel = GameLevel.valueOf(cmd);
        operatorLabel.setText(selectedOperation);
        // Reset state and generate question for new selection
        questionAnswered = false;
        readyForNext = false;
        inputState = InputState.ANSWERING;
        generateNewQuestion();
        // Reset UI for new selection
        submitButton.setVisible(true);
        continueButton.setVisible(false);
        exitButton.setVisible(false);
        answerField.setBackground(CARD);
        answerField.setEditable(true);
    }

    private void generateNewQuestion() {
        generator.generateQuestion(selectedOperation, selectedLevel);
        num1Label.setText(String.valueOf(generator.getNum1()));
        num2Label.setText(String.valueOf(generator.getNum2()));
        operatorLabel.setText(generator.getOperator());
        answerField.setText("");
        answerField.setEditable(true);
        answerField.requestFocusInWindow();
        // Ensure input state is ANSWERING when a new question appears
        inputState = InputState.ANSWERING;
    }

    private void checkAnswer() {
        String userText = answerField.getText().trim();

        // If already answered or waiting for next, ignore further input
        if (userText.isEmpty()) return;
        if (questionAnswered || readyForNext) return;
        // Prevent check immediately after new question
        if (!answerField.isEditable()) return;

        // Mark as answered immediately to avoid double-processing (race or rapid Enter presses)
        questionAnswered = true;

        boolean correct;
        double userAnswer;

        try {
            userAnswer = Double.parseDouble(userText);
            correct = Math.abs(userAnswer - generator.getCorrectAnswer()) < 0.0001;
        } catch (NumberFormatException ex) {
            correct = false;
        }

        // Show correct answer and color feedback
        answerField.setText(formatAnswer(generator.getCorrectAnswer()));
        answerField.setBackground(correct ? GREEN : RED);
        answerField.setEditable(false);

        submitButton.setVisible(false);
        continueButton.setVisible(true);
        exitButton.setVisible(true);

        updateScore(correct);
        readyForNext = true;
    }

    // Format answer: no decimal if not needed
    private String formatAnswer(double ans) {
        if (ans == (long) ans) {
            return String.format("%d", (long) ans);
        } else {
            return String.valueOf(ans);
        }
    }

    private void updateScore(boolean correct) {
        if (correct) {
            correctScoreLabel.setText(String.valueOf(++correctCount));
        } else {
            incorrectScoreLabel.setText(String.valueOf(++incorrectCount));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton)
            checkAnswer();
    }

    private void scaleUI() {
        int w = getWidth();
        int base = Math.max(16, Math.min(getHeight(), w) / 20);
        Font numFont = new Font("Montserrat", Font.BOLD, Math.max(36, base + 16));
        num1Label.setFont(numFont);
        num2Label.setFont(numFont);
        operatorLabel.setFont(numFont);
        answerField.setFont(numFont);
        correctScoreLabel.setFont(new Font("Montserrat", Font.BOLD, Math.max(18, base)));
        incorrectScoreLabel.setFont(new Font("Montserrat", Font.BOLD, Math.max(18, base)));
    }

    private static void installNimbusLaf() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
                if ("Nimbus".equals(info.getName()))
                    UIManager.setLookAndFeel(info.getClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ignored) {
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ArithmeticGame::new);
    }
}

// --- Rounded Button ---
class RoundedButton extends JButton {
    private final int radius;

    public RoundedButton(String text, int radius, Color bg) {
        super(text);
        this.radius = radius;
        setBackground(bg);
        setForeground(Color.WHITE);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setFont(new Font("Segoe UI", Font.BOLD, 16));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color baseColor = getBackground();
        if (getModel().isPressed()) g2.setColor(baseColor.darker());
        else if (getModel().isRollover()) g2.setColor(baseColor.brighter());
        else g2.setColor(baseColor);

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isRollover()) g2.setColor(getBackground().brighter());
        else g2.setColor(getBackground().darker().darker());

        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        g2.dispose();
    }
}

// --- Rounded Panel ---
class RoundedPanel extends JPanel {
    private final int radius;
    private final Color bg;

    public RoundedPanel(int radius, Color bg) {
        this.radius = radius;
        this.bg = bg;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(0, 0, 0, 20));
        g2.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, radius, radius);

        g2.setColor(bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        super.paintComponent(g2);
        g2.dispose();
    }
}

// --- Helper Classes ---
enum GameLevel {
    LEVEL1(1, 100), LEVEL2(101, 500), LEVEL3(501, 1000);
    private final int min, max;

    GameLevel(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getMin() { return min; }
    public int getMax() { return max; }
    public String getDisplay() { return min + "-" + max; }
}

class QuestionGenerator {
    private final Random random = new Random();
    private int num1, num2;
    private double correctAnswer;
    private String operator = "+";

    public void generateQuestion(String op, GameLevel level) {
        operator = op;
        int min = level.getMin(), max = level.getMax();

        switch (op) {
            case "+" -> { num1 = r(min, max); num2 = r(min, max); correctAnswer = num1 + num2; }
            case "-" -> { num1 = r(min, max); num2 = r(min, max); if(num2>num1){int t=num1;num1=num2;num2=t;} correctAnswer = num1 - num2; }
            case "*" -> { num1 = r(min, max); num2 = r(min, max); correctAnswer = num1 * num2; }
            case "/" -> {
                // Generate both operands within the selected level bounds so Division questions
                // respect the chosen GameLevel (e.g., Level3 -> 501..1000).
                // Allow non-integer results (floating-point division) but ensure divisor != 0.
                num1 = r(min, max);
                num2 = Math.max(1, r(min, max));
                correctAnswer = (double) num1 / num2;
            }
            case "%" -> { num2 = Math.max(1, r(min, max)); num1 = r(min, max); correctAnswer = num1 % num2; }
        }
    }

    private int r(int min, int max) { return random.nextInt((max - min) + 1) + min; }

    public int getNum1() { return num1; }
    public int getNum2() { return num2; }
    public String getOperator() { return operator; }
    public double getCorrectAnswer() { return correctAnswer; }
}
