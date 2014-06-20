package br.com.altamira.data.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.altamira.data.model.Request;

@Named
@Stateless
public class RequestDao {

	@PersistenceContext
	private EntityManager entityManager;

	public List<Request> getAll(int startPosition, int maxResult) {

		TypedQuery<Request> findAllQuery = entityManager.createNamedQuery("Request.findAll", Request.class);

		findAllQuery.setFirstResult(startPosition);
		findAllQuery.setMaxResults(maxResult);

		return findAllQuery.getResultList();
	}
	
	public Request create(Request entity) {
		entity.setId(null);
		entityManager.persist(entity);
		return entity;
	}
	
	public Request find(Long id) {
		TypedQuery<Request> findByIdQuery = entityManager.createNamedQuery("Request.findById", Request.class);
        findByIdQuery.setParameter("id", id);
        Request entity;
        try {
            entity = findByIdQuery.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
        
        // Lazy load of items
        entity.getItems().size();

        return entity;
	}
	
	public Request update(Request entity) {
		if (entity.getId() == null && entity.getId() == 0l) {
			return null;
		}
		return entityManager.contains(entity) ? null : entityManager.merge(entity);
	}
	
	public Request remove(Request entity) {
		entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
		entity.setId(null);
		return entity;
	}
	
	public Request remove(long id) {
		Request entity = entityManager.find(Request.class, id);
        if (entity != null) {
	        entityManager.remove(entity);
	        entity.setId(null);
        }
		return entity;
	}
	
    public Request getCurrent() {
        List<Request> requests;

        requests = (List<Request>) entityManager
                .createNamedQuery("Request.getCurrent", Request.class)
                .getResultList();

        if (requests.isEmpty()) {

            return create(new Request(null, null, "system"));

        }

        return requests.get(0);
    }

    /*public byte[] getRequestReportJasperFile() throws SQLException {
        Blob tempBlob = (Blob) entityManager.createNativeQuery("SELECT JASPER_FILE FROM REQUEST_REPORT WHERE REPORT = (SELECT MAX(REPORT) FROM REQUEST_REPORT)")
                .getSingleResult();

        return tempBlob.getBytes(1, (int) tempBlob.length());
    }

    public byte[] getRequestReportAltamiraImage() throws SQLException {
        Blob tempBlob = (Blob) entityManager.createNativeQuery("SELECT ALTAMIRA_LOGO FROM REQUEST_REPORT WHERE REPORT = (SELECT MAX(REPORT) FROM REQUEST_REPORT)")
                .getSingleResult();

        return tempBlob.getBytes(1, (int) tempBlob.length());
    }

    public List selectRequestReportDataById(long requestId) {
        StringBuffer selectSql = new StringBuffer().append(" SELECT M.ID, ")
                                                   .append("        M.LAMINATION, ")
                                                   .append("        M.TREATMENT, ")
                                                   .append("        M.THICKNESS, ")
                                                   .append("        M.WIDTH, ")
                                                   .append("        M.LENGTH, ")
                                                   .append("        RT.WEIGHT, ")
                                                   .append("        RT.ARRIVAL_DATE ")
                                                   .append(" FROM REQUEST R, REQUEST_ITEM RT, MATERIAL M ")
                                                   .append(" WHERE R.ID = RT.REQUEST ")
                                                   .append(" AND RT.MATERIAL = M.ID ")
                                                   .append(" AND R.ID = :request_id ");

        List<Object[]> list = entityManager.createNativeQuery(selectSql.toString())
                                           .setParameter("request_id", requestId)
                                           .getResultList();

        return list;
    }

    public boolean insertGeneratedRequestReport(JasperPrint print) {
        byte[] bArray = null;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            oos.writeObject(print);
            oos.close();
            baos.close();

            bArray = baos.toByteArray();
        } catch (Exception e) {
            System.out.println("Error converting JasperPrint object to byte[] array");
            e.printStackTrace();
            return false;
        }

        try {
            RequestReportLog log = new RequestReportLog();
            log.setReportInstance(bArray);

            entityManager.persist(log);
        } catch (Exception e) {
            System.out.println("Error while inserting generated report in database.");
            e.printStackTrace();
            return false;
        }

        return true;
    }*/
}
