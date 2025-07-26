
package com.springboot.backend.optica.afip.wsfe;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Clase Java para ArrayOfCondicionIvaReceptor complex type.
 * 
 * &lt;p&gt;El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="ArrayOfCondicionIvaReceptor"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="CondicionIvaReceptor" type="{http://ar.gov.afip.dif.FEV1/}CondicionIvaReceptor" maxOccurs="unbounded" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfCondicionIvaReceptor", propOrder = {
    "condicionIvaReceptor"
})
public class ArrayOfCondicionIvaReceptor {

    @XmlElement(name = "CondicionIvaReceptor", nillable = true)
    protected List<CondicionIvaReceptor> condicionIvaReceptor;

    /**
     * Gets the value of the condicionIvaReceptor property.
     * 
     * &lt;p&gt;
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the condicionIvaReceptor property.
     * 
     * &lt;p&gt;
     * For example, to add a new item, do as follows:
     * &lt;pre&gt;
     *    getCondicionIvaReceptor().add(newItem);
     * &lt;/pre&gt;
     * 
     * 
     * &lt;p&gt;
     * Objects of the following type(s) are allowed in the list
     * {@link CondicionIvaReceptor }
     * 
     * 
     */
    public List<CondicionIvaReceptor> getCondicionIvaReceptor() {
        if (condicionIvaReceptor == null) {
            condicionIvaReceptor = new ArrayList<CondicionIvaReceptor>();
        }
        return this.condicionIvaReceptor;
    }

}
