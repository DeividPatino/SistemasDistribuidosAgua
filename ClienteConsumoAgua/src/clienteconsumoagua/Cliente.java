/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package clienteconsumoagua;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;


/**
 *
 * @author patino
 */
public class Cliente extends JFrame {

    /**
     * @param args the command line arguments
     */
    // Datos de conexión
    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    // Componentes de la interfaz
    private JTextField txtLitros;
    private JTextField txtHabitantes;
    private JLabel lblResultado;

    public Cliente() {
        configurarVentana();
        crearInterfaz();
    }

    private void configurarVentana() {
        setTitle("Consumo de Agua por Habitante");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void crearInterfaz() {

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Título
        JLabel lblTitulo = new JLabel(
                "CONSUMO DE AGUA",
                SwingConstants.CENTER
        );

        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));

        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Panel de datos
        JPanel panelDatos = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Etiqueta litros
        JLabel lblLitros = new JLabel("Litros consumidos en el día:");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;

        panelDatos.add(lblLitros, gbc);

        // Campo litros
        txtLitros = new JTextField();

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;

        panelDatos.add(txtLitros, gbc);

        // Etiqueta habitantes
        JLabel lblHabitantes = new JLabel("Habitantes de la vivienda:");

        gbc.gridx = 0;
        gbc.gridy = 1;

        panelDatos.add(lblHabitantes, gbc);

        // Campo habitantes
        txtHabitantes = new JTextField();

        gbc.gridx = 1;
        gbc.gridy = 1;

        panelDatos.add(txtHabitantes, gbc);

        // Botón calcular
        JButton btnCalcular = new JButton("CALCULAR");

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;

        panelDatos.add(btnCalcular, gbc);

        // Botón limpiar
        JButton btnLimpiar = new JButton("LIMPIAR");

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;

        panelDatos.add(btnLimpiar, gbc);

        // Resultado
        lblResultado = new JLabel(
                "Consumo promedio: -- litros por habitante",
                SwingConstants.CENTER
        );

        lblResultado.setFont(new Font("Arial", Font.BOLD, 16));

        // Eventos de los botones
        btnCalcular.addActionListener(e -> calcularConsumo());

        btnLimpiar.addActionListener(e -> limpiarCampos());

        panelPrincipal.add(panelDatos, BorderLayout.CENTER);
        panelPrincipal.add(lblResultado, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private void calcularConsumo() {

        String litrosTexto = txtLitros.getText().trim();
        String habitantesTexto = txtHabitantes.getText().trim();

        // Validar campos vacíos
        if (litrosTexto.isEmpty() || habitantesTexto.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar todos los datos.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        double litrosTotales;
        int habitantes;

        // Convertir y validar litros
        try {
            litrosTotales = Double.parseDouble(litrosTexto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "La cantidad de litros debe ser un número válido.",
                    "Dato incorrecto",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Convertir y validar habitantes
        try {
            habitantes = Integer.parseInt(habitantesTexto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "La cantidad de habitantes debe ser un número entero.",
                    "Dato incorrecto",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Validar valores positivos
        if (litrosTotales <= 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Los litros consumidos deben ser mayores que cero.",
                    "Dato incorrecto",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (habitantes <= 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "La cantidad de habitantes debe ser mayor que cero.",
                    "Dato incorrecto",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Comunicación con el servidor
        try (
                Socket socket = new Socket(HOST, PUERTO);
                DataOutputStream salida = new DataOutputStream(
                        socket.getOutputStream()
                );
                DataInputStream entrada = new DataInputStream(
                        socket.getInputStream()
                )
        ) {

            // Enviar los datos originales al servidor
            salida.writeDouble(litrosTotales);
            salida.writeInt(habitantes);
            salida.flush();

            // Recibir el resultado calculado por el servidor
            double promedio = entrada.readDouble();

            // Mostrar resultado
            lblResultado.setText(
                    String.format(
                            "Consumo promedio: %.2f litros por habitante",
                            promedio
                    )
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo conectar con el servidor.\n"
                    + "Verifique que el servidor esté ejecutándose.",
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void limpiarCampos() {

        txtLitros.setText("");
        txtHabitantes.setText("");

        lblResultado.setText(
                "Consumo promedio: -- litros por habitante"
        );

        txtLitros.requestFocus();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            Cliente ventana = new Cliente();
            ventana.setVisible(true);
        });
    }
}