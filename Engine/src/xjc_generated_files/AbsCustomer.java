
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
 *         &lt;element ref="{}abs-balance"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "absBalance"
})
@XmlRootElement(name = "abs-customer")
public class AbsCustomer {

    @XmlElement(name = "abs-balance")
    protected int absBalance;
    @XmlAttribute(name = "name", required = true)
    protected String name;

    /**
     * Gets the value of the absBalance property.
     *
     */
    public int getAbsBalance() {
        return absBalance;
    }

    /**
     * Sets the value of the absBalance property.
     *
     */
    public void setAbsBalance(int value) {
        this.absBalance = value;
    }

    /**
     * Gets the value of the name property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

}
