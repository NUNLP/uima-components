

/* First created by JCasGen Sun Nov 24 22:34:46 CST 2013 */
package org.northshore.cbri.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sun Nov 24 22:34:46 CST 2013
 * XML source: C:/WKT/git/schorndorfer/uima-components/uima-groovy-dsl/src/main/resources/descriptors/TypeSystem.xml
 * @generated */
public class NegationScopeTerminator extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(NegationScopeTerminator.class);
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
  protected NegationScopeTerminator() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public NegationScopeTerminator(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public NegationScopeTerminator(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public NegationScopeTerminator(JCas jcas, int begin, int end) {
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

    