
package com.springboot.backend.optica.afip.wsfe;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Clase Java para FECAERequest complex type.
 * 
 * &lt;p&gt;El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="FECAERequest"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="FeCabReq" type="{http://ar.gov.afip.dif.FEV1/}FECAECabRequest" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="FeDetReq" type="{http://ar.gov.afip.dif.FEV1/}ArrayOfFECAEDetRequest" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FECAERequest", propOrder = {
    "feCabReq",
    "feDetReq"
})
public class FECAERequest {

    @XmlElement(name = "FeCabReq")
    protected FECAECabRequest feCabReq;
    @XmlElement(name = "FeDetReq")
    protected ArrayOfFECAEDetRequest feDetReq;

    /**
     * Obtiene el valor de la propiedad feCabReq.
     * 
     * @return
     *     possible object is
     *     {@link FECAECabRequest }
     *     
     */
    public FECAECabRequest getFeCabReq() {
        return feCabReq;
    }

    /**
     * Define el valor de la propiedad feCabReq.
     * 
     * @param value
     *     allowed object is
     *     {@link FECAECabRequest }
     *     
     */
    public void setFeCabReq(FECAECabRequest value) {
        this.feCabReq = value;
    }

    /**
     * Obtiene el valor de la propiedad feDetReq.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfFECAEDetRequest }
     *     
     */
    public ArrayOfFECAEDetRequest getFeDetReq() {
        return feDetReq;
    }

    /**
     * Define el valor de la propiedad feDetReq.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfFECAEDetRequest }
     *     
     */
    public void setFeDetReq(ArrayOfFECAEDetRequest value) {
        this.feDetReq = value;
    }

}
