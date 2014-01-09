
/* First created by JCasGen Fri Jan 03 13:40:16 CST 2014 */
package org.apache.ctakes.typesystem.type.textsem;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;

/** An introduction of a measure of doubt into a statement.
				If indicated, there is uncertainty.
 * Updated by JCasGen Fri Jan 03 13:40:16 CST 2014
 * @generated */
public class UncertaintyModifier_Type extends Modifier_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (UncertaintyModifier_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = UncertaintyModifier_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new UncertaintyModifier(addr, UncertaintyModifier_Type.this);
  			   UncertaintyModifier_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new UncertaintyModifier(addr, UncertaintyModifier_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = UncertaintyModifier.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.ctakes.typesystem.type.textsem.UncertaintyModifier");
 
  /** @generated */
  final Feature casFeat_indicated;
  /** @generated */
  final int     casFeatCode_indicated;
  /** @generated */ 
  public boolean getIndicated(int addr) {
        if (featOkTst && casFeat_indicated == null)
      jcas.throwFeatMissing("indicated", "org.apache.ctakes.typesystem.type.textsem.UncertaintyModifier");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_indicated);
  }
  /** @generated */    
  public void setIndicated(int addr, boolean v) {
        if (featOkTst && casFeat_indicated == null)
      jcas.throwFeatMissing("indicated", "org.apache.ctakes.typesystem.type.textsem.UncertaintyModifier");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_indicated, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public UncertaintyModifier_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_indicated = jcas.getRequiredFeatureDE(casType, "indicated", "uima.cas.Boolean", featOkTst);
    casFeatCode_indicated  = (null == casFeat_indicated) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_indicated).getCode();

  }
}



    