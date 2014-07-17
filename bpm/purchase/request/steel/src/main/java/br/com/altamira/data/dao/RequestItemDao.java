package br.com.altamira.data.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.altamira.data.model.Material;
import br.com.altamira.data.model.Request;
import br.com.altamira.data.model.RequestItem;

@Named
@Stateless
public class RequestItemDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Inject
	private RequestDao requestDao;
	
	@Inject
	private MaterialDao materialDao;

	public List<RequestItem> list(Long requestId, int startPosition, int maxResult) {

		TypedQuery<RequestItem> findAllQuery = entityManager.createNamedQuery("RequestItem.list", RequestItem.class);
		findAllQuery.setParameter("requestId", requestId);

		findAllQuery.setFirstResult(startPosition);
		findAllQuery.setMaxResults(maxResult);

		return findAllQuery.getResultList();
	}
	
	public RequestItem find(long id) {
        RequestItem entity;

		TypedQuery<RequestItem> findByIdQuery = entityManager.createNamedQuery("RequestItem.findById", RequestItem.class);
        findByIdQuery.setParameter("id", id);
        try {
            entity = findByIdQuery.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }

        return entity;
	}
	
	public RequestItem create(RequestItem entity) {
		if (entity == null) {
			throw new IllegalArgumentException("RequestItem cant'b be null");
		}
		
		if (entity.getId() != null) {
			throw new IllegalArgumentException("RequestItem Id must be null");
		}
		
		Request request = requestDao.current();	// get current request
		
		if (entity.getRequest() == null) {
			//throw new IllegalArgumentException("Request parent not assigned");
			
			entity.setRequest(request);
		}
		
		if (entity.getRequest().getId() != request.getId()) {
			throw new IllegalArgumentException("Insert item to non current Request is not allowed");
		}
		
		if (entity.getId() != null) {
			throw new IllegalArgumentException("To create this entity id must be null.");
		}
		
		Material material = materialDao.find(entity.getMaterial());
		
		if (material == null) {
			//Create a fresh copy with null id of material

			Material previousMaterial = entity.getMaterial();
			Material newMaterial = new Material();
			
			newMaterial.setLamination(previousMaterial.getLamination());
			newMaterial.setLength(previousMaterial.getLength());
			newMaterial.setTax(previousMaterial.getTax());
			newMaterial.setThickness(previousMaterial.getThickness());
			newMaterial.setTreatment(previousMaterial.getTreatment());
			newMaterial.setWidth(previousMaterial.getWidth());
			
			material = materialDao.create(newMaterial);
			
		}
		
		entity.setMaterial(material);
		
		entityManager.persist(entity);
		entityManager.flush();
		
		// Reload to update child references

		return entityManager.find(RequestItem.class, entity.getId());
	}
	
	public RequestItem update(RequestItem entity) {
		
		if (entity == null) {
			throw new IllegalArgumentException("RequestItem can't be null");
		}
		
		if (entity.getId() == null || entity.getId() == 0l) {
			throw new IllegalArgumentException("RequestItem Id can't be null or zero");
		}

		Request request = requestDao.current();	// get current request
		
		if (entity.getRequest() == null) {
			//throw new IllegalArgumentException("Request parent not assigned");
			
			entity.setRequest(request);
		}
		
		if (entity.getRequest().getId() != request.getId()) {
			throw new IllegalArgumentException("Update item of non current Request is not allowed");
		}
		
		if (entity.getMaterial() == null) {
			throw new IllegalArgumentException("Material is required.");
		}
		
		Material material = materialDao.find(entity.getMaterial());
		
		if (entity.getMaterial().getId() != null && entity.getMaterial().getId() != 0l) {
			if (material == null) {
				throw new IllegalArgumentException("Material id doesn't match with properties.");
			}
			if (entity.getMaterial().getId() != material.getId()) {
				throw new IllegalArgumentException("Material id doesn't match with properties. Material id is " + entity.getMaterial().getId() + ", expected id is " + material.getId());
			}
		}
		
		if (material == null) {
			//Create a fresh copy with null id of material

			Material previousMaterial = entity.getMaterial();
			Material newMaterial = new Material();
			
			newMaterial.setLamination(previousMaterial.getLamination());
			newMaterial.setLength(previousMaterial.getLength());
			newMaterial.setTax(previousMaterial.getTax());
			newMaterial.setThickness(previousMaterial.getThickness());
			newMaterial.setTreatment(previousMaterial.getTreatment());
			newMaterial.setWidth(previousMaterial.getWidth());
			
			material = materialDao.create(newMaterial);
			
		}
		
		entity.setMaterial(material);
		
		entityManager.merge(entity);

		// Reload to update child references

		return entityManager.find(RequestItem.class, entity.getId());
	}
	
	public RequestItem remove(RequestItem entity) {
		if (entity == null) {
			throw new IllegalArgumentException();
		}
		
		if (entity.getId() == null || entity.getId() == 0l) {
			throw new IllegalArgumentException();
		}
		
		return remove(entity.getId());
	}
	
	public RequestItem remove(long id) {
		if (id == 0) {
			throw new IllegalArgumentException();
		}
		
		//entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));

		RequestItem entity = entityManager.find(RequestItem.class, id);
        
		if (entity != null) {
	        entityManager.remove(entity);
        }
		
		return entity;
	}


}
