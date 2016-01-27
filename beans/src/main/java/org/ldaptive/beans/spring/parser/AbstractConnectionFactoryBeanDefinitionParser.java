/* See LICENSE for licensing and NOTICE for copyright. */
package org.ldaptive.beans.spring.parser;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;

/**
 * Common implementation for all connection factories.
 *
 * @author Middleware Services
 */
public abstract class AbstractConnectionFactoryBeanDefinitionParser extends AbstractConnectionConfigBeanDefinitionParser
{


  /**
   * Creates a provider.
   *
   * @param  element  containing configuration
   *
   * @return  provider bean definition
   */
  protected BeanDefinitionBuilder parseProvider(final Element element)
  {
    final BeanDefinitionBuilder provider = BeanDefinitionBuilder.genericBeanDefinition(
      element.getAttribute("provider"));
    return provider;
  }
}
