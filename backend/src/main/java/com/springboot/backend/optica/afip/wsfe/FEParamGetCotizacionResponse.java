
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
 *         &amp;lt;element name="FEParamGetCotizacionResult" type="{http://ar.gov.afip.dif.FEV1/}FECotizacionResponse" minOccurs="0"/&amp;gt;
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
    "feParamGetCotizacionResult"
})
@XmlRootElement(name = "FEParamGetCotizacionResponse")
public class FEParamGetCotizacionResponse {

    @XmlElement(name = "FEParamGetCotizacionResult")
    protected FECotizacionResponse feParamGetCotizacionResult;

    /**
     * Obtiene el valor de la propiedad feParamGetCotizacionResult.
     * 
     * @return
     *     possible object is
     *     {@link FECotizacionResponse }
     *     
     */
    public FECotizacionResponse getFEParamGetCotizacionResult() {
        return feParamGetCotizacionResult;
    }

    /**
     * Define el valor de la propiedad feParamGetCotizacionResult.
     * 
     * @param value
     *     allowed object is
     *     {@link FECotizacionResponse }
     *     
     */
    public void setFEParamGetCotizacionResult(FECotizacionResponse value) {
        this.feParamGetCotizacionResult = value;
    }

}
