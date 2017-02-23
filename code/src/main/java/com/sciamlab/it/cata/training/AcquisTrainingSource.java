package com.sciamlab.it.cata.training;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import com.mchange.v2.c3p0.DataSources;
import com.mchange.v2.c3p0.PooledDataSource;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme.Theme;
import com.sciamlab.it.cata.Cata;
import com.sciamlab.it.cata.classifier.ClassifiedEntry;

//sorgente ACQUIS DB
public class AcquisTrainingSource implements TrainingSource {
	
	private static final Logger logger = Logger.getLogger(AcquisTrainingSource.class);
	private PooledDataSource ds_pooled;
	private String JDBC_DRIVER;
	private String JDBC_URL;
	private String JDBC_USER;
	private String JDBC_PASSWORD;
	private String ACQUIS_TABLE;
	private Map<String, ClassifiedEntry> docMap;
	private TrainingSet trainingSet;
	
	public AcquisTrainingSource() throws ClassNotFoundException, SQLException{
		JDBC_DRIVER 	= Cata.PROPS.getProperty("persistence.jdbc.driver");
		JDBC_URL 		= Cata.PROPS.getProperty("persistence.jdbc.url");
		JDBC_USER 		= Cata.PROPS.getProperty("persistence.jdbc.user");
		JDBC_PASSWORD	= Cata.PROPS.getProperty("persistence.jdbc.password");
		ACQUIS_TABLE	= Cata.PROPS.getProperty("persistence.jdbc.acquis");
		init();
		loadData();
	}

	private Connection getConnection() throws SQLException {
		//  return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
		return this.ds_pooled.getConnection();
	}

	public void close() throws SQLException{
		if(ds_pooled!=null){
			logger.info("Releasing connection pool "+this);
			ds_pooled.close();
		}
	}

	private void init() throws ClassNotFoundException, SQLException{
		logger.debug("-------- PostgreSQL Connection Testing ------------");
		//try {
		logger.debug(JDBC_DRIVER);
		Class.forName(JDBC_DRIVER);
		//} catch (ClassNotFoundException e) {
		//throw new DAOException("Where is your PostgreSQL Driver? Include in your library path!");
		//}
		logger.debug("PostgreSQL Driver Registered!");
		//try{
		DataSource ds_unpooled = DataSources.unpooledDataSource(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
		ds_pooled = (PooledDataSource) DataSources.pooledDataSource( ds_unpooled );
		//} catch (SQLException e) {
		//throw new DAOException(e);
		//}
	}

	//docmap: mappa String-ClassifiedEntry
	//classifiedEntry: List<String>-Map<Theme-Double>
	//docmap: String(id)-List<String>-Map<Theme-Double>
	public void loadData() throws SQLException{
		//AGRI, ENER, GOVE, INTR, JUST, ECON, SOCI, EDUC, TECH, TRAN, ENVI, REGI, HEAL
		PreparedStatement stmt = getConnection().prepareStatement("SELECT * from "+ACQUIS_TABLE);
		//logger.info(stmt.toString());
		stmt.execute();
		ResultSet rs = stmt.getResultSet();
		docMap = new HashMap<String, ClassifiedEntry>();
		while(rs.next()){
			List<String> featureSet = Arrays.asList((String[])rs.getArray("features").getArray());
			Map<Theme, Double> categories = new HashMap<Theme, Double>();
			for(String cat : Arrays.asList((String[])rs.getArray("dcats").getArray()))
				categories.put(Theme.valueOf(cat), 0.0);
			docMap.put(rs.getString("doc"), new ClassifiedEntry(featureSet, categories));
		}
	}
	
	public TrainingSet getTrainingSet(Object... objects) {
		trainingSet = new TrainingSetImpl(docMap);
		return this.trainingSet;		
	}
}