

/* First created by JCasGen Fri Jan 03 13:40:16 CST 2014 */
package org.apache.ctakes.typesystem.type.textsem;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** Indication of whether the medication is mentioned in the
				context of Allergies.
 * Updated by JCasGen Fri Jan 03 13:40:16 CST 2014
 * XML source: C:/WKT/git/schorndorfer/uima-components/annotator-parent/type-system/src/main/resources/org/apache/ctakes/typesystem/types/TypeSystem.xml
 * @generated */
public class MedicationAllergyModifier extends Modifier {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(MedicationAllergyModifier.class);
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
  protected MedicationAllergyModifier() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public MedicationAllergyModifier(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public MedicationAllergyModifier(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public MedicationAllergyModifier(JCas jcas, int begin, int end) {
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
  //* Feature: indicated

  /** getter for indicated - gets 
   * @generated */
  public boolean getIndicated() {
    if (MedicationAllergyModifier_Type.featOkTst && ((MedicationAllergyModifier_Type)jcasType).casFeat_indicated == null)
      jcasType.jcas.throwFeatMissing("indicated", "org.apache.ctakes.typesystem.type.textsem.MedicationAllergyModifier");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((MedicationAllergyModifier_Type)jcasType).casFeatCode_indicated);}
    
  /** setter for indicated - sets  
   * @generated */
  public void setIndicated(boolean v) {
    if (MedicationAllergyModifier_Type.featOkTst && ((MedicationAllergyModifier_Type)jcasType).casFeat_indicated == null)
      jcasType.jcas.throwFeatMissing("indicated", "org.apache.ctakes.typesystem.type.textsem.MedicationAllergyModifier");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((MedicationAllergyModifier_Type)jcasType).casFeatCode_indicated, v);}    
  }

    