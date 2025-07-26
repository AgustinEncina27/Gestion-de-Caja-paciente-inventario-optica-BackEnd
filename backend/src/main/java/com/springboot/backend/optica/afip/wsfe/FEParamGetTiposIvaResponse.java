
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
 *         &amp;lt;element name="FEParamGetTiposIvaResult" type="{http://ar.gov.afip.dif.FEV1/}IvaTipoResponse" minOccurs="0"/&amp;gt;
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
    "feParamGetTiposIvaResult"
})
@XmlRootElement(name = "FEParamGetTiposIvaResponse")
public class FEParamGetTiposIvaResponse {

    @XmlElement(name = "FEParamGetTiposIvaResult")
    protected IvaTipoResponse feParamGetTiposIvaResult;

    /**
     * Obtiene el valor de la propiedad feParamGetTiposIvaResult.
     * 
     * @return
     *     possible object is
     *     {@link IvaTipoResponse }
     *     
     */
    public IvaTipoResponse getFEParamGetTiposIvaResult() {
        return feParamGetTiposIvaResult;
    }

    /**
     * Define el valor de la propiedad feParamGetTiposIvaResult.
     * 
     * @param value
     *     allowed object is
     *     {@link IvaTipoResponse }
     *     
     */
    public void setFEParamGetTiposIvaResult(IvaTipoResponse value) {
        this.feParamGetTiposIvaResult = value;
    }

}
