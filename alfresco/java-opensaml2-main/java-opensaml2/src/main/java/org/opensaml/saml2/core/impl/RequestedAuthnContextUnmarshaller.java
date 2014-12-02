/*
 * Copyright [2005] [University Corporation for Advanced Internet Development, Inc.]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 
 */

package org.opensaml.saml2.core.impl;

import org.opensaml.common.impl.AbstractSAMLObjectUnmarshaller;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AuthnContextComparisonTypeEnumeration;
import org.opensaml.saml2.core.AuthnContextDeclRef;
import org.opensaml.saml2.core.RequestedAuthnContext;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.w3c.dom.Attr;

/**
 * A thread-safe Unmarshaller for {@link org.opensaml.saml2.core.RequestedAuthnContext} objects.
 */
public class RequestedAuthnContextUnmarshaller extends AbstractSAMLObjectUnmarshaller {

    /**
     * Constructor.
     * 
     */
    public RequestedAuthnContextUnmarshaller() {
        super(SAMLConstants.SAML20P_NS, RequestedAuthnContext.DEFAULT_ELEMENT_LOCAL_NAME);
    }

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace URI of either the schema type QName or element QName of the elements this
     *            unmarshaller operates on
     * @param elementLocalName the local name of either the schema type QName or element QName of the elements this
     *            unmarshaller operates on
     */
    protected RequestedAuthnContextUnmarshaller(String namespaceURI, String elementLocalName) {
        super(namespaceURI, elementLocalName);
    }

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject samlObject, Attr attribute) throws UnmarshallingException {
        RequestedAuthnContext rac = (RequestedAuthnContext) samlObject;

        if (attribute.getLocalName().equals(RequestedAuthnContext.COMPARISON_ATTRIB_NAME)) {
            if ("exact".equals(attribute.getValue())) {
                rac.setComparison(AuthnContextComparisonTypeEnumeration.EXACT);
            } else if ("minimum".equals(attribute.getValue())) {
                rac.setComparison(AuthnContextComparisonTypeEnumeration.MINIMUM);
            } else if ("maximum".equals(attribute.getValue())) {
                rac.setComparison(AuthnContextComparisonTypeEnumeration.MAXIMUM);
            } else if ("better".equals(attribute.getValue())) {
                rac.setComparison(AuthnContextComparisonTypeEnumeration.BETTER);
            } else {
                throw new UnmarshallingException("Saw an invalid value for Comparison attribute: "
                        + attribute.getValue());
            }
        } else {
            super.processAttribute(samlObject, attribute);
        }
    }

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentSAMLObject, XMLObject childSAMLObject)
            throws UnmarshallingException {
        RequestedAuthnContext rac = (RequestedAuthnContext) parentSAMLObject;
        if (childSAMLObject instanceof AuthnContextClassRef) {
            rac.getAuthnContextClassRefs().add((AuthnContextClassRef) childSAMLObject);
        } else if (childSAMLObject instanceof AuthnContextDeclRef) {
            rac.getAuthnContextDeclRefs().add((AuthnContextDeclRef) childSAMLObject);
        } else {
            super.processChildElement(parentSAMLObject, childSAMLObject);
        }
    }
}