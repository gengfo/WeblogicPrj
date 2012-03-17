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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;


/**
 * Base class for all our entities.
 *
 * <p>Defines identity and equality.</p>
 *
 * @author Philip Aston
 */
@MappedSuperclass
public abstract class DomainEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  private static long TRANSIENT_INSTANCE_ID = 0;

  @Id
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CommonSequence")
  @SequenceGenerator(name="CommonSequence", sequenceName="COMMON_SEQUENCE")
  private long id = TRANSIENT_INSTANCE_ID;

  @SuppressWarnings("unused")
  @Version
  private int version;

  @Transient private final Class<? extends DomainEntity> entityClass;

  protected DomainEntity() {
    entityClass = implementationClass(getClass());
  }

  /**
   * Protected scope, for subclasses and unit tests only.
   *
   * <p>Client code should use {@link #equals(Object)} to compare
   * {@code DomainObjects}, {@link #getExternalIdentity()} to generate
   * an String that represents this entity externally, and
   * {@link #findByExternalIdentity} to parse external
   * representations.</p>
   */
  protected final long getId() {
    return id;
  }

  /**
   * Package scope, for the unit tests only.
   */
  final void setId(long id) {
    this.id = id;
  }

  /**
   * Produce a representation of this entity that can be used externally,
   * e.g. in URLs.
   *
   * @return The external representation.
   * @throws IllegalStateException if the entity is transient.
   */
  public String getExternalIdentity() {
    if (isTransient()) {
      throw new IllegalStateException(
        "Transient instances have no external identity");
    }

    return new ExternalIdentityParser(this).toExternalIdentity();
  }

  /**
   * Parse representations created by {@link #getExternalIdentity()} and
   * find the corresponding entity in the supplied entity manager.
   *
   * @param em The entity manager to use.
   * @param entityClass The expected class.
   * @param externalIdentity The external representation.
   * @return The entity.
   * @throws IllegalArgumentException If {@code externalIdentity} could not be
   *  parsed, or represents an entity of a class different to {@code
   *  entityClass}.
   */
  public static <T extends DomainEntity> T findByExternalIdentity(
    EntityManager em,
    Class<T> entityClass,
    String externalIdentity)
    throws IllegalArgumentException {

    final ExternalIdentityParser parser =
      new ExternalIdentityParser(externalIdentity, entityClass);

    // See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6302954
    // return find(em, entityClass, parser.getId());

    return DomainEntity.<T>find(em, entityClass, parser.getId());
  }

  /**
   * Look up matching entity in the supplied {@code EntityManager}.
   *
   * @param em The entity manager.
   * @return The found entity instance or {@code null} if the entity does not
   *  exist.
   */
  public final <T extends DomainEntity> T findEntity(EntityManager em) {
    // See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6302954
    // return find(em, entityClass, getId());

    return DomainEntity.<T>find(em, entityClass, getId());
  }

  // Suppressing warnings is safe unless our type parameter is outside
  // of the Big Rez packages as defined by {@link
  // JPAUtilties#implementationClass}. Unfortunately there is no way to
  // assert this is true at runtime due to type erasure.
  @SuppressWarnings("unchecked")
  private static <T extends DomainEntity>
  T find(EntityManager em, Class<? extends DomainEntity> entityClass, long id) {

    if (id == TRANSIENT_INSTANCE_ID) {
      // Don't bother checking the database for transient instances.
      return null;
    }

    return (T) em.find(entityClass, id);
  }

  /**
   * {@inheritDoc}
   */
  @Override public final int hashCode() {
    if (isTransient()) {
      return super.hashCode();
    }

    return (int) (id ^ id >> 32) ^ entityClass.hashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (isTransient()) {
      return false;
    }

    if (!(o instanceof DomainEntity)) {
      return false;
    }

    final DomainEntity other = (DomainEntity)o;

    return id == other.id &&
           entityClass == other.entityClass;
  }

  protected final boolean isTransient() {
    return id == TRANSIENT_INSTANCE_ID;
  }

  /**
   * String representation for debugging.
   *
   * @return A description of this <code>BigRezEntity</code>.
   */
  @Override public final String toString() {
    return new Formatter().format(
      "%s[0x%h, %s, \"%s\"]",
      entityClass.getSimpleName(),
      super.hashCode(),
      id,
      toStringDescription()).toString();
  }

  /**
   * Allows subclasses to provide additional information for {@link #toString}.
   *
   * @return Additional information from the sub class.
   */
  protected abstract String toStringDescription();

  private static final Pattern externalFormatRegex =
    Pattern.compile("^(\\w+)-(\\d+)$");

  private static final class ExternalIdentityParser {
    private final String className;
    private final long id;

    public ExternalIdentityParser(DomainEntity entity) {
      className = entity.entityClass.getSimpleName();
      id = entity.getId();
    }

    public ExternalIdentityParser(String externalIdentity,
                                  Class<? extends DomainEntity> entityClass)
      throws IllegalArgumentException {

      final Matcher matcher = externalFormatRegex.matcher(externalIdentity);

      if (!matcher.find()) {
        throw new IllegalArgumentException("Invalid representation");
      }

      className = matcher.group(1);

      if (!entityClass.getSimpleName().equals(className)) {
        throw new IllegalArgumentException(
          new Formatter().format(
            "External identity is for a %s, but wanted a %s",
            matcher.group(1), entityClass.getSimpleName()).toString());
      }

      try {
        id = Long.valueOf(matcher.group(2));
      }
      catch (NumberFormatException e) {
        throw new IllegalArgumentException("Invalid representation");
      }
    }

    public String toExternalIdentity() {
      return new Formatter().format("%s-%d", className, getId()).toString();
    }

    public long getId() {
      return id;
    }
  }
}