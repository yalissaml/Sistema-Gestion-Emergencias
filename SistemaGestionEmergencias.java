package sistemagestionemergencias;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SistemaGestionEmergencias {

    private Scanner sc;

    // ===== Variables globales =====
    private int opcion, opcionTipo, personasAfectadas, cantidadEmergencias;
    private int totalIncendios, totalDesastres, totalRobos, totalAccidentes, totalMedicas;
    private int numBomberosReq, numPoliciaReq, numAmbulanciaReq, numRiesgoReq;
    private int numBomberosAsig, numPoliciaAsig, numAmbulanciaAsig, numRiesgoAsig;

    private String ubicacion, descripcion, institucion, prioridad, tipoEmergencia;
    private double porcIncendios, porcDesastres, porcRobos, porcAccidentes, porcMedicas;

    private int[] afectados, bomberosAsig, policiaAsig, ambulanciaAsig, riesgoAsig;
    private int[][] bomberosIdxAsig, policiaIdxAsig, ambulanciaIdxAsig, riesgoIdxAsig;
    private int[] levesReg, gravesReg, riesgoVidaReg;
    private int[] puntosPrioridadReg;

    private boolean[] heridosFlag, riesgoFlag, armaFlag;
    private boolean salir, hayHeridos, riesgoPoblacional, armaFuego;
    private String[] tipo, lugar, detalle, prioridadEmergencia, institucionAsignada, estadoEmergencia;

    private int[] Bomberos, Policia, Ambulancia, GestionRiesgos;

    private static final String ESTADO_ACTIVA = "Activa";
    private static final String ESTADO_ESPERA = "En espera de apoyo externo";
    private static final String ESTADO_ATENDIDA = "Atendida";

    public static void main(String[] args) {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            
        }
        SistemaGestionEmergencias app = new SistemaGestionEmergencias();
        try {
            app.sc = new Scanner(System.in, "UTF-8");
        } catch (Exception e) {
            app.sc = new Scanner(System.in);
        }
        app.ejecutar();
    }

    // ================= Algoritmo principal =================
    private void ejecutar() {
        final int MAX = 100;

        tipo = new String[MAX + 1];
        lugar = new String[MAX + 1];
        detalle = new String[MAX + 1];
        afectados = new int[MAX + 1];
        prioridadEmergencia = new String[MAX + 1];
        institucionAsignada = new String[MAX + 1];
        estadoEmergencia = new String[MAX + 1];
        bomberosAsig = new int[MAX + 1];
        policiaAsig = new int[MAX + 1];
        ambulanciaAsig = new int[MAX + 1];
        riesgoAsig = new int[MAX + 1];
        bomberosIdxAsig = new int[MAX + 1][];
        policiaIdxAsig = new int[MAX + 1][];
        ambulanciaIdxAsig = new int[MAX + 1][];
        riesgoIdxAsig = new int[MAX + 1][];
        heridosFlag = new boolean[MAX + 1];
        riesgoFlag = new boolean[MAX + 1];
        armaFlag = new boolean[MAX + 1];
        levesReg = new int[MAX + 1];
        gravesReg = new int[MAX + 1];
        riesgoVidaReg = new int[MAX + 1];
        puntosPrioridadReg = new int[MAX + 1];

        Bomberos = new int[10];
        Policia = new int[10];
        Ambulancia = new int[10];
        GestionRiesgos = new int[10];

        cantidadEmergencias = 1;
        salir = false;
        totalIncendios = 0;
        totalDesastres = 0;
        totalRobos = 0;
        totalAccidentes = 0;
        totalMedicas = 0;

        inicializarUnidades(Bomberos, Policia, Ambulancia, GestionRiesgos);

        do {
            mostrarMenu(cantidadEmergencias);

            opcion = leerEntero();
            switch (opcion) {
                case 1:
                    if (cantidadEmergencias > 100) {
                        System.out.println("No es posible registrar más emergencias.");
                    } else {
                        registrarEmergencia();
                    }
                    break;

                case 2:
                    consultarReporte();
                    pausarPantalla();
                    break;

                case 3:
                    marcarEmergenciaAtendida();
                    pausarPantalla();
                    break;

                case 4:
                    System.out.println("===== UNIDADES DISPONIBLES =====");
                    System.out.println("Bomberos:            " + contarDisponibles(Bomberos) + " / 10 disponibles");
                    System.out.println("Policía:              " + contarDisponibles(Policia) + " / 10 disponibles");
                    System.out.println("Ambulancias:          " + contarDisponibles(Ambulancia) + " / 10 disponibles");
                    System.out.println("Gestión de Riesgos:   " + contarDisponibles(GestionRiesgos) + " / 10 disponibles");
                    pausarPantalla();
                    break;

                case 5:
                    if (cantidadEmergencias == 1) {
                        System.out.println("No existen emergencias registradas para generar estadísticas.");
                        pausarPantalla();
                    } else {
                        porcIncendios = calcularPorcentaje(totalIncendios, cantidadEmergencias - 1);
                        porcDesastres = calcularPorcentaje(totalDesastres, cantidadEmergencias - 1);
                        porcRobos = calcularPorcentaje(totalRobos, cantidadEmergencias - 1);
                        porcAccidentes = calcularPorcentaje(totalAccidentes, cantidadEmergencias - 1);
                        porcMedicas = calcularPorcentaje(totalMedicas, cantidadEmergencias - 1);

                        System.out.println("===== RESUMEN ESTADÍSTICO =====");
                        System.out.println();
                        System.out.println("Total de reportes registrados: " + (cantidadEmergencias - 1));
                        System.out.println("Incendios:           " + totalIncendios + "  (" + porcIncendios + "%)");
                        System.out.println("Desastres naturales: " + totalDesastres + "  (" + porcDesastres + "%)");
                        System.out.println("Robos:               " + totalRobos + "  (" + porcRobos + "%)");
                        System.out.println("Acc. de tránsito:    " + totalAccidentes + "  (" + porcAccidentes + "%)");
                        System.out.println("Emergencias médicas: " + totalMedicas + "  (" + porcMedicas + "%)");
                        pausarPantalla();
                    }
                    break;

                case 6:
                    salir = confirmarSalida();
                    break;

                default:
                    System.out.println("Opción inválida. Seleccione una opción del 1 al 6.");
                    pausarPantalla();
                    break;
            }
        } while (!salir);

        sc.close();
    }

    private void registrarEmergencia() {
        EmergenciaInput datos = solicitarDatosEmergencia();
        opcionTipo = datos.opcionTipo;
        ubicacion = datos.ubicacion;
        descripcion = datos.descripcion;
        personasAfectadas = datos.personasAfectadas;
        hayHeridos = datos.hayHeridos;
        armaFuego = datos.armaFuego;

        EvaluacionResultado ev = evaluarPrioridadYRecursos(personasAfectadas, datos.leves, datos.graves,
                datos.riesgoVida, hayHeridos, armaFuego, opcionTipo);
        riesgoPoblacional = ev.riesgoPoblacional;
        prioridad = ev.prioridad;
        tipoEmergencia = ev.tipoEmergencia;
        numBomberosReq = ev.numBomberosReq;
        numPoliciaReq = ev.numPoliciaReq;
        numAmbulanciaReq = ev.numAmbulanciaReq;
        numRiesgoReq = ev.numRiesgoReq;

        switch (opcionTipo) {
            case 1: totalIncendios++; break;
            case 2: totalDesastres++; break;
            case 3: totalRobos++; break;
            case 4: totalAccidentes++; break;
            case 5: totalMedicas++; break;
        }

        AsignacionResultado asig = asignarUnidades(Bomberos, Policia, Ambulancia, GestionRiesgos,
                numBomberosReq, numPoliciaReq, numAmbulanciaReq, numRiesgoReq);
        numBomberosAsig = asig.numBomberosAsig;
        numPoliciaAsig = asig.numPoliciaAsig;
        numAmbulanciaAsig = asig.numAmbulanciaAsig;
        numRiesgoAsig = asig.numRiesgoAsig;
        institucion = asig.institucion;

        boolean hayDeficit = (numBomberosAsig < numBomberosReq) || (numPoliciaAsig < numPoliciaReq)
                || (numAmbulanciaAsig < numAmbulanciaReq) || (numRiesgoAsig < numRiesgoReq);
        String estadoInicial = hayDeficit ? ESTADO_ESPERA : ESTADO_ACTIVA;

        guardarDatosBasicos(tipoEmergencia, ubicacion, descripcion, personasAfectadas, prioridad,
                institucion, estadoInicial);

        guardarDatosAsignacion(asig, datos.leves, datos.graves, datos.riesgoVida, ev.puntosPrioridad,
                hayHeridos, riesgoPoblacional, armaFuego);

        mostrarResumenRegistro(prioridad, numBomberosAsig, numPoliciaAsig, numAmbulanciaAsig, numRiesgoAsig,
                tipoEmergencia, hayHeridos, datos.leves, datos.graves, datos.riesgoVida, riesgoPoblacional,
                ev.puntosPrioridad, estadoInicial);

        cantidadEmergencias = cantidadEmergencias + 1;
        pausarPantalla();
    }

    // ================= Subprocesos / Funciones =================

    private void mostrarMenu(int cantidadEmergencias) {
        System.out.println("=======================================================");
        System.out.println("===== SISTEMA DE GESTIÓN DE EMERGENCIAS - MACHALA =====");
        System.out.println("=======================================================");
        System.out.println();
        System.out.println("Emergencias registradas: " + (cantidadEmergencias - 1));
        System.out.println();
        System.out.println("1. Registrar emergencia");
        System.out.println("2. Consultar reporte");
        System.out.println("3. Marcar emergencia como atendida");
        System.out.println("4. Ver disponibilidad de unidades");
        System.out.println("5. Resumen estadístico de emergencias");
        System.out.println("6. Salir");
        System.out.println();
        System.out.println("Seleccione una opción:");
    }

    private void pausarPantalla() {
        System.out.println("Presione ENTER para continuar...");
        sc.nextLine();
        limpiarPantalla();
    }

    private void limpiarPantalla() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    private static class EmergenciaInput {
        int opcionTipo;
        String ubicacion;
        String descripcion;
        int personasAfectadas;
        boolean hayHeridos;
        int leves;
        int graves;
        int riesgoVida;
        boolean armaFuego;
    }

    private EmergenciaInput solicitarDatosEmergencia() {
        EmergenciaInput r = new EmergenciaInput();
        r.armaFuego = false;
        r.hayHeridos = false;
        r.leves = 0;
        r.graves = 0;
        r.riesgoVida = 0;

        System.out.println("Seleccione el tipo de emergencia:");
        System.out.println("1. Incendio");
        System.out.println("2. Desastre natural");
        System.out.println("3. Robo");
        System.out.println("4. Accidente de tránsito");
        System.out.println("5. Emergencia médica");
        do {
            r.opcionTipo = leerEntero();
            if (r.opcionTipo < 1 || r.opcionTipo > 5) {
                System.out.println("Opción inválida. Intente nuevamente (1-5):");
            }
        } while (!(r.opcionTipo >= 1 && r.opcionTipo <= 5));

        if (r.opcionTipo == 3) {
            int i;
            do {
                System.out.println("¿La emergencia involucra arma de fuego o arma blanca? (1-2)");
                System.out.println("1. Sí | 2. No");
                i = leerEntero();
                if (i != 1 && i != 2) {
                    System.out.println("Seleccione una opción válida.");
                }
            } while (!(i == 1 || i == 2));
            if (i == 1) {
                r.armaFuego = true;
            }
        }

        do {
            System.out.println("Ingrese la ubicación de la emergencia:");
            r.ubicacion = sc.nextLine();
            if (r.ubicacion.equals("")) {
                System.out.println("La ubicación no puede estar vacía.");
            }
        } while (r.ubicacion.equals(""));

        do {
            System.out.println("Ingrese una descripción:");
            r.descripcion = sc.nextLine();
            if (r.descripcion.equals("")) {
                System.out.println("La descripción no puede estar vacía.");
            }
        } while (r.descripcion.equals(""));

        r.personasAfectadas = leerEnteroNoNegativo("Ingrese el número de personas afectadas:");

        if (r.personasAfectadas > 0) {
            int i;
            do {
                System.out.println("¿Las personas afectadas presentan lesiones? (1-2)");
                System.out.println("1. Sí");
                System.out.println("2. No");
                i = leerEntero();
                if (i != 1 && i != 2) {
                    System.out.println("Seleccione una opción válida.");
                }
            } while (!(i == 1 || i == 2));

            if (i == 1) {
                r.hayHeridos = true;
                System.out.println("Desglose la gravedad de las lesiones entre los " + r.personasAfectadas + " afectados:");
                boolean sumaValida;
                do {
                    r.leves = leerEnteroNoNegativo("  Personas con lesiones leves:");
                    r.graves = leerEnteroNoNegativo("  Personas con lesiones graves:");
                    r.riesgoVida = leerEnteroNoNegativo("  Personas con riesgo de vida:");
                    int suma = r.leves + r.graves + r.riesgoVida;
                    sumaValida = suma > 0 && suma <= r.personasAfectadas;
                    if (!sumaValida) {
                        System.out.println("La suma de heridos (" + suma + ") debe ser mayor a 0 y no puede superar"
                                + " el total de personas afectadas (" + r.personasAfectadas + "). Intente de nuevo.");
                    }
                } while (!sumaValida);
            }
        }
        return r;
    }

    private static class AsignacionResultado {
        int numBomberosAsig, numPoliciaAsig, numAmbulanciaAsig, numRiesgoAsig;
        String institucion;
        int[] bomberosIdx;
        int[] policiaIdx;
        int[] ambulanciaIdx;
        int[] riesgoIdx;
    }

    private int buscarLibre(int[] unidades) {
        for (int j = 0; j <= 9; j++) {
            if (unidades[j] == 0) {
                return j;
            }
        }
        return -1;
    }

    private int[] aArregloInt(List<Integer> lista) {
        int[] r = new int[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            r[i] = lista.get(i);
        }
        return r;
    }

    private AsignacionResultado asignarUnidades(int[] Bomberos, int[] Policia, int[] Ambulancia,
            int[] GestionRiesgos, int numBomberosReq, int numPoliciaReq, int numAmbulanciaReq,
            int numRiesgoReq) {
        AsignacionResultado res = new AsignacionResultado();
        List<Integer> bIdx = new ArrayList<Integer>();
        List<Integer> pIdx = new ArrayList<Integer>();
        List<Integer> aIdx = new ArrayList<Integer>();
        List<Integer> rIdx = new ArrayList<Integer>();

        // ---- Bomberos ----
        if (numBomberosReq > 0) {
            for (int k = 0; k < numBomberosReq; k++) {
                int idx = buscarLibre(Bomberos);
                if (idx != -1) {
                    Bomberos[idx] = 1;
                    bIdx.add(idx);
                }
            }
            int deficit = numBomberosReq - bIdx.size();
            if (deficit > 0) {
                System.out.println("ALERTA: No hay suficientes unidades de Bomberos disponibles.");
                if (numRiesgoReq == 0) {
                    int cubiertos = 0;
                    for (int k = 0; k < deficit; k++) {
                        int idx = buscarLibre(GestionRiesgos);
                        if (idx == -1) {
                            break;
                        }
                        GestionRiesgos[idx] = 1;
                        rIdx.add(idx);
                        cubiertos++;
                    }
                    if (cubiertos > 0) {
                        System.out.println("Se envían " + cubiertos + " unidad(es) de Gestión de Riesgos como apoyo alterno.");
                    }
                    int faltante = deficit - cubiertos;
                    if (faltante > 0) {
                        System.out.println("Se solicita apoyo externo de " + faltante + " unidad(es) de Bomberos a una localidad vecina.");
                    }
                } else {
                    System.out.println("Se solicita apoyo externo de " + deficit + " unidad(es) de Bomberos a una localidad vecina.");
                }
            }
        }

        // ---- Policía ----
        if (numPoliciaReq > 0) {
            for (int k = 0; k < numPoliciaReq; k++) {
                int idx = buscarLibre(Policia);
                if (idx != -1) {
                    Policia[idx] = 1;
                    pIdx.add(idx);
                }
            }
            int deficit = numPoliciaReq - pIdx.size();
            if (deficit > 0) {
                System.out.println("ALERTA: No hay suficientes unidades de Policía disponibles.");
                System.out.println("Se solicita apoyo externo de " + deficit + " unidad(es) de Policía a una jurisdicción vecina.");
            }
        }

        // ---- Ambulancia ----
        if (numAmbulanciaReq > 0) {
            for (int k = 0; k < numAmbulanciaReq; k++) {
                int idx = buscarLibre(Ambulancia);
                if (idx != -1) {
                    Ambulancia[idx] = 1;
                    aIdx.add(idx);
                }
            }
            int deficit = numAmbulanciaReq - aIdx.size();
            if (deficit > 0) {
                System.out.println("ALERTA: No hay suficientes ambulancias disponibles.");
                System.out.println("Se solicita apoyo externo de " + deficit + " ambulancia(s) a una localidad vecina.");
            }
        }

        // ---- Gestión de Riesgos ----
        if (numRiesgoReq > 0) {
            for (int k = 0; k < numRiesgoReq; k++) {
                int idx = buscarLibre(GestionRiesgos);
                if (idx != -1) {
                    GestionRiesgos[idx] = 1;
                    rIdx.add(idx);
                }
            }
            int deficit = numRiesgoReq - (rIdx.size());
            if (deficit > 0) {
                System.out.println("ALERTA: No hay suficientes unidades de Gestión de Riesgos disponibles.");
                if (numBomberosReq == 0) {
                    int cubiertos = 0;
                    for (int k = 0; k < deficit; k++) {
                        int idx = buscarLibre(Bomberos);
                        if (idx == -1) {
                            break;
                        }
                        Bomberos[idx] = 1;
                        bIdx.add(idx);
                        cubiertos++;
                    }
                    if (cubiertos > 0) {
                        System.out.println("Se envían " + cubiertos + " unidad(es) de Bomberos como apoyo alterno.");
                    }
                    int faltante = deficit - cubiertos;
                    if (faltante > 0) {
                        System.out.println("Se solicita apoyo externo de " + faltante + " unidad(es) de Gestión de Riesgos.");
                    }
                } else {
                    System.out.println("Se solicita apoyo externo de " + deficit + " unidad(es) de Gestión de Riesgos.");
                }
            }
        }

        res.numBomberosAsig = bIdx.size();
        res.numPoliciaAsig = pIdx.size();
        res.numAmbulanciaAsig = aIdx.size();
        res.numRiesgoAsig = rIdx.size();
        res.bomberosIdx = aArregloInt(bIdx);
        res.policiaIdx = aArregloInt(pIdx);
        res.ambulanciaIdx = aArregloInt(aIdx);
        res.riesgoIdx = aArregloInt(rIdx);

        StringBuilder inst = new StringBuilder();
        if (res.numBomberosAsig > 0) inst.append("Bomberos ");
        if (res.numPoliciaAsig > 0) inst.append("Policía ");
        if (res.numAmbulanciaAsig > 0) inst.append("Ambulancia ");
        if (res.numRiesgoAsig > 0) inst.append("Gestión de Riesgos ");
        res.institucion = inst.toString();
        if (res.institucion.equals("")) {
            res.institucion = "SIN UNIDADES DISPONIBLES - Se generó alerta a localidades vecinas";
        }

        return res;
    }

    private void guardarDatosBasicos(String tipoEmergencia, String ubicacion, String descripcion,
            int personasAfectadas, String prioridad, String institucion, String estadoInicial) {
        tipo[cantidadEmergencias] = tipoEmergencia;
        lugar[cantidadEmergencias] = ubicacion;
        detalle[cantidadEmergencias] = descripcion;
        afectados[cantidadEmergencias] = personasAfectadas;
        prioridadEmergencia[cantidadEmergencias] = prioridad;
        institucionAsignada[cantidadEmergencias] = institucion;
        estadoEmergencia[cantidadEmergencias] = estadoInicial;
    }

    private void guardarDatosAsignacion(AsignacionResultado asig, int leves, int graves, int riesgoVida,
            int puntosPrioridad, boolean hayHeridos, boolean riesgoPoblacional, boolean armaFuego) {
        bomberosAsig[cantidadEmergencias] = asig.numBomberosAsig;
        policiaAsig[cantidadEmergencias] = asig.numPoliciaAsig;
        ambulanciaAsig[cantidadEmergencias] = asig.numAmbulanciaAsig;
        riesgoAsig[cantidadEmergencias] = asig.numRiesgoAsig;
        bomberosIdxAsig[cantidadEmergencias] = asig.bomberosIdx;
        policiaIdxAsig[cantidadEmergencias] = asig.policiaIdx;
        ambulanciaIdxAsig[cantidadEmergencias] = asig.ambulanciaIdx;
        riesgoIdxAsig[cantidadEmergencias] = asig.riesgoIdx;

        heridosFlag[cantidadEmergencias] = hayHeridos;
        riesgoFlag[cantidadEmergencias] = riesgoPoblacional;
        armaFlag[cantidadEmergencias] = armaFuego;
        levesReg[cantidadEmergencias] = leves;
        gravesReg[cantidadEmergencias] = graves;
        riesgoVidaReg[cantidadEmergencias] = riesgoVida;
        puntosPrioridadReg[cantidadEmergencias] = puntosPrioridad;
    }

    private static class EvaluacionResultado {
        boolean riesgoPoblacional;
        String prioridad;
        String tipoEmergencia;
        int numBomberosReq, numPoliciaReq, numAmbulanciaReq, numRiesgoReq;
        int puntosPrioridad;
    }

    private EvaluacionResultado evaluarPrioridadYRecursos(int personasAfectadas, int leves, int graves,
            int riesgoVida, boolean hayHeridos, boolean armaFuego, int opcionTipo) {
        EvaluacionResultado ev = new EvaluacionResultado();
        int heridosTotal = leves + graves + riesgoVida;

        ev.riesgoPoblacional = (personasAfectadas >= 5) || (riesgoVida > 0) || (opcionTipo == 2);

        int puntos = 0;
        switch (opcionTipo) {
            case 1: puntos += 10; break; // Incendio
            case 2: puntos += 20; break; // Desastre natural
            case 3: puntos += 5;  break; // Robo
            case 4: puntos += 5;  break; // Accidente de tránsito
            case 5: puntos += 10; break; // Emergencia médica
        }
        puntos += leves * 2;
        puntos += graves * 5;
        puntos += riesgoVida * 10;
        if (armaFuego) {
            puntos += 15;
        }
        if (ev.riesgoPoblacional) {
            puntos += 10;
        }
        ev.puntosPrioridad = puntos;

        if (puntos < 10) {
            ev.prioridad = "Baja";
        } else if (puntos < 25) {
            ev.prioridad = "Media";
        } else if (puntos < 45) {
            ev.prioridad = "Alta";
        } else {
            ev.prioridad = "Muy Alta";
        }

        ev.numBomberosReq = 0;
        ev.numPoliciaReq = 0;
        ev.numAmbulanciaReq = 0;
        ev.numRiesgoReq = 0;

        switch (opcionTipo) {
            case 1:
                ev.tipoEmergencia = "Incendio";
                ev.numBomberosReq = Math.max(1, redondear(personasAfectadas / 3.0));
                ev.numAmbulanciaReq = heridosTotal > 0 ? Math.max(1, redondear(heridosTotal / 4.0)) : 0;
                if (ev.riesgoPoblacional) {
                    ev.numPoliciaReq = 1;
                    ev.numRiesgoReq = 1;
                }
                break;
            case 2:
                ev.tipoEmergencia = "Desastre natural";
                ev.numRiesgoReq = Math.max(1, redondear(personasAfectadas / 5.0));
                ev.numBomberosReq = 1;
                if (ev.riesgoPoblacional) {
                    ev.numPoliciaReq = 1;
                }
                ev.numAmbulanciaReq = heridosTotal > 0 ? Math.max(1, redondear(heridosTotal / 4.0)) : 0;
                if (riesgoVida > 0) {
                    ev.numAmbulanciaReq = Math.max(ev.numAmbulanciaReq, 2);
                }
                break;
            case 3:
                ev.tipoEmergencia = "Robo";
                ev.numPoliciaReq = armaFuego ? 2 : 1;
                ev.numAmbulanciaReq = heridosTotal > 0 ? Math.max(1, redondear(heridosTotal / 4.0)) : 0;
                break;
            case 4:
                ev.tipoEmergencia = "Accidente de tránsito";
                ev.numPoliciaReq = 1;
                ev.numAmbulanciaReq = heridosTotal > 0 ? Math.max(1, redondear(heridosTotal / 4.0)) : 0;
                if (riesgoVida > 0) {
                    ev.numAmbulanciaReq = Math.max(ev.numAmbulanciaReq, 2);
                }
                break;
            case 5:
                ev.tipoEmergencia = "Emergencia médica";
                int base = heridosTotal > 0 ? heridosTotal : Math.max(personasAfectadas, 1);
                ev.numAmbulanciaReq = Math.max(1, redondear(base / 4.0));
                if (riesgoVida > 0) {
                    ev.numAmbulanciaReq = Math.max(ev.numAmbulanciaReq, 2);
                }
                if (ev.riesgoPoblacional) {
                    ev.numPoliciaReq = 1;
                }
                break;
        }

        return ev;
    }

    private int redondear(double valor) {
        return (int) Math.round(valor);
    }

    private void inicializarUnidades(int[] Bomberos, int[] Policia, int[] Ambulancia, int[] GestionRiesgos) {
        for (int j = 0; j <= 9; j++) {
            Bomberos[j] = 0;
            Policia[j] = 0;
            Ambulancia[j] = 0;
            GestionRiesgos[j] = 0;
        }
    }

    private int contarDisponibles(int[] unidades) {
        int c = 0;
        for (int j = 0; j <= 9; j++) {
            if (unidades[j] == 0) {
                c++;
            }
        }
        return c;
    }

    private double calcularPorcentaje(int totalTipo, int totalGeneral) {
        double porc;
        if (totalGeneral == 0) {
            porc = 0;
        } else {
            porc = (totalTipo * 100.0) / totalGeneral;
        }
        return porc;
    }

    private void mostrarResumenRegistro(String prioridad, int numBomberosAsig, int numPoliciaAsig,
            int numAmbulanciaAsig, int numRiesgoAsig, String tipoEmergencia, boolean hayHeridos,
            int leves, int graves, int riesgoVida, boolean riesgoPoblacional, int puntosPrioridad,
            String estadoInicial) {
        System.out.println("==============================================================================");
        System.out.println("Reporte #" + cantidadEmergencias + " registrado | Prioridad: " + prioridad
                + " (" + puntosPrioridad + " pts) | Estado: " + estadoInicial);
        System.out.println("Unidades despachadas:");
        if (numBomberosAsig > 0) {
            System.out.println("  - " + numBomberosAsig + " unidad(es) de Bomberos");
        }
        if (numPoliciaAsig > 0) {
            System.out.println("  - " + numPoliciaAsig + " unidad(es) de Policía");
        }
        if (numAmbulanciaAsig > 0) {
            System.out.println("  - " + numAmbulanciaAsig + " unidad(es) de Ambulancia");
        }
        if (numRiesgoAsig > 0) {
            System.out.println("  - " + numRiesgoAsig + " unidad(es) de Gestión de Riesgos");
        }
        System.out.println("Motivo:");
        System.out.println("        La emergencia corresponde a : " + tipoEmergencia);
        if (hayHeridos) {
            System.out.println("        Heridos -> leves: " + leves + " | graves: " + graves
                    + " | riesgo de vida: " + riesgoVida);
        } else {
            System.out.println("        Sin personas heridas reportadas");
        }
        if (riesgoPoblacional) {
            System.out.println("        Presenta un riesgo alto.");
        } else {
            System.out.println("        Presentan un riesgo bajo/moderado.");
        }
        System.out.println("============================================================================");
    }

    private void consultarReporte() {
        int opcionReporte;

        if (cantidadEmergencias == 1) {
            System.out.println("No existen emergencias registradas.");
        } else {
            System.out.println("========= REPORTES REGISTRADOS =========");
            for (int i = 1; i <= cantidadEmergencias - 1; i++) {
                System.out.println(i + ". [" + prioridadEmergencia[i] + "] " + tipo[i] + " - "
                        + lugar[i] + " (" + estadoEmergencia[i] + ")");
            }
            do {
                System.out.println(" ");
                System.out.println("Seleccione el número del reporte (1 a " + (cantidadEmergencias - 1) + "):");
                opcionReporte = leerEntero();
                if (opcionReporte < 1 || opcionReporte > cantidadEmergencias - 1) {
                    System.out.println("Número de reporte no válido.");
                }
            } while (!(opcionReporte >= 1 && opcionReporte <= cantidadEmergencias - 1));

            System.out.println("====== DETALLE DEL REPORTE #" + opcionReporte + " ======");
            System.out.println("Tipo: " + tipo[opcionReporte] + " | Ubicación: " + lugar[opcionReporte]);
            System.out.println("Descripción: " + detalle[opcionReporte]);
            System.out.println("Personas afectadas: " + afectados[opcionReporte]);
            System.out.println("Institución(es): " + institucionAsignada[opcionReporte]);
            System.out.println("Prioridad: " + prioridadEmergencia[opcionReporte]
                    + " (" + puntosPrioridadReg[opcionReporte] + " pts)");
            System.out.println("Estado: " + estadoEmergencia[opcionReporte]);
            System.out.println();
            System.out.println("     ----- TABLA DE JUSTIFICACIÓN -----");
            System.out.println(" ");
            System.out.println("¿Existen personas heridas?          -> " + heridosFlag[opcionReporte]);
            System.out.println("¿Riesgo para la población?          -> " + riesgoFlag[opcionReporte]);
            System.out.println("Heridos leves / graves / riesgo vida -> " + levesReg[opcionReporte] + " / "
                    + gravesReg[opcionReporte] + " / " + riesgoVidaReg[opcionReporte]);
            System.out.println("¿Arma de fuego/blanca involucrada?  -> " + armaFlag[opcionReporte]);
            System.out.println("-----------------------------------------------------------");
            System.out.println("Unidades despachadas:");
            System.out.println("  Bomberos: " + bomberosAsig[opcionReporte] + arregloComoTexto(bomberosIdxAsig[opcionReporte]));
            System.out.println("  Policía: " + policiaAsig[opcionReporte] + arregloComoTexto(policiaIdxAsig[opcionReporte]));
            System.out.println("  Ambulancia: " + ambulanciaAsig[opcionReporte] + arregloComoTexto(ambulanciaIdxAsig[opcionReporte]));
            System.out.println("  Gestión de Riesgos: " + riesgoAsig[opcionReporte] + arregloComoTexto(riesgoIdxAsig[opcionReporte]));
        }
    }

    private String arregloComoTexto(int[] idx) {
        if (idx == null || idx.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(" (unidades #: ");
        for (int i = 0; i < idx.length; i++) {
            sb.append(idx[i]);
            if (i < idx.length - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    private void marcarEmergenciaAtendida() {
        int opcionReporte;

        if (cantidadEmergencias == 1) {
            System.out.println("No existen emergencias registradas.");
        } else {
            System.out.println("===== EMERGENCIAS ACTIVAS / EN ESPERA =========");
            for (int i = 1; i <= cantidadEmergencias - 1; i++) {
                if (!estadoEmergencia[i].equals(ESTADO_ATENDIDA)) {
                    System.out.println(i + ". [" + prioridadEmergencia[i] + "] " + tipo[i] + " - " + lugar[i]
                            + " (" + estadoEmergencia[i] + ")");
                }
            }
            do {
                System.out.println("");
                System.out.println("Ingrese el número de la emergencia a marcar como atendida (0 para cancelar):");
                opcionReporte = leerEntero();
                if (opcionReporte != 0 && (opcionReporte < 1 || opcionReporte > cantidadEmergencias - 1)) {
                    System.out.println("Número no válido.");
                }
            } while (!(opcionReporte == 0 || (opcionReporte >= 1 && opcionReporte <= cantidadEmergencias - 1)));

            if (opcionReporte != 0) {
                if (estadoEmergencia[opcionReporte].equals(ESTADO_ATENDIDA)) {
                    System.out.println("Esa emergencia ya había sido marcada como atendida.");
                } else {
                    liberarPorIndice(Bomberos, bomberosIdxAsig[opcionReporte]);
                    liberarPorIndice(Policia, policiaIdxAsig[opcionReporte]);
                    liberarPorIndice(Ambulancia, ambulanciaIdxAsig[opcionReporte]);
                    liberarPorIndice(GestionRiesgos, riesgoIdxAsig[opcionReporte]);

                    estadoEmergencia[opcionReporte] = ESTADO_ATENDIDA;
                    System.out.println("Emergencia #" + opcionReporte + " marcada como ATENDIDA.");
                    System.out.println();
                    System.out.println("Liberando unidades...");
                    System.out.println("======================================");
                    System.out.println("EMERGENCIA CERRADA");
                    System.out.println("Unidades liberadas correctamente");
                    System.out.println("======================================");
                }
            }
        }
    }

    private void liberarPorIndice(int[] unidades, int[] indices) {
        if (indices == null) {
            return;
        }
        for (int idx : indices) {
            unidades[idx] = 0;
        }
    }

    private boolean confirmarSalida() {
        int opcionConfirmacion;
        boolean salirLocal = false;

        do {
            System.out.println("¿Está seguro que desea salir?");
            System.out.println("1. Sí");
            System.out.println("2. No");
            opcionConfirmacion = leerEntero();
            if (opcionConfirmacion != 1 && opcionConfirmacion != 2) {
                System.out.println("Seleccione una opción válida.");
            }
        } while (!(opcionConfirmacion == 1 || opcionConfirmacion == 2));

        limpiarPantalla();
        if (opcionConfirmacion == 1) {
            salirLocal = true;
            System.out.println("==== Gracias por utilizar el sistema ====");
        }
        return salirLocal;
    }

    // ================= Utilidades de lectura =================

    private int leerEntero() {
        while (true) {
            String linea = sc.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número entero válido:");
            }
        }
    }

    private int leerEnteroNoNegativo(String prompt) {
        int v;
        do {
            System.out.println(prompt);
            v = leerEntero();
            if (v < 0) {
                System.out.println("Ingrese un número válido (0 o mayor).");
            }
        } while (v < 0);
        return v;
    }
}