
package com.springboot.backend.optica.afip.wsfe;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Clase Java para Cotizacion complex type.
 * 
 * &lt;p&gt;El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="Cotizacion"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="MonId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="MonCotiz" type="{http://www.w3.org/2001/XMLSchema}double"/&amp;gt;
 *         &amp;lt;element name="FchCotiz" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Cotizacion", propOrder = {
    "monId",
    "monCotiz",
    "fchCotiz"
})
public class Cotizacion {

    @XmlElement(name = "MonId")
    protected String monId;
    @XmlElement(name = "MonCotiz")
    protected double monCotiz;
    @XmlElement(name = "FchCotiz")
    protected String fchCotiz;

    /**
     * Obtiene el valor de la propiedad monId.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMonId() {
        return monId;
    }

    /**
     * Define el valor de la propiedad monId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMonId(String value) {
        this.monId = value;
    }

    /**
     * Obtiene el valor de la propiedad monCotiz.
     * 
     */
    public double getMonCotiz() {
        return monCotiz;
    }

    /**
     * Define el valor de la propiedad monCotiz.
     * 
     */
    public void setMonCotiz(double value) {
        this.monCotiz = value;
    }

    /**
     * Obtiene el valor de la propiedad fchCotiz.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFchCotiz() {
        return fchCotiz;
    }

    /**
     * Define el valor de la propiedad fchCotiz.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFchCotiz(String value) {
        this.fchCotiz = value;
    }

}
