package Vista;

import Modelo.Lugar;
import Modelo.RutaViaje;
import Persistencia.LugarData;
import Persistencia.RutaViajeData;
import Persistencia.PasajeData;
import Validaciones.ValidacionLugar;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class GestionLugares extends javax.swing.JInternalFrame {

    private RutaViajeData rutaData = new RutaViajeData();
    private LugarData lugarData = new LugarData();
    private PasajeData pasajeData = new PasajeData();
    private DefaultTableModel modelo;
    private boolean seleccionandoDesdeTabla = false;
    private boolean cargandoCombo = false;

    public GestionLugares() {
        initComponents();

        modelo = (DefaultTableModel) TablaLugares.getModel();

        cargarComboViajes();
        limpiarCampos();
        cargarTabla();
    }

    private void cargarComboViajes() {
        cargandoCombo = true;

        try {
            cboxDato_viaje.removeAllItems();

            List<RutaViaje> listaViajes = rutaData.listarRutas();

            for (RutaViaje viaje : listaViajes) {
                cboxDato_viaje.addItem(viaje);
            }
        } finally {
            cargandoCombo = false;
        }
    }

    private void cargarTablaLugares(int idRuta) {
        modelo.setRowCount(0);

        List<Lugar> lugares = lugarData.listarLugaresPorRuta(idRuta);

        for (Lugar lugar : lugares) {
            modelo.addRow(new Object[]{
                lugar.getID_lugar(),
                lugar.getID_ruta(),
                lugar.getNumeroAsiento(),
                lugar.getEstado() == LugarData.OCUPADO ? "Ocupado" : "Libre"
            });
        }
    }

    private void cargarTabla() {
        modelo.setRowCount(0);

        List<Lugar> lugares = lugarData.listarLugares();

        for (Lugar lugar : lugares) {
            modelo.addRow(new Object[]{
                lugar.getID_lugar(),
                lugar.getID_ruta(),
                lugar.getNumeroAsiento(),
                lugar.getEstado() == LugarData.OCUPADO ? "Ocupado" : "Libre"
            });
        }
    }

    private void limpiarCampos() {
        dato_lugar.setText("el ID se autogenera");
        dato_numeroAsiento.setText("");
        checkEstado.setSelected(false);
        TablaLugares.clearSelection();
        habilitarBotones(false);
    }

    private void cargarDatosSeleccionados() {
        int fila = TablaLugares.getSelectedRow();

        if (fila == -1) {
            return;
        }

        dato_lugar.setText(TablaLugares.getValueAt(fila, 0).toString());
        dato_numeroAsiento.setText(TablaLugares.getValueAt(fila, 2).toString());
        checkEstado.setSelected(TablaLugares.getValueAt(fila, 3).toString().equals("Ocupado"));

        int idRuta = Integer.parseInt(TablaLugares.getValueAt(fila, 1).toString());

        seleccionandoDesdeTabla = true;

        try {
            for (int i = 0; i < cboxDato_viaje.getItemCount(); i++) {
                RutaViaje viaje = (RutaViaje) cboxDato_viaje.getItemAt(i);

                if (viaje.getID_ruta() == idRuta) {
                    cboxDato_viaje.setSelectedItem(viaje);
                    break;
                }
            }
        } finally {
            seleccionandoDesdeTabla = false;
        }
    }

    private void habilitarBotones(boolean seleccionado) {
        boolean ocupado = seleccionado && checkEstado.isSelected();

        btnAgregarLugar.setEnabled(!seleccionado);
        btnModificar.setEnabled(seleccionado);
        btnOcupar.setEnabled(seleccionado && !ocupado);
        btnDesocupar.setEnabled(ocupado);
        btnCancelar.setEnabled(seleccionado);
    }

    private Lugar obtenerYValidarDatos(int idActual) {
        if (dato_numeroAsiento.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes completar el número de asiento.", "Campo vacío", JOptionPane.WARNING_MESSAGE);
            dato_numeroAsiento.requestFocusInWindow();
            return null;
        }

        RutaViaje viajeSeleccionado = (RutaViaje) cboxDato_viaje.getSelectedItem();

        if (viajeSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar una ruta.", "Ruta no seleccionada", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        try {
            int numeroAsiento = Integer.parseInt(dato_numeroAsiento.getText().trim());
            int estado = checkEstado.isSelected() ? LugarData.OCUPADO : LugarData.LIBRE;

            Lugar lugar = (idActual == 0)
                    ? new Lugar(viajeSeleccionado, numeroAsiento, estado)
                    : new Lugar(idActual, viajeSeleccionado, numeroAsiento, estado);

            String error = ValidacionLugar.validarLugar(lugar, idActual, rutaData, lugarData);

            if (error != null) {
                JOptionPane.showMessageDialog(this, error, "Error de validación", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            return lugar;

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El número de asiento debe ser un valor numérico válido.", "Error de formato", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void cambiarEstado(boolean ocupar) {
        int fila = TablaLugares.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un asiento.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idLugar = Integer.parseInt(TablaLugares.getValueAt(fila, 0).toString());

        if (!ocupar && pasajeData.tienePasajeActivo(idLugar)) {
            JOptionPane.showMessageDialog(this, "No se puede desocupar el asiento porque tiene un pasaje activo.", "Asiento ocupado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String mensaje = ocupar ? "¿Desea ocupar este asiento?" : "¿Desea desocupar este asiento?";
        int opcion = JOptionPane.showConfirmDialog(this, mensaje, "Confirmar operación", JOptionPane.YES_NO_OPTION);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        boolean resultado = ocupar ? lugarData.ocuparLugar(idLugar) : lugarData.desocuparLugar(idLugar);

        if (resultado) {
            JOptionPane.showMessageDialog(this, ocupar ? "Asiento ocupado correctamente." : "Asiento desocupado correctamente.");
            limpiarCampos();
            cargarTablaSegunFiltro();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo cambiar el estado del asiento.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTablaSegunFiltro() {
        String filtro = (String) cboxListar.getSelectedItem();

        if ("Listar Todos".equals(filtro)) {
            cargarTabla();
            return;
        }

        RutaViaje viajeSeleccionado = (RutaViaje) cboxDato_viaje.getSelectedItem();

        if (viajeSeleccionado == null) {
            modelo.setRowCount(0);
            JOptionPane.showMessageDialog(this, "Debe seleccionar un viaje para aplicar este filtro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if ("Listar Por Ruta/Viaje".equals(filtro)) {
            cargarTablaLugares(viajeSeleccionado.getID_ruta());
            return;
        }

        int estado = "Listar Ocupados".equals(filtro) ? LugarData.OCUPADO : LugarData.LIBRE;

        modelo.setRowCount(0);

        List<Lugar> lista = lugarData.listarLugaresPorRutaYEstado(viajeSeleccionado.getID_ruta(), estado);

        for (Lugar lugar : lista) {
            modelo.addRow(new Object[]{
                lugar.getID_lugar(),
                lugar.getID_ruta(),
                lugar.getNumeroAsiento(),
                lugar.getEstado() == LugarData.OCUPADO ? "Ocupado" : "Libre"
            });
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
        lblLugar = new javax.swing.JLabel();
        lblRutaViaje = new javax.swing.JLabel();
        dato_lugar = new javax.swing.JTextField();
        checkEstado = new javax.swing.JCheckBox();
        cboxDato_viaje = new javax.swing.JComboBox();
        dato_numeroAsiento = new javax.swing.JTextField();
        lblNumeroAsiento = new javax.swing.JLabel();
        lblEstado = new javax.swing.JLabel();
        PanelBotones = new javax.swing.JPanel();
        btnLimpiarCampos = new javax.swing.JButton();
        btnAgregarLugar = new javax.swing.JButton();
        btnOcupar = new javax.swing.JButton();
        btnDesocupar = new javax.swing.JButton();
        lblBuscar = new javax.swing.JLabel();
        dato_buscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        cboxListar = new javax.swing.JComboBox<>();
        lblListar = new javax.swing.JLabel();
        PanelTabla = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaLugares = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Gestion Lugares");

        PanelPrincipal.setBorder(javax.swing.BorderFactory.createCompoundBorder());

        PanelDatos.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Gestion De Lugares", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N

        lblLugar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLugar.setText("ID Lugar :");

        lblRutaViaje.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRutaViaje.setText("Ruta/Viaje:");

        dato_lugar.setEditable(false);
        dato_lugar.setText("el ID se autogenera");
        dato_lugar.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        dato_lugar.setFocusable(false);

        checkEstado.setSelected(true);
        checkEstado.setText("Ocupado");
        checkEstado.setEnabled(false);
        checkEstado.addActionListener(this::checkEstadoActionPerformed);

        cboxDato_viaje.addActionListener(this::cboxDato_viajeActionPerformed);

        lblNumeroAsiento.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNumeroAsiento.setText("Numero Asiento :");

        lblEstado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstado.setText("Estado:");

        javax.swing.GroupLayout PanelDatosLayout = new javax.swing.GroupLayout(PanelDatos);
        PanelDatos.setLayout(PanelDatosLayout);
        PanelDatosLayout.setHorizontalGroup(
            PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDatosLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(PanelDatosLayout.createSequentialGroup()
                        .addComponent(lblRutaViaje)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboxDato_viaje, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(PanelDatosLayout.createSequentialGroup()
                        .addComponent(lblLugar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dato_lugar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelDatosLayout.createSequentialGroup()
                        .addComponent(lblNumeroAsiento)
                        .addGap(18, 18, 18)
                        .addComponent(dato_numeroAsiento, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelDatosLayout.createSequentialGroup()
                        .addComponent(lblEstado)
                        .addGap(18, 18, 18)
                        .addComponent(checkEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(33, 33, 33))
        );

        PanelDatosLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {lblEstado, lblLugar, lblNumeroAsiento, lblRutaViaje});

        PanelDatosLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cboxDato_viaje, checkEstado, dato_lugar, dato_numeroAsiento});

        PanelDatosLayout.setVerticalGroup(
            PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelDatosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLugar, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(dato_numeroAsiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dato_lugar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblNumeroAsiento)))
                .addGap(18, 18, 18)
                .addGroup(PanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboxDato_viaje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEstado)
                    .addComponent(checkEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRutaViaje))
                .addContainerGap())
        );

        PanelBotones.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Funciones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N

        btnLimpiarCampos.setText("Limpiar campos");
        btnLimpiarCampos.addActionListener(this::btnLimpiarCamposActionPerformed);

        btnAgregarLugar.setText("Agregar Lugar");
        btnAgregarLugar.addActionListener(this::btnAgregarLugarActionPerformed);

        btnOcupar.setForeground(new java.awt.Color(0, 102, 0));
        btnOcupar.setText("Ocupar ");
        btnOcupar.addActionListener(this::btnOcuparActionPerformed);

        btnDesocupar.setForeground(new java.awt.Color(153, 0, 0));
        btnDesocupar.setText("Desocupar");
        btnDesocupar.addActionListener(this::btnDesocuparActionPerformed);

        lblBuscar.setText("Buscar Por N° Asiento:");

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(this::btnBuscarActionPerformed);

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(this::btnCancelarActionPerformed);

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(this::btnModificarActionPerformed);

        cboxListar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Listar Todos", "Listar Por Ruta/Viaje", "Listar Ocupados", "Listar Desocupados" }));
        cboxListar.addActionListener(this::cboxListarActionPerformed);

        lblListar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblListar.setText("Listar Por:");

        javax.swing.GroupLayout PanelBotonesLayout = new javax.swing.GroupLayout(PanelBotones);
        PanelBotones.setLayout(PanelBotonesLayout);
        PanelBotonesLayout.setHorizontalGroup(
            PanelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelBotonesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(PanelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLimpiarCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAgregarLugar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dato_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnModificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(PanelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblListar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelBotonesLayout.createSequentialGroup()
                        .addComponent(btnOcupar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDesocupar, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cboxListar, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42))
        );
        PanelBotonesLayout.setVerticalGroup(
            PanelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLimpiarCampos)
                    .addComponent(btnAgregarLugar)
                    .addComponent(btnModificar)
                    .addComponent(btnCancelar)
                    .addComponent(btnOcupar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDesocupar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBuscar)
                    .addComponent(dato_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar)
                    .addComponent(lblListar)
                    .addComponent(cboxListar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PanelBotonesLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnAgregarLugar, btnBuscar, btnDesocupar, btnLimpiarCampos, btnOcupar});

        TablaLugares.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID Lugar", "ID Ruta", "Numero Asiento", "Estado"
            }
        ));
        TablaLugares.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        TablaLugares.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaLugaresMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TablaLugares);

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
            .addGroup(PanelPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelBotones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelDatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelTabla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            .addComponent(PanelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLimpiarCamposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarCamposActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarCamposActionPerformed

    private void btnAgregarLugarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarLugarActionPerformed
        Lugar nuevoLugar = obtenerYValidarDatos(0);

        if (nuevoLugar == null) {
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this, "¿Desea agregar este asiento?", "Confirmar alta", JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            if (lugarData.guardarLugar(nuevoLugar)) {
                JOptionPane.showMessageDialog(this, "Asiento agregado correctamente.");
                limpiarCampos();
                cargarTablaSegunFiltro();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo agregar el asiento.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnAgregarLugarActionPerformed

    private void TablaLugaresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaLugaresMouseClicked
        cargarDatosSeleccionados();
        habilitarBotones(TablaLugares.getSelectedRow() != -1);
    }//GEN-LAST:event_TablaLugaresMouseClicked

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        String textoAsiento = dato_buscar.getText().trim();

        if (textoAsiento.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un número de asiento.", "Campo vacío", JOptionPane.WARNING_MESSAGE);
            dato_buscar.requestFocusInWindow();
            return;
        }

        RutaViaje viajeSeleccionado = (RutaViaje) cboxDato_viaje.getSelectedItem();

        if (viajeSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un viaje del combo para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int numeroAsiento = Integer.parseInt(textoAsiento);

            if (numeroAsiento <= 0) {
                JOptionPane.showMessageDialog(this, "El número de asiento debe ser mayor que cero.", "Valor inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            limpiarCampos();
            modelo.setRowCount(0);

            Lugar lugar = lugarData.buscarLugarPorNumero(viajeSeleccionado.getID_ruta(), numeroAsiento);

            String filtro = (String) cboxListar.getSelectedItem();

            if (lugar != null && "Listar Ocupados".equals(filtro) && lugar.getEstado() != LugarData.OCUPADO) {
                lugar = null;
            }

            if (lugar != null && "Listar Desocupados".equals(filtro) && lugar.getEstado() != LugarData.LIBRE) {
                lugar = null;
            }

            if (lugar != null) {
                modelo.addRow(new Object[]{
                    lugar.getID_lugar(),
                    lugar.getID_ruta(),
                    lugar.getNumeroAsiento(),
                    lugar.getEstado() == LugarData.OCUPADO ? "Ocupado" : "Libre"
                });
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el asiento N° " + numeroAsiento + " para este viaje.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El número de asiento debe ser un valor numérico válido.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void checkEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkEstadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkEstadoActionPerformed

    private void cboxDato_viajeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboxDato_viajeActionPerformed
        if (cargandoCombo || seleccionandoDesdeTabla) {
            return;
        }

        if (cboxDato_viaje.getSelectedItem() != null) {
            limpiarCampos();
            cargarTablaSegunFiltro();
        }
    }//GEN-LAST:event_cboxDato_viajeActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        if (dato_lugar.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un asiento para modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int idLugar = Integer.parseInt(dato_lugar.getText().trim());
            Lugar lugarModificado = obtenerYValidarDatos(idLugar);

            if (lugarModificado == null) {
                return;
            }

            int opcion = JOptionPane.showConfirmDialog(this, "¿Desea modificar este asiento?", "Confirmar modificación", JOptionPane.YES_NO_OPTION);

            if (opcion == JOptionPane.YES_OPTION) {
                if (lugarData.modificarLugar(lugarModificado)) {
                    JOptionPane.showMessageDialog(this, "Asiento modificado correctamente.");
                    limpiarCampos();
                    cargarTablaSegunFiltro();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo modificar el asiento.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID del asiento debe ser numérico.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnDesocuparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesocuparActionPerformed
        cambiarEstado(false);
    }//GEN-LAST:event_btnDesocuparActionPerformed

    private void btnOcuparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOcuparActionPerformed
        cambiarEstado(true);
    }//GEN-LAST:event_btnOcuparActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        int filaSeleccionada = TablaLugares.getSelectedRow();

        if (filaSeleccionada == -1) {
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this, "¿Desea descartar los cambios realizados?", "Cancelar", JOptionPane.YES_NO_OPTION);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        cargarDatosSeleccionados();
        habilitarBotones(true);
        limpiarCampos();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void cboxListarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboxListarActionPerformed
        limpiarCampos();
        cargarTablaSegunFiltro();
    }//GEN-LAST:event_cboxListarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelBotones;
    private javax.swing.JPanel PanelDatos;
    private javax.swing.JPanel PanelPrincipal;
    private javax.swing.JPanel PanelTabla;
    private javax.swing.JTable TablaLugares;
    private javax.swing.JButton btnAgregarLugar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnDesocupar;
    private javax.swing.JButton btnLimpiarCampos;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnOcupar;
    private javax.swing.JComboBox cboxDato_viaje;
    private javax.swing.JComboBox<String> cboxListar;
    private javax.swing.JCheckBox checkEstado;
    private javax.swing.JTextField dato_buscar;
    private javax.swing.JTextField dato_lugar;
    private javax.swing.JTextField dato_numeroAsiento;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblListar;
    private javax.swing.JLabel lblLugar;
    private javax.swing.JLabel lblNumeroAsiento;
    private javax.swing.JLabel lblRutaViaje;
    // End of variables declaration//GEN-END:variables
}
