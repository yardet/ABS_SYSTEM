
//package Engine.src.xjc_generated_files;
package xjc_generated_files;
import javax.xml.bind.annotation.*;


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
 *         &lt;element ref="{}abs-categories"/>
 *         &lt;element ref="{}abs-loans"/>
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
        "absCategories",
        "absLoans",
        //"absCustomers"//not relenat to part3
})
@XmlRootElement(name = "abs-descriptor")
public class AbsDescriptor {

    @XmlElement(name = "abs-categories", required = true)
    protected xjc_generated_files.AbsCategories absCategories;
    @XmlElement(name = "abs-loans", required = true)
    protected xjc_generated_files.AbsLoans absLoans;
    /*@XmlElement(name = "abs-customers", required = true)
    protected AbsCustomers absCustomers;//not relenat to part3 */

    /**
     * Gets the value of the absCategories property.
     *
     * @return
     *     possible object is
     *     {@link xjc_generated_files.AbsCategories }
     *
     */
    public xjc_generated_files.AbsCategories getAbsCategories() {
        return absCategories;
    }

    /**
     * Sets the value of the absCategories property.
     *
     * @param value
     *     allowed object is
     *     {@link xjc_generated_files.AbsCategories }
     *
     */
    public void setAbsCategories(xjc_generated_files.AbsCategories value) {
        this.absCategories = value;
    }

    /**
     * Gets the value of the absLoans property.
     *
     * @return
     *     possible object is
     *     {@link AbsLoans }
     *
     */
    public AbsLoans getAbsLoans() {
        return absLoans;
    }

    /**
     * Sets the value of the absLoans property.
     *
     * @param value
     *     allowed object is
     *     {@link AbsLoans }
     *
     */
    public void setAbsLoans(AbsLoans value) {
        this.absLoans = value;
    }

    /**
     * Gets the value of the absCustomers property.
     *
     * @return
     *     possible object is
     *     {@link Engine.src.xjc_generated_files.AbsCustomers }
     *
     */
    /*public AbsCustomers getAbsCustomers() {
        return absCustomers;
    }//not relevant to part3*/

    /**
     * Sets the value of the absCustomers property.
     *
     * @param value
     *     allowed object is
     *     {@link Engine.src.xjc_generated_files.AbsCustomers }
     *
     */
    /*public void setAbsCustomers(AbsCustomers value) {
        this.absCustomers = value;
    }// not relevnat to part3*/

}
