
package com.springboot.backend.optica.afip.wsfe;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Clase Java para FECAEARequest complex type.
 * 
 * &lt;p&gt;El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="FECAEARequest"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="FeCabReq" type="{http://ar.gov.afip.dif.FEV1/}FECAEACabRequest" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="FeDetReq" type="{http://ar.gov.afip.dif.FEV1/}ArrayOfFECAEADetRequest" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FECAEARequest", propOrder = {
    "feCabReq",
    "feDetReq"
})
public class FECAEARequest {

    @XmlElement(name = "FeCabReq")
    protected FECAEACabRequest feCabReq;
    @XmlElement(name = "FeDetReq")
    protected ArrayOfFECAEADetRequest feDetReq;

    /**
     * Obtiene el valor de la propiedad feCabReq.
     * 
     * @return
     *     possible object is
     *     {@link FECAEACabRequest }
     *     
     */
    public FECAEACabRequest getFeCabReq() {
        return feCabReq;
    }

    /**
     * Define el valor de la propiedad feCabReq.
     * 
     * @param value
     *     allowed object is
     *     {@link FECAEACabRequest }
     *     
     */
    public void setFeCabReq(FECAEACabRequest value) {
        this.feCabReq = value;
    }

    /**
     * Obtiene el valor de la propiedad feDetReq.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfFECAEADetRequest }
     *     
     */
    public ArrayOfFECAEADetRequest getFeDetReq() {
        return feDetReq;
    }

    /**
     * Define el valor de la propiedad feDetReq.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfFECAEADetRequest }
     *     
     */
    public void setFeDetReq(ArrayOfFECAEADetRequest value) {
        this.feDetReq = value;
    }

}
