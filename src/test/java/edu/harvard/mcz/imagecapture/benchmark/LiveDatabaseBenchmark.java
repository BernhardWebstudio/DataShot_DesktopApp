package edu.harvard.mcz.imagecapture.benchmark;

import edu.harvard.mcz.imagecapture.data.HibernateUtil;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

/**
 * Benchmark comparing query and hydration strategies against the live MySQL
 * database.
 */
public class LiveDatabaseBenchmark {

	public static void main(String[] args) {
		System.out.println(
				"=======================================================================================================");
		System.out.println(
				"                                DATASHOT LIVE DATABASE BENCHMARK                                       ");
		System.out.println(
				"=======================================================================================================");

		SessionFactory sessionFactory = HibernateUtil.getSessionFactory(true, false);
		if (sessionFactory == null) {
			System.err.println("Failed to initialize SessionFactory! Check MySQL connection.");
			System.exit(1);
		}

		try {
			Session checkSession = sessionFactory.openSession();
			Long totalSpecimens = checkSession.createQuery("SELECT count(s.specimenId) FROM Specimen s", Long.class)
					.getSingleResult();
			System.out.println("Connected to MySQL live database: `lepidoptera`");
			System.out.println(String.format("Total specimens in database: %,d", totalSpecimens));
			checkSession.close();

			System.out.println("\nWarming up JVM and connection pool...");
			runWarmup(sessionFactory);

			int[] limits = {100, 1000};

			System.out.println(
					"\n-------------------------------------------------------------------------------------------------------");
			System.out.println("SCENARIO 1: Unfiltered Specimen Browser (Browse All Specimens)");
			System.out.println(
					"-------------------------------------------------------------------------------------------------------");
			for (int limit : limits) {
				runBenchmarkSuite(sessionFactory, Collections.emptyMap(), limit, "Browse All");
			}

			System.out.println(
					"\n-------------------------------------------------------------------------------------------------------");
			System.out.println("SCENARIO 2: Filtered Search (Family = 'Papilionidae')");
			System.out.println(
					"-------------------------------------------------------------------------------------------------------");
			Map<String, Object> filteredMap = new HashMap<>();
			filteredMap.put("family", "Papilionidae");
			for (int limit : limits) {
				runBenchmarkSuite(sessionFactory, filteredMap, limit, "Search (Family=Papilionidae)");
			}

			System.out.println(
					"\n=======================================================================================================");
			System.out.println(
					"                                      BENCHMARK COMPLETE                                               ");
			System.out.println(
					"=======================================================================================================");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.terminateSessionFactory();
			System.exit(0);
		}
	}

	private static void runWarmup(SessionFactory sf) {
		Session session = sf.openSession();
		session.beginTransaction();
		List<Specimen> warm = session.createQuery("FROM Specimen s ORDER BY s.specimenId", Specimen.class)
				.setMaxResults(100).list();
		for (Specimen s : warm) {
			s.getBarcode();
			s.getNumbers().size();
		}
		session.getTransaction().commit();
		session.close();
	}

	private static void runBenchmarkSuite(SessionFactory sf, Map<String, Object> criteria, int limit, String desc) {
		System.out.printf("\n>>> [%s] Page Size / Limit: %,d specimens\n", desc, limit);
		System.out.println(
				"+--------------------------------------------------------------------+-------------+-------------+-------------+");
		System.out.println(
				"| Strategy                                                           | Query Time  | UI Hydration| Total Time  |");
		System.out.println(
				"+--------------------------------------------------------------------+-------------+-------------+-------------+");

		// 1. Single-Step Direct Query (Current Implementation with batch-size=1000)
		benchmarkStrategy(sf, "1. Single-Step Direct Query (Current Master)", limit, () -> {
			Session session = sf.openSession();
			session.beginTransaction();
			long t0 = System.nanoTime();
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Specimen> cr = cb.createQuery(Specimen.class);
			Root<Specimen> root = cr.from(Specimen.class);
			if (!criteria.isEmpty()) {
				for (Map.Entry<String, Object> e : criteria.entrySet()) {
					cr.where(cb.equal(root.get(e.getKey()), e.getValue()));
				}
			}
			cr.select(root).distinct(true).orderBy(cb.asc(root.get("specimenId")));
			List<Specimen> results = session.createQuery(cr).setMaxResults(limit).list();
			long t1 = System.nanoTime();

			simulateTableUIAccess(results);
			long t2 = System.nanoTime();

			session.getTransaction().commit();
			session.close();
			return new long[]{t1 - t0, t2 - t1};
		});

		// 2. Initial Approach: 2-Step with 8-way Multi-Collection LEFT JOIN FETCH
		benchmarkStrategy(sf, "2. Initial Approach (2-Step + 8-way Multi-Collection Fetch)", limit, () -> {
			Session session = sf.openSession();
			session.beginTransaction();
			long t0 = System.nanoTime();

			// Step 1: fetch IDs
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Long> cr = cb.createQuery(Long.class);
			Root<Specimen> root = cr.from(Specimen.class);
			if (!criteria.isEmpty()) {
				for (Map.Entry<String, Object> e : criteria.entrySet()) {
					cr.where(cb.equal(root.get(e.getKey()), e.getValue()));
				}
			}
			cr.select(root.get("specimenId")).distinct(true).orderBy(cb.asc(root.get("specimenId")));
			List<Long> ids = session.createQuery(cr).setMaxResults(limit).list();

			// Step 2: fetch with multi-join fetch
			List<Specimen> results;
			if (ids.isEmpty()) {
				results = Collections.emptyList();
			} else {
				String hql = "SELECT DISTINCT s FROM Specimen s " + "LEFT JOIN FETCH s.ICImages "
						+ "LEFT JOIN FETCH s.collectors " + "LEFT JOIN FETCH s.specimenParts "
						+ "LEFT JOIN FETCH s.numbers " + "LEFT JOIN FETCH s.trackings "
						+ "LEFT JOIN FETCH s.externalHistory " + "LEFT JOIN FETCH s.LatLong "
						+ "LEFT JOIN FETCH s.determinations " + "WHERE s.specimenId IN (:ids) "
						+ "ORDER BY s.specimenId ASC";
				results = session.createQuery(hql, Specimen.class).setParameterList("ids", ids).list();
			}
			long t1 = System.nanoTime();

			simulateTableUIAccess(results);
			long t2 = System.nanoTime();

			session.getTransaction().commit();
			session.close();
			return new long[]{t1 - t0, t2 - t1};
		});

		// 3. Two-Step: ID Fetch + Simple WHERE id IN (:ids) (Lazy batch)
		benchmarkStrategy(sf, "3. 2-Step: ID Fetch + Simple WHERE id IN (:ids) (Batch)", limit, () -> {
			Session session = sf.openSession();
			session.beginTransaction();
			long t0 = System.nanoTime();

			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Long> cr = cb.createQuery(Long.class);
			Root<Specimen> root = cr.from(Specimen.class);
			if (!criteria.isEmpty()) {
				for (Map.Entry<String, Object> e : criteria.entrySet()) {
					cr.where(cb.equal(root.get(e.getKey()), e.getValue()));
				}
			}
			cr.select(root.get("specimenId")).distinct(true).orderBy(cb.asc(root.get("specimenId")));
			List<Long> ids = session.createQuery(cr).setMaxResults(limit).list();

			List<Specimen> results;
			if (ids.isEmpty()) {
				results = Collections.emptyList();
			} else {
				String hql = "SELECT s FROM Specimen s WHERE s.specimenId IN (:ids) ORDER BY s.specimenId ASC";
				results = session.createQuery(hql, Specimen.class).setParameterList("ids", ids).list();
			}
			long t1 = System.nanoTime();

			simulateTableUIAccess(results);
			long t2 = System.nanoTime();

			session.getTransaction().commit();
			session.close();
			return new long[]{t1 - t0, t2 - t1};
		});

		// 4. Two-Step with TARGETED Fetch Join (Only s.numbers for UI column)
		benchmarkStrategy(sf, "4. 2-Step: ID Fetch + Targeted Single Fetch (s.numbers)", limit, () -> {
			Session session = sf.openSession();
			session.beginTransaction();
			long t0 = System.nanoTime();

			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Long> cr = cb.createQuery(Long.class);
			Root<Specimen> root = cr.from(Specimen.class);
			if (!criteria.isEmpty()) {
				for (Map.Entry<String, Object> e : criteria.entrySet()) {
					cr.where(cb.equal(root.get(e.getKey()), e.getValue()));
				}
			}
			cr.select(root.get("specimenId")).distinct(true).orderBy(cb.asc(root.get("specimenId")));
			List<Long> ids = session.createQuery(cr).setMaxResults(limit).list();

			List<Specimen> results;
			if (ids.isEmpty()) {
				results = Collections.emptyList();
			} else {
				String hql = "SELECT DISTINCT s FROM Specimen s LEFT JOIN FETCH s.numbers WHERE s.specimenId IN (:ids) ORDER BY s.specimenId ASC";
				results = session.createQuery(hql, Specimen.class).setParameterList("ids", ids).list();
			}
			long t1 = System.nanoTime();

			simulateTableUIAccess(results);
			long t2 = System.nanoTime();

			session.getTransaction().commit();
			session.close();
			return new long[]{t1 - t0, t2 - t1};
		});

		// 5. Scalar Projection (DTO / Direct Columns without entity hydration)
		benchmarkStrategy(sf, "5. Scalar Projection (Table Columns Only, No Entities)", limit, () -> {
			Session session = sf.openSession();
			session.beginTransaction();
			long t0 = System.nanoTime();

			String hql = "SELECT s.specimenId, s.barcode, s.workFlowStatus, s.family, s.subfamily, "
					+ "s.tribe, s.genus, s.specificEpithet, s.subspecificEpithet, s.country, "
					+ "s.primaryDivison, s.verbatimLocality, s.collection FROM Specimen s ";
			if (!criteria.isEmpty()) {
				hql += "WHERE s.family = :family ";
			}
			hql += "ORDER BY s.specimenId ASC";

			Query<Object[]> q = session.createQuery(hql, Object[].class).setMaxResults(limit);
			if (!criteria.isEmpty()) {
				q.setParameter("family", criteria.get("family"));
			}
			List<Object[]> rows = q.list();
			long t1 = System.nanoTime();

			for (Object[] row : rows) {
				for (Object col : row) {
					if (col != null)
						col.toString();
				}
			}
			long t2 = System.nanoTime();

			session.getTransaction().commit();
			session.close();
			return new long[]{t1 - t0, t2 - t1};
		});

		// 6. Fast Entity Projection (SELECT new Specimen(...))
		benchmarkStrategy(sf, "6. Fast Table Projection (SELECT new Specimen(...))", limit, () -> {
			Session session = sf.openSession();
			session.beginTransaction();
			long t0 = System.nanoTime();

			String hql = "SELECT new edu.harvard.mcz.imagecapture.entity.Specimen("
					+ "s.specimenId, s.barcode, s.workFlowStatus, s.family, s.subfamily, "
					+ "s.tribe, s.genus, s.specificEpithet, s.subspecificEpithet, s.country, "
					+ "s.primaryDivison, s.verbatimLocality, s.collection, "
					+ "(SELECT n.number FROM Number n WHERE n.specimen = s AND n.numberType = 'Collection Number' ORDER BY n.numberId ASC LIMIT 1)) "
					+ "FROM Specimen s ";
			if (!criteria.isEmpty()) {
				hql += "WHERE s.family = :family ";
			}
			hql += "ORDER BY s.specimenId ASC";

			Query<Specimen> q = session.createQuery(hql, Specimen.class).setMaxResults(limit);
			if (!criteria.isEmpty()) {
				q.setParameter("family", criteria.get("family"));
			}
			List<Specimen> results = q.list();
			long t1 = System.nanoTime();

			simulateTableUIAccess(results);
			long t2 = System.nanoTime();

			session.getTransaction().commit();
			session.close();
			return new long[]{t1 - t0, t2 - t1};
		});

		System.out.println(
				"+--------------------------------------------------------------------+-------------+-------------+-------------+");
	}

	private interface TimedOperation {
		long[] run() throws Exception;
	}

	private static void benchmarkStrategy(SessionFactory sf, String name, int limit, TimedOperation op) {
		int iterations = 3;
		long totalQueryNs = 0;
		long totalUiNs = 0;

		for (int i = 0; i < iterations; i++) {
			try {
				System.gc();
				long[] times = op.run();
				totalQueryNs += times[0];
				totalUiNs += times[1];
			} catch (Exception e) {
				System.out.printf("| %-66s | ERROR: %-37s |\n", name, e.getMessage());
				return;
			}
		}

		double avgQueryMs = (totalQueryNs / (double) iterations) / 1_000_000.0;
		double avgUiMs = (totalUiNs / (double) iterations) / 1_000_000.0;
		double avgTotalMs = avgQueryMs + avgUiMs;

		System.out.printf("| %-66s | %9.2f ms | %9.2f ms | %9.2f ms |\n", name, avgQueryMs, avgUiMs, avgTotalMs);
	}

	private static void simulateTableUIAccess(List<Specimen> specimens) {
		for (Specimen s : specimens) {
			s.getSpecimenId();
			s.getBarcode();
			s.getWorkFlowStatus();
			s.getFamily();
			s.getSubfamily();
			s.getTribe();
			s.getGenus();
			s.getSpecificEpithet();
			s.getSubspecificEpithet();
			s.getCountry();
			s.getPrimaryDivison();
			s.getVerbatimLocality();
			s.getCollection();
			try {
				s.getFirstNumberWithType("Collection Number");
			} catch (Exception ignored) {
			}
		}
	}
}
