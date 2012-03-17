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

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.bigrez.jpa.DomainEntity;
import com.bigrez.service.EntityNotFoundException;
import com.bigrez.service.EntityNotTransientException;
import com.bigrez.service.NotFoundException;


/**
 * Base class for JPA DAOs.
 *
 * <p>Partly modelled on discussion on Gregg Bolinger's blog at
 * http://www.greggbolinger.com/blog/2008/04/17/1208457000000.html</p>
 *
 * @author Philip Aston
 * @version $Revision:$
 */
abstract class JPABaseDAO<T extends DomainEntity> {

  private final Class<T> persistentClass;

  @SuppressWarnings("unchecked")
  public JPABaseDAO() {
    persistentClass = (Class<T>)
      ((ParameterizedType)getClass().getGenericSuperclass())
      .getActualTypeArguments()[0];
  }

  protected abstract EntityManager getEntityManager();

  public void create(T t) {
    getEntityManager().persist(t);
  }

  /**
   * Create or update a new {@code T} from the supplied {@code t}. This simply
   * delegates to {@link EntityManager#merge}.
   *
   * <p>
   * If {@code t} is unmanaged, e.g. it has not yet been persisted, a new
   * managed {@code T} instance will be returned. Be careful not to persist the
   * original {@code t} later, or you will end up with two distinct entities.
   * This can be easy to do accidently if you've added {@code t} to a cascade
   * persist association that has not yet been flushed.
   * </p>
   *
   * @param t
   *          The original.
   * @return The created or updated {@code T}.
   */
  public T createOrUpdate(T t) {
    return getEntityManager().merge(t);
  }

  public void delete(T t) {
    getEntityManager().remove(t);
  }

  public T checkExists(T t) throws EntityNotFoundException {
    final T entityFromDatabase = t.<T>findEntity(getEntityManager());

    if (entityFromDatabase == null) {
      throw new EntityNotFoundException(t);
    }

    return entityFromDatabase;
  }

  public T checkTransient(T t) throws EntityNotTransientException {
    final T entityFromDatabase = t.<T>findEntity(getEntityManager());

    if (entityFromDatabase != null) {
      throw new EntityNotTransientException(t);
    }

    return t;
  }

  public T findByExternalIdentity(String externalIdentity)
    throws NotFoundException {
    final T result = DomainEntity.findByExternalIdentity(
      getEntityManager(), persistentClass, externalIdentity);

    if (result == null) {
      throw new NotFoundException(
        "Could not find " + persistentClass.getSimpleName() +
        " with external identity '" + externalIdentity + "'");
    }

    return result;
  }

  @SuppressWarnings("unchecked")
  public T findOne(String namedQuery, Object... parameters)
    throws NotFoundException {

    try {
      final Query query = getEntityManager().createNamedQuery(namedQuery);

      int i = 0;

      for (Object p : parameters) {
        query.setParameter(++i, p);
      }

      return (T)query.getSingleResult();
    }
    catch (NoResultException e) {
      throw new NotFoundException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public List<T> findAll() {
    final String query =
      "select c from " + persistentClass.getSimpleName() + " c";
    return (List<T>)getEntityManager().createQuery(query).getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<T> find(String namedQuery, Object... parameters) {
    final Query query = getEntityManager().createNamedQuery(namedQuery);

    int i = 0;

    for (Object p : parameters) {
      query.setParameter(++i, p);
    }

    return (List<T>)query.getResultList();
  }
}
