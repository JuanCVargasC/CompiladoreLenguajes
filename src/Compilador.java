
import com.formdev.flatlaf.FlatIntelliJLaf;
import compilerTools.CodeBlock;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import compilerTools.Directory;
import compilerTools.ErrorLSSL;
import compilerTools.Functions;
import compilerTools.Grammar;
import compilerTools.Production;
import compilerTools.TextColor;
import compilerTools.Token;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.lang.reflect.Field;

public class Compilador extends javax.swing.JFrame {

    private String title;
    private Directory directorio;
    private ArrayList<Token> tokens;
    private ArrayList<ErrorLSSL> errors;
    private ArrayList<TextColor> textsColor;
    private Timer timerKeyReleased;// funcion para colorear palabras de codigo
    private ArrayList<Production> identProd;// identificadores sintaxis
    private HashMap<String, String> identificadores;//
    private boolean codeHasBeenCompiled = false;
    String archivo;

    public Compilador() {
        initComponents();
        init();
    }

    private void init() {
        title = "Compiler";// nombre compiladore
        setLocationRelativeTo(null);
        setTitle(title);
        directorio = new Directory(this, jtpCode, title, ".comp");// 4to extencion archivhos de codigo
        addWindowListener(new WindowAdapter() {// Cuando presiona la "X" de la esquina superior derecha
            @Override
            public void windowClosing(WindowEvent e) {
                directorio.Exit();
                System.exit(0);
            }
        });
        Functions.setLineNumberOnJTextComponent(jtpCode);//
        timerKeyReleased = new Timer((int) (1000 * 0.3), (ActionEvent e) -> {// timer para escribir
            timerKeyReleased.stop();
            colorAnalysis();// funcion
        });
        Functions.insertAsteriskInName(this, jtpCode, () -> {// editor añade el asterisco a linea editada
            timerKeyReleased.restart();
        });
        // inciamos nuestros arraylist
        tokens = new ArrayList<>();
        errors = new ArrayList<>();
        textsColor = new ArrayList<>();
        identProd = new ArrayList<>();
        identificadores = new HashMap<>();
        Functions.setAutocompleterJTextComponent(new String[] {}, jtpCode, () -> {// auto completador
            timerKeyReleased.restart();
        });
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        rootPanel = new javax.swing.JPanel();
        buttonsFilePanel = new javax.swing.JPanel();
        btnAbrir = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnGuardarC = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtpCode = new javax.swing.JTextPane();
        panelButtonCompilerExecute = new javax.swing.JPanel();
        btnCompilar = new javax.swing.JButton();
        btnEjecutar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtaOutputConsole = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblTokens = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        btnAbrir.setText("Abrir");
        btnAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirActionPerformed(evt);
            }
        });

        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnGuardarC.setText("Guardar como");
        btnGuardarC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarCActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonsFilePanelLayout = new javax.swing.GroupLayout(buttonsFilePanel);
        buttonsFilePanel.setLayout(buttonsFilePanelLayout);
        buttonsFilePanelLayout.setHorizontalGroup(
                buttonsFilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(buttonsFilePanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btnNuevo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAbrir)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnGuardar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnGuardarC)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        buttonsFilePanelLayout.setVerticalGroup(
                buttonsFilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(buttonsFilePanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(buttonsFilePanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnAbrir)
                                        .addComponent(btnNuevo)
                                        .addComponent(btnGuardar)
                                        .addComponent(btnGuardarC))
                                .addContainerGap(16, Short.MAX_VALUE)));

        jScrollPane1.setViewportView(jtpCode);

        btnCompilar.setText("Compilar");
        btnCompilar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCompilarActionPerformed(evt);
            }
        });

        btnEjecutar.setText("Ejecutar");
        btnEjecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEjecutarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelButtonCompilerExecuteLayout = new javax.swing.GroupLayout(
                panelButtonCompilerExecute);
        panelButtonCompilerExecute.setLayout(panelButtonCompilerExecuteLayout);
        panelButtonCompilerExecuteLayout.setHorizontalGroup(
                panelButtonCompilerExecuteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelButtonCompilerExecuteLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnCompilar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEjecutar)
                                .addContainerGap()));
        panelButtonCompilerExecuteLayout.setVerticalGroup(
                panelButtonCompilerExecuteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelButtonCompilerExecuteLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelButtonCompilerExecuteLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnCompilar)
                                        .addComponent(btnEjecutar))
                                .addContainerGap(7, Short.MAX_VALUE)));

        jtaOutputConsole.setEditable(false);
        jtaOutputConsole.setBackground(new java.awt.Color(255, 255, 255));
        jtaOutputConsole.setColumns(20);
        jtaOutputConsole.setRows(5);
        jScrollPane2.setViewportView(jtaOutputConsole);

        tblTokens.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "Componente léxico", "Lexema", "[Línea, Columna]"
                }) {
            boolean[] canEdit = new boolean[] {
                    false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        tblTokens.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tblTokens);

        javax.swing.GroupLayout rootPanelLayout = new javax.swing.GroupLayout(rootPanel);
        rootPanel.setLayout(rootPanelLayout);
        rootPanelLayout.setHorizontalGroup(
                rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(rootPanelLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(rootPanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, rootPanelLayout
                                                .createSequentialGroup()
                                                .addComponent(buttonsFilePanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(panelButtonCompilerExecute,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 693,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 693,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
                                .addGap(17, 17, 17)));
        rootPanelLayout.setVerticalGroup(
                rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(rootPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(buttonsFilePanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(panelButtonCompilerExecute,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(rootPanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(rootPanelLayout.createSequentialGroup()
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 203,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0,
                                                Short.MAX_VALUE))
                                .addContainerGap(8, Short.MAX_VALUE)));

        getContentPane().add(rootPanel);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {
        directorio.New();
        clearFields();
    }// GEN-LAST:event_btnNuevoActionPerformed

    private void btnAbrirActionPerformed(java.awt.event.ActionEvent evt) {
        if (directorio.Open()) {
            colorAnalysis();
            clearFields();

        }
    }

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {
        if (directorio.Save()) {
            clearFields();
        }
    }

    private void btnGuardarCActionPerformed(java.awt.event.ActionEvent evt) {
        if (directorio.SaveAs()) {
            clearFields();
        }
    }

    private void btnCompilarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Field field = directorio.getClass().getDeclaredField("file");
            field.setAccessible(true);
            File archivoAbierto = (File) field.get(directorio);

            if (archivoAbierto != null) {
                String nombreArchivo = archivoAbierto.getName();
                int index = nombreArchivo.lastIndexOf('.');
                if (index > 0) {
                    nombreArchivo = nombreArchivo.substring(0, index);
                }
                JOptionPane.showMessageDialog(null, "Lenguaje es: " + nombreArchivo, "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Ningún archivo abierto.", "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (getTitle().contains("*") || getTitle().equals(title)) {
            if (directorio.Save()) {
                compile();
            }
        } else {
            compile();
        }
    }

    private void btnEjecutarActionPerformed(java.awt.event.ActionEvent evt) {
        btnCompilar.doClick();
        if (codeHasBeenCompiled) {
            if (!errors.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "No se puede ejecutar el código ya que se encontró uno o más errores",
                        "Error en la compilación", JOptionPane.ERROR_MESSAGE);
            } else {
                CodeBlock codeBlock = Functions.splitCodeInCodeBlocks(tokens, "{", "}", ";");
                System.out.println(codeBlock);
                ArrayList<String> blocksOfCode = codeBlock.getBlocksOfCodeInOrderOfExec();
                System.out.println(blocksOfCode);

            }
        }
    }

    private void compile() {
        clearFields();
        lexicalAnalysis();
        fillTableTokens();
        syntacticAnalysis();
        semanticAnalysis();
        printConsole();
        codeHasBeenCompiled = true;
    }

    private void lexicalAnalysis() {
        // Extraer tokens

        Lexer lexer;
        try {
            File codigo = new File("code.encrypter");
            FileOutputStream output = new FileOutputStream(codigo);
            // bytes del texto
            byte[] bytesText = jtpCode.getText().getBytes();
            output.write(bytesText);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(new FileInputStream(codigo), "UTF-8"));
            lexer = new Lexer(entrada);
            while (true) {
                Token token = lexer.yylex();
                if (token == null) {
                    break;
                }
                tokens.add(token);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("El archivo no pudo ser encontrado... " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Error al escribir en el archivo... " + ex.getMessage());
        }
    }

    private void syntacticAnalysis() {
        Grammar gramatica = new Grammar(tokens, errors);
        /* Eliminar los errores */
        gramatica.delete(new String[] { "ERROR", "ERROR_1", "ERROR_2" });
        // gramatica.delete(new String[] { "ERROR" });

        /* Agrupacion de valores */
        gramatica.group("VALOR", "(NUMERO|COLOR|NOMBRE)", true);

        /* VAR_PEARLs */
        gramatica.group("VAR_PEARL", "TIPO_DATO IDENTIFICADOR_P OP_ASIG VALOR COMA", true, identProd);
        gramatica.group("VAR_PEARL", "TIPO_DATO OP_ASIG VALOR", true,
                2, "ERROR, falta Identificador de VAR_PEARL [#,%]");
        gramatica.finalLineColumn();
        gramatica.group("VAR_PEARL", "TIPO_DATO OP_ASIG IDENTIFICADOR", true,
                3, "ERROR, falta valor de VAR_PEARL [#,%]");

        /* VAR_RUBY */
        gramatica.group("VAR_RUBY", "IDENTIFICADOR OP_ASIG VALOR", true, identProd);
        gramatica.group("VAR_PEARL", "IDENTIFICADOR OP_ASIG VALOR LLAVE_A", true, identProd);
        gramatica.group("VAR_PEARL", "IDENTIFICADOR OP_ASIG VALOR PARENTESIS_A", true,
                22, "ERROR, falta Identificador de VAR_RUBY [#,%]");

        gramatica.finalLineColumn();

        /* ELiminacion de tipos de dato y operadores de asig */
        gramatica.delete("TIPO_DATO", 4, "ERROR,Tipo de dato no pertenece a declaracion [#,%]");
        gramatica.delete("OP_ASIG", 5, "ERROR,Tipo de asignacion no pertenece a declaracion [#,%]");

        /* Agrupar identificadores como un valor */
        gramatica.group("VALOR", "IDENTIFICADOR", true);
        gramatica.group("PARAMETROS", "VALOR (COMA VALOR)+");

        /* Agrupacion de funciones */
        // FUNCIONES DEFINIRLAS gramatica.group("FUNCION","(fun1|fun2)",true);
        // FUNCION QUE PERTENECE a ()
        gramatica.group("FUN_COMPLET", "FUNCION PARENTESIS_OPEN (VALOR|PARENTESIS)? PARENTESIS_CLOSE", true);
        // usuario no tiene ( que abre
        gramatica.group("FUN_COMPLET", "FUNCION PARENTESIS_OPEN (VALOR|PARENTESIS)? ", true, 6,
                "ERROR, falta Parentesis (");
        gramatica.group("FUN_COMPLET", "FUNCION (VALOR|PARENTESIS) PARENTESIS_CLOSE", true, 7,
                "ERROR, falta Parentesis )");
        gramatica.initialLineColumn();

        /* ELimacion de funciones que no sirven */
        gramatica.delete("FUNCION", 8, "ERROR,FUNCION declaracion incorrecta");

        /* EXPRESIONES LOGICAS */
        gramatica.loopForFunExecUntilChangeNotDetected(() -> {
            gramatica.group("EXP_LOG", "(FUN_COMPLET | EXP_LOG) (OP_LOGICO (FUN_COMPLET|EXP_LOG))+");
            gramatica.group("EXP_LOG", "PARENTESIS_OPEN (EXP_LOG | FUN_COMPLET) PARENTESIS_CLOSE)+");
        });
        /* ELIMINACION DE EXPRESIONEs */
        gramatica.delete("OP_LOGICO", 10, "ERROR, operador logico no pertenece a una expresion");

        /* Agrupacion expresiones logicas, valor y parametro */
        gramatica.group("VALOR", "EXP_LOG");
        gramatica.group("PARAMETROS", "VALOR (COMA VALOR)+");

        /* AGrupacion de estructuras de control */
        gramatica.group("EST_CONTROL", "(REPETIR  ESTRUCTURA_SI)");
        gramatica.group("EST_CONTROL_COMP", "EST_CONTROL_PARENTESIS_O PARENTESIS_C");
        gramatica.group("EST_CONTROL", "EST_CONTROL (VALOR | PARAMETROS)");
        gramatica.group("EST_CONTROL_COMP", "EST_CONTROL_PARENTESIS_O (VALOR | PARAMETROS) PARENTESIS_C");
        /** AGREAGAR LOGICA PARA PARENTESIS QUE ABREN Y CIERRA LINEA 389 */

        /* Eliminacion estr control */
        gramatica.delete("EST_CONTROL", 11, "ERROR estructura incompleta [#,%]");
        gramatica.delete(
                new String[] { "PARENTESIS_O", "PARENTESIS_C" }, 12,
                "EROR en la declaracion del parentesis Linea [#,%]");
        gramatica.finalLineColumn();

        /* Verificación de punto y coma al final de la sentencia */
        // Identificadores
        gramatica.group("VAR_PEARL_PC", "VAR_PEARL PUNTO_COMA", true);
        gramatica.group("VAR_PEARL_PC", "VAR_PEARL", true,
                18, " Error sintáctico {}: falta el punto y coma al final de la declaración de VAR_PEARL [#, %]");
        // Funciones
        gramatica.group("FUNCION_COMP_PC", "FUNCION_COMP PUNTO_COMA", true);
        gramatica.group("FUNCION_COMP_PC", "FUNCION_COMP", true,
                19, " Error sintáctico {}: falta el punto y coma al final de la declaración de función [#, %]");

        gramatica.initialLineColumn();

        /* Eliminación de punto y coma */
        gramatica.delete("PUNTO_COMA",
                20, " Error sintáctico {}: el punto y coma no está al final de una sentencia [#, %]");

        /* Agrupación de sentencias */
        gramatica.group("SENTENCIAS", "(VAR_PEARL_PC | FUNCION_COMP_PC)+");
        /* Estructuras de control completas */
        gramatica.loopForFunExecUntilChangeNotDetected(() -> {
            gramatica.group("EST_CONTROL_COMP_LASLC", "EST_CONTROL_COMP LLAVE_O (SENTENCIAS)? LLAVE_C", true);
            gramatica.group("SENTENCIAS", "(SENTENCIAS | EST_CONTROL_COMP_LASLC)+");
        });

        /* Estructuras de control incompletas */
        gramatica.loopForFunExecUntilChangeNotDetected(() -> {
            gramatica.initialLineColumn();

            gramatica.group("EST_CONTROL_COMP_LASLC", "EST_CONTROL_COMP (SENTENCIAS)? LLAVE_C", true,
                    21, " Error sintáctico {}: falta la llave que abre en la estructura de control [#, %]");

            gramatica.finalLineColumn();

            gramatica.group("EST_CONTROL_COMP_LASLC", "EST_CONTROL_COMP LLAVE_O SENTENCIAS",
                    22, " Error sintáctico {}: falta la llave que cierra en la estructura de control [#, %]");
            gramatica.group("SENTENCIAS", "(SENTENCIAS | EST_CONTROL_COMP_LASLC)+");
        });

        /* Eliminación de llaves */
        gramatica.delete(new String[] { "LLAVE_O", "LLAVE_C" },
                23, " Error sintáctico {}: la llave no está contenida en una agrupación [#, %]");

        /* Mostrar gramáticas */
        gramatica.show();

    }

    private void semanticAnalysis() {
        HashMap<String, String> identDataType = new HashMap<>();
        identDataType.put("Color", "COLOR");
        identDataType.put("numero", "NUMERO");

        for (Production id : identProd) {
            if (!identDataType.get(id.lexemeRank(0)).equals(id.lexicalCompRank(-1))) {
                errors.add(new ErrorLSSL(1, "ERROR semantico {}: valor incompatible con tipo de dato [#,%]", id, true));
            } else if (id.lexicalCompRank(-1).equals("COLOR") && !id.lexemeRank(-1).matches("#[0-9a-fA-F+]")) {
                errors.add(new ErrorLSSL(2, "ERROR semantico {}: color incompatible [#,%]", id, false));
            } else {
                identificadores.put(id.lexemeRank(1), id.lexemeRank(-1));
            }
            System.err.println(id.lexemeRank(0, -1));
            System.err.println(id.lexicalCompRank(0, -1));
            System.out.println("*");
        }
    }

    private void colorAnalysis() {
        /* Limpiar el arreglo de colores */
        textsColor.clear();
        /* Extraer rangos de colores */

        LexerColor lexerColor;
        try {
            File codigo = new File("color.encrypter");
            FileOutputStream output = new FileOutputStream(codigo);
            byte[] bytesText = jtpCode.getText().getBytes();
            output.write(bytesText);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(new FileInputStream(codigo), "UTF8"));
            lexerColor = new LexerColor(entrada);
            while (true) {
                TextColor textColor = lexerColor.yylex();
                if (textColor == null) {
                    break;
                }
                textsColor.add(textColor);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("El archivo no pudo ser encontrado... " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Error al escribir en el archivo... " + ex.getMessage());
        }
        Functions.colorTextPane(textsColor, jtpCode, new Color(40, 40, 40));
    }

    private void fillTableTokens() {// recorrer tokens pos 1 comp lexico,2 pos lexema. 3 linea y columna
        tokens.forEach(token -> {
            Object[] data = new Object[] { token.getLexicalComp(), token.getLexeme(),
                    "[" + token.getLine() + ", " + token.getColumn() + "]" };
            Functions.addRowDataInTable(tblTokens, data);
        });
    }

    private void printConsole() {// para mostrar errores
        int sizeErrors = errors.size();
        if (sizeErrors > 0) {
            Functions.sortErrorsByLineAndColumn(errors);// organiza errores por linea y colum
            String strErrors = "\n";
            for (ErrorLSSL error : errors) {
                String strError = String.valueOf(error); // obtiene cadena de error
                strErrors += strError + "\n";
            }
            jtaOutputConsole
                    .setText("Compilación terminada...\n" + strErrors + "\nLa compilación terminó con errores...");// minimo
                                                                                                                   // 1
                                                                                                                   // error
        } else {
            jtaOutputConsole.setText("Compilación terminada...");
        }
        jtaOutputConsole.setCaretPosition(0);
    }

    private void clearFields() {
        Functions.clearDataInTable(tblTokens);// limpiar datos tabla de tokens
        jtaOutputConsole.setText("");
        tokens.clear();
        errors.clear();
        identProd.clear();
        identificadores.clear();
        codeHasBeenCompiled = false;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Compilador.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Compilador.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Compilador.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Compilador.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        }
        // </editor-fold>
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatIntelliJLaf());
            } catch (UnsupportedLookAndFeelException ex) {
                System.out.println("LookAndFeel no soportado: " + ex);
            }
            new Compilador().setVisible(true);
        });
    }

    // VAR_PEARLs declaration - do not modify//GEN-BEGIN:VAR_PEARLs
    private javax.swing.JButton btnAbrir;
    private javax.swing.JButton btnCompilar;
    private javax.swing.JButton btnEjecutar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnGuardarC;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JPanel buttonsFilePanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jtaOutputConsole;
    private javax.swing.JTextPane jtpCode;
    private javax.swing.JPanel panelButtonCompilerExecute;
    private javax.swing.JPanel rootPanel;
    private javax.swing.JTable tblTokens;
    // End of VAR_PEARLs declaration//GEN-END:VAR_PEARLs
}
