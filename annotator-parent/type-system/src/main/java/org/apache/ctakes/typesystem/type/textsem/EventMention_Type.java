
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

/** A text string (IdentifiedAnnotation) that refers to an
				Event.
 * Updated by JCasGen Fri Jan 03 13:40:16 CST 2014
 * @generated */
public class EventMention_Type extends IdentifiedAnnotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (EventMention_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = EventMention_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new EventMention(addr, EventMention_Type.this);
  			   EventMention_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new EventMention(addr, EventMention_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = EventMention.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.ctakes.typesystem.type.textsem.EventMention");
 
  /** @generated */
  final Feature casFeat_event;
  /** @generated */
  final int     casFeatCode_event;
  /** @generated */ 
  public int getEvent(int addr) {
        if (featOkTst && casFeat_event == null)
      jcas.throwFeatMissing("event", "org.apache.ctakes.typesystem.type.textsem.EventMention");
    return ll_cas.ll_getRefValue(addr, casFeatCode_event);
  }
  /** @generated */    
  public void setEvent(int addr, int v) {
        if (featOkTst && casFeat_event == null)
      jcas.throwFeatMissing("event", "org.apache.ctakes.typesystem.type.textsem.EventMention");
    ll_cas.ll_setRefValue(addr, casFeatCode_event, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public EventMention_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_event = jcas.getRequiredFeatureDE(casType, "event", "org.apache.ctakes.typesystem.type.refsem.Event", featOkTst);
    casFeatCode_event  = (null == casFeat_event) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_event).getCode();

  }
}



    