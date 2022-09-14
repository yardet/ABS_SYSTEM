
//package Engine.src.xjc_generated_files;
package xjc_generated_files;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}abs-customer" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "absCustomer"
})
@XmlRootElement(name = "abs-customers")
public class AbsCustomers {

    @XmlElement(name = "abs-customer", required = true)
    protected List<xjc_generated_files.AbsCustomer> absCustomer;

    /**
     * Gets the value of the absCustomer property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the absCustomer property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbsCustomer().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link xjc_generated_files.AbsCustomer }
     *
     *
     */
    public List<xjc_generated_files.AbsCustomer> getAbsCustomer() {
        if (absCustomer == null) {
            absCustomer = new ArrayList<xjc_generated_files.AbsCustomer>();
        }
        return this.absCustomer;
    }

}
