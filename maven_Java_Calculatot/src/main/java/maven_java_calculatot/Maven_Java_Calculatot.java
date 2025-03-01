package maven_java_calculatot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class Maven_Java_Calculatot extends JFrame {

    private JTextField pantalla;
    private JTextField pantallaOperaciones;
    private double resultado;
    private String operacionPendiente;
    private boolean nuevaOperacion;

    public Maven_Java_Calculatot() {
        this.setIconImage(new ImageIcon(getClass().getResource("calculadora.png")).getImage());

        setTitle("Calculadora");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(32, 33, 36));

        // Panel principal con padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(32, 33, 36));

        // Título
        JLabel tituloLabel = new JLabel("Calculadora Java");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 24));
        tituloLabel.setForeground(Color.WHITE);
        tituloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tituloLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        mainPanel.add(tituloLabel, BorderLayout.NORTH);

        // Panel central para pantallas y botones
        JPanel centralPanel = new JPanel();
        centralPanel.setLayout(new BorderLayout(10, 10));
        centralPanel.setBackground(new Color(32, 33, 36));

        // Panel para las pantallas
        JPanel pantallasPanel = new JPanel();
        pantallasPanel.setLayout(new BorderLayout(5, 5));
        pantallasPanel.setBackground(new Color(32, 33, 36));

        // Pantalla de operaciones (nueva)
        pantallaOperaciones = new JTextField("");
        pantallaOperaciones.setHorizontalAlignment(JTextField.RIGHT);
        pantallaOperaciones.setEditable(false);
        pantallaOperaciones.setFont(new Font("Arial", Font.PLAIN, 16));
        pantallaOperaciones.setBackground(new Color(48, 49, 52));
        pantallaOperaciones.setForeground(new Color(180, 180, 180));
        pantallaOperaciones.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 64, 67), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        pantallasPanel.add(pantallaOperaciones, BorderLayout.NORTH);

        // Pantalla principal
        pantalla = new JTextField("0");
        pantalla.setHorizontalAlignment(JTextField.RIGHT);
        pantalla.setEditable(false);
        pantalla.setFont(new Font("Arial", Font.BOLD, 32));
        pantalla.setBackground(new Color(48, 49, 52));
        pantalla.setForeground(Color.WHITE);
        pantalla.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 64, 67), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        pantallasPanel.add(pantalla, BorderLayout.CENTER);

        centralPanel.add(pantallasPanel, BorderLayout.NORTH);

        // Panel para los botones
        JPanel botonesPanel = new JPanel();
        botonesPanel.setLayout(new GridLayout(5, 4, 10, 10));
        botonesPanel.setBackground(new Color(32, 33, 36));

        // Crear y agregar botones
        String[] botones = {
            "C", "±", "%", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "DEL", "="
        };

        for (String boton : botones) {
            JButton btn = new JButton(boton) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    super.paintComponent(g2);
                    g2.dispose();
                }
            };

            // Ajustar la fuente según el tipo de botón
            if (boton.equals("DEL")) {
                btn.setFont(new Font("Arial", Font.BOLD, 14)); // Fuente más pequeña para DEL
            } else {
                btn.setFont(new Font("Arial", Font.BOLD, 20)); // Tamaño normal para el resto
            }

            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setOpaque(false);

            // Configurar colores según el tipo de botón
            if (boton.matches("[0-9.]")) {
                btn.setBackground(new Color(60, 64, 67));
                btn.setForeground(Color.WHITE);
            } else if (boton.equals("=")) {
                btn.setBackground(new Color(26, 115, 232));
                btn.setForeground(Color.WHITE);
            } else if (boton.equals("C")) {
                btn.setBackground(new Color(234, 67, 53));
                btn.setForeground(Color.WHITE);
            } else if (boton.equals("DEL")) {
                btn.setBackground(new Color(234, 67, 53));
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(new Color(95, 99, 104));
                btn.setForeground(Color.WHITE);
            }

            // Efectos hover
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(btn.getBackground().brighter());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(btn.getBackground().darker());
                }
            });

            if (boton.matches("[0-9.]")) {
                btn.addActionListener(e -> numeroPresionado(boton));
            } else if (boton.matches("[+\\-*/]")) {
                btn.addActionListener(e -> operadorPresionado(boton));
            } else if (boton.equals("=")) {
                btn.addActionListener(e -> calcularResultado());
            } else if (boton.equals("C")) {
                btn.addActionListener(e -> limpiarCalculadora());
            } else if (boton.equals("%")) {
                btn.addActionListener(e -> calcularPorcentaje());
            } else if (boton.equals("±")) {
                btn.addActionListener(e -> cambiarSigno());
            } else if (boton.equals("DEL")) {
                btn.addActionListener(e -> borrarUltimoCaracter());
            }
            botonesPanel.add(btn);
        }

        centralPanel.add(botonesPanel, BorderLayout.CENTER);

        mainPanel.add(centralPanel, BorderLayout.CENTER);

        // Pie de página
        JLabel pieLabel = new JLabel("Desarrollado por: Ing.JCadenas ®");
        pieLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        pieLabel.setForeground(new Color(180, 180, 180));
        pieLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        pieLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        mainPanel.add(pieLabel, BorderLayout.SOUTH);

        add(mainPanel);

        pack();
        setLocationRelativeTo(null);

        // Agregar estas líneas para establecer el foco
        botonesPanel.setFocusable(true);
        botonesPanel.requestFocusInWindow();

        // Hacer que el panel de la pantalla no sea focusable
        pantalla.setFocusable(false);
        pantallaOperaciones.setFocusable(false);

        // Inicializar variables
        resultado = 0;
        operacionPendiente = "";
        nuevaOperacion = true;

        // Configurar ventana
        setSize(350, 580);
        setLocationRelativeTo(null);
        setResizable(false);

        // Configurar las teclas para los números
        for (char numero = '0'; numero <= '9'; numero++) {
            final String digito = String.valueOf(numero);
            configurarTecla(digito, false, e -> numeroPresionado(digito));
        }

        // Configurar el punto decimal
        configurarTecla(".", false, e -> numeroPresionado("."));

        // Configurar operadores
        configurarTecla("+", false, e -> operadorPresionado("+"));
        configurarTecla("-", false, e -> operadorPresionado("-"));
        configurarTecla("*", false, e -> operadorPresionado("*"));
        configurarTecla("/", false, e -> operadorPresionado("/"));

        // Configurar otras teclas
        configurarTecla("ENTER", true, e -> calcularResultado());
        configurarTecla("=", false, e -> calcularResultado());
        configurarTecla("ESCAPE", true, e -> limpiarCalculadora());
        configurarTecla("BACK_SPACE", true, e -> borrarUltimoCaracter());
        configurarTecla("%", false, e -> calcularPorcentaje());
    }

    // Configurar KeyBindings (Captura los botones por teclado fisico)
    private void configurarTecla(String tecla, boolean isKeyCode, ActionListener action) {
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = ((JComponent) getContentPane()).getInputMap(condition);
        ActionMap actionMap = ((JComponent) getContentPane()).getActionMap();

        String actionKey = "Action_" + tecla;
        KeyStroke keyStroke;

        if (isKeyCode) {
            keyStroke = KeyStroke.getKeyStroke(tecla);
        } else {
            keyStroke = KeyStroke.getKeyStroke(tecla.charAt(0));
        }

        inputMap.put(keyStroke, actionKey);
        actionMap.put(actionKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.actionPerformed(e);
            }
        });
    }

    private void numeroPresionado(String digito) {
        if (nuevaOperacion) {
            pantalla.setText(digito);
            nuevaOperacion = false;
        } else {
            pantalla.setText(pantalla.getText() + digito);
        }

        // Actualizar display de operaciones
        if (pantallaOperaciones.getText().isEmpty()) {
            pantallaOperaciones.setText(digito);
        } else if (nuevaOperacion) {
            // Si es un nuevo número después de un operador, agregarlo a la expresión
            pantallaOperaciones.setText(pantallaOperaciones.getText() + digito);
        } else {
            // Si estamos continuando un número, agregarlo a la expresión
            String ultimoCaracter = pantallaOperaciones.getText().substring(pantallaOperaciones.getText().length() - 1);
            if (ultimoCaracter.matches("[0-9.]")) {
                pantallaOperaciones.setText(pantallaOperaciones.getText() + digito);
            } else {
                pantallaOperaciones.setText(pantallaOperaciones.getText() + " " + digito);
            }
        }
    }

    private void operadorPresionado(String operador) {
        if (!pantalla.getText().equals("0")) {
            if (!operacionPendiente.isEmpty()) {
                calcularResultado(false);
            } else {
                resultado = Double.parseDouble(pantalla.getText());
            }

            // Actualizar la expresión con el operador
            String expresionActual = pantallaOperaciones.getText();
            if (expresionActual.endsWith(" ")) {
                // Si ya hay un espacio, solo agregar el operador y otro espacio
                pantallaOperaciones.setText(expresionActual + operador + " ");
            } else {
                // Si no hay espacio, agregar espacio, operador y espacio
                pantallaOperaciones.setText(expresionActual + " " + operador + " ");
            }

            operacionPendiente = operador;
            nuevaOperacion = true;
        }
    }

    private void calcularResultado() {
        calcularResultado(true); // true indica que es el cálculo final
    }

    private void calcularResultado(boolean esFinal) {
        if (!operacionPendiente.isEmpty()) {
            double numeroActual = Double.parseDouble(pantalla.getText());

            switch (operacionPendiente) {
                case "+":
                    resultado += numeroActual;
                    break;
                case "-":
                    resultado -= numeroActual;
                    break;
                case "*":
                    resultado *= numeroActual;
                    break;
                case "/":
                    if (numeroActual != 0) {
                        resultado /= numeroActual;
                    } else {
                        JOptionPane.showMessageDialog(this, "No se puede dividir por cero");
                        limpiarCalculadora();
                        return;
                    }
                    break;
            }

            pantalla.setText(String.valueOf(resultado));

            if (esFinal) {
                // Si es el cálculo final, agregar el signo igual
                if (!pantallaOperaciones.getText().endsWith(" = ")) {
                    pantallaOperaciones.setText(pantallaOperaciones.getText() + " = ");
                }
                operacionPendiente = "";
            }

            nuevaOperacion = true;
        }
    }

    private void limpiarCalculadora() {
        resultado = 0;
        operacionPendiente = "";
        nuevaOperacion = true;
        pantalla.setText("0");
        pantallaOperaciones.setText("");
    }

    private void calcularPorcentaje() {
        try {
            double numero = Double.parseDouble(pantalla.getText());

            if (operacionPendiente.isEmpty()) {
                resultado = numero / 100;
                pantallaOperaciones.setText(numero + "% = ");
            } else {
                double porcentaje = (resultado * numero) / 100;

                // Construir la expresión correcta
                String expresionBase = String.valueOf(resultado);
                pantallaOperaciones.setText(expresionBase + " " + operacionPendiente + " " + numero + "% = ");

                switch (operacionPendiente) {
                    case "+":
                        resultado += porcentaje;
                        break;
                    case "-":
                        resultado -= porcentaje;
                        break;
                    case "*":
                        resultado = porcentaje;
                        break;
                    case "/":
                        if (porcentaje != 0) {
                            resultado /= porcentaje;
                        } else {
                            JOptionPane.showMessageDialog(this, "No se puede dividir por cero");
                            limpiarCalculadora();
                            return;
                        }
                        break;
                }
            }

            pantalla.setText(String.valueOf(resultado));
            operacionPendiente = "";
            nuevaOperacion = true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error en el formato del número");
            limpiarCalculadora();
        }
    }

    private void cambiarSigno() {
        if (!pantalla.getText().equals("0")) {
            double numero = Double.parseDouble(pantalla.getText());
            numero = -numero;
            pantalla.setText(String.valueOf(numero));

            // Actualizar la expresión en la pantalla de operaciones
            String expresion = pantallaOperaciones.getText();
            if (!expresion.isEmpty()) {
                // Buscar el último número en la expresión y cambiar su signo
                String[] partes = expresion.split(" ");
                if (partes.length > 0) {
                    String ultimaParte = partes[partes.length - 1];
                    if (ultimaParte.matches("-?\\d+(\\.\\d+)?")) {
                        partes[partes.length - 1] = String.valueOf(numero);
                        pantallaOperaciones.setText(String.join(" ", partes));
                    }
                }
            }
        }
    }

    private void borrarUltimoCaracter() {
        String textoActual = pantalla.getText();
        String expresionActual = pantallaOperaciones.getText();

        if (textoActual.length() > 0) {
            // Borrar último carácter del display principal
            if (textoActual.length() == 1 || (textoActual.length() == 2 && textoActual.startsWith("-"))) {
                pantalla.setText("0");
                nuevaOperacion = true;
            } else {
                pantalla.setText(textoActual.substring(0, textoActual.length() - 1));
            }

            // Borrar último carácter del display de operaciones
            if (!expresionActual.isEmpty()) {
                if (expresionActual.endsWith(" ")) {
                    pantallaOperaciones.setText(expresionActual.substring(0, expresionActual.length() - 3));
                    operacionPendiente = "";
                } else {
                    pantallaOperaciones.setText(expresionActual.substring(0, expresionActual.length() - 1));
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Maven_Java_Calculatot c = new Maven_Java_Calculatot();
            c.setVisible(true);
        });
    }
}
