
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
 *         &amp;lt;element name="FEParamGetTiposConceptoResult" type="{http://ar.gov.afip.dif.FEV1/}ConceptoTipoResponse" minOccurs="0"/&amp;gt;
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
    "feParamGetTiposConceptoResult"
})
@XmlRootElement(name = "FEParamGetTiposConceptoResponse")
public class FEParamGetTiposConceptoResponse {

    @XmlElement(name = "FEParamGetTiposConceptoResult")
    protected ConceptoTipoResponse feParamGetTiposConceptoResult;

    /**
     * Obtiene el valor de la propiedad feParamGetTiposConceptoResult.
     * 
     * @return
     *     possible object is
     *     {@link ConceptoTipoResponse }
     *     
     */
    public ConceptoTipoResponse getFEParamGetTiposConceptoResult() {
        return feParamGetTiposConceptoResult;
    }

    /**
     * Define el valor de la propiedad feParamGetTiposConceptoResult.
     * 
     * @param value
     *     allowed object is
     *     {@link ConceptoTipoResponse }
     *     
     */
    public void setFEParamGetTiposConceptoResult(ConceptoTipoResponse value) {
        this.feParamGetTiposConceptoResult = value;
    }

}
