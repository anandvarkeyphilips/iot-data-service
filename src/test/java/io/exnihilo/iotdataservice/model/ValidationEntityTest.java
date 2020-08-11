package io.exnihilo.nearestplaces.model;

import io.exnihilo.nearestplaces.entity.DnsRecord;
import org.junit.Assert;
import org.junit.Test;

/**
 * Validation Response Entity has the response details for all configured editor methods. Tests are
 * based on: https://www.artima.com/lejava/articles/equality.html
 */
public class ValidationEntityTest {

  @Test
  public void newValidationEntityWithBuilder_whenEqual_ThenTrue() {

    DnsRecord x = new DnsRecord(1, "example.com", "A", "1.2.3.4", 3600);
    DnsRecord y = new DnsRecord(1, "example.com", "A", "1.2.3.4", 3600);
    DnsRecord z = x;

    // reflexive: for any non-null value x, the expression x.equals(x) should return true.
    Assert.assertTrue(x.equals(x));
    // symmetric: for any non-null values x and y, x.equals(y) should return true if and only if
    // y.equals(x) returns true.
    Assert.assertTrue(x.equals(y) && y.equals(x));
    // transitive: for any non-null values x, y, and z, if x.equals(y) returns true and y.equals(z)
    // returns true, then x.equals(z) should return true.
    Assert.assertTrue(x.equals(z) && y.equals(z) && x.equals(y));
    // consistent: for any non-null values x and y, multiple invocations of x.equals(y) should
    // consistently return true or consistently return false, provided no information used in equals
    // comparisons on the objects is modified.
    Assert.assertTrue(x.equals(y) && x.equals(y) && x.equals(y) && x.equals(y));
    // Null check: for any non-null value x, x.equals(null) should return false.
    Assert.assertFalse(x.equals(null));

    Assert.assertEquals(x, y);
    Assert.assertEquals(x.hashCode(), y.hashCode());
    Assert.assertEquals(x.toString(), y.toString());
  }

  @Test
  public void newValidationEntityWithBuilder_whenNotEqual_ThenFalse() {
    DnsRecord x = new DnsRecord(1, "example.com", "A", "1.2.3.4", 3600);
    DnsRecord y = new DnsRecord(2, "example.com", "NS", "ns1.jimdo.com", 3600);

    Assert.assertFalse(x.equals(y) && y.equals(x));
    Assert.assertNotEquals(x.hashCode(), y.hashCode());
    Assert.assertNotEquals(x.toString(), y.toString());
  }

}
