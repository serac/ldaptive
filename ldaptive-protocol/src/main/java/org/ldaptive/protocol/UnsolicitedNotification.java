/* See LICENSE for licensing and NOTICE for copyright. */
package org.ldaptive.protocol;

import org.ldaptive.LdapUtils;
import org.ldaptive.asn1.DERBuffer;

/**
 * LDAP unsolicited notification defined as:
 *
 * <pre>
   ExtendedResponse ::= [APPLICATION 24] SEQUENCE {
     COMPONENTS OF LDAPResult,
     responseName     [10] LDAPOID OPTIONAL,
     responseValue    [11] OCTET STRING OPTIONAL }
 * </pre>
 *
 * where the messageID is always zero.
 *
 * @author  Middleware Services
 */
public class UnsolicitedNotification extends ExtendedResponse
{

  /** OID of this response. */
  public static final String OID = "1.3.6.1.4.1.1466.20037";

  /** hash code seed. */
  private static final int HASH_CODE_SEED = 10321;


  /**
   * Default constructor.
   */
  protected UnsolicitedNotification()
  {
    setMessageID(0);
  }


  /**
   * Creates a new unsolicited notification.
   *
   * @param  buffer  to decode
   */
  public UnsolicitedNotification(final DERBuffer buffer)
  {
    super(buffer);
  }


  @Override
  public void setMessageID(final int id)
  {
    if (id != 0) {
      throw new IllegalArgumentException("Message ID must be zero");
    }
    super.setMessageID(id);
  }


  @Override
  public boolean equals(final Object o)
  {
    if (o == this) {
      return true;
    }
    return o instanceof UnsolicitedNotification && super.equals(o);
  }


  @Override
  public int hashCode()
  {
    return
      LdapUtils.computeHashCode(
        HASH_CODE_SEED,
        getMessageID(),
        getControls(),
        getResultCode(),
        getMatchedDN(),
        getDiagnosticMessage(),
        getReferralURLs(),
        getResponseName(),
        getResponseValue());
  }


  /**
   * Creates a builder for this class.
   *
   * @return  new builder
   */
  protected static Builder builder()
  {
    return new Builder();
  }


  // CheckStyle:OFF
  protected static class Builder extends ExtendedResponse.Builder
  {


    protected Builder()
    {
      super(new UnsolicitedNotification());
    }


    protected Builder(final UnsolicitedNotification n)
    {
      super(n);
    }


    @Override
    protected Builder self()
    {
      return this;
    }


    public Builder responseName(final String name)
    {
      object.setResponseName(name);
      return this;
    }


    public Builder responseValue(final byte[] value)
    {
      object.setResponseValue(value);
      return this;
    }
  }
  // CheckStyle:ON
}
