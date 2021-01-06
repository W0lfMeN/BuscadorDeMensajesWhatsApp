/*
 * @author Carlos
 * Created on 23 dic. 2020
 * @version
 */
package LecturaFichero;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

// TODO: Auto-generated Javadoc
/**
 * The Class Ventana.
 */
public class Ventana extends JFrame implements ActionListener {

	/** Declaracion de atributos. */
	private static final long serialVersionUID = 1L;

	private JLabel lineaRuta; // Label que informa de la accion del boton abrir
	private JLabel lineaCadenaBuscar; // Label que informa de lo que se debe de colocar en el JTextField

	private JButton btIniciar; // Boton que inicia la busqueda de las cadenas en el fichero
	private JButton btBuscar; // Boton que abre un cuadro para la busqueda de la cadena en el fichero
	private JButton btReset; // Boton que resetea el JTextArea, el fichero seleccionado y el JTextField

	private JTextArea areaBusquedas; // Se mostraran los resultados de la busqueda

	private JTextField cadenaBuscar; // Se escribe la palabra o cadena a buscar

	private static String ruta; // Se guardará la ruta del archivo una vez buscado

	private static ArrayList<String> coincidencias = new ArrayList<String>(); // Guarda las cadenas en las que coincide

	/**
	 * Instantiates a new ventana. Created on 23 dic. 2020
	 *
	 * @author Carlos
	 */
	public Ventana() {
		/**
		 * Declaracion del titulo de la ventana y de las medidas de la misma
		 */
		super("Swing Practica01 CarlosJoseMartinezSanchez");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 500);
		setLocationRelativeTo(null);
		setLayout(null);
		// Esto hace que no podamos modificar las dimensiones de la ventana
		setResizable(false);

		// Inicializamos variables
		lineaRuta = new JLabel("No hay archivo seleccionado");
		lineaCadenaBuscar = new JLabel("Introduce la cadena a buscar :");
		btIniciar = new JButton("Iniciar busqueda");
		btBuscar = new JButton("Abrir...");
		btReset = new JButton("Reset");

		areaBusquedas = new JTextArea();
		areaBusquedas.setEditable(false);
		JScrollPane deslizar = new JScrollPane(areaBusquedas);
		deslizar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		deslizar.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		cadenaBuscar = new JTextField();
		// ---------------------------------------------------

		// Damos medidas
		lineaRuta.setBounds(25, 25, 200, 25);
		btBuscar.setBounds(850, 25, 100, 25);
		btIniciar.setBounds(810, 90, 140, 25);
		btReset.setBounds(700, 90, 100, 25);
		lineaCadenaBuscar.setBounds(25, 50, 175, 100);
		cadenaBuscar.setBounds(200, 90, 400, 25);
		deslizar.setBounds(0, 125, 987, 339);
		// ---------------------------------------------------

		// Añadimos los listeners
		btBuscar.addActionListener(this);
		btIniciar.addActionListener(this);
		btReset.addActionListener(this);

		// Añadimos
		add(lineaRuta);
		add(lineaCadenaBuscar);
		add(deslizar);
		add(cadenaBuscar);

		/*
		 * Este bloque try cambia la interfaz por la que usa el sistema operativo
		 * Se coloca antes de los botones para evitar que estos cambien de forma durante la ejecución del programa
		 * Asi evitamos que el resto de la ventana cambie tambien a la interfaz que usa el sistema operativo
		 */
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "SE HA PRODUCIDO UN ERROR", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		
		//Añadimos los botones
		add(btIniciar);
		add(btBuscar);
		add(btReset);
		
		// Mostramos el panel
		this.setVisible(true);
	}

	/**
	 * Metodo que realida una accion en base al boton pulsado
	 *
	 * @param El actionListener
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btBuscar) {
			seleccionarFichero();
		}

		if (e.getSource() == btIniciar) {
			iniciarBusqueda();
		}

		if (e.getSource() == btReset) {
			areaBusquedas.setText("");
			cadenaBuscar.setText("");
			lineaRuta.setText("No hay archivo seleccionado");
			ruta = null;

		}
	}

	/**
	 * Metodo que permite seleccionar un fichero fichero txt
	 */
	private void seleccionarFichero() {
		// muestra el cuadro de diálogo de archivos, para que el usuario pueda elegir el
		// archivo a abrir
		JFileChooser selectorArchivos = new JFileChooser();
		selectorArchivos.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos Txt", "txt");
		selectorArchivos.setFileFilter(filtro);

		// indica cual fue la accion de usuario sobre el jfilechooser
		int resultado = selectorArchivos.showOpenDialog(this);

		File archivo = selectorArchivos.getSelectedFile(); // obtiene el archivo seleccionado

		if (resultado == 0) {
			// muestra error si es inválido
			if ((archivo == null) || (archivo.getName().equals(""))) {
				JOptionPane.showMessageDialog(this, "Nombre de archivo inválido", "Nombre de archivo inválido",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			ruta = archivo.getAbsolutePath(); // Se asigna la ruta a la variable
			lineaRuta.setText(ruta); // Se cambia el texto del label por la ruta seleccionada
		}
	}

	/**
	 * Metodo que inicia la busqueda en el fichero, comprobando previamente si se
	 * han introducido bien todos los campos como lo son la ruta y la cadena a
	 * buscar
	 */
	private void iniciarBusqueda() {
		if (ruta == null) {
			JOptionPane.showMessageDialog(this, "No hay fichero seleccionado", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (cadenaBuscar.getText().trim().length() == 0) {
			JOptionPane.showMessageDialog(this, "No hay ninguna cadena a buscar", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		leerFichero(); // Llamada al metodo de leerFichero

		//Bucle que añade una a una las posiciones del arrayList
		for (int i = 0; i < coincidencias.size(); i++) {
			areaBusquedas.append("Linea " + i + ": " + coincidencias.get(i) + "\n");
		}

		areaBusquedas.append("------------------FIN DEL ARCHIVO");
	}

	/**
	 * Metodo que lee el fichero en busca de la cadena introducida
	 */
	private void leerFichero() {
		String cadena;
		File archivo = new File(ruta);
		int cantidadCoincidencias = 1;
		try (BufferedReader fileIN = new BufferedReader(new FileReader(archivo))) {
			cadena = fileIN.readLine();

			areaBusquedas.setText("------------------LEYENDO ARCHIVO\n");

			while (cadena != null) {
				cadena = cadena.toLowerCase();
				if (cadena.contains(cadenaBuscar.getText().trim())) {
					coincidencias.add(cadena);
					cantidadCoincidencias = cantidadCoincidencias + 1;
				}

				cadena = fileIN.readLine();
			}
			areaBusquedas.append("------------------Fichero leido correctamente, se han encontrado "
					+ cantidadCoincidencias + " coincidencias\n");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "SE HA PRODUCIDO UN ERROR DE LECTURA", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

}
