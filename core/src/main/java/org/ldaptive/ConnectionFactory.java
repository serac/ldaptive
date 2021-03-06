/* See LICENSE for licensing and NOTICE for copyright. */
package org.ldaptive;

/**
 * Factory for creating connections.
 *
 * @author  Middleware Services
 */
public interface ConnectionFactory
{


  /**
   * Creates a new connection.
   *
   * @return  connection
   *
   * @throws  LdapException  if a connection cannot be returned
   */
  Connection getConnection()
    throws LdapException;
}
