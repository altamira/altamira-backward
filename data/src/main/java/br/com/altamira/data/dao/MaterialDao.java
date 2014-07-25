/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.altamira.data.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.altamira.data.model.Material;

/**
 * 
 * @author Alessandro
 */
@Named
@Stateless
public class MaterialDao {

	@PersistenceContext
	private EntityManager entityManager;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public List<Material> list(int startPosition, int maxResult) {

		TypedQuery<Material> findAllQuery = entityManager.createNamedQuery("Material.list", Material.class);

		findAllQuery.setFirstResult(startPosition);
		findAllQuery.setMaxResults(maxResult);

		return findAllQuery.getResultList();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public Material find(Material material) {
		return find(material.getLamination(), material.getTreatment(),
				material.getThickness(), material.getWidth(),
				material.getLength());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public Material find(String lamination, String treatment,
			BigDecimal thickness, BigDecimal width, BigDecimal length) {

		List<Material> materials = entityManager
				.createNamedQuery("Material.findUnique", Material.class)
				.setParameter("lamination", lamination)
				.setParameter("treatment", treatment)
				.setParameter("thickness", thickness)
				.setParameter("width", width)
				.setParameter("length", length)
				.getResultList();

		if (materials.isEmpty()) {
			return null;
		}

		return materials.get(0);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public Material find(long id) {
		return entityManager.find(Material.class, id);
	}

	public Material create(Material entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity can't be null.");
		}
		
		if (entity.getId() != null && entity.getId() > 0) {
			throw new IllegalArgumentException("To create this entity, id must be null or zero.");
		}
		
		entity.setId(null);
		
		entityManager.persist(entity);
		entityManager.flush();
		
		return entityManager.find(Material.class, entity.getId());
	}

	public Material update(Material entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity can't be null.");
		}
		if (entity.getId() == null || entity.getId() == 0l) {
			throw new IllegalArgumentException("Entity id can't be null or zero.");
		}
		return entityManager.contains(entity) ? null : entityManager.merge(entity);
	}

	public Material remove(Material entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity can't be null.");
		}
		
		if (entity.getId() == null || entity.getId() == 0l) {
			throw new IllegalArgumentException("Entity id can't be null or zero.");
		}
		
		return remove(entity.getId());
	}
	
	public Material remove(long id) {
		if (id == 0) {
			throw new IllegalArgumentException("Entity id can't be zero.");
		}
		
		Material entity = entityManager.find(Material.class, id);
        
		if (entity == null) {
			throw new IllegalArgumentException("Entity not found.");
		}

        entityManager.remove(entity);
        entityManager.flush();
		
		return entity;
	}
}
