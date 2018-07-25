package Modelo;

import java.awt.HeadlessException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.jespxml.JespXML;
import org.jespxml.excepciones.AtributoNotFoundException;
import org.jespxml.excepciones.TagHijoNotFoundException;
import org.jespxml.modelo.Tag;
import org.xml.sax.SAXException;

/*
 * @author jorge
 */
public class EstructraXML {

    private String VERSION, FECHA, MONEDA, RFC_EMISOR, NOMBRE_EMISOR, RFC_RECEPTOR, NOMBRE_RECEPTOR;
    private double SUBTOTAL, IVA, TOTAL;

    public EstructraXML() {

        this.setVERSION("");
        this.setFECHA("");
        this.setMONEDA("");
        this.setRFC_EMISOR("");
        this.setRFC_RECEPTOR("");
        this.setNOMBRE_RECEPTOR("");
        this.setSUBTOTAL(0f);
        this.setIVA(0f);
        this.setTOTAL(0f);

    }

    public EstructraXML(String VERSION, String FECHA, String MONEDA, String RFC_EMISOR, String NOMBRE_EMISOR, String RFC_RECEPTOR, String NOMBRE_RECEPTOR, double SUBTOTAL, double IVA, double TOTAL) {
        this.VERSION = VERSION;
        this.FECHA = FECHA;
        this.MONEDA = MONEDA;
        this.RFC_EMISOR = RFC_EMISOR;
        this.NOMBRE_EMISOR = NOMBRE_EMISOR;
        this.RFC_RECEPTOR = RFC_RECEPTOR;
        this.NOMBRE_RECEPTOR = NOMBRE_RECEPTOR;
        this.SUBTOTAL = SUBTOTAL;
        this.IVA = IVA;
        this.TOTAL = TOTAL;
    }

    public void LeerXML(String Ruta) {
        try {
            //Cargo el archivo
            JespXML archivo = new JespXML(Ruta);

            //CFDI 3.3
            //Padre
            Tag CFDIComprobate = archivo.leerXML();
            DecimalFormat df = new DecimalFormat("#.##");

            this.setVERSION(CFDIComprobate.getValorDeAtributo("Version"));

            if (this.getVERSION().equals("3.3")) {

                this.setFECHA(CFDIComprobate.getValorDeAtributo("fecha"));
                this.setMONEDA(CFDIComprobate.getValorDeAtributo("Moneda"));
                this.setSUBTOTAL(Double.parseDouble(CFDIComprobate.getValorDeAtributo("subTotal")));
                this.setTOTAL(Double.parseDouble(CFDIComprobate.getValorDeAtributo("total")));
                this.setIVA(Double.parseDouble(df.format(this.getSUBTOTAL() * 0.16)));

                //Hijo
                Tag CFDIEmisor = CFDIComprobate.getTagHijoByName("cfdi:Emisor");
                this.setRFC_EMISOR(CFDIEmisor.getValorDeAtributo("rfc"));
                this.setNOMBRE_EMISOR(CFDIEmisor.getValorDeAtributo("nombre"));

                //Hijo
                Tag CFDIReceptor = CFDIComprobate.getTagHijoByName("cfdi:Receptor");
                this.setRFC_RECEPTOR(CFDIReceptor.getValorDeAtributo("rfc"));
                this.setNOMBRE_RECEPTOR(CFDIReceptor.getValorDeAtributo("nombre"));

                String Receptor[] = { RFC_RECEPTOR, NOMBRE_RECEPTOR};
                String Emisor[] = { RFC_EMISOR, NOMBRE_EMISOR};
                String Comprobante[] = { VERSION, MONEDA, String.valueOf(SUBTOTAL), String.valueOf(TOTAL)};

                System.out.println(Arrays.toString(Comprobante));
                System.out.println(Arrays.toString(Emisor));
                System.out.println(Arrays.toString(Receptor));

            } else {
                JOptionPane.showMessageDialog(null, " La Version de CFDI " + VERSION + ", No es compatible ");
            }
        } catch (HeadlessException | IOException | ParserConfigurationException | AtributoNotFoundException | TagHijoNotFoundException | SAXException ex) {
            //exception lanzada cuando no se encuentra el atributo
            System.out.println(ex.getMessage()); 
        }

    }

    public String getVERSION() {
        return VERSION;
    }

    public void setVERSION(String VERSION) {
        this.VERSION = VERSION;
    }

    public String getFECHA() {
        return FECHA;
    }

    public void setFECHA(String FECHA) {
        this.FECHA = FECHA;
    }

    public String getMONEDA() {
        return MONEDA;
    }

    public void setMONEDA(String MONEDA) {
        this.MONEDA = MONEDA;
    }

    public String getRFC_EMISOR() {
        return RFC_EMISOR;
    }

    public void setRFC_EMISOR(String RFC_EMISOR) {
        this.RFC_EMISOR = RFC_EMISOR;
    }

    public String getNOMBRE_EMISOR() {
        return NOMBRE_EMISOR;
    }

    public void setNOMBRE_EMISOR(String NOMBRE_EMISOR) {
        this.NOMBRE_EMISOR = NOMBRE_EMISOR;
    }

    public String getRFC_RECEPTOR() {
        return RFC_RECEPTOR;
    }

    public void setRFC_RECEPTOR(String RFC_RECEPTOR) {
        this.RFC_RECEPTOR = RFC_RECEPTOR;
    }

    public String getNOMBRE_RECEPTOR() {
        return NOMBRE_RECEPTOR;
    }

    public void setNOMBRE_RECEPTOR(String NOMBRE_RECEPTOR) {
        this.NOMBRE_RECEPTOR = NOMBRE_RECEPTOR;
    }

    public double getSUBTOTAL() {
        return SUBTOTAL;
    }

    public void setSUBTOTAL(double SUBTOTAL) {
        this.SUBTOTAL = SUBTOTAL;
    }

    public double getIVA() {
        return IVA;
    }

    public void setIVA(double IVA) {
        this.IVA = IVA;
    }

    public double getTOTAL() {
        return TOTAL;
    }

    public void setTOTAL(double TOTAL) {
        this.TOTAL = TOTAL;
    }

}
