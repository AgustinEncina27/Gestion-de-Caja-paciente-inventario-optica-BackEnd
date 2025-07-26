
package com.springboot.backend.optica.afip.wsfe;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Clase Java para FECAEADetRequest complex type.
 * 
 * &lt;p&gt;El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="FECAEADetRequest"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;extension base="{http://ar.gov.afip.dif.FEV1/}FEDetRequest"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="CAEA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="CbteFchHsGen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/extension&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FECAEADetRequest", propOrder = {
    "caea",
    "cbteFchHsGen"
})
public class FECAEADetRequest
    extends FEDetRequest
{

    @XmlElement(name = "CAEA")
    protected String caea;
    @XmlElement(name = "CbteFchHsGen")
    protected String cbteFchHsGen;

    /**
     * Obtiene el valor de la propiedad caea.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCAEA() {
        return caea;
    }

    /**
     * Define el valor de la propiedad caea.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCAEA(String value) {
        this.caea = value;
    }

    /**
     * Obtiene el valor de la propiedad cbteFchHsGen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCbteFchHsGen() {
        return cbteFchHsGen;
    }

    /**
     * Define el valor de la propiedad cbteFchHsGen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCbteFchHsGen(String value) {
        this.cbteFchHsGen = value;
    }

}
