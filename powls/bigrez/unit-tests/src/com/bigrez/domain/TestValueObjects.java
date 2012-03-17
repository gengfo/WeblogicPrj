// Copyright (C) 2008, 2009 Philip Aston
// All rights reserved.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
// FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
// COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
// INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
// STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

package com.bigrez.domain;

import static com.bigrez.testutilties.AssertUtilties.assertContainsPattern;
import static com.bigrez.testutilties.AssertUtilties.assertNotEquals;
import static com.bigrez.testutilties.BeanTester.testAccessors;
import static com.bigrez.testutilties.BeanTester.testConstructorAndGetters;
import static com.bigrez.testutilties.Serializer.serialize;
import static org.junit.Assert.assertEquals;

import java.util.Currency;

import org.junit.Test;


/**
 * Unit tests for the value objects.
 *
 * @author Philip Aston
 */
public class TestValueObjects {
  @Test public void testAddress() throws Exception {
    final Address address1 = testConstructorAndGetters(Address.class,
      "address1", "address2", "city", "stateCode", "postalCode");
    assertEquals(address1, address1);
    assertNotEquals(address1, "Some string");
    assertNotEquals(address1, null);

    final Address address2 = serialize(address1);
    assertEquals(address1, address2);
    assertEquals(address2, address1);
    assertEquals(address1.hashCode(), address2.hashCode());
    assertContainsPattern(address1.toString(),
    "^Address\\[0x\\p{XDigit}+, \".*\"\\]$");
    assertNotEquals(address1.toString(), address2.toString());

    final Address address3 = new Address();
    assertEquals(address3, address3);
    assertNotEquals(address1, address3);

    final Address address4 = new Address();
    assertEquals(address3, address4);
    assertEquals(address3.hashCode(), address4.hashCode());

    assertContainsPattern(
      address4.toString(),
      "^Address\\[0x\\p{XDigit}+, \"null/null/null/null/null\"\\]$");

    testAccessors(
      address4, "address1", "address2", "city", "stateCode", "postalCode");
  }

  @Test public void testCardDetails() throws Exception {
    final CardDetails cardDetails1 =
      testConstructorAndGetters(CardDetails.class, "type", "number", "expiry");
    assertEquals(cardDetails1, cardDetails1);
    assertNotEquals(cardDetails1, "Some string");
    assertNotEquals(cardDetails1, null);

    final CardDetails cardDetails2 = serialize(cardDetails1);
    assertEquals(cardDetails1, cardDetails2);
    assertEquals(cardDetails2, cardDetails1);
    assertEquals(cardDetails1.hashCode(), cardDetails2.hashCode());
    assertContainsPattern(cardDetails1.toString(),
    "^CardDetails\\[0x\\p{XDigit}+, \".*\"\\]$");
    assertNotEquals(cardDetails1.toString(), cardDetails2.toString());

    final CardDetails cardDetails3 = new CardDetails();
    assertEquals(cardDetails3, cardDetails3);
    assertNotEquals(cardDetails1, cardDetails3);

    final CardDetails cardDetails4 = new CardDetails();
    assertEquals(cardDetails3, cardDetails4);
    assertEquals(cardDetails3.hashCode(), cardDetails4.hashCode());

    assertContainsPattern(
      cardDetails4.toString(),
      "^CardDetails\\[0x\\p{XDigit}+, \"null\"\\]$");

    testAccessors(cardDetails4, "type", "number", "expiry");
  }

  @Test public void testMoney() throws Exception {
    final Money money1 = testConstructorAndGetters(Money.class, "amount");
    assertEquals(money1, money1);
    assertNotEquals(money1, "Some string");
    assertNotEquals(money1, null);

    final Money money2 = serialize(money1);
    assertEquals(money1, money2);
    assertEquals(money2, money1);
    assertEquals(money1.hashCode(), money2.hashCode());
    assertContainsPattern(money1.toString(),
    "^Money\\[0x\\p{XDigit}+, \".*\"\\]$");
    assertNotEquals(money1.toString(), money2.toString());

    final Money money3 = new Money();
    assertEquals(money3, money3);
    assertNotEquals(money1, money3);

    final Money money4 = new Money();
    assertEquals(money3, money4);
    assertEquals(money3.hashCode(), money4.hashCode());

    assertContainsPattern(
      money4.toString(),
      "^Money\\[0x\\p{XDigit}+, \"\\Q" +
      Currency.getInstance("USD").getSymbol() +
      "\\E null\"\\]$");

    testAccessors(money4, "amount");
  }
}
