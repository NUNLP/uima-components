

/* First created by JCasGen Fri Jan 03 13:40:16 CST 2014 */
package org.apache.ctakes.typesystem.type.textsem;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** Equivalent to cTAKES:
				edu.mayo.bmi.uima.cdt.type.MeasurementAnnotation
 * Updated by JCasGen Fri Jan 03 13:40:16 CST 2014
 * XML source: C:/WKT/git/schorndorfer/uima-components/annotator-parent/type-system/src/main/resources/org/apache/ctakes/typesystem/types/TypeSystem.xml
 * @generated */
public class MeasurementAnnotation extends IdentifiedAnnotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(MeasurementAnnotation.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated  */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected MeasurementAnnotation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public MeasurementAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public MeasurementAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public MeasurementAnnotation(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
}

    