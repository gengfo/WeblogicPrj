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

package com.bigrez.jpa;

import static com.bigrez.jpa.JPAUtilties.implementationClass;

import java.io.Serializable;
import java.util.Formatter;

import javax.persistence.MappedSuperclass;


/**
 * Abstract base class for our value objects.
 *
 * <p>Value objects are simple objects whose equality isn't based on identity.
 * For a good discussion, see Fowler, Patterns of Enterprise Architecture,
 * Addison-Wesley 2003.</p>
 *
 * <p>We have chosen also to make value objects immutable.  Due to JPA
 * requirements, our subclasses need a default constructor (although OpenJPA
 * can generate one automatically), should be non-final (although OpenJPA
 * does not require this), and our fields cannot be marked final. We weakly
 * enforce immutability by removing all setter methods and making the default
 * constructor protected.
 *
 * @author Philip Aston
 * @version $Revision:$
 */
@MappedSuperclass
public abstract class ValueObject implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Compare two objects, returning true if they are both null, or they
   * are equal. Used by subclasses value based equality implementations.
   *
   * @param <T> The concrete type of the objects to be compared. Provides
   * a quick compile time error if we're comparing Apples to Pears.
   * @param one The first object.
   * @param two The second object.
   * @return {@code true} if and only if the objects are equal.
   */
  protected static final <T> boolean nullSafeEquals(T one, T two) {
    return one == null ? two == null : one.equals(two);
  }

  /**
   * Return a hash code for reference, safely producing 0 if the reference
   * is {@code null}.
   *
   * @param o The object.
   * @return {@code o == null ? 0 : o.hashCode()}
   */
  protected static final int nullSafeHashCode(Object o) {
    return o == null ? 0 : o.hashCode();
  }

  /**
   * String representation for debugging.
   *
   * @return A description of this {@code ValueObject}.
   */
  @Override public final String toString() {
    return new Formatter().format(
      "%s[0x%h, \"%s\"]",
      implementationClass(getClass()).getSimpleName(),
      super.hashCode(),
      toStringDescription()).toString();
  }

  protected abstract String toStringDescription();
}
