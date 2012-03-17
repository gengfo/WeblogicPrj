// Copyright (C) 2008 Philip Aston
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

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import com.bigrez.domain.GuestProfile;
import com.bigrez.service.DuplicateKeyException;
import com.bigrez.service.NotFoundException;
import com.bigrez.service.ProfileServices;


/**
 * Stateless Session Bean implementation of {@link ProfileServices}.
 *
 * @author Philip Aston
 */
@Stateless
@Local
@Interceptors(LoggingInterceptor.class)
public class ProfileServicesImpl implements ProfileServices {

  @PersistenceContext
  private EntityManager entityManager;

  /**
   * For unit tests.
   */
  void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  private final JPABaseDAO<GuestProfile> guestProfileDAO =
    new JPABaseDAO<GuestProfile>() {
      @Override protected EntityManager getEntityManager() {
        return entityManager;
      }
    };

  /**
   * {@inheritDoc}
   */
  @Override
  public GuestProfile createOrUpdate(GuestProfile guestProfile)
    throws DuplicateKeyException {

    final GuestProfile result = guestProfileDAO.createOrUpdate(guestProfile);

    try {
      entityManager.flush();
    }
    catch (PersistenceException e) {
      throw new DuplicateKeyException(
        "GuestProfile exists with logon name '" +
        guestProfile.getLogon() + "'");
    }

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GuestProfile findByLogonAndPassword(String logon, String password)
    throws NotFoundException {

    return guestProfileDAO.findOne(
      GuestProfile.QUERY_BY_LOGON_AND_PASSWORD, logon, password);
  }
}
