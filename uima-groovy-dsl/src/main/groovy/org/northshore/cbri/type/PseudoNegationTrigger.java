

/* First created by JCasGen Sun Nov 24 22:34:46 CST 2013 */
package org.northshore.cbri.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** Type defined in org.northshore.cbri.type
 * Updated by JCasGen Sun Nov 24 22:34:46 CST 2013
 * XML source: C:/WKT/git/schorndorfer/uima-components/uima-groovy-dsl/src/main/resources/descriptors/TypeSystem.xml
 * @generated */
public class PseudoNegationTrigger extends NegationTrigger {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(PseudoNegationTrigger.class);
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
  protected PseudoNegationTrigger() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public PseudoNegationTrigger(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public PseudoNegationTrigger(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public PseudoNegationTrigger(JCas jcas, int begin, int end) {
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

    