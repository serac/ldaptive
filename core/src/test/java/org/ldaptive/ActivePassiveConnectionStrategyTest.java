/* See LICENSE for licensing and NOTICE for copyright. */
package org.ldaptive;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for {@link ActivePassiveConnectionStrategy}.
 *
 * @author  Middleware Services
 */
public class ActivePassiveConnectionStrategyTest
{


  /**
   * URL test data.
   *
   * @return  test data
   */
  @DataProvider(name = "urls")
  public Object[][] createURLs()
  {
    return
      new Object[][] {
        new Object[] {
          "ldap://directory.ldaptive.org",
          new LdapURL[] {new LdapURL("ldap://directory.ldaptive.org")},
        },
        new Object[] {
          "ldap://directory-1.ldaptive.org ldap://directory-2.ldaptive.org",
          new LdapURL[] {
            new LdapURL("ldap://directory-1.ldaptive.org"),
            new LdapURL("ldap://directory-2.ldaptive.org"),
          },
        },
        new Object[] {
          "ldap://directory-1.ldaptive.org ldap://directory-2.ldaptive.org ldap://directory-3.ldaptive.org",
          new LdapURL[] {
            new LdapURL("ldap://directory-1.ldaptive.org"),
            new LdapURL("ldap://directory-2.ldaptive.org"),
            new LdapURL("ldap://directory-3.ldaptive.org"),
          },
        },
      };
  }


  /**
   * Unit test for {@link ActivePassiveConnectionStrategy#next(LdapURLSet)}.
   *
   * @param  actual  to initialize strategy with
   * @param  expected  to compare
   *
   * @throws  LdapException  On LDAP errors.
   */
  @Test(groups = "conn", dataProvider = "urls")
  public void next(final String actual, final LdapURL[] expected) throws LdapException
  {
    final LdapURLSet urlSet = new LdapURLSet(new ActivePassiveConnectionStrategy(), actual);
    Assert.assertEquals(urlSet.getActiveUrls().size(), expected.length);
    for (int i = 0; i < expected.length; i++) {
      Assert.assertEquals(urlSet.getActiveUrls().get(i), expected[i]);
    }
    final LdapURL first = urlSet.getActiveUrls().get(0);
    for (int i = 0; i < expected.length * 2; i++) {
      urlSet.doWithNextActiveUrl(u -> Assert.assertEquals(u, first));
    }
  }
//
//
//  @Test(groups = "conn")
//  public void firstUrlInactive()
//    throws Exception
//  {
//    final ActivePassiveConnectionStrategy strategy = new ActivePassiveConnectionStrategy();
//    final ConnectionConfig cc = new ConnectionConfig();
//    cc.setLdapUrl("ldap://directory-1.ldaptive.org ldap://directory-2.ldaptive.org ldap://directory-3.ldaptive.org");
//    cc.setConnectionStrategy(strategy);
//    final MockConnection conn = new MockConnection(cc);
//    conn.setOpenPredicate(ldapURL -> !ldapURL.getHostname().contains("-1"));
//    conn.setTestPredicate(ldapURL -> true);
//    Assert.assertEquals(strategy.ldapURLSet.active.size(), 3);
//    Assert.assertEquals(
//      strategy.ldapURLSet.active.values(),
//      List.of(
//        new LdapURL("ldap://directory-1.ldaptive.org"),
//        new LdapURL("ldap://directory-2.ldaptive.org"),
//        new LdapURL("ldap://directory-3.ldaptive.org")));
//    Assert.assertEquals(strategy.ldapURLSet.inactive.size(), 0);
//
//    // first entry should fail, list should reorder with that entry last
//    conn.open();
//    Assert.assertEquals(strategy.ldapURLSet.active.size(), 2);
//    Assert.assertEquals(
//      strategy.ldapURLSet.active.values(),
//      List.of(
//        new LdapURL("ldap://directory-2.ldaptive.org"),
//        new LdapURL("ldap://directory-3.ldaptive.org")));
//    Assert.assertEquals(strategy.ldapURLSet.inactive.size(), 1);
//    Assert.assertEquals(
//      strategy.ldapURLSet.inactive.values().iterator().next().getValue(),
//      new LdapURL("ldap://directory-1.ldaptive.org"));
//
//    // confirm the inactive entry stays at the end
//    Assert.assertEquals(
//      strategy.apply(),
//      List.of(
//        new LdapURL("ldap://directory-2.ldaptive.org"),
//        new LdapURL("ldap://directory-3.ldaptive.org"),
//        new LdapURL("ldap://directory-1.ldaptive.org")));
//    Assert.assertEquals(strategy.ldapURLSet.active.size(), 2);
//    Assert.assertEquals(
//      strategy.ldapURLSet.active.values(),
//      List.of(
//        new LdapURL("ldap://directory-2.ldaptive.org"),
//        new LdapURL("ldap://directory-3.ldaptive.org")));
//    Assert.assertEquals(strategy.ldapURLSet.inactive.size(), 1);
//    Assert.assertEquals(
//      strategy.ldapURLSet.inactive.values().iterator().next().getValue(),
//      new LdapURL("ldap://directory-1.ldaptive.org"));
//
//    // mark first entry as active, list should reorder with that entry first
//    strategy.success(new LdapURL("ldap://directory-1.ldaptive.org"));
//    Assert.assertEquals(
//      strategy.apply(),
//      List.of(
//        new LdapURL("ldap://directory-1.ldaptive.org"),
//        new LdapURL("ldap://directory-2.ldaptive.org"),
//        new LdapURL("ldap://directory-3.ldaptive.org")));
//    Assert.assertEquals(strategy.ldapURLSet.active.size(), 3);
//    Assert.assertEquals(
//      strategy.ldapURLSet.active.values(),
//      List.of(
//        new LdapURL("ldap://directory-1.ldaptive.org"),
//        new LdapURL("ldap://directory-2.ldaptive.org"),
//        new LdapURL("ldap://directory-3.ldaptive.org")));
//    Assert.assertEquals(strategy.ldapURLSet.inactive.size(), 0);
//  }
//
//
//  @Test(groups = "conn")
//  public void firstAndSecondUrlInactive()
//    throws Exception
//  {
//    final ActivePassiveConnectionStrategy strategy = new ActivePassiveConnectionStrategy();
//    final ConnectionConfig cc = new ConnectionConfig();
//    cc.setLdapUrl("ldap://directory-1.ldaptive.org ldap://directory-2.ldaptive.org ldap://directory-3.ldaptive.org");
//    cc.setConnectionStrategy(strategy);
//    final MockConnection conn = new MockConnection(cc);
//    conn.setOpenPredicate(ldapURL -> !ldapURL.getHostname().contains("-1") && !ldapURL.getHostname().contains("-2"));
//    conn.setTestPredicate(ldapURL -> true);
//    Assert.assertEquals(strategy.ldapURLSet.active.size(), 3);
//    Assert.assertEquals(
//      strategy.ldapURLSet.active.values(),
//      List.of(
//        new LdapURL("ldap://directory-1.ldaptive.org"),
//        new LdapURL("ldap://directory-2.ldaptive.org"),
//        new LdapURL("ldap://directory-3.ldaptive.org")));
//    Assert.assertEquals(strategy.ldapURLSet.inactive.size(), 0);
//
//    // first and second entry should fail, list should reorder with those entries last
//    conn.open();
//    Assert.assertEquals(strategy.ldapURLSet.active.size(), 1);
//    Assert.assertEquals(
//      strategy.ldapURLSet.active.values().iterator().next(),
//      new LdapURL("ldap://directory-3.ldaptive.org"));
//    Assert.assertEquals(strategy.ldapURLSet.inactive.size(), 2);
//    Assert.assertEquals(
//      strategy.ldapURLSet.inactive.values().stream().map(e -> e.getValue()).collect(Collectors.toList()),
//      List.of(
//        new LdapURL("ldap://directory-1.ldaptive.org"),
//        new LdapURL("ldap://directory-2.ldaptive.org")));
//
//    // confirm the inactive entries stay at the end
//    Assert.assertEquals(
//      strategy.apply(),
//      List.of(
//        new LdapURL("ldap://directory-3.ldaptive.org"),
//        new LdapURL("ldap://directory-1.ldaptive.org"),
//        new LdapURL("ldap://directory-2.ldaptive.org")));
//    Assert.assertEquals(strategy.ldapURLSet.active.size(), 1);
//    Assert.assertEquals(
//      strategy.ldapURLSet.active.values().iterator().next(),
//      new LdapURL("ldap://directory-3.ldaptive.org"));
//    Assert.assertEquals(strategy.ldapURLSet.inactive.size(), 2);
//    Assert.assertEquals(
//      strategy.ldapURLSet.inactive.values().stream().map(e -> e.getValue()).collect(Collectors.toList()),
//      List.of(
//        new LdapURL("ldap://directory-1.ldaptive.org"),
//        new LdapURL("ldap://directory-2.ldaptive.org")));
//
//    // mark first entry as active, list should reorder with that entry first
//    strategy.success(new LdapURL("ldap://directory-1.ldaptive.org"));
//    Assert.assertEquals(
//      strategy.apply(),
//      List.of(
//        new LdapURL("ldap://directory-1.ldaptive.org"),
//        new LdapURL("ldap://directory-3.ldaptive.org"),
//        new LdapURL("ldap://directory-2.ldaptive.org")));
//    Assert.assertEquals(strategy.ldapURLSet.active.size(), 2);
//    Assert.assertEquals(
//      strategy.ldapURLSet.active.values(),
//      List.of(
//        new LdapURL("ldap://directory-1.ldaptive.org"),
//        new LdapURL("ldap://directory-3.ldaptive.org")));
//    Assert.assertEquals(strategy.ldapURLSet.inactive.size(), 1);
//    Assert.assertEquals(
//      strategy.ldapURLSet.inactive.values().iterator().next().getValue(),
//      new LdapURL("ldap://directory-2.ldaptive.org"));
//
//    // mark second entry as active, list should reorder with that entry second
//    strategy.success(new LdapURL("ldap://directory-2.ldaptive.org"));
//    Assert.assertEquals(
//      strategy.apply(),
//      List.of(
//        new LdapURL("ldap://directory-1.ldaptive.org"),
//        new LdapURL("ldap://directory-2.ldaptive.org"),
//        new LdapURL("ldap://directory-3.ldaptive.org")));
//    Assert.assertEquals(strategy.ldapURLSet.active.size(), 3);
//    Assert.assertEquals(
//      strategy.ldapURLSet.active.values(),
//      List.of(
//        new LdapURL("ldap://directory-1.ldaptive.org"),
//        new LdapURL("ldap://directory-2.ldaptive.org"),
//        new LdapURL("ldap://directory-3.ldaptive.org")));
//    Assert.assertEquals(strategy.ldapURLSet.inactive.size(), 0);
//  }
}
