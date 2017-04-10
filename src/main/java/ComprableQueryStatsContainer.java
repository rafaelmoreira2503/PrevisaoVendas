//import java.util.Iterator;
//import java.util.TreeSet;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
//
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.stat.QueryStatistics;
//import org.hibernate.stat.Statistics;
//
//public class ComprableQueryStatsContainer implements Comparable {
//	public String query;
//	public QueryStatistics queryStatistics;
//
//	EntityManagerFactory factory = Persistence
//			.createEntityManagerFactory("ConfiabilidadePU");
//	EntityManager em = factory.createEntityManager();
//	Session session = em.unwrap(Session.class);
//	SessionFactory hbmSession = session.getSessionFactory();
//
//	Statistics statistics = hbmSession.getStatistics();
//
//	@SuppressWarnings("rawtypes")
//	public int compareTo(Object object) {
//		if (!(object instanceof ComprableQueryStatsContainer)) {
//			throw new RuntimeException("Not instance of QueryComparator");
//		}
//
//		QueryStatistics queryStatisticsObject = ((ComprableQueryStatsContainer) object).queryStatistics;
//
//		long diff = queryStatistics.getExecutionAvgTime()
//				* queryStatistics.getExecutionCount();
//		diff -= queryStatisticsObject.getExecutionAvgTime()
//				* queryStatisticsObject.getExecutionCount();
//
//		if (diff == 0) {
//			diff = queryStatistics.getExecutionMaxTime()
//					- queryStatisticsObject.getExecutionMaxTime();
//		}
//		return (int) (-diff);
//		
//		
//
//	}
//	
//	TreeSet treeSet = new TreeSet();
//	 String[] queries = statistics.getQueries();
//	 for(int entityIndex=0;entityIndex<queries.length;entityIndex ++)
//	 {
//	 ComprableQueryStatsContainer comprableQueryStatsContainer = new ComprableQueryStatsContainer();
//	 QueryStatistics queryStatistics =statistics.getQueryStatistics(queries[entityIndex]);
//	 comprableQueryStatsContainer.queryStatistics = queryStatistics;
//	 comprableQueryStatsContainer.query=queries[entityIndex];
//	 treeSet.add(comprableQueryStatsContainer);
//	 
//	 int index =0;
//	 for(Iterator iterator = treeSet.iterator();iterator.hasNext();index++)
//	 {
//	 ComprableQueryStatsContainer comprableQueryStatsContainer = (ComprableQueryStatsContainer)iterator.next();
//	 QueryStatistics queryStatistics =comprableQueryStatsContainer.queryStatistics;
//	 String query = comprableQueryStatsContainer.query;
//
//	 }
//}
