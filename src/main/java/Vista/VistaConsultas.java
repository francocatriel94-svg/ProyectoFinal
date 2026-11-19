package Vista;

import Modelo.Pasajero;
import Persistencia.PasajeData;
import Persistencia.PasajeroData;
import Persistencia.LugarData;
import Persistencia.RutaViajeData;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class VistaConsultas extends javax.swing.JInternalFrame {

    private RutaViajeData rutaData;
    private DefaultTableModel modeloViajes;
    private PasajeData pasajeData;
    private PasajeroData pasajeroData;
    private LugarData lugarData;

    private List<Pasajero> pasajerosMes = new ArrayList<>();

    public VistaConsultas() {
        initComponents();

        rutaData = new RutaViajeData();
        pasajeData = new PasajeData();
        pasajeroData = new PasajeroData();
        lugarData = new LugarData();

        modeloViajes = (DefaultTableModel) TablaViajes.getModel();
        modeloViajes.setRowCount(0);

        llenarCombosViajes();
        cargarPasajerosMes();
        llenarCombosAsientos();

        cmbMesPasajes.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
        txtAnioPasajes.setText(String.valueOf(LocalDate.now().getYear()));

        limpiarTablasIniciales();
    }

    private void llenarCombosViajes() {
        cmbHoraConsulta.removeAllItems();
        cmbMinutoConsulta.removeAllItems();

        cmbHoraConsulta.addItem("Todas");
        cmbMinutoConsulta.addItem("Todos");

        for (int i = 0; i < 24; i++) {
            cmbHoraConsulta.addItem(String.format("%02d", i));
        }

        for (int i = 0; i < 60; i++) {
            cmbMinutoConsulta.addItem(String.format("%02d", i));
        }

        txtDestinoViajes.setText("");
    }

    private void cargarPasajerosMes() {
        pasajerosMes.clear();
        cmbPasajeroMes.removeAllItems();

        cmbPasajeroMes.addItem("Seleccione un pasajero");

        for (Pasajero pasajero : pasajeroData.listarTodosLosPasajeros()) {
            pasajerosMes.add(pasajero);
            cmbPasajeroMes.addItem(pasajero.getDni() + " - " + pasajero.getNombre() + " " + pasajero.getApellido());
        }
    }

    private void llenarCombosAsientos() {
        cmbHoraAsientos.removeAllItems();
        cmbMinutoAsientos.removeAllItems();

        for (int i = 0; i < 24; i++) {
            cmbHoraAsientos.addItem(String.format("%02d", i));
        }

        for (int i = 0; i < 60; i++) {
            cmbMinutoAsientos.addItem(String.format("%02d", i));
        }

        cmbHoraAsientos.setSelectedItem("20");
        cmbMinutoAsientos.setSelectedItem("00");
    }

    private void limpiarTablasIniciales() {
        JTable[] tablas = {
            TablaViajes,
            tablaPasajesMes,
            tablaColectivosChoferes,
            tablaAsientosHorario,
            tablaColectivosDestino,
            tablaAsientosRuta
        };

        for (JTable tabla : tablas) {
            ((DefaultTableModel) tabla.getModel()).setRowCount(0);
        }
    }

    private void mostrarResultados(JTable tabla, List<Object[]> resultados, int indiceFecha) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);

        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Object[] fila : resultados) {
            Object[] filaVisible = fila.clone();

            if (filaVisible[indiceFecha] instanceof LocalDate) {
                filaVisible[indiceFecha] = ((LocalDate) filaVisible[indiceFecha]).format(formatoFecha);
            }

            modelo.addRow(filaVisible);
        }

        if (resultados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron resultados para la consulta.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        panelViajesCapacidad = new javax.swing.JPanel();
        panelFiltrosViajes = new javax.swing.JPanel();
        lblDestinoConsulta = new javax.swing.JLabel();
        lblTipoHora = new javax.swing.JLabel();
        cmbTipoHora = new javax.swing.JComboBox<>();
        lblHoraConsulta = new javax.swing.JLabel();
        cmbHoraConsulta = new javax.swing.JComboBox<>();
        cmbMinutoConsulta = new javax.swing.JComboBox<>();
        txtDestinoViajes = new javax.swing.JTextField();
        btnConsultar = new javax.swing.JButton();
        btnLimpiarConsulta = new javax.swing.JButton();
        panelTablaViajes = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaViajes = new javax.swing.JTable();
        panelPasajesMes = new javax.swing.JPanel();
        panelTablaPasajesMes = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaPasajesMes = new javax.swing.JTable();
        panelFiltrosPasajesPorMes = new javax.swing.JPanel();
        cmbPasajeroMes = new javax.swing.JComboBox<>();
        cmbMesPasajes = new javax.swing.JComboBox<>();
        txtAnioPasajes = new javax.swing.JTextField();
        btnLimpiarPasajes = new javax.swing.JButton();
        btnConsultarPasajes = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblAnioConsulta = new javax.swing.JLabel();
        panelColectivosChoferes = new javax.swing.JPanel();
        panelConsultarColectivosChoferes = new javax.swing.JPanel();
        btnLimpiarChoferes = new javax.swing.JButton();
        btnConsultarChoferes = new javax.swing.JButton();
        panelTablaColectivosChoferes = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaColectivosChoferes = new javax.swing.JTable();
        panelAsientosHorario = new javax.swing.JPanel();
        panelConsultarAsientosHorario = new javax.swing.JPanel();
        cmbHoraAsientos = new javax.swing.JComboBox<>();
        cmbMinutoAsientos = new javax.swing.JComboBox<>();
        btnConsultarAsientosHora = new javax.swing.JButton();
        btnLimpiarAsientosHora = new javax.swing.JButton();
        lblHoraConsulta1 = new javax.swing.JLabel();
        panelTablaAsientosHorario = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaAsientosHorario = new javax.swing.JTable();
        panelColectivosDestino = new javax.swing.JPanel();
        panelConsultaDestinoColectivos = new javax.swing.JPanel();
        panelBotones = new javax.swing.JPanel();
        lblFechaConsulta = new javax.swing.JLabel();
        dateColectivosDestino = new com.toedter.calendar.JDateChooser();
        txtDestinoColectivos = new javax.swing.JTextField();
        lblDestinoConsulta1 = new javax.swing.JLabel();
        btnConsultarColectivosPorDestino = new javax.swing.JButton();
        btnLimpiarColectivosPorDestino = new javax.swing.JButton();
        panelTablaColectivosDestino = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablaColectivosDestino = new javax.swing.JTable();
        panelAsientosRuta = new javax.swing.JPanel();
        panelConsultaAsientosRuta = new javax.swing.JPanel();
        lblViaje = new javax.swing.JLabel();
        txtRutaAsientos = new javax.swing.JTextField();
        btnLimpiarAsientosPorRuta = new javax.swing.JButton();
        btnConsultarAsientosPorRuta = new javax.swing.JButton();
        panelTablaAsientosRuta = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tablaAsientosRuta = new javax.swing.JTable();

        setClosable(true);
        setMaximizable(true);
        setResizable(true);

        lblDestinoConsulta.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDestinoConsulta.setText("Destino:");

        lblTipoHora.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTipoHora.setText("Filtrar Por:");

        cmbTipoHora.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hora de salida", "Hora de llegada" }));

        lblHoraConsulta.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHoraConsulta.setText("Hora:");

        cmbHoraConsulta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbMinutoConsulta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnConsultar.setText("Consultar");
        btnConsultar.addActionListener(this::btnConsultarActionPerformed);

        btnLimpiarConsulta.setText("Limpiar");
        btnLimpiarConsulta.addActionListener(this::btnLimpiarConsultaActionPerformed);

        javax.swing.GroupLayout panelFiltrosViajesLayout = new javax.swing.GroupLayout(panelFiltrosViajes);
        panelFiltrosViajes.setLayout(panelFiltrosViajesLayout);
        panelFiltrosViajesLayout.setHorizontalGroup(
            panelFiltrosViajesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFiltrosViajesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFiltrosViajesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFiltrosViajesLayout.createSequentialGroup()
                        .addComponent(lblDestinoConsulta, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDestinoViajes, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                        .addGap(27, 27, 27)
                        .addComponent(lblTipoHora)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbTipoHora, 0, 224, Short.MAX_VALUE)
                        .addGap(28, 28, 28)
                        .addComponent(lblHoraConsulta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbHoraConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbMinutoConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28))
                    .addGroup(panelFiltrosViajesLayout.createSequentialGroup()
                        .addComponent(btnConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLimpiarConsulta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        panelFiltrosViajesLayout.setVerticalGroup(
            panelFiltrosViajesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFiltrosViajesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFiltrosViajesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDestinoConsulta)
                    .addComponent(txtDestinoViajes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTipoHora)
                    .addComponent(cmbTipoHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblHoraConsulta)
                    .addComponent(cmbHoraConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbMinutoConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelFiltrosViajesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLimpiarConsulta)
                    .addComponent(btnConsultar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        TablaViajes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Origen", "Destino", "Fecha salida", "Hora salida", "Hora llegada", "Capacidad"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(TablaViajes);

        javax.swing.GroupLayout panelTablaViajesLayout = new javax.swing.GroupLayout(panelTablaViajes);
        panelTablaViajes.setLayout(panelTablaViajesLayout);
        panelTablaViajesLayout.setHorizontalGroup(
            panelTablaViajesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaViajesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        panelTablaViajesLayout.setVerticalGroup(
            panelTablaViajesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaViajesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelViajesCapacidadLayout = new javax.swing.GroupLayout(panelViajesCapacidad);
        panelViajesCapacidad.setLayout(panelViajesCapacidadLayout);
        panelViajesCapacidadLayout.setHorizontalGroup(
            panelViajesCapacidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelViajesCapacidadLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelViajesCapacidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelFiltrosViajes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelTablaViajes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelViajesCapacidadLayout.setVerticalGroup(
            panelViajesCapacidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelViajesCapacidadLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelFiltrosViajes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTablaViajes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Viajes y capacidad", panelViajesCapacidad);

        tablaPasajesMes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Pasaje", "DNI", "Nombre", "Apellido", "ID Viaje", "ID Asiento", "Fecha venta", "Hora venta", "Precio", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tablaPasajesMes);

        javax.swing.GroupLayout panelTablaPasajesMesLayout = new javax.swing.GroupLayout(panelTablaPasajesMes);
        panelTablaPasajesMes.setLayout(panelTablaPasajesMesLayout);
        panelTablaPasajesMesLayout.setHorizontalGroup(
            panelTablaPasajesMesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaPasajesMesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        panelTablaPasajesMesLayout.setVerticalGroup(
            panelTablaPasajesMesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaPasajesMesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 680, Short.MAX_VALUE)
                .addContainerGap())
        );

        cmbMesPasajes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Enero", "Febrero ", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" }));

        btnLimpiarPasajes.setText("Limpiar");
        btnLimpiarPasajes.addActionListener(this::btnLimpiarPasajesActionPerformed);

        btnConsultarPasajes.setText("Consultar");
        btnConsultarPasajes.addActionListener(this::btnConsultarPasajesActionPerformed);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Pasajero:");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Mes:");

        lblAnioConsulta.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAnioConsulta.setText("Año:");

        javax.swing.GroupLayout panelFiltrosPasajesPorMesLayout = new javax.swing.GroupLayout(panelFiltrosPasajesPorMes);
        panelFiltrosPasajesPorMes.setLayout(panelFiltrosPasajesPorMesLayout);
        panelFiltrosPasajesPorMesLayout.setHorizontalGroup(
            panelFiltrosPasajesPorMesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFiltrosPasajesPorMesLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbPasajeroMes, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbMesPasajes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblAnioConsulta)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAnioPasajes, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnConsultarPasajes, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLimpiarPasajes)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        panelFiltrosPasajesPorMesLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cmbMesPasajes, cmbPasajeroMes, txtAnioPasajes});

        panelFiltrosPasajesPorMesLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnConsultarPasajes, btnLimpiarPasajes});

        panelFiltrosPasajesPorMesLayout.setVerticalGroup(
            panelFiltrosPasajesPorMesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFiltrosPasajesPorMesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFiltrosPasajesPorMesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbPasajeroMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(cmbMesPasajes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAnioConsulta)
                    .addComponent(txtAnioPasajes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConsultarPasajes)
                    .addComponent(btnLimpiarPasajes))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        panelFiltrosPasajesPorMesLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cmbMesPasajes, cmbPasajeroMes, txtAnioPasajes});

        javax.swing.GroupLayout panelPasajesMesLayout = new javax.swing.GroupLayout(panelPasajesMes);
        panelPasajesMes.setLayout(panelPasajesMesLayout);
        panelPasajesMesLayout.setHorizontalGroup(
            panelPasajesMesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPasajesMesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPasajesMesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelTablaPasajesMes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelFiltrosPasajesPorMes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelPasajesMesLayout.setVerticalGroup(
            panelPasajesMesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPasajesMesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelFiltrosPasajesPorMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTablaPasajesMes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Pasajes por mes", panelPasajesMes);

        btnLimpiarChoferes.setText("Limpiar");
        btnLimpiarChoferes.addActionListener(this::btnLimpiarChoferesActionPerformed);

        btnConsultarChoferes.setText("Consultar");
        btnConsultarChoferes.addActionListener(this::btnConsultarChoferesActionPerformed);

        javax.swing.GroupLayout panelConsultarColectivosChoferesLayout = new javax.swing.GroupLayout(panelConsultarColectivosChoferes);
        panelConsultarColectivosChoferes.setLayout(panelConsultarColectivosChoferesLayout);
        panelConsultarColectivosChoferesLayout.setHorizontalGroup(
            panelConsultarColectivosChoferesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelConsultarColectivosChoferesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnConsultarChoferes, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLimpiarChoferes, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelConsultarColectivosChoferesLayout.setVerticalGroup(
            panelConsultarColectivosChoferesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelConsultarColectivosChoferesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelConsultarColectivosChoferesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConsultarChoferes)
                    .addComponent(btnLimpiarChoferes))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tablaColectivosChoferes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Viaje", "Origen", "Destino", "Fecha salida", "Hora salida", "Matrícula", "Marca", "Modelo", "Chofer"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tablaColectivosChoferes);

        javax.swing.GroupLayout panelTablaColectivosChoferesLayout = new javax.swing.GroupLayout(panelTablaColectivosChoferes);
        panelTablaColectivosChoferes.setLayout(panelTablaColectivosChoferesLayout);
        panelTablaColectivosChoferesLayout.setHorizontalGroup(
            panelTablaColectivosChoferesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaColectivosChoferesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );
        panelTablaColectivosChoferesLayout.setVerticalGroup(
            panelTablaColectivosChoferesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaColectivosChoferesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 686, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelColectivosChoferesLayout = new javax.swing.GroupLayout(panelColectivosChoferes);
        panelColectivosChoferes.setLayout(panelColectivosChoferesLayout);
        panelColectivosChoferesLayout.setHorizontalGroup(
            panelColectivosChoferesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelColectivosChoferesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelColectivosChoferesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelConsultarColectivosChoferes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelTablaColectivosChoferes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelColectivosChoferesLayout.setVerticalGroup(
            panelColectivosChoferesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelColectivosChoferesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelConsultarColectivosChoferes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTablaColectivosChoferes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Colectivos y choferes", panelColectivosChoferes);

        cmbHoraAsientos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbMinutoAsientos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnConsultarAsientosHora.setText("Consultar");
        btnConsultarAsientosHora.addActionListener(this::btnConsultarAsientosHoraActionPerformed);

        btnLimpiarAsientosHora.setText("Limpiar");
        btnLimpiarAsientosHora.addActionListener(this::btnLimpiarAsientosHoraActionPerformed);

        lblHoraConsulta1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHoraConsulta1.setText("Hora:");

        javax.swing.GroupLayout panelConsultarAsientosHorarioLayout = new javax.swing.GroupLayout(panelConsultarAsientosHorario);
        panelConsultarAsientosHorario.setLayout(panelConsultarAsientosHorarioLayout);
        panelConsultarAsientosHorarioLayout.setHorizontalGroup(
            panelConsultarAsientosHorarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelConsultarAsientosHorarioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblHoraConsulta1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbHoraAsientos, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbMinutoAsientos, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnConsultarAsientosHora, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLimpiarAsientosHora, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelConsultarAsientosHorarioLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnConsultarAsientosHora, btnLimpiarAsientosHora});

        panelConsultarAsientosHorarioLayout.setVerticalGroup(
            panelConsultarAsientosHorarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelConsultarAsientosHorarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelConsultarAsientosHorarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHoraConsulta1)
                    .addComponent(cmbHoraAsientos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbMinutoAsientos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpiarAsientosHora)
                    .addComponent(btnConsultarAsientosHora))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tablaAsientosHorario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Viaje", "Origen", "Destino", "Fecha Salida", "Hora Salida", "Colectivo", "Asiento", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tablaAsientosHorario);

        javax.swing.GroupLayout panelTablaAsientosHorarioLayout = new javax.swing.GroupLayout(panelTablaAsientosHorario);
        panelTablaAsientosHorario.setLayout(panelTablaAsientosHorarioLayout);
        panelTablaAsientosHorarioLayout.setHorizontalGroup(
            panelTablaAsientosHorarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaAsientosHorarioLayout.createSequentialGroup()
                .addComponent(jScrollPane4)
                .addContainerGap())
        );
        panelTablaAsientosHorarioLayout.setVerticalGroup(
            panelTablaAsientosHorarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaAsientosHorarioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 686, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelAsientosHorarioLayout = new javax.swing.GroupLayout(panelAsientosHorario);
        panelAsientosHorario.setLayout(panelAsientosHorarioLayout);
        panelAsientosHorarioLayout.setHorizontalGroup(
            panelAsientosHorarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAsientosHorarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAsientosHorarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelConsultarAsientosHorario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelTablaAsientosHorario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelAsientosHorarioLayout.setVerticalGroup(
            panelAsientosHorarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAsientosHorarioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelConsultarAsientosHorario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTablaAsientosHorario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Asientos por horario", panelAsientosHorario);

        lblFechaConsulta.setText("Fecha:");

        dateColectivosDestino.setDateFormatString("dd/MM/yyyy");

        lblDestinoConsulta1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDestinoConsulta1.setText("Destino:");

        btnConsultarColectivosPorDestino.setText("Consultar");
        btnConsultarColectivosPorDestino.addActionListener(this::btnConsultarColectivosPorDestinoActionPerformed);

        btnLimpiarColectivosPorDestino.setText("Limpiar");
        btnLimpiarColectivosPorDestino.addActionListener(this::btnLimpiarColectivosPorDestinoActionPerformed);

        javax.swing.GroupLayout panelBotonesLayout = new javax.swing.GroupLayout(panelBotones);
        panelBotones.setLayout(panelBotonesLayout);
        panelBotonesLayout.setHorizontalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDestinoConsulta1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDestinoColectivos, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblFechaConsulta)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateColectivosDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnConsultarColectivosPorDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLimpiarColectivosPorDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelBotonesLayout.setVerticalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateColectivosDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblDestinoConsulta1)
                        .addComponent(txtDestinoColectivos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblFechaConsulta))
                    .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnConsultarColectivosPorDestino)
                        .addComponent(btnLimpiarColectivosPorDestino)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tablaColectivosDestino.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Viaje", "Origen", "Destino", "Fecha Salida", "Hora Salida", "Matricula", "Marca", "Modelo", "Chofer"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(tablaColectivosDestino);

        javax.swing.GroupLayout panelTablaColectivosDestinoLayout = new javax.swing.GroupLayout(panelTablaColectivosDestino);
        panelTablaColectivosDestino.setLayout(panelTablaColectivosDestinoLayout);
        panelTablaColectivosDestinoLayout.setHorizontalGroup(
            panelTablaColectivosDestinoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTablaColectivosDestinoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5)
                .addContainerGap())
        );
        panelTablaColectivosDestinoLayout.setVerticalGroup(
            panelTablaColectivosDestinoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaColectivosDestinoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelConsultaDestinoColectivosLayout = new javax.swing.GroupLayout(panelConsultaDestinoColectivos);
        panelConsultaDestinoColectivos.setLayout(panelConsultaDestinoColectivosLayout);
        panelConsultaDestinoColectivosLayout.setHorizontalGroup(
            panelConsultaDestinoColectivosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelConsultaDestinoColectivosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelBotones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(panelTablaColectivosDestino, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelConsultaDestinoColectivosLayout.setVerticalGroup(
            panelConsultaDestinoColectivosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelConsultaDestinoColectivosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTablaColectivosDestino, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelColectivosDestinoLayout = new javax.swing.GroupLayout(panelColectivosDestino);
        panelColectivosDestino.setLayout(panelColectivosDestinoLayout);
        panelColectivosDestinoLayout.setHorizontalGroup(
            panelColectivosDestinoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelColectivosDestinoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelConsultaDestinoColectivos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelColectivosDestinoLayout.setVerticalGroup(
            panelColectivosDestinoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelColectivosDestinoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelConsultaDestinoColectivos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Colectivos por destino", panelColectivosDestino);

        lblViaje.setText("ID Viaje:");

        btnLimpiarAsientosPorRuta.setText("Limpiar");
        btnLimpiarAsientosPorRuta.addActionListener(this::btnLimpiarAsientosPorRutaActionPerformed);

        btnConsultarAsientosPorRuta.setText("Consultar");
        btnConsultarAsientosPorRuta.addActionListener(this::btnConsultarAsientosPorRutaActionPerformed);

        javax.swing.GroupLayout panelConsultaAsientosRutaLayout = new javax.swing.GroupLayout(panelConsultaAsientosRuta);
        panelConsultaAsientosRuta.setLayout(panelConsultaAsientosRutaLayout);
        panelConsultaAsientosRutaLayout.setHorizontalGroup(
            panelConsultaAsientosRutaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelConsultaAsientosRutaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblViaje)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRutaAsientos, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnConsultarAsientosPorRuta, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLimpiarAsientosPorRuta, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelConsultaAsientosRutaLayout.setVerticalGroup(
            panelConsultaAsientosRutaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelConsultaAsientosRutaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelConsultaAsientosRutaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblViaje)
                    .addComponent(txtRutaAsientos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelConsultaAsientosRutaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnConsultarAsientosPorRuta)
                        .addComponent(btnLimpiarAsientosPorRuta)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tablaAsientosRuta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Viaje", "Origen", "Destino", "Fecha Salida", "Hora Salida", "Asiento", "Estado", "ID Pasaje"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(tablaAsientosRuta);

        javax.swing.GroupLayout panelTablaAsientosRutaLayout = new javax.swing.GroupLayout(panelTablaAsientosRuta);
        panelTablaAsientosRuta.setLayout(panelTablaAsientosRutaLayout);
        panelTablaAsientosRutaLayout.setHorizontalGroup(
            panelTablaAsientosRutaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaAsientosRutaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6)
                .addContainerGap())
        );
        panelTablaAsientosRutaLayout.setVerticalGroup(
            panelTablaAsientosRutaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaAsientosRutaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 686, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelAsientosRutaLayout = new javax.swing.GroupLayout(panelAsientosRuta);
        panelAsientosRuta.setLayout(panelAsientosRutaLayout);
        panelAsientosRutaLayout.setHorizontalGroup(
            panelAsientosRutaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAsientosRutaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAsientosRutaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelConsultaAsientosRuta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelTablaAsientosRuta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelAsientosRutaLayout.setVerticalGroup(
            panelAsientosRutaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAsientosRutaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelConsultaAsientosRuta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTablaAsientosRuta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Asientos por ruta", panelAsientosRuta);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed
        String destino = txtDestinoViajes.getText().trim();

        String horaSeleccionada = cmbHoraConsulta.getSelectedItem().toString();
        String minutoSeleccionado = cmbMinutoConsulta.getSelectedItem().toString();

        boolean sinHora = horaSeleccionada.equals("Todas");
        boolean sinMinuto = minutoSeleccionado.equals("Todos");

        if (sinHora != sinMinuto) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar tanto la hora como los minutos, o dejar ambos sin filtro.", "Filtro incompleto", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalTime hora = null;

        if (!sinHora) {
            int h = Integer.parseInt(horaSeleccionada);
            int m = Integer.parseInt(minutoSeleccionado);
            hora = LocalTime.of(h, m);
        }

        boolean porSalida = cmbTipoHora.getSelectedIndex() == 0;

        List<Object[]> resultados = rutaData.listarViajesYCapacidad(destino, hora, porSalida);

        modeloViajes.setRowCount(0);

        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Object[] fila : resultados) {
            fila[3] = ((LocalDate) fila[3]).format(formatoFecha);
            modeloViajes.addRow(fila);
        }

        if (resultados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron viajes con los filtros seleccionados.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnConsultarActionPerformed

    private void btnLimpiarConsultaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarConsultaActionPerformed
        txtDestinoViajes.setText("");
        cmbTipoHora.setSelectedIndex(0);
        cmbHoraConsulta.setSelectedIndex(0);
        cmbMinutoConsulta.setSelectedIndex(0);
        modeloViajes.setRowCount(0);
    }//GEN-LAST:event_btnLimpiarConsultaActionPerformed

    private void btnLimpiarPasajesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarPasajesActionPerformed
        cmbPasajeroMes.setSelectedIndex(0);
        cmbMesPasajes.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
        txtAnioPasajes.setText(String.valueOf(LocalDate.now().getYear()));
        ((DefaultTableModel) tablaPasajesMes.getModel()).setRowCount(0);
    }//GEN-LAST:event_btnLimpiarPasajesActionPerformed

    private void btnConsultarPasajesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarPasajesActionPerformed
        int indicePasajero = cmbPasajeroMes.getSelectedIndex();

        if (indicePasajero <= 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un pasajero.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int anio;

        try {
            anio = Integer.parseInt(txtAnioPasajes.getText().trim());

            if (anio < 1900 || anio > 9999) {
                JOptionPane.showMessageDialog(this, "Ingrese un año válido.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El año debe ser un número entero.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Pasajero pasajero = pasajerosMes.get(indicePasajero - 1);
        int mes = cmbMesPasajes.getSelectedIndex() + 1;

        List<Object[]> resultados = pasajeData.listarPasajesPorPasajeroYMes(pasajero.getID_pasajero(), mes, anio);

        mostrarResultados(tablaPasajesMes, resultados, 6);

    }//GEN-LAST:event_btnConsultarPasajesActionPerformed

    private void btnConsultarColectivosPorDestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarColectivosPorDestinoActionPerformed
        String destino = txtDestinoColectivos.getText().trim();

        if (destino.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un destino.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (dateColectivosDestino.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una fecha.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate fecha = dateColectivosDestino.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        List<Object[]> resultados = rutaData.listarColectivosPorDestinoYFecha(destino, fecha);

        mostrarResultados(tablaColectivosDestino, resultados, 3);

    }//GEN-LAST:event_btnConsultarColectivosPorDestinoActionPerformed

    private void btnLimpiarColectivosPorDestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarColectivosPorDestinoActionPerformed
        txtDestinoColectivos.setText("");
        dateColectivosDestino.setDate(null);
        ((DefaultTableModel) tablaColectivosDestino.getModel()).setRowCount(0);
    }//GEN-LAST:event_btnLimpiarColectivosPorDestinoActionPerformed

    private void btnLimpiarAsientosPorRutaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarAsientosPorRutaActionPerformed
        txtRutaAsientos.setText("");
        ((DefaultTableModel) tablaAsientosRuta.getModel()).setRowCount(0);
    }//GEN-LAST:event_btnLimpiarAsientosPorRutaActionPerformed

    private void btnConsultarAsientosPorRutaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarAsientosPorRutaActionPerformed
        int idRuta;

        try {
            idRuta = Integer.parseInt(txtRutaAsientos.getText().trim());

            if (idRuta <= 0) {
                JOptionPane.showMessageDialog(this, "Debe ingresar un ID de viaje válido.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID del viaje debe ser un número entero.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Object[]> resultados = lugarData.listarAsientosPorRuta(idRuta);

        mostrarResultados(tablaAsientosRuta, resultados, 3);

    }//GEN-LAST:event_btnConsultarAsientosPorRutaActionPerformed

    private void btnConsultarChoferesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarChoferesActionPerformed
        List<Object[]> resultados = rutaData.listarColectivosPorViaje();
        mostrarResultados(tablaColectivosChoferes, resultados, 3);
    }//GEN-LAST:event_btnConsultarChoferesActionPerformed

    private void btnLimpiarChoferesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarChoferesActionPerformed
        ((DefaultTableModel) tablaColectivosChoferes.getModel()).setRowCount(0);
    }//GEN-LAST:event_btnLimpiarChoferesActionPerformed

    private void btnConsultarAsientosHoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarAsientosHoraActionPerformed
        int hora = Integer.parseInt(cmbHoraAsientos.getSelectedItem().toString());
        int minuto = Integer.parseInt(cmbMinutoAsientos.getSelectedItem().toString());

        LocalTime horario = LocalTime.of(hora, minuto);

        List<Object[]> resultados = lugarData.listarAsientosPorHora(horario);

        mostrarResultados(tablaAsientosHorario, resultados, 3);
    }//GEN-LAST:event_btnConsultarAsientosHoraActionPerformed

    private void btnLimpiarAsientosHoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarAsientosHoraActionPerformed
        cmbHoraAsientos.setSelectedItem("20");
        cmbMinutoAsientos.setSelectedItem("00");
        ((DefaultTableModel) tablaAsientosHorario.getModel()).setRowCount(0);
    }//GEN-LAST:event_btnLimpiarAsientosHoraActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TablaViajes;
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnConsultarAsientosHora;
    private javax.swing.JButton btnConsultarAsientosPorRuta;
    private javax.swing.JButton btnConsultarChoferes;
    private javax.swing.JButton btnConsultarColectivosPorDestino;
    private javax.swing.JButton btnConsultarPasajes;
    private javax.swing.JButton btnLimpiarAsientosHora;
    private javax.swing.JButton btnLimpiarAsientosPorRuta;
    private javax.swing.JButton btnLimpiarChoferes;
    private javax.swing.JButton btnLimpiarColectivosPorDestino;
    private javax.swing.JButton btnLimpiarConsulta;
    private javax.swing.JButton btnLimpiarPasajes;
    private javax.swing.JComboBox<String> cmbHoraAsientos;
    private javax.swing.JComboBox<String> cmbHoraConsulta;
    private javax.swing.JComboBox<String> cmbMesPasajes;
    private javax.swing.JComboBox<String> cmbMinutoAsientos;
    private javax.swing.JComboBox<String> cmbMinutoConsulta;
    private javax.swing.JComboBox<Object> cmbPasajeroMes;
    private javax.swing.JComboBox<String> cmbTipoHora;
    private com.toedter.calendar.JDateChooser dateColectivosDestino;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblAnioConsulta;
    private javax.swing.JLabel lblDestinoConsulta;
    private javax.swing.JLabel lblDestinoConsulta1;
    private javax.swing.JLabel lblFechaConsulta;
    private javax.swing.JLabel lblHoraConsulta;
    private javax.swing.JLabel lblHoraConsulta1;
    private javax.swing.JLabel lblTipoHora;
    private javax.swing.JLabel lblViaje;
    private javax.swing.JPanel panelAsientosHorario;
    private javax.swing.JPanel panelAsientosRuta;
    private javax.swing.JPanel panelBotones;
    private javax.swing.JPanel panelColectivosChoferes;
    private javax.swing.JPanel panelColectivosDestino;
    private javax.swing.JPanel panelConsultaAsientosRuta;
    private javax.swing.JPanel panelConsultaDestinoColectivos;
    private javax.swing.JPanel panelConsultarAsientosHorario;
    private javax.swing.JPanel panelConsultarColectivosChoferes;
    private javax.swing.JPanel panelFiltrosPasajesPorMes;
    private javax.swing.JPanel panelFiltrosViajes;
    private javax.swing.JPanel panelPasajesMes;
    private javax.swing.JPanel panelTablaAsientosHorario;
    private javax.swing.JPanel panelTablaAsientosRuta;
    private javax.swing.JPanel panelTablaColectivosChoferes;
    private javax.swing.JPanel panelTablaColectivosDestino;
    private javax.swing.JPanel panelTablaPasajesMes;
    private javax.swing.JPanel panelTablaViajes;
    private javax.swing.JPanel panelViajesCapacidad;
    private javax.swing.JTable tablaAsientosHorario;
    private javax.swing.JTable tablaAsientosRuta;
    private javax.swing.JTable tablaColectivosChoferes;
    private javax.swing.JTable tablaColectivosDestino;
    private javax.swing.JTable tablaPasajesMes;
    private javax.swing.JTextField txtAnioPasajes;
    private javax.swing.JTextField txtDestinoColectivos;
    private javax.swing.JTextField txtDestinoViajes;
    private javax.swing.JTextField txtRutaAsientos;
    // End of variables declaration//GEN-END:variables
}
