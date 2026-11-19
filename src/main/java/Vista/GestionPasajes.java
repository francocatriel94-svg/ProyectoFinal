package Vista;

import Modelo.Lugar;
import Modelo.Pasaje;
import Modelo.Pasajero;
import Modelo.RutaViaje;
import Persistencia.PasajeData;
import Persistencia.PasajeroData;
import Persistencia.RutaViajeData;
import Persistencia.LugarData;
import Validaciones.ValidacionPasaje;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GestionPasajes extends javax.swing.JInternalFrame {

    private PasajeData pasajeData;
    private PasajeroData pasajeroData;
    private RutaViajeData rutaData;
    private LugarData lugarData;
    private DefaultTableModel modeloTabla;

    public GestionPasajes() {
        initComponents();

        pasajeData = new PasajeData();
        pasajeroData = new PasajeroData();
        rutaData = new RutaViajeData();
        lugarData = new LugarData();

        modeloTabla = (DefaultTableModel) tablaPasajes.getModel();

        limpiarCampos();
        cargarTabla();
        cargarComboPasajeros();
        cargarComboRutas();
        cargarLugaresDisponibles();
    }

    private void cargarComboPasajeros() {
        ComboPasajeros.removeAllItems();

        for (Pasajero p : pasajeroData.listarTodosLosPasajeros()) {
            ComboPasajeros.addItem(p);
        }
    }

    private void cargarLugaresDisponibles() {
        ComboLugares.removeAllItems();

        RutaViaje rutaSeleccionada = (RutaViaje) ComboRutas.getSelectedItem();

        if (rutaSeleccionada == null) {
            return;
        }

        int idRuta = rutaSeleccionada.getID_ruta();

        List<Lugar> lugaresDisponibles = lugarData.listarLugaresLibresPorRuta(idRuta);

        // Si estamos modificando un pasaje activo, incluimos su asiento actual.
        int fila = tablaPasajes.getSelectedRow();

        if (fila != -1) {
            boolean pasajeActivo = "Activo".equals(tablaPasajes.getValueAt(fila, 7).toString());
            int idRutaActual = (int) tablaPasajes.getValueAt(fila, 3);
            int idLugarActual = (int) tablaPasajes.getValueAt(fila, 2);

            if (pasajeActivo && idRutaActual == idRuta) {
                Lugar lugarActual = lugarData.buscarLugar(idLugarActual);

                boolean existe = false;

                for (Lugar lugar : lugaresDisponibles) {
                    if (lugar.getID_lugar() == idLugarActual) {
                        existe = true;
                        break;
                    }
                }

                if (lugarActual != null && !existe) {
                    lugaresDisponibles.add(lugarActual);
                }
            }
        }
        for (Lugar lugar : lugaresDisponibles) {
            ComboLugares.addItem(lugar);
        }
        if (ComboLugares.getItemCount() > 0) {
            ComboLugares.setSelectedIndex(0);
        }
    }

    private void cargarComboRutas() {
        ComboRutas.removeAllItems();

        LocalDateTime ahora = LocalDateTime.now();

        int idRutaSeleccionada = -1;
        int fila = tablaPasajes.getSelectedRow();

        if (fila != -1) {
            idRutaSeleccionada = (int) tablaPasajes.getValueAt(fila, 3);
        }

        for (RutaViaje r : rutaData.listarRutas()) {

            boolean disponible = r.getEstado() == 1
                    && r.getFechaSalida() != null
                    && r.getHoraSalida() != null
                    && LocalDateTime.of(
                            r.getFechaSalida(),
                            r.getHoraSalida()
                    ).isAfter(ahora);

            if (disponible || r.getID_ruta() == idRutaSeleccionada) {
                ComboRutas.addItem(r);
            }
        }
    }

    private void cargarTabla() {
        limpiarCampos();
        modeloTabla.setRowCount(0);

        List<Pasaje> listaPasajes = pasajeData.listarPasajes();

        for (Pasaje p : listaPasajes) {
            modeloTabla.addRow(new Object[]{
                p.getID_pasaje(),
                p.getID_pasajero(),
                p.getID_lugar(),
                p.getID_ruta(),
                p.getFechaVenta(),
                p.getHoraVenta(),
                p.getPrecio(),
                p.getEstado() == 1 ? "Activo" : "Inactivo"
            });
        }
    }

    private void limpiarCampos() {
        txtPasaje.setText("");
        txtFechaVenta.setText("");
        txtHoraVenta.setText("");
        txtPrecio.setText("");
        checkEstado.setSelected(true);
        tablaPasajes.clearSelection();
        habilitarBotones(false);
        ComboPasajeros.requestFocusInWindow();
    }

    private void seleccionarPasajeroEnCombo(String valor) {
        ComboPasajeros.setSelectedIndex(-1);   // limpia si no encuentra nada
        for (int i = 0; i < ComboPasajeros.getItemCount(); i++) {
            Pasajero p;
            p = (Pasajero) ComboPasajeros.getItemAt(i);
            if (String.valueOf(p.getID_pasajero()).equals(valor) || p.toString().equals(valor)) {
                ComboPasajeros.setSelectedIndex(i);
                return;
            }
        }
    }

    private void seleccionarRutaEnCombo(String valor) {
        ComboRutas.setSelectedIndex(-1); // limpia si no encuentra nada

        for (int i = 0; i < ComboRutas.getItemCount(); i++) {
            RutaViaje ruta = (RutaViaje) ComboRutas.getItemAt(i);

            if (String.valueOf(ruta.getID_ruta()).equals(valor) || ruta.toString().equals(valor)) {
                ComboRutas.setSelectedIndex(i);
                return;
            }
        }
    }

    private void seleccionarLugar(String valor) {
        ComboLugares.setSelectedIndex(-1);

        for (int i = 0; i < ComboLugares.getItemCount(); i++) {
            Lugar lugar = (Lugar) ComboLugares.getItemAt(i);

            if (String.valueOf(lugar.getID_lugar()).equals(valor)) {
                ComboLugares.setSelectedIndex(i);
                return;
            }
        }
    }

    private void cargarDatosSeleccionados() {
        int fila = tablaPasajes.getSelectedRow();

        if (fila == -1) {
            return;
        }

        txtPasaje.setText(tablaPasajes.getValueAt(fila, 0).toString());

        cargarComboRutas();
        seleccionarRutaEnCombo(tablaPasajes.getValueAt(fila, 3).toString());
        cargarLugaresDisponibles();
        seleccionarPasajeroEnCombo(tablaPasajes.getValueAt(fila, 1).toString());
        seleccionarLugar(tablaPasajes.getValueAt(fila, 2).toString());

        txtFechaVenta.setText(tablaPasajes.getValueAt(fila, 4).toString());
        txtHoraVenta.setText(tablaPasajes.getValueAt(fila, 5).toString());
        txtPrecio.setText(tablaPasajes.getValueAt(fila, 6).toString());

        boolean activo = tablaPasajes.getValueAt(fila, 7).toString().equals("Activo");
        checkEstado.setSelected(activo);
    }

    private void habilitarBotones(boolean seleccionado) {
        boolean activo = seleccionado && checkEstado.isSelected();
        btnGuardar.setEnabled(!seleccionado);
        btnModificar.setEnabled(seleccionado);
        btnEliminar.setEnabled(seleccionado);
        btnDarBaja.setEnabled(activo);
        btnDarAlta.setEnabled(seleccionado && !activo);
        btnCancelar.setEnabled(seleccionado);
    }

    private Pasaje obtenerYValidarDatos(int idActual) {
        Pasajero p = (Pasajero) ComboPasajeros.getSelectedItem();
        RutaViaje r = (RutaViaje) ComboRutas.getSelectedItem();
        Lugar l = (Lugar) ComboLugares.getSelectedItem();

        if (p == null || r == null || l == null || txtPrecio.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        try {
            BigDecimal precio = new BigDecimal(txtPrecio.getText().trim().replace(",", "."));
            int estado = idActual == 0 ? 1 : (checkEstado.isSelected() ? 1 : 0);

            Pasaje pasaje = new Pasaje();

            if (idActual == 0) {
                pasaje.setFechaVenta(LocalDate.now());
                pasaje.setHoraVenta(LocalTime.now());
            } else {
                int fila = tablaPasajes.getSelectedRow();

                if (fila == -1) {
                    JOptionPane.showMessageDialog(this, "Debe seleccionar un pasaje de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return null;
                }

                pasaje.setID_pasaje(idActual);
                pasaje.setFechaVenta((LocalDate) tablaPasajes.getValueAt(fila, 4));
                pasaje.setHoraVenta((LocalTime) tablaPasajes.getValueAt(fila, 5));
            }

            pasaje.setID_pasajero(p.getID_pasajero());
            pasaje.setID_lugar(l.getID_lugar());
            pasaje.setID_ruta(r.getID_ruta());
            pasaje.setPrecio(precio);
            pasaje.setEstado(estado);

            String error = ValidacionPasaje.validarPasaje(pasaje, idActual, pasajeroData, rutaData, lugarData);

            if (error != null) {
                JOptionPane.showMessageDialog(this, error, "Error de validación", JOptionPane.WARNING_MESSAGE);
                return null;
            }

            return pasaje;

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El precio debe ser numérico.", "Error de formato", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelPrincipal = new javax.swing.JPanel();
        PanelDatos = new javax.swing.JPanel();
        lblPasaje = new javax.swing.JLabel();
        lblPasajero = new javax.swing.JLabel();
        lblLugar = new javax.swing.JLabel();
        lblRuta = new javax.swing.JLabel();
        lblFechaVenta = new javax.swing.JLabel();
        lblHoraVenta = new javax.swing.JLabel();
        lblPrecio = new javax.swing.JLabel();
        lblEstado = new javax.swing.JLabel();
        checkEstado = new javax.swing.JCheckBox();
        txtPasaje = new javax.swing.JTextField();
        txtFechaVenta = new javax.swing.JTextField();
        txtPrecio = new javax.swing.JTextField();
        txtHoraVenta = new javax.swing.JTextField();
        ComboPasajeros = new javax.swing.JComboBox<>();
        ComboRutas = new javax.swing.JComboBox<>();
        ComboLugares = new javax.swing.JComboBox<>();
        PanelBotones = new javax.swing.JPanel();
        txtBuscar = new javax.swing.JTextField();
        lblBuscar = new javax.swing.JLabel();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        btnDarBaja = new javax.swing.JButton();
        btnRecargar = new javax.swing.JButton();
        cboxBusqueda = new javax.swing.JComboBox<>();
        btnListarTodos = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnDarAlta = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        PanelTabla = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaPasajes = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Gestión De Pasajes");

        PanelDatos.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos Del Pasaje", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N

        lblPasaje.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPasaje.setText("Pasaje:");

        lblPasajero.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPasajero.setText("Pasajero (ID):");

        lblLugar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLugar.setText("Lugar:");

        lblRuta.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRuta.setText("Ruta (ID):");

        lblFechaVenta.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFechaVenta.setText("Fecha Venta:");

        lblHoraVenta.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHoraVenta.setText("Hora Venta:");

        lblPrecio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPrecio.setText("Precio:");

        lblEstado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstado.setText("Estado:");

        checkEstado.setSelected(true);
        checkEstado.setText("Activo");
        checkEstado.setEnabled(false);

        txtPasaje.setEditable(false);
        txtPasaje.setFocusable(false);

        ComboRutas.addActionListener(this::ComboRutasActionPerformed);

        javax.swing.GroupLayout PanelDatosLayout = new javax.swing.GroupLayout(PanelDatos);
        PanelDatos.setLayout(PanelDatosLayout);
        PanelDatosLayout.setHorizontalGroup(
            PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lblPasaje, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblRuta))
                    .addComponent(lblLugar)
                    .addComponent(lblPasajero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(ComboPasajeros, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ComboLugares, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ComboRutas, javax.swing.GroupLayout.Alignment.LEADING, 0, 200, Short.MAX_VALUE)
                    .addComponent(txtPasaje))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelDatosLayout.createSequentialGroup()
                        .addComponent(lblPrecio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelDatosLayout.createSequentialGroup()
                        .addComponent(lblEstado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(checkEstado))
                    .addGroup(PanelDatosLayout.createSequentialGroup()
                        .addComponent(lblFechaVenta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtFechaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelDatosLayout.createSequentialGroup()
                        .addComponent(lblHoraVenta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtHoraVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(79, 79, 79))
        );

        PanelDatosLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {lblEstado, lblFechaVenta, lblHoraVenta, lblLugar, lblPasaje, lblPasajero, lblPrecio, lblRuta});

        PanelDatosLayout.setVerticalGroup(
            PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPasaje)
                    .addComponent(txtPasaje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFechaVenta)
                    .addComponent(txtFechaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHoraVenta)
                    .addComponent(txtHoraVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ComboRutas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRuta))
                .addGap(18, 18, 18)
                .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ComboLugares, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblLugar))
                    .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblPrecio)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(checkEstado)
                        .addComponent(lblEstado))
                    .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ComboPasajeros, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblPasajero)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PanelBotones.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Funciones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N

        lblBuscar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBuscar.setText("Buscar por:");

        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(this::btnNuevoActionPerformed);

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(this::btnModificarActionPerformed);

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(this::btnBuscarActionPerformed);

        btnDarBaja.setForeground(new java.awt.Color(153, 0, 0));
        btnDarBaja.setText("Dar Baja");
        btnDarBaja.addActionListener(this::btnDarBajaActionPerformed);

        btnRecargar.setText("Recargar Tabla");
        btnRecargar.addActionListener(this::btnRecargarActionPerformed);

        cboxBusqueda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID Pasaje", "ID Pasajero", "ID Ruta" }));

        btnListarTodos.setText("Listar Todos");
        btnListarTodos.addActionListener(this::btnListarTodosActionPerformed);

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(this::btnEliminarActionPerformed);

        btnDarAlta.setForeground(new java.awt.Color(0, 102, 0));
        btnDarAlta.setText("Dar Alta");
        btnDarAlta.addActionListener(this::btnDarAltaActionPerformed);

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(this::btnCancelarActionPerformed);

        javax.swing.GroupLayout PanelBotonesLayout = new javax.swing.GroupLayout(PanelBotones);
        PanelBotones.setLayout(PanelBotonesLayout);
        PanelBotonesLayout.setHorizontalGroup(
            PanelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(PanelBotonesLayout.createSequentialGroup()
                        .addComponent(lblBuscar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboxBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelBotonesLayout.createSequentialGroup()
                        .addComponent(btnNuevo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelBotonesLayout.createSequentialGroup()
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnListarTodos, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRecargar, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE))
                    .addGroup(PanelBotonesLayout.createSequentialGroup()
                        .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDarAlta, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDarBaja)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        PanelBotonesLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnEliminar, btnModificar});

        PanelBotonesLayout.setVerticalGroup(
            PanelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo)
                    .addComponent(btnGuardar)
                    .addComponent(btnModificar)
                    .addComponent(btnDarBaja)
                    .addComponent(btnEliminar)
                    .addComponent(btnDarAlta)
                    .addComponent(btnCancelar))
                .addGap(12, 12, 12)
                .addGroup(PanelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBuscar)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRecargar)
                    .addComponent(cboxBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnListarTodos)
                    .addComponent(btnBuscar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tablaPasajes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Pasaje", "Pasajero", "Lugar", "Ruta", "Fecha Venta", "Hora Venta", "Precio", "Estado"
            }
        ));
        tablaPasajes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaPasajesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaPasajes);

        javax.swing.GroupLayout PanelTablaLayout = new javax.swing.GroupLayout(PanelTabla);
        PanelTabla.setLayout(PanelTablaLayout);
        PanelTablaLayout.setHorizontalGroup(
            PanelTablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelTablaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        PanelTablaLayout.setVerticalGroup(
            PanelTablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelTablaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PanelPrincipalLayout = new javax.swing.GroupLayout(PanelPrincipal);
        PanelPrincipal.setLayout(PanelPrincipalLayout);
        PanelPrincipalLayout.setHorizontalGroup(
            PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(PanelTabla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelDatos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelBotones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        PanelPrincipalLayout.setVerticalGroup(
            PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelDatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelTabla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        if (!txtPasaje.getText().trim().isEmpty() || !txtPrecio.getText().trim().isEmpty()) {

            int respuesta = JOptionPane.showConfirmDialog(this, "¿Desea descartar los datos actuales y comenzar un nuevo pasaje?", "Nuevo pasaje", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (respuesta != JOptionPane.YES_OPTION) {
                return;
            }
        }
        limpiarCampos();
        cargarComboRutas();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        Pasaje nuevoPasaje = obtenerYValidarDatos(0);

        if (nuevoPasaje == null) {
            return;
        }

        if (pasajeData.guardarPasaje(nuevoPasaje)) {
            JOptionPane.showMessageDialog(this, "Pasaje guardado correctamente.");
            cargarTabla();
            cargarLugaresDisponibles();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo guardar el pasaje.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        int fila = tablaPasajes.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un pasaje de la tabla.");
            return;
        }

        int idPasaje = (int) tablaPasajes.getValueAt(fila, 0);

        Pasaje pasajeModificado = obtenerYValidarDatos(idPasaje);

        if (pasajeModificado == null) {
            return;
        }

        if (pasajeData.modificarPasaje(pasajeModificado)) {
            JOptionPane.showMessageDialog(this, "Pasaje modificado correctamente.");
            cargarTabla();
            cargarLugaresDisponibles();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo modificar el pasaje.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        String textoBusqueda = txtBuscar.getText().trim();
        int criterio = cboxBusqueda.getSelectedIndex();

        if (textoBusqueda.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un valor para buscar.", "Campo vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int valor = Integer.parseInt(textoBusqueda);
            List<Pasaje> pasajes = new ArrayList<>();

            switch (criterio) {
                case 0 -> {
                    Pasaje pasaje = pasajeData.buscarPasaje(valor);

                    if (pasaje != null) {
                        pasajes.add(pasaje);
                    }
                }
                case 1 ->
                    pasajes = pasajeData.listarPasajesPorPasajero(valor);
                case 2 ->
                    pasajes = pasajeData.listarPasajesPorRuta(valor);
                default -> {
                    JOptionPane.showMessageDialog(this, "Seleccione un criterio de búsqueda válido.", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            modeloTabla.setRowCount(0);

            for (Pasaje p : pasajes) {
                modeloTabla.addRow(new Object[]{
                    p.getID_pasaje(),
                    p.getID_pasajero(),
                    p.getID_lugar(),
                    p.getID_ruta(),
                    p.getFechaVenta(),
                    p.getHoraVenta(),
                    p.getPrecio(),
                    p.getEstado() == 1 ? "Activo" : "Inactivo"
                });
            }

            if (pasajes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron pasajes con ese criterio.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El valor de búsqueda debe ser un número entero.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnDarBajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDarBajaActionPerformed
        int filaSeleccionada = tablaPasajes.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un pasaje de la tabla para dar de baja.", "Ningún pasaje seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea dar de baja este pasaje?", "Confirmar baja", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            int idPasaje = (int) tablaPasajes.getValueAt(filaSeleccionada, 0);

            if (pasajeData.darDeBaja(idPasaje)) {
                JOptionPane.showMessageDialog(this, "Pasaje dado de baja correctamente.", "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                cargarLugaresDisponibles();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo dar de baja el pasaje.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnDarBajaActionPerformed

    private void btnRecargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecargarActionPerformed
        cargarTabla();
        cargarLugaresDisponibles();
    }//GEN-LAST:event_btnRecargarActionPerformed

    private void btnListarTodosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListarTodosActionPerformed
        cargarTabla();
        cargarLugaresDisponibles();
    }//GEN-LAST:event_btnListarTodosActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int filaSeleccionada = tablaPasajes.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un pasaje de la tabla para eliminar.", "Ningún pasaje seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este pasaje?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            int idPasaje = (int) tablaPasajes.getValueAt(filaSeleccionada, 0);

            if (pasajeData.eliminarPasaje(idPasaje)) {
                JOptionPane.showMessageDialog(this, "Pasaje eliminado correctamente.", "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                cargarLugaresDisponibles();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el pasaje.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnDarAltaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDarAltaActionPerformed
        int filaSeleccionada = tablaPasajes.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un pasaje de la tabla para dar de alta.", "Ningún pasaje seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idPasaje = (int) tablaPasajes.getValueAt(filaSeleccionada, 0);

        if (pasajeData.darDeAlta(idPasaje)) {
            JOptionPane.showMessageDialog(this, "Pasaje dado de alta correctamente.", "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);
            cargarTabla();
            cargarLugaresDisponibles();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo dar de alta el pasaje. Verifique que la ruta esté activa, que el viaje no haya salido y que el asiento esté disponible.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnDarAltaActionPerformed

    private void tablaPasajesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaPasajesMouseClicked
        cargarDatosSeleccionados();
        habilitarBotones(tablaPasajes.getSelectedRow() != -1);
    }//GEN-LAST:event_tablaPasajesMouseClicked

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        int filaSeleccionada = tablaPasajes.getSelectedRow();

        if (filaSeleccionada == -1) {
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this, "¿Desea descartar los cambios realizados?", "Cancelar", JOptionPane.YES_NO_OPTION);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        cargarDatosSeleccionados();
        habilitarBotones(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void ComboRutasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboRutasActionPerformed
        RutaViaje ruta = (RutaViaje) ComboRutas.getSelectedItem();

        if (ruta != null) {
            txtPrecio.setText(String.valueOf(ruta.getPrecio()));
        }

        cargarLugaresDisponibles();

        if (txtPasaje.getText().trim().isEmpty()) {
            LocalDate fecha = LocalDate.now();
            LocalTime hora = LocalTime.now();
            DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

            txtFechaVenta.setText(fecha.toString());
            txtHoraVenta.setText(hora.format(formatoHora));
        }
    }//GEN-LAST:event_ComboRutasActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<Lugar> ComboLugares;
    private javax.swing.JComboBox<Pasajero> ComboPasajeros;
    private javax.swing.JComboBox<RutaViaje> ComboRutas;
    private javax.swing.JPanel PanelBotones;
    private javax.swing.JPanel PanelDatos;
    private javax.swing.JPanel PanelPrincipal;
    private javax.swing.JPanel PanelTabla;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnDarAlta;
    private javax.swing.JButton btnDarBaja;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnListarTodos;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnRecargar;
    private javax.swing.JComboBox<String> cboxBusqueda;
    private javax.swing.JCheckBox checkEstado;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblFechaVenta;
    private javax.swing.JLabel lblHoraVenta;
    private javax.swing.JLabel lblLugar;
    private javax.swing.JLabel lblPasaje;
    private javax.swing.JLabel lblPasajero;
    private javax.swing.JLabel lblPrecio;
    private javax.swing.JLabel lblRuta;
    private javax.swing.JTable tablaPasajes;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtFechaVenta;
    private javax.swing.JTextField txtHoraVenta;
    private javax.swing.JTextField txtPasaje;
    private javax.swing.JTextField txtPrecio;
    // End of variables declaration//GEN-END:variables
}
