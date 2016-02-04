/* See LICENSE for licensing and NOTICE for copyright. */
package org.ldaptive.beans.spring.parser;

import java.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Common implementation for all bean definition parsers
 *
 * @author Middleware Services
 */
public abstract class AbstractBeanDefinitionParser extends AbstractSingleBeanDefinitionParser
{

  /** Logger for this class. */
  protected final Logger logger = LoggerFactory.getLogger(getClass());


  /**
   * Returns a {@link Period} for the supplied value.
   *
   * @param  value  to parse
   *
   * @return  period
   */
  protected static Period parsePeriod(final String value)
  {
    return Period.parse(value);
  }


  /**
   * Returns the first direct child element of the parent element with a name that matches any of the supplied names.
   *
   * @param  parent  element to inspect
   * @param  names  local names of the element to return
   *
   * @return  child element or null
   */
  protected static Element getDirectChild(final Element parent, final String... names)
  {
    if (parent != null) {
      for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
        for (String name : names) {
          if (child instanceof Element && name.equals(child.getLocalName())) {
            return (Element) child;
          }
        }
      }
    }
    return null;
  }


  /**
   * Sets a property if the given attribute exists on the element. The property name used is the same as the attribute
   * name.
   *
   * @param  element  from which to obtain property
   * @param  attribute  value for obtaining property
   * @param  builder  to receive property
   */
  protected void setIfPresent(
    final Element element,
    final String attribute,
    final BeanDefinitionBuilder builder)
  {
    setIfPresent(element, attribute, attribute, builder);
  }


  /**
   * Sets a property if the given attribute exists on the element.
   *
   * @param  element  from which to obtain property
   * @param  property  to set
   * @param  attribute  value for obtaining property
   * @param  builder  to receive property
   */
  protected void setIfPresent(
    final Element element,
    final String attribute,
    final String property,
    final BeanDefinitionBuilder builder)
  {
    if (element.hasAttribute(attribute)) {
      builder.addPropertyValue(property, element.getAttribute(attribute));
    }
  }
}
