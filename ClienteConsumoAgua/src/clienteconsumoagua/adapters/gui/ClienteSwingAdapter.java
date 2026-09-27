package clienteconsumoagua.adapters.gui;

import clienteconsumoagua.domain.model.RespuestaConsumo;
import clienteconsumoagua.ports.input.SolicitarConsumoAguaUseCase;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ClienteSwingAdapter {
    private final SolicitarConsumoAguaUseCase casoDeUso;
    private final JFrame ventana = new JFrame("Consumo de Agua por Habitante");
    private final JTextField txtLitros = new JTextField();
    private final JTextField txtHabitantes = new JTextField();
    private final JLabel lblResultado = new JLabel("Consumo promedio: -- litros por habitante", SwingConstants.CENTER);

    public ClienteSwingAdapter(SolicitarConsumoAguaUseCase casoDeUso) {
        this.casoDeUso = casoDeUso;
        construirInterfaz();
    }

    public void mostrar() {
        ventana.setVisible(true);
    }

    private void construirInterfaz() {
        ventana.setSize(500, 400);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null);
        ventana.setResizable(false);
        JPanel principal = new JPanel(new BorderLayout(10, 10));
        principal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        JLabel titulo = new JLabel("CONSUMO DE AGUA", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        principal.add(titulo, BorderLayout.NORTH);
        JPanel datos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        agregarFila(datos, gbc, 0, "Litros consumidos en el día:", txtLitros);
        agregarFila(datos, gbc, 1, "Habitantes de la vivienda:", txtHabitantes);
        JButton calcular = new JButton("CALCULAR");
        JButton limpiar = new JButton("LIMPIAR");
        gbc.gridy = 2;
        gbc.gridx = 0;
        datos.add(calcular, gbc);
        gbc.gridx = 1;
        datos.add(limpiar, gbc);
        lblResultado.setFont(new Font("Arial", Font.BOLD, 16));
        calcular.addActionListener(event -> calcular());
        limpiar.addActionListener(event -> limpiar());
        principal.add(datos, BorderLayout.CENTER);
        principal.add(lblResultado, BorderLayout.SOUTH);
        ventana.add(principal);
    }

    private void agregarFila(JPanel panel, GridBagConstraints gbc, int fila, String texto, JTextField campo) {
        gbc.gridy = fila;
        gbc.gridx = 0;
        gbc.weightx = 0.5;
        panel.add(new JLabel(texto), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(campo, gbc);
    }

    private void calcular() {
        String litrosTexto = txtLitros.getText().trim();
        String habitantesTexto = txtHabitantes.getText().trim();
        if (litrosTexto.isEmpty() || habitantesTexto.isEmpty()) {
            mostrarError("Debe ingresar todos los datos.");
            return;
        }
        double litros;
        int habitantes;
        try {
            litros = Double.parseDouble(litrosTexto);
        } catch (NumberFormatException ex) {
            mostrarError("La cantidad de litros debe ser un número válido.");
            return;
        }
        try {
            habitantes = Integer.parseInt(habitantesTexto);
        } catch (NumberFormatException ex) {
            mostrarError("La cantidad de habitantes debe ser un número entero.");
            return;
        }
        if (!Double.isFinite(litros) || litros <= 0) {
            mostrarError("Los litros consumidos deben ser mayores que cero.");
            return;
        }
        if (habitantes <= 0) {
            mostrarError("La cantidad de habitantes debe ser mayor que cero.");
            return;
        }
        RespuestaConsumo respuesta = casoDeUso.solicitar(litros, habitantes);
        if (respuesta.exitosa()) {
            lblResultado.setText(String.format(Locale.US, "Consumo promedio: %.2f litros por habitante", respuesta.promedio()));
        } else {
            mostrarError(respuesta.mensaje());
        }
    }

    private void limpiar() {
        txtLitros.setText("");
        txtHabitantes.setText("");
        lblResultado.setText("Consumo promedio: -- litros por habitante");
        txtLitros.requestFocus();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(ventana, mensaje, "Dato incorrecto", JOptionPane.ERROR_MESSAGE);
    }
}