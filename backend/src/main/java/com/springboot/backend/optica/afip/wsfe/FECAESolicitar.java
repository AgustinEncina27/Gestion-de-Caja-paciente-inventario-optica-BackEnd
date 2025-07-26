
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
 *         &amp;lt;element name="Auth" type="{http://ar.gov.afip.dif.FEV1/}FEAuthRequest" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="FeCAEReq" type="{http://ar.gov.afip.dif.FEV1/}FECAERequest" minOccurs="0"/&amp;gt;
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
    "auth",
    "feCAEReq"
})
@XmlRootElement(name = "FECAESolicitar")
public class FECAESolicitar {

    @XmlElement(name = "Auth")
    protected FEAuthRequest auth;
    @XmlElement(name = "FeCAEReq")
    protected FECAERequest feCAEReq;

    /**
     * Obtiene el valor de la propiedad auth.
     * 
     * @return
     *     possible object is
     *     {@link FEAuthRequest }
     *     
     */
    public FEAuthRequest getAuth() {
        return auth;
    }

    /**
     * Define el valor de la propiedad auth.
     * 
     * @param value
     *     allowed object is
     *     {@link FEAuthRequest }
     *     
     */
    public void setAuth(FEAuthRequest value) {
        this.auth = value;
    }

    /**
     * Obtiene el valor de la propiedad feCAEReq.
     * 
     * @return
     *     possible object is
     *     {@link FECAERequest }
     *     
     */
    public FECAERequest getFeCAEReq() {
        return feCAEReq;
    }

    /**
     * Define el valor de la propiedad feCAEReq.
     * 
     * @param value
     *     allowed object is
     *     {@link FECAERequest }
     *     
     */
    public void setFeCAEReq(FECAERequest value) {
        this.feCAEReq = value;
    }

}
