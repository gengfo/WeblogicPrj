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

package com.bigrez.service.impl;

import static com.bigrez.testutilties.AssertUtilties.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.bigrez.domain.CardDetails;
import com.bigrez.domain.GuestProfile;
import com.bigrez.service.DuplicateKeyException;
import com.bigrez.service.NotFoundException;
import com.bigrez.testutilties.AbstractEntityManagerTests;


/**
 * Unit tests for {@link ProfileServicesImpl}.
 *
 * @author Philip Aston
 */
public class TestProfileServices extends AbstractEntityManagerTests {

  private ProfileServicesImpl profileServices;

  @Before public void setUp() throws Exception {
    profileServices = new ProfileServicesImpl();
    profileServices.setEntityManager(getEntityManager());
  }

  @Test public void testProfileServices() throws Exception {
    final GuestProfile guestProfile1 = new GuestProfile();
    guestProfile1.setLogon("logon");
    guestProfile1.setPassword("password");

    final GuestProfile returnedGuestProfile1 =
      profileServices.createOrUpdate(guestProfile1);

    getEntityManager().flush();

    // Identity only sure to be assigned once flushed.
    returnedGuestProfile1.getExternalIdentity();
    assertNull(returnedGuestProfile1.getCard());

    final CardDetails card = new CardDetails("type", "number", "expiry");
    returnedGuestProfile1.setCard(card);

    final GuestProfile returnedGuestProfile2 =
      profileServices.createOrUpdate(returnedGuestProfile1);

    assertEquals(returnedGuestProfile1, returnedGuestProfile2);
    assertEquals(card, returnedGuestProfile2.getCard());

    final GuestProfile guestProfile2 = new GuestProfile();
    guestProfile2.setLogon("logon");
    guestProfile2.setPassword("password");

    try {
      profileServices.createOrUpdate(guestProfile2);
      fail("Expected DuplicateKeyException");
    }
    catch (DuplicateKeyException e) {
    }

    getEntityManager().clear();

    guestProfile2.setLogon("another");

    final GuestProfile returnedGuestProfile3 =
      profileServices.createOrUpdate(guestProfile2);

    assertNotEquals(returnedGuestProfile2, returnedGuestProfile3);
  }

  @Test public void testFindByLogonAndPassword() throws Exception {
    final GuestProfile guestProfile1 = new GuestProfile();
    guestProfile1.setLogon("logon");
    guestProfile1.setPassword("password");

    final GuestProfile createdGP =
      profileServices.createOrUpdate(guestProfile1);

    final GuestProfile guestProfile2 =
      profileServices.findByLogonAndPassword("logon", "password");

    assertEquals(createdGP, guestProfile2);

    try {
      profileServices.findByLogonAndPassword("logon", "blah");
      fail("Expected NotFoundException");
    }
    catch (NotFoundException e) {
    }

    try {
      profileServices.findByLogonAndPassword("logon1", "password");
      fail("Expected NotFoundException");
    }
    catch (NotFoundException e) {
    }
  }
}
