/* See LICENSE for licensing and NOTICE for copyright. */
package org.ldaptive.transport.netty;

import java.util.Map;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.ThreadPerTaskExecutor;

/**
 * Creates netty connections using an {@link NioEventLoopGroup}. The event loop group is shutdown when the connection is
 * closed.
 *
 * @author  Middleware Services
 */
public class NioConnectionTransport extends ConnectionTransport
{


  /**
   * Creates a new nio connection transport.
   */
  public NioConnectionTransport()
  {
    this(0);
  }


  /**
   * Creates a new nio connection transport.
   *
   * @param  ioThreads  number of threads used for I/O in the event loop group
   */
  public NioConnectionTransport(final int ioThreads)
  {
    this(ioThreads, null);
  }


  /**
   * Creates a new nio connection transport.
   *
   * @param  ioThreads  number of threads used for I/O in the event loop group
   * @param  options  netty channel options
   */
  public NioConnectionTransport(final int ioThreads, final Map<ChannelOption, Object> options)
  {
    super(ioThreads, options);
  }


  /**
   * Creates a new nio connection transport.
   *
   * @param  ioThreads  number of threads used for I/O in the event loop group
   * @param  messageThreads  number of threads for LDAP message handling in the event loop group
   */
  public NioConnectionTransport(final int ioThreads, final int messageThreads)
  {
    this(ioThreads, messageThreads, null);
  }


  /**
   * Creates a new nio connection transport.
   *
   * @param  ioThreads  number of threads used for I/O in the event loop group
   * @param  messageThreads  number of threads for LDAP message handling in the event loop group
   * @param  options  netty channel options
   */
  public NioConnectionTransport(
    final int ioThreads,
    final int messageThreads,
    final Map<ChannelOption, Object> options)
  {
    super(ioThreads, messageThreads, options);
  }


  @Override
  protected Class<? extends Channel> getSocketChannelType()
  {
    return NioSocketChannel.class;
  }


  @Override
  protected EventLoopGroup createEventLoopGroup(final String name, final int numThreads)
  {
    return new NioEventLoopGroup(
      numThreads,
      new ThreadPerTaskExecutor(new DefaultThreadFactory(name, true, Thread.NORM_PRIORITY)));
  }
}
