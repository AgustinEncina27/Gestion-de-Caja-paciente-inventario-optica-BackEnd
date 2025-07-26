
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
 *         &amp;lt;element name="FECAESolicitarResult" type="{http://ar.gov.afip.dif.FEV1/}FECAEResponse" minOccurs="0"/&amp;gt;
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
    "fecaeSolicitarResult"
})
@XmlRootElement(name = "FECAESolicitarResponse")
public class FECAESolicitarResponse {

    @XmlElement(name = "FECAESolicitarResult")
    protected FECAEResponse fecaeSolicitarResult;

    /**
     * Obtiene el valor de la propiedad fecaeSolicitarResult.
     * 
     * @return
     *     possible object is
     *     {@link FECAEResponse }
     *     
     */
    public FECAEResponse getFECAESolicitarResult() {
        return fecaeSolicitarResult;
    }

    /**
     * Define el valor de la propiedad fecaeSolicitarResult.
     * 
     * @param value
     *     allowed object is
     *     {@link FECAEResponse }
     *     
     */
    public void setFECAESolicitarResult(FECAEResponse value) {
        this.fecaeSolicitarResult = value;
    }

}
