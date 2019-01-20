/* See LICENSE for licensing and NOTICE for copyright. */
package org.ldaptive.io;

import java.nio.charset.StandardCharsets;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;
import org.ldaptive.ResultCode;
import org.ldaptive.protocol.BindResponse;
import org.ldaptive.protocol.SASLClientRequest;
import org.ldaptive.sasl.Mechanism;
import org.ldaptive.sasl.QualityOfProtection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SASL client that negotiates the details of the bind operation.
 *
 * @author  Middleware Services
 */
public class SASLClient
{

  /** Logger for this class. */
  private static final Logger LOGGER = LoggerFactory.getLogger(SASLClient.class);

  /** SASL server hostname. */
  private final String serverName;

  /** Underlying SASL client. */
  private SaslClient client;


  /**
   * Creates a new SASL client.
   *
   * @param  host  SASL server hostname
   */
  public SASLClient(final String host)
  {
    serverName = host;
  }


  /**
   * Returns the underlying SASL client.
   *
   * @return  SASL client
   */
  public SaslClient getClient()
  {
    return client;
  }


  /**
   * Performs a SASL bind.
   *
   * @param  conn  to perform the bind on
   * @param  request  SASL request to perform
   *
   * @return  final result of the bind process
   *
   * @throws  SaslException  if an error occurs
   */
  public BindResponse bind(final Connection conn, final SASLClientRequest request)
    throws SaslException
  {
    BindResponse response;
    try {
      client = Sasl.createSaslClient(
        new String[]{request.getMechanism()},
        request.getAuthorizationID(),
        "ldap",
        serverName,
        request.getSaslProperties(),
        request);

      byte[] bytes = client.hasInitialResponse() ? client.evaluateChallenge(new byte[0]) : null;
      OperationHandle handle = conn.operation(request.createBindRequest(bytes)).execute();
      response = (BindResponse) handle.await().get();
      while (!client.isComplete() &&
        (ResultCode.SASL_BIND_IN_PROGRESS == response.getResultCode() ||
          ResultCode.SUCCESS == response.getResultCode())) {
        bytes = client.evaluateChallenge(response.getServerSaslCreds().getBytes(StandardCharsets.UTF_8));
        if (ResultCode.SUCCESS == response.getResultCode()) {
          if (bytes != null) {
            throw new SaslException("SASL client error: received response after completion");
          }
          break;
        }
        handle = conn.operation(request.createBindRequest(bytes)).execute();
        response = (BindResponse) handle.await().get();
      }
      return response;
    } catch (Throwable e) {
      dispose();
      if (e instanceof SaslException) {
        throw (SaslException) e;
      }
      throw new SaslException("SASL bind failed", e);
    }
  }


  /**
   * Returns the SASL mechanism for this client. See {@link SaslClient#getMechanismName()}.
   *
   * @return  SASL mechanism
   */
  public Mechanism getMechanism()
  {
    return Mechanism.valueOf(client.getMechanismName());
  }


  /**
   * Returns the QOP for this client. See {@link SaslClient#getNegotiatedProperty(String)}.
   *
   * @return  QOP
   */
  public QualityOfProtection getQualityOfProtection()
  {
    return QualityOfProtection.fromString((String) client.getNegotiatedProperty(Sasl.QOP));
  }


  /**
   * Disposes the underly SASL client. See {@link SaslClient#dispose()}.
   */
  public void dispose()
  {
    if (client != null) {
      try {
        client.dispose();
      } catch (SaslException se) {
        LOGGER.warn("Error disposing of SASL client", se);
      } finally {
        client = null;
      }
    }
  }
}