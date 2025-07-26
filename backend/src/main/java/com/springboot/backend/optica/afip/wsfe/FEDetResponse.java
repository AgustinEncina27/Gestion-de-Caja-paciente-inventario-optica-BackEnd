
package com.springboot.backend.optica.afip.wsfe;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Clase Java para FEDetResponse complex type.
 * 
 * &lt;p&gt;El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="FEDetResponse"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="Concepto" type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt;
 *         &amp;lt;element name="DocTipo" type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt;
 *         &amp;lt;element name="DocNro" type="{http://www.w3.org/2001/XMLSchema}long"/&amp;gt;
 *         &amp;lt;element name="CbteDesde" type="{http://www.w3.org/2001/XMLSchema}long"/&amp;gt;
 *         &amp;lt;element name="CbteHasta" type="{http://www.w3.org/2001/XMLSchema}long"/&amp;gt;
 *         &amp;lt;element name="CbteFch" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="Resultado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="Observaciones" type="{http://ar.gov.afip.dif.FEV1/}ArrayOfObs" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FEDetResponse", propOrder = {
    "concepto",
    "docTipo",
    "docNro",
    "cbteDesde",
    "cbteHasta",
    "cbteFch",
    "resultado",
    "observaciones"
})
@XmlSeeAlso({
    FECAEDetResponse.class,
    FECAEADetResponse.class
})
public class FEDetResponse {

    @XmlElement(name = "Concepto")
    protected int concepto;
    @XmlElement(name = "DocTipo")
    protected int docTipo;
    @XmlElement(name = "DocNro")
    protected long docNro;
    @XmlElement(name = "CbteDesde")
    protected long cbteDesde;
    @XmlElement(name = "CbteHasta")
    protected long cbteHasta;
    @XmlElement(name = "CbteFch")
    protected String cbteFch;
    @XmlElement(name = "Resultado")
    protected String resultado;
    @XmlElement(name = "Observaciones")
    protected ArrayOfObs observaciones;

    /**
     * Obtiene el valor de la propiedad concepto.
     * 
     */
    public int getConcepto() {
        return concepto;
    }

    /**
     * Define el valor de la propiedad concepto.
     * 
     */
    public void setConcepto(int value) {
        this.concepto = value;
    }

    /**
     * Obtiene el valor de la propiedad docTipo.
     * 
     */
    public int getDocTipo() {
        return docTipo;
    }

    /**
     * Define el valor de la propiedad docTipo.
     * 
     */
    public void setDocTipo(int value) {
        this.docTipo = value;
    }

    /**
     * Obtiene el valor de la propiedad docNro.
     * 
     */
    public long getDocNro() {
        return docNro;
    }

    /**
     * Define el valor de la propiedad docNro.
     * 
     */
    public void setDocNro(long value) {
        this.docNro = value;
    }

    /**
     * Obtiene el valor de la propiedad cbteDesde.
     * 
     */
    public long getCbteDesde() {
        return cbteDesde;
    }

    /**
     * Define el valor de la propiedad cbteDesde.
     * 
     */
    public void setCbteDesde(long value) {
        this.cbteDesde = value;
    }

    /**
     * Obtiene el valor de la propiedad cbteHasta.
     * 
     */
    public long getCbteHasta() {
        return cbteHasta;
    }

    /**
     * Define el valor de la propiedad cbteHasta.
     * 
     */
    public void setCbteHasta(long value) {
        this.cbteHasta = value;
    }

    /**
     * Obtiene el valor de la propiedad cbteFch.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCbteFch() {
        return cbteFch;
    }

    /**
     * Define el valor de la propiedad cbteFch.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCbteFch(String value) {
        this.cbteFch = value;
    }

    /**
     * Obtiene el valor de la propiedad resultado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultado() {
        return resultado;
    }

    /**
     * Define el valor de la propiedad resultado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultado(String value) {
        this.resultado = value;
    }

    /**
     * Obtiene el valor de la propiedad observaciones.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfObs }
     *     
     */
    public ArrayOfObs getObservaciones() {
        return observaciones;
    }

    /**
     * Define el valor de la propiedad observaciones.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfObs }
     *     
     */
    public void setObservaciones(ArrayOfObs value) {
        this.observaciones = value;
    }

}
