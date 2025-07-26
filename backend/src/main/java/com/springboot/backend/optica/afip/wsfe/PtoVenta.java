
package com.springboot.backend.optica.afip.wsfe;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Clase Java para PtoVenta complex type.
 * 
 * &lt;p&gt;El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="PtoVenta"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="Nro" type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt;
 *         &amp;lt;element name="EmisionTipo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="Bloqueado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="FchBaja" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PtoVenta", propOrder = {
    "nro",
    "emisionTipo",
    "bloqueado",
    "fchBaja"
})
public class PtoVenta {

    @XmlElement(name = "Nro")
    protected int nro;
    @XmlElement(name = "EmisionTipo")
    protected String emisionTipo;
    @XmlElement(name = "Bloqueado")
    protected String bloqueado;
    @XmlElement(name = "FchBaja")
    protected String fchBaja;

    /**
     * Obtiene el valor de la propiedad nro.
     * 
     */
    public int getNro() {
        return nro;
    }

    /**
     * Define el valor de la propiedad nro.
     * 
     */
    public void setNro(int value) {
        this.nro = value;
    }

    /**
     * Obtiene el valor de la propiedad emisionTipo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmisionTipo() {
        return emisionTipo;
    }

    /**
     * Define el valor de la propiedad emisionTipo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmisionTipo(String value) {
        this.emisionTipo = value;
    }

    /**
     * Obtiene el valor de la propiedad bloqueado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBloqueado() {
        return bloqueado;
    }

    /**
     * Define el valor de la propiedad bloqueado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBloqueado(String value) {
        this.bloqueado = value;
    }

    /**
     * Obtiene el valor de la propiedad fchBaja.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFchBaja() {
        return fchBaja;
    }

    /**
     * Define el valor de la propiedad fchBaja.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFchBaja(String value) {
        this.fchBaja = value;
    }

}
