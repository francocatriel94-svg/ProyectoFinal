package Vista;

import Modelo.Colectivo;
import Modelo.RutaViaje;
import Persistencia.ColectivoData;
import Persistencia.RutaViajeData;
import Validaciones.ValidacionRutaViaje;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class GestionRutaViajes extends javax.swing.JInternalFrame {

    private DefaultTableModel modelo;

    public GestionRutaViajes() {
        initComponents();
        modelo = (DefaultTableModel) TablaRutaViaje.getModel();
        llenarComboColectivos();
        llenarCombosHorasYMinutos();
        cargarTabla();
    }

    private void llenarComboColectivos() {

        dato_colectivo.removeAllItems();

        dato_colectivo.addItem("Colectivos");

        ColectivoData colData = new ColectivoData();

        for (Colectivo c : colData.listarColectivosActivos()) {
            dato_colectivo.addItem(c);
        }

        dato_colectivo.setSelectedIndex(0);
    }

    private void llenarCombosHorasYMinutos() {

        cboxHoraSalida.removeAllItems();
        cboxMinSalida.removeAllItems();
        cboxHoraLlegada.removeAllItems();
        cboxMinLlegada.removeAllItems();

        for (int i = 0; i < 24; i++) {
            String hora = String.format("%02d", i);
            cboxHoraSalida.addItem(hora);
            cboxHoraLlegada.addItem(hora);
        }

        String[] minutos = {"00", "15", "30", "45"};
        for (String m : minutos) {
            cboxMinSalida.addItem(m);
            cboxMinLlegada.addItem(m);
        }
    }

    private void cambiarEstadoRuta(boolean activar) {
        try {
            if (dato_id.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un viaje de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String accion = activar ? "activar" : "dar de baja";
            int respuesta = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea " + accion + " este viaje?", "Confirmar operación", JOptionPane.YES_NO_OPTION);

            if (respuesta != JOptionPane.YES_OPTION) {
                return;
            }

            int idRuta = Integer.parseInt(dato_id.getText().trim());
            RutaViajeData data = new RutaViajeData();
            boolean resultado = activar ? data.altaLogicaRuta(idRuta) : data.bajaLogicaRuta(idRuta);

            if (resultado) {
                JOptionPane.showMessageDialog(this, activar ? "Viaje activado correctamente." : "Viaje dado de baja correctamente.");
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo modificar el estado del viaje.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla() {
        modelo.setRowCount(0);

        RutaViajeData rutaData = new RutaViajeData();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (RutaViaje r : rutaData.listarRutas()) {
            String estadoTexto = (r.getEstado() == 1) ? "Activo" : "Inactivo";
            String salida = LocalDateTime.of(r.getFechaSalida(), r.getHoraSalida()).format(formato);
            String llegada = LocalDateTime.of(r.getFechaLlegada(), r.getHoraLlegada()).format(formato);

            modelo.addRow(new Object[]{
                r.getID_ruta(),
                r.getOrigen(),
                r.getDestino(),
                salida,
                llegada,
                r.getID_colectivo(),
                "$" + r.getPrecio(),
                estadoTexto
            });
        }
    }

    private void limpiarCampos() {
        dato_id.setText("");
        dato_origen.setText("");
        dato_destino.setText("");
        dato_precio.setText("");

        // Limpiar fechas.
        dateFechaSalida.setDate(null);
        dateFechaLlegada.setDate(null);

        checkEstado.setSelected(true);

        if (dato_colectivo.getItemCount() > 0) {
            dato_colectivo.setSelectedIndex(0);
        }

        if (cboxHoraSalida.getItemCount() > 0) {
            cboxHoraSalida.setSelectedIndex(0);
        }

        if (cboxMinSalida.getItemCount() > 0) {
            cboxMinSalida.setSelectedIndex(0);
        }

        if (cboxHoraLlegada.getItemCount() > 0) {
            cboxHoraLlegada.setSelectedIndex(0);
        }

        if (cboxMinLlegada.getItemCount() > 0) {
            cboxMinLlegada.setSelectedIndex(0);
        }

        dato_origen.requestFocus();

        btnDarAlta.setEnabled(false);
        btnDarBaja.setEnabled(false);
        btnModificar.setEnabled(false);
        btnCancelar.setEnabled(false);

        cargarTabla();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelDatos = new javax.swing.JPanel();
        lblID = new javax.swing.JLabel();
        lblOrigen = new javax.swing.JLabel();
        lblHoraSalida = new javax.swing.JLabel();
        dato_id = new javax.swing.JTextField();
        dato_origen = new javax.swing.JTextField();
        lblColectivo = new javax.swing.JLabel();
        lblDestino = new javax.swing.JLabel();
        lblHoraLlegada = new javax.swing.JLabel();
        dato_destino = new javax.swing.JTextField();
        dato_colectivo = new javax.swing.JComboBox<>();
        cboxHoraSalida = new javax.swing.JComboBox<>();
        cboxMinSalida = new javax.swing.JComboBox<>();
        cboxHoraLlegada = new javax.swing.JComboBox<>();
        cboxMinLlegada = new javax.swing.JComboBox<>();
        lblFechaSalida = new javax.swing.JLabel();
        dateFechaSalida = new com.toedter.calendar.JDateChooser();
        dateFechaLlegada = new com.toedter.calendar.JDateChooser();
        lblFechaLlegada = new javax.swing.JLabel();
        lblPrecio = new javax.swing.JLabel();
        dato_precio = new javax.swing.JTextField();
        lblEstado = new javax.swing.JLabel();
        checkEstado = new javax.swing.JCheckBox();
        PanelBotones = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnDarAlta = new javax.swing.JButton();
        btnDarBaja = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        lblBusqueda = new javax.swing.JLabel();
        dato_buscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnMostrarTodo = new javax.swing.JButton();
        btnRecargarTabla = new javax.swing.JButton();
        PanelTabla = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaRutaViaje = new javax.swing.JTable();

        setIconifiable(true);
        setTitle("Gestión de Viajes");

        PanelDatos.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos de los viajes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N
        PanelDatos.setToolTipText("");
        PanelDatos.setName("asdaf"); // NOI18N

        lblID.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblID.setText("ID:");

        lblOrigen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOrigen.setText("Origen:");

        lblHoraSalida.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHoraSalida.setText("Hora salida:");

        dato_id.setEditable(false);
        dato_id.setEnabled(false);

        lblColectivo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblColectivo.setText("Colectivo:");

        lblDestino.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDestino.setText("Destino:");

        lblHoraLlegada.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHoraLlegada.setText("Hora llegada:");

        cboxHoraSalida.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hora" }));

        cboxMinSalida.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Min" }));

        cboxHoraLlegada.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hora" }));

        cboxMinLlegada.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Min" }));

        lblFechaSalida.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFechaSalida.setText("Fecha Salida:");

        dateFechaSalida.setDateFormatString("dd/MM/yyyy");

        dateFechaLlegada.setDateFormatString("dd/MM/yyyy");

        lblFechaLlegada.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFechaLlegada.setText("Fecha Llegada:");

        lblPrecio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPrecio.setText("Precio:");

        lblEstado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstado.setText("Estado:");

        checkEstado.setText("Activo");
        checkEstado.setEnabled(false);

        javax.swing.GroupLayout PanelDatosLayout = new javax.swing.GroupLayout(PanelDatos);
        PanelDatos.setLayout(PanelDatosLayout);
        PanelDatosLayout.setHorizontalGroup(
            PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(PanelDatosLayout.createSequentialGroup()
                            .addComponent(lblHoraSalida)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cboxHoraSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cboxMinSalida, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(PanelDatosLayout.createSequentialGroup()
                            .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblID)
                                .addComponent(lblOrigen))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(dato_origen, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(dato_id, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(PanelDatosLayout.createSequentialGroup()
                            .addComponent(lblFechaSalida)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(dateFechaSalida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(PanelDatosLayout.createSequentialGroup()
                        .addComponent(lblPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dato_precio, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelDatosLayout.createSequentialGroup()
                        .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblDestino, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblColectivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(lblFechaLlegada, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblHoraLlegada, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(PanelDatosLayout.createSequentialGroup()
                                .addComponent(cboxHoraLlegada, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboxMinLlegada, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(dateFechaLlegada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dato_destino)
                            .addComponent(dato_colectivo, 0, 229, Short.MAX_VALUE)))
                    .addGroup(PanelDatosLayout.createSequentialGroup()
                        .addComponent(lblEstado)
                        .addGap(18, 18, 18)
                        .addComponent(checkEstado)))
                .addGap(27, 27, 27))
        );

        PanelDatosLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {lblColectivo, lblDestino, lblEstado, lblFechaLlegada, lblFechaSalida, lblHoraLlegada, lblHoraSalida, lblID, lblOrigen});

        PanelDatosLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {dato_colectivo, dato_destino, dato_id, dato_origen, dato_precio});

        PanelDatosLayout.setVerticalGroup(
            PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelDatosLayout.createSequentialGroup()
                        .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblColectivo)
                            .addComponent(dato_colectivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dato_destino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDestino))
                        .addGap(18, 18, 18)
                        .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblFechaLlegada)
                            .addComponent(dateFechaLlegada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(PanelDatosLayout.createSequentialGroup()
                        .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblID)
                            .addComponent(dato_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblOrigen)
                            .addComponent(dato_origen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateFechaSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblFechaSalida))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cboxHoraSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboxMinSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cboxHoraLlegada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboxMinLlegada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblHoraLlegada))
                    .addComponent(lblHoraSalida))
                .addGap(18, 18, 18)
                .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblPrecio)
                        .addComponent(dato_precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblEstado)
                        .addComponent(checkEstado)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PanelBotones.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Funciones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N

        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(this::btnNuevoActionPerformed);

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(this::btnModificarActionPerformed);

        btnDarAlta.setForeground(new java.awt.Color(0, 102, 0));
        btnDarAlta.setText("Dar Alta");
        btnDarAlta.addActionListener(this::btnDarAltaActionPerformed);

        btnDarBaja.setForeground(new java.awt.Color(153, 0, 0));
        btnDarBaja.setText("Dar Baja");
        btnDarBaja.addActionListener(this::btnDarBajaActionPerformed);

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(this::btnCancelarActionPerformed);

        lblBusqueda.setText("Buscar por destino:");

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(this::btnBuscarActionPerformed);

        btnMostrarTodo.setText("Mostrar todos");
        btnMostrarTodo.addActionListener(this::btnMostrarTodoActionPerformed);

        btnRecargarTabla.setText("Recargar Tabla");
        btnRecargarTabla.setActionCommand("");
        btnRecargarTabla.addActionListener(this::btnRecargarTablaActionPerformed);

        javax.swing.GroupLayout PanelBotonesLayout = new javax.swing.GroupLayout(PanelBotones);
        PanelBotones.setLayout(PanelBotonesLayout);
        PanelBotonesLayout.setHorizontalGroup(
            PanelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(PanelBotonesLayout.createSequentialGroup()
                        .addComponent(lblBusqueda)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dato_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnMostrarTodo))
                    .addGroup(PanelBotonesLayout.createSequentialGroup()
                        .addComponent(btnNuevo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnModificar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDarAlta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDarBaja)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRecargarTabla)
                .addContainerGap())
        );
        PanelBotonesLayout.setVerticalGroup(
            PanelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnRecargarTabla, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, PanelBotonesLayout.createSequentialGroup()
                        .addGroup(PanelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnNuevo)
                            .addComponent(btnGuardar)
                            .addComponent(btnModificar)
                            .addComponent(btnDarAlta)
                            .addComponent(btnDarBaja)
                            .addComponent(btnCancelar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(PanelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblBusqueda)
                            .addComponent(dato_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscar)
                            .addComponent(btnMostrarTodo))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        TablaRutaViaje.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Origen", "Destino", "Salida", "Llegada", "Colectivo", "Precio", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaRutaViaje.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaRutaViajeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TablaRutaViaje);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelDatos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelBotones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelTabla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(PanelDatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelBotones, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelTabla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        try {
            String origen = dato_origen.getText().trim();
            String destino = dato_destino.getText().trim();

            // Comprobar que se hayan seleccionado las fechas.
            if (dateFechaSalida.getDate() == null || dateFechaLlegada.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar las fechas de salida y llegada.", "Error de validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Comprobar que se haya seleccionado un colectivo.
            if (!(dato_colectivo.getSelectedItem() instanceof Colectivo)) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un colectivo válido.", "Error de validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Colectivo colectivoSeleccionado = (Colectivo) dato_colectivo.getSelectedItem();

            // Obtener las fechas seleccionadas.
            LocalDate fechaSalida = dateFechaSalida.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate fechaLlegada = dateFechaLlegada.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // Obtener la hora de salida.
            int hSalida = Integer.parseInt(cboxHoraSalida.getSelectedItem().toString());
            int mSalida = Integer.parseInt(cboxMinSalida.getSelectedItem().toString());
            LocalTime horaSalida = LocalTime.of(hSalida, mSalida);

            // Obtener la hora de llegada.
            int hLlegada = Integer.parseInt(cboxHoraLlegada.getSelectedItem().toString());
            int mLlegada = Integer.parseInt(cboxMinLlegada.getSelectedItem().toString());
            LocalTime horaLlegada = LocalTime.of(hLlegada, mLlegada);

            // Comprobar el precio.
            if (dato_precio.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El campo precio no puede estar vacío.", "Error de validación", JOptionPane.WARNING_MESSAGE);
                dato_precio.requestFocus();
                return;
            }

            BigDecimal precio = new BigDecimal(dato_precio.getText().trim().replace("$", ""));

            int estado = checkEstado.isSelected() ? 1 : 0;

            // Crear el objeto RutaViaje.
            RutaViaje ruta = new RutaViaje();
            ruta.setOrigen(origen);
            ruta.setDestino(destino);
            ruta.setFechaSalida(fechaSalida);
            ruta.setFechaLlegada(fechaLlegada);
            ruta.setHoraSalida(horaSalida);
            ruta.setHoraLlegada(horaLlegada);
            ruta.setID_colectivo(colectivoSeleccionado.getID_colectivo());
            ruta.setPrecio(precio);
            ruta.setEstado(estado);

            // Validar los datos.
            ColectivoData colectivoData = new ColectivoData();

            String error = ValidacionRutaViaje.validarRutaViaje(ruta, colectivoData);

            if (error != null) {
                JOptionPane.showMessageDialog(this, error, "Error de validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Confirmar el guardado.
            int respuesta = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea guardar esta ruta de viaje?", "Confirmar guardado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (respuesta != JOptionPane.YES_OPTION) {
                return;
            }

            // Guardar la ruta.
            RutaViajeData rutaData = new RutaViajeData();

            if (rutaData.guardarRutaViaje(ruta)) {
                JOptionPane.showMessageDialog(this, "La ruta de viaje se guardó correctamente.", "Operación exitosa", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar la ruta de viaje.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El precio ingresado debe ser un número válido.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al intentar guardar el viaje: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed

        if (!dato_origen.getText().trim().isEmpty() || !dato_destino.getText().trim().isEmpty() 
                || !dato_precio.getText().trim().isEmpty() || dateFechaSalida.getDate() != null || dateFechaLlegada.getDate() != null) {
            
            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea descartar los datos actuales y comenzar un nuevo viaje?",
                    "Nuevo viaje",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (respuesta != JOptionPane.YES_OPTION) {
                return;
            }
        }
        limpiarCampos();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void TablaRutaViajeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaRutaViajeMouseClicked
        int filaSeleccionada = TablaRutaViaje.getSelectedRow();

        if (filaSeleccionada != -1) {
            int idRuta = Integer.parseInt(TablaRutaViaje.getValueAt(filaSeleccionada, 0).toString());
            RutaViajeData rutaData = new RutaViajeData();
            RutaViaje ruta = rutaData.buscarRuta(idRuta);

            if (ruta == null) {
                JOptionPane.showMessageDialog(this, "No se encontró la ruta seleccionada.");
                return;
            }

            dato_id.setText(String.valueOf(ruta.getID_ruta()));
            dato_origen.setText(ruta.getOrigen());
            dato_destino.setText(ruta.getDestino());

            dateFechaSalida.setDate(java.sql.Date.valueOf(ruta.getFechaSalida()));
            dateFechaLlegada.setDate(java.sql.Date.valueOf(ruta.getFechaLlegada()));

            cboxHoraSalida.setSelectedItem(String.format("%02d", ruta.getHoraSalida().getHour()));
            cboxMinSalida.setSelectedItem(String.format("%02d", ruta.getHoraSalida().getMinute()));
            cboxHoraLlegada.setSelectedItem(String.format("%02d", ruta.getHoraLlegada().getHour()));
            cboxMinLlegada.setSelectedItem(String.format("%02d", ruta.getHoraLlegada().getMinute()));

            for (int i = 0; i < dato_colectivo.getItemCount(); i++) {
                Object item = dato_colectivo.getItemAt(i);
                if (item instanceof Colectivo && ((Colectivo) item).getID_colectivo() == ruta.getID_colectivo()) {
                    dato_colectivo.setSelectedIndex(i);
                    break;
                }
            }

            dato_precio.setText(ruta.getPrecio().toString());

            boolean esActivo = ruta.getEstado() == 1;
            checkEstado.setSelected(esActivo);

            btnDarAlta.setEnabled(!esActivo);
            btnDarBaja.setEnabled(esActivo);
            btnModificar.setEnabled(true);
            btnCancelar.setEnabled(true);
        }

    }//GEN-LAST:event_TablaRutaViajeMouseClicked

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        try {
            int filaSeleccionada = TablaRutaViaje.getSelectedRow();

            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un viaje de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idRuta = Integer.parseInt(TablaRutaViaje.getValueAt(filaSeleccionada, 0).toString());

            String origen = dato_origen.getText().trim();
            String destino = dato_destino.getText().trim();

            // Comprobar fechas.
            if (dateFechaSalida.getDate() == null || dateFechaLlegada.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar las fechas de salida y llegada.", "Error de validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Comprobar colectivo.
            if (!(dato_colectivo.getSelectedItem() instanceof Colectivo)) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un colectivo válido.", "Error de validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Colectivo colectivoSeleccionado = (Colectivo) dato_colectivo.getSelectedItem();

            // Obtener fechas.
            LocalDate fechaSalida = dateFechaSalida.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate fechaLlegada = dateFechaLlegada.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // Obtener hora de salida.
            int hSalida = Integer.parseInt(cboxHoraSalida.getSelectedItem().toString());
            int mSalida = Integer.parseInt(cboxMinSalida.getSelectedItem().toString());
            LocalTime horaSalida = LocalTime.of(hSalida, mSalida);

            // Obtener hora de llegada.
            int hLlegada = Integer.parseInt(cboxHoraLlegada.getSelectedItem().toString());
            int mLlegada = Integer.parseInt(cboxMinLlegada.getSelectedItem().toString());
            LocalTime horaLlegada = LocalTime.of(hLlegada, mLlegada);

            // Comprobar precio.
            if (dato_precio.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El campo precio no puede estar vacío.", "Error de validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            BigDecimal precio = new BigDecimal(dato_precio.getText().trim().replace("$", ""));

            int estado = checkEstado.isSelected() ? 1 : 0;

            // Crear objeto RutaViaje.
            RutaViaje ruta = new RutaViaje();
            ruta.setID_ruta(idRuta);
            ruta.setOrigen(origen);
            ruta.setDestino(destino);
            ruta.setFechaSalida(fechaSalida);
            ruta.setFechaLlegada(fechaLlegada);
            ruta.setHoraSalida(horaSalida);
            ruta.setHoraLlegada(horaLlegada);
            ruta.setID_colectivo(colectivoSeleccionado.getID_colectivo());
            ruta.setPrecio(precio);
            ruta.setEstado(estado);

            // Validar datos.
            ColectivoData colectivoData = new ColectivoData();

            String error = ValidacionRutaViaje.validarRutaViaje(ruta, colectivoData);

            if (error != null) {
                JOptionPane.showMessageDialog(this, error, "Error de validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Confirmar modificación.
            int respuesta = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea modificar esta ruta de viaje?", "Confirmar modificación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (respuesta != JOptionPane.YES_OPTION) {
                return;
            }

            // Modificar ruta.
            RutaViajeData rutaData = new RutaViajeData();

            if (rutaData.modificarRutaViaje(ruta)) {
                JOptionPane.showMessageDialog(this, "La ruta de viaje se modificó correctamente.", "Operación exitosa", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo modificar la ruta de viaje.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El precio ingresado debe ser un número válido.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al modificar el viaje: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnDarAltaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDarAltaActionPerformed
        cambiarEstadoRuta(true);
    }//GEN-LAST:event_btnDarAltaActionPerformed

    private void btnDarBajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDarBajaActionPerformed
        cambiarEstadoRuta(false);
    }//GEN-LAST:event_btnDarBajaActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        String destinoBuscado = dato_buscar.getText().trim();

        if (destinoBuscado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un destino para buscar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        modelo.setRowCount(0);
        RutaViajeData rutaData = new RutaViajeData();
        List<RutaViaje> listaFiltrada = rutaData.buscarRutasPorDestino(destinoBuscado);

        if (listaFiltrada.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron rutas con ese destino.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            btnMostrarTodo.setEnabled(true);
            return;
        }

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (RutaViaje r : listaFiltrada) {
            String estadoTexto = (r.getEstado() == 1) ? "Activo" : "Inactivo";
            String salida = LocalDateTime.of(r.getFechaSalida(), r.getHoraSalida()).format(formato);
            String llegada = LocalDateTime.of(r.getFechaLlegada(), r.getHoraLlegada()).format(formato);

            modelo.addRow(new Object[]{r.getID_ruta(), r.getOrigen(), r.getDestino(), salida, llegada, r.getID_colectivo(), "$" + r.getPrecio(), estadoTexto});
        }

        btnMostrarTodo.setEnabled(true);
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnMostrarTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarTodoActionPerformed
        dato_buscar.setText("");
        cargarTabla();
        btnMostrarTodo.setEnabled(false);
    }//GEN-LAST:event_btnMostrarTodoActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        if (TablaRutaViaje.getSelectedRow() == -1) {
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea descartar los cambios realizados?", "Confirmar cancelación", JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION)
            TablaRutaViajeMouseClicked(null);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnRecargarTablaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecargarTablaActionPerformed
        cargarTabla();
    }//GEN-LAST:event_btnRecargarTablaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelBotones;
    private javax.swing.JPanel PanelDatos;
    private javax.swing.JPanel PanelTabla;
    private javax.swing.JTable TablaRutaViaje;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnDarAlta;
    private javax.swing.JButton btnDarBaja;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnMostrarTodo;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnRecargarTabla;
    private javax.swing.JComboBox<String> cboxHoraLlegada;
    private javax.swing.JComboBox<String> cboxHoraSalida;
    private javax.swing.JComboBox<String> cboxMinLlegada;
    private javax.swing.JComboBox<String> cboxMinSalida;
    private javax.swing.JCheckBox checkEstado;
    private com.toedter.calendar.JDateChooser dateFechaLlegada;
    private com.toedter.calendar.JDateChooser dateFechaSalida;
    private javax.swing.JTextField dato_buscar;
    private javax.swing.JComboBox<Object> dato_colectivo;
    private javax.swing.JTextField dato_destino;
    private javax.swing.JTextField dato_id;
    private javax.swing.JTextField dato_origen;
    private javax.swing.JTextField dato_precio;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBusqueda;
    private javax.swing.JLabel lblColectivo;
    private javax.swing.JLabel lblDestino;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblFechaLlegada;
    private javax.swing.JLabel lblFechaSalida;
    private javax.swing.JLabel lblHoraLlegada;
    private javax.swing.JLabel lblHoraSalida;
    private javax.swing.JLabel lblID;
    private javax.swing.JLabel lblOrigen;
    private javax.swing.JLabel lblPrecio;
    // End of variables declaration//GEN-END:variables
}
