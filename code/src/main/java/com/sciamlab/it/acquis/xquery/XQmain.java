package com.sciamlab.it.acquis.xquery;
import java.io.FileNotFoundException;
import java.util.HashSet;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;
import com.sciamlab.common.nlp.EurovocThesaurus;
import com.sciamlab.it.eurovoc.EVoc;
import net.sf.saxon.xqj.SaxonXQDataSource;

public class XQmain {

	//returns features list for an acquis document
	public static String corpusFile(String file, String path) 
			throws FileNotFoundException, XQException, FileWithoutEurovocTagException{
		
		String corpus="";
		XQDataSource ds = new SaxonXQDataSource();
		XQConnection conn = ds.getConnection();	
		
		
		String q="for $x in doc('"+path+"/"+file+"')//body return $x/head/text()";
		XQPreparedExpression exp = conn.prepareExpression(q);
		XQResultSequence result = exp.executeQuery();
		while (result.next()){
			corpus=corpus+" "+(result.getItemAsString(null));
		}
		
		q="for $x in doc('"+path+"/"+file+"')//body/div[@type = \"body\"] return $x/p/text()";
		exp = conn.prepareExpression(q);
		result = exp.executeQuery();

		while (result.next()){
			corpus=corpus+" "+(result.getItemAsString(null));
		}
		return corpus;
	}

	//returns dcats list for an acquis document
	public static HashSet<String> dcatsFile(String file, String path, EurovocThesaurus thesaurusInfo) 
			throws FileNotFoundException, XQException, FileWithoutEurovocTagException{

		HashSet<String> list=new HashSet<String>();
		String xquery="for $x in doc('"+path+"/"+file+"')//textClass return $x/classCode/text()";

		XQDataSource ds = new SaxonXQDataSource();
		XQConnection conn = ds.getConnection();	
		XQPreparedExpression exp = conn.prepareExpression(xquery);
		XQResultSequence result = exp.executeQuery();

		while (result.next()) {
			list.add(result.getItemAsString(null));
		}
		//System.out.println(list);
		return EVoc.returnsDCATCAT(list, thesaurusInfo);
	}

}