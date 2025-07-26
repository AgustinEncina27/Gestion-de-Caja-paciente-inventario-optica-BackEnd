
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
 *         &amp;lt;element name="FECAEASolicitarResult" type="{http://ar.gov.afip.dif.FEV1/}FECAEAGetResponse" minOccurs="0"/&amp;gt;
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
    "fecaeaSolicitarResult"
})
@XmlRootElement(name = "FECAEASolicitarResponse")
public class FECAEASolicitarResponse {

    @XmlElement(name = "FECAEASolicitarResult")
    protected FECAEAGetResponse fecaeaSolicitarResult;

    /**
     * Obtiene el valor de la propiedad fecaeaSolicitarResult.
     * 
     * @return
     *     possible object is
     *     {@link FECAEAGetResponse }
     *     
     */
    public FECAEAGetResponse getFECAEASolicitarResult() {
        return fecaeaSolicitarResult;
    }

    /**
     * Define el valor de la propiedad fecaeaSolicitarResult.
     * 
     * @param value
     *     allowed object is
     *     {@link FECAEAGetResponse }
     *     
     */
    public void setFECAEASolicitarResult(FECAEAGetResponse value) {
        this.fecaeaSolicitarResult = value;
    }

}
