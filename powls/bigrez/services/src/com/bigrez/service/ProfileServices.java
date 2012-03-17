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

package com.bigrez.service;

import com.bigrez.domain.GuestProfile;


/**
 * Guest profile services.
 *
 * @author Philip Aston
 */
public interface ProfileServices {

  /**
   * Persist the supplied {@code GuestProfile}, creating or updating the
   * database record as required.
   *
   * @param guestProfile A guest profile.
   * @return The created or updated {@code GuestProfile}.
   * @throws DuplicateKeyException If a guest with the same logon already
   *    exists.
   */
  GuestProfile createOrUpdate(GuestProfile guestProfile)
    throws DuplicateKeyException;

  /**
   * Find a {@code GuestProfile} by user logon name and password.
   *
   * <p>TODO The requirement for this method should be replaced with a SQL
   * Authenticator.
   * </p>
   *
   * @param logon User name.
   * @param password Password.
   * @return The profile with the given user name and password.
   * @throws NotFoundException if there is no result.
   */
  GuestProfile findByLogonAndPassword(String logon, String password)
    throws NotFoundException;
}
