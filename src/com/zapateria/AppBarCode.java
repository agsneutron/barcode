package com.zapateria;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;

import com.modelo.Conexion;
import net.sourceforge.jbarcodebean.BarcodeException;
import net.sourceforge.jbarcodebean.JBarcodeBean;
import net.sourceforge.jbarcodebean.model.Interleaved25;

public class AppBarCode {
    private JTextPane textPane1;
    private JButton creaCodigoDeBarrasButton;
    private JButton button2;
    private JButton button3;
    private JTextField txtCodigo;
    private JPanel panelMain;

    public AppBarCode() {
        creaCodigoDeBarrasButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                createBarCode(txtCodigo.getText());
                guardaCódigoBarras(txtCodigo.getText());
                JOptionPane.showMessageDialog(null,txtCodigo.getText());
            }
        });
    }


    public static void createBarCode(String code){

        JBarcodeBean barcode = new JBarcodeBean();

        // nuestro tipo de codigo de barra
        barcode.setCodeType(new Interleaved25());
        //barcode.setCodeType(new Code39());

        // nuestro valor a codificar y algunas configuraciones mas
        barcode.setCode(code);
        barcode.setCheckDigit(true);

        BufferedImage bufferedImage = barcode.draw(new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB));

        // guardar en disco como png
        File file = new File("codebar_" + code + ".png");
        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void guardaCódigoBarras(String codigo){
        PreparedStatement pS;
        ResultSet rS;
        Connection con;
        Conexion conn = new Conexion();
        con = conn.getConexion();

        try {
            pS = con.prepareStatement("Select * from almacen_articulo");
            rS = pS.executeQuery();
            while (rS.next()){
                createBarCode(rS.getString("clave"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args){
        JFrame frame = new JFrame("AppBarCode");
        frame.setContentPane(new AppBarCode().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
