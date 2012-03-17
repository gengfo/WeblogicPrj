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

package com.bigrez.service;

import com.bigrez.jpa.DomainEntity;

/**
 * Base class for exceptions that refer to a {@link DomainEntity}.
 *
 * @author Philip Aston
 */
public abstract class AbstractEntityException extends Exception {

  private final DomainEntity entity;

  /**
   * Constructor.
   *
   * @param message Text message describing the exception.
   * @param entity The entity to which the exception refers.
   */
  protected AbstractEntityException(String message, DomainEntity entity) {
    super(message);

    this.entity = entity;
  }

  /**
   * The entity that was not found in the database.
   *
   * @return The entity.
   */
  public final DomainEntity getEntity() {
    return entity;
  }
}