//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB)
// Reference Implementation, v2.2.5-2
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source
// schema.
// Generated on: 2012.11.02 at 06:39:56 PM EET
//

package com.sirma.itt.seip.definition.jaxb;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for regionsDefinition complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="regionsDefinition"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="region" type="{}regionDefinition" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "regionsDefinition", propOrder = { "region" })
public class RegionsDefinition {

	/**
	 * The region.
	 */
	protected List<RegionDefinition> region;

	/**
	 * Gets the value of the region property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the region property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <p>
	 * <pre>
	 * getRegion().add(newItem);
	 * </pre>
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 *
	 * @return the region {@link RegionDefinition }
	 */
	public List<RegionDefinition> getRegion() {
		if (region == null) {
			region = new ArrayList<>();
		}
		return region;
	}

	public void setRegion(List<RegionDefinition> region) {
		this.region = region;
	}

}