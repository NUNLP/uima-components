

/* First created by JCasGen Fri Jan 03 13:40:16 CST 2014 */
package org.apache.ctakes.typesystem.type.syntax;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** Differentiates a token as being a number rather than a
				punctuation, symbol, newline, word, or contraction.
				Equivalent to
				cTAKES: edu.mayo.bmi.uima.core.type.NumToken
 * Updated by JCasGen Fri Jan 03 13:40:16 CST 2014
 * XML source: C:/WKT/git/schorndorfer/uima-components/annotator-parent/type-system/src/main/resources/org/apache/ctakes/typesystem/types/TypeSystem.xml
 * @generated */
public class NumToken extends BaseToken {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(NumToken.class);
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
  protected NumToken() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public NumToken(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public NumToken(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public NumToken(JCas jcas, int begin, int end) {
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
     
 
    
  //*--------------*
  //* Feature: numType

  /** getter for numType - gets 
   * @generated */
  public int getNumType() {
    if (NumToken_Type.featOkTst && ((NumToken_Type)jcasType).casFeat_numType == null)
      jcasType.jcas.throwFeatMissing("numType", "org.apache.ctakes.typesystem.type.syntax.NumToken");
    return jcasType.ll_cas.ll_getIntValue(addr, ((NumToken_Type)jcasType).casFeatCode_numType);}
    
  /** setter for numType - sets  
   * @generated */
  public void setNumType(int v) {
    if (NumToken_Type.featOkTst && ((NumToken_Type)jcasType).casFeat_numType == null)
      jcasType.jcas.throwFeatMissing("numType", "org.apache.ctakes.typesystem.type.syntax.NumToken");
    jcasType.ll_cas.ll_setIntValue(addr, ((NumToken_Type)jcasType).casFeatCode_numType, v);}    
  }

    