

/* First created by JCasGen Fri Jan 03 13:40:16 CST 2014 */
package org.apache.ctakes.typesystem.type.textsem;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** Test result interpretations organized in a valued list
				e.g., "rare cells seen" or "few cells seen" in urinalysis, now
				combined with very_abnormal and abnormal.
 * Updated by JCasGen Fri Jan 03 13:40:16 CST 2014
 * XML source: C:/WKT/git/schorndorfer/uima-components/annotator-parent/type-system/src/main/resources/org/apache/ctakes/typesystem/types/TypeSystem.xml
 * @generated */
public class LabInterpretationModifier extends Modifier {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(LabInterpretationModifier.class);
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
  protected LabInterpretationModifier() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public LabInterpretationModifier(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public LabInterpretationModifier(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public LabInterpretationModifier(JCas jcas, int begin, int end) {
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
  //* Feature: value

  /** getter for value - gets 
   * @generated */
  public String getValue() {
    if (LabInterpretationModifier_Type.featOkTst && ((LabInterpretationModifier_Type)jcasType).casFeat_value == null)
      jcasType.jcas.throwFeatMissing("value", "org.apache.ctakes.typesystem.type.textsem.LabInterpretationModifier");
    return jcasType.ll_cas.ll_getStringValue(addr, ((LabInterpretationModifier_Type)jcasType).casFeatCode_value);}
    
  /** setter for value - sets  
   * @generated */
  public void setValue(String v) {
    if (LabInterpretationModifier_Type.featOkTst && ((LabInterpretationModifier_Type)jcasType).casFeat_value == null)
      jcasType.jcas.throwFeatMissing("value", "org.apache.ctakes.typesystem.type.textsem.LabInterpretationModifier");
    jcasType.ll_cas.ll_setStringValue(addr, ((LabInterpretationModifier_Type)jcasType).casFeatCode_value, v);}    
  }

    