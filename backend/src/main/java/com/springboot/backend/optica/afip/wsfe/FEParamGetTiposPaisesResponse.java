
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
 *         &amp;lt;element name="FEParamGetTiposPaisesResult" type="{http://ar.gov.afip.dif.FEV1/}FEPaisResponse" minOccurs="0"/&amp;gt;
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
    "feParamGetTiposPaisesResult"
})
@XmlRootElement(name = "FEParamGetTiposPaisesResponse")
public class FEParamGetTiposPaisesResponse {

    @XmlElement(name = "FEParamGetTiposPaisesResult")
    protected FEPaisResponse feParamGetTiposPaisesResult;

    /**
     * Obtiene el valor de la propiedad feParamGetTiposPaisesResult.
     * 
     * @return
     *     possible object is
     *     {@link FEPaisResponse }
     *     
     */
    public FEPaisResponse getFEParamGetTiposPaisesResult() {
        return feParamGetTiposPaisesResult;
    }

    /**
     * Define el valor de la propiedad feParamGetTiposPaisesResult.
     * 
     * @param value
     *     allowed object is
     *     {@link FEPaisResponse }
     *     
     */
    public void setFEParamGetTiposPaisesResult(FEPaisResponse value) {
        this.feParamGetTiposPaisesResult = value;
    }

}
