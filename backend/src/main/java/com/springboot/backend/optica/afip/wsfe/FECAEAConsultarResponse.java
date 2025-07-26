
package com.springboot.backend.optica.afip.wsfe;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Clase Java para anonymous complex type.
 * 
 * &lt;p&gt;El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="FECAEAConsultarResult" type="{http://ar.gov.afip.dif.FEV1/}FECAEAGetResponse" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "fecaeaConsultarResult"
})
@XmlRootElement(name = "FECAEAConsultarResponse")
public class FECAEAConsultarResponse {

    @XmlElement(name = "FECAEAConsultarResult")
    protected FECAEAGetResponse fecaeaConsultarResult;

    /**
     * Obtiene el valor de la propiedad fecaeaConsultarResult.
     * 
     * @return
     *     possible object is
     *     {@link FECAEAGetResponse }
     *     
     */
    public FECAEAGetResponse getFECAEAConsultarResult() {
        return fecaeaConsultarResult;
    }

    /**
     * Define el valor de la propiedad fecaeaConsultarResult.
     * 
     * @param value
     *     allowed object is
     *     {@link FECAEAGetResponse }
     *     
     */
    public void setFECAEAConsultarResult(FECAEAGetResponse value) {
        this.fecaeaConsultarResult = value;
    }

}
